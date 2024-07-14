package it.crystalnest.soul_fire_d.api;

import com.google.common.base.Suppliers;
import it.crystalnest.cobweb.api.pack.DynamicDataPack;
import it.crystalnest.cobweb.api.pack.DynamicTagBuilder;
import it.crystalnest.cobweb.api.registry.CobwebEntry;
import it.crystalnest.cobweb.api.registry.CobwebRegister;
import it.crystalnest.cobweb.api.registry.CobwebRegistry;
import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.block.CustomCampfireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomFireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomLanternBlock;
import it.crystalnest.soul_fire_d.api.block.CustomTorchBlock;
import it.crystalnest.soul_fire_d.api.block.CustomWallTorchBlock;
import it.crystalnest.soul_fire_d.api.block.entity.CustomCampfireBlockEntity;
import it.crystalnest.soul_fire_d.api.block.entity.DynamicBlockEntityType;
import it.crystalnest.soul_fire_d.api.type.FireTypeChanger;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Static manager for registered Fires.
 */
public final class FireManager {
  /**
   * ID of the tag used to save fire type.
   */
  public static final String FIRE_TYPE_TAG = "FireType";

  /**
   * fire type of Vanilla Fire.
   */
  public static final ResourceLocation DEFAULT_FIRE_TYPE = ResourceLocation.withDefaultNamespace("");

  /**
   * fire type of Soul Fire.
   */
  public static final ResourceLocation SOUL_FIRE_TYPE = ResourceLocation.withDefaultNamespace("soul");

  /**
   * Default {@link Fire} used as fallback to retrieve default properties.
   */
  @SuppressWarnings("DataFlowIssue")
  public static final Fire DEFAULT_FIRE = new Fire(
    DEFAULT_FIRE_TYPE,
    Fire.Builder.DEFAULT_LIGHT,
    Fire.Builder.DEFAULT_DAMAGE,
    Fire.Builder.DEFAULT_INVERT_HEAL_AND_HARM,
    true,
    Fire.Builder.DEFAULT_ON_CAMPFIRE_GETTER,
    Fire.Builder.DEFAULT_IN_FIRE_GETTER,
    Fire.Builder.DEFAULT_ON_FIRE_GETTER,
    Fire.Builder.DEFAULT_BEHAVIOR,
    Map.ofEntries(
      Map.entry(Fire.Component.SOURCE_BLOCK, BuiltInRegistries.BLOCK.getKey(Blocks.FIRE)),
      Map.entry(Fire.Component.CAMPFIRE_BLOCK, BuiltInRegistries.BLOCK.getKey(Blocks.CAMPFIRE)),
      Map.entry(Fire.Component.LANTERN_BLOCK, BuiltInRegistries.BLOCK.getKey(Blocks.LANTERN)),
      Map.entry(Fire.Component.TORCH_BLOCK, BuiltInRegistries.BLOCK.getKey(Blocks.TORCH)),
      Map.entry(Fire.Component.WALL_TORCH_BLOCK, BuiltInRegistries.BLOCK.getKey(Blocks.WALL_TORCH)),
      Map.entry(Fire.Component.FLAME_PARTICLE, BuiltInRegistries.PARTICLE_TYPE.getKey(ParticleTypes.FLAME))
    )
  );

  /**
   * Default {@link DynamicBlockEntityType} for custom campfires.
   */
  public static final CobwebEntry<DynamicBlockEntityType<CustomCampfireBlockEntity>> CUSTOM_CAMPFIRE_ENTITY_TYPE = CobwebRegistry.of(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).register(
    "custom_campfire",
    () -> new DynamicBlockEntityType<>(CustomCampfireBlockEntity::new)
  );

  /**
   * Dynamic data pack to automatically add {@link BlockTags#FIRE} to fire source block.
   */
  private static final DynamicDataPack FIRE_SOURCE_TAGS = DynamicDataPack.named(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "fire_source_tags"));

  /**
   * Dynamic data pack to automatically add {@link BlockTags#CAMPFIRES} to campfire block.
   */
  private static final DynamicDataPack CAMPFIRE_TAGS = DynamicDataPack.named(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "campfire_tags"));

  /**
   * {@link ConcurrentHashMap} of all registered {@link Fire Fires}.
   */
  private static final ConcurrentHashMap<ResourceLocation, Fire> FIRES = new ConcurrentHashMap<>();

  static {
    FIRE_SOURCE_TAGS.register();
    CAMPFIRE_TAGS.register();
  }

  private FireManager() {}

  /**
   * Returns a new {@link Fire.Builder}.
   *
   * @param modId {@code modId} of the new {@link Fire} to build.
   * @param fireId {@code fireId} of the new {@link Fire} to build.
   * @return a new {@link Fire.Builder}.
   */
  public static Fire.Builder fireBuilder(String modId, String fireId) {
    return new Fire.Builder(modId, fireId);
  }

  /**
   * Returns a new {@link Fire.Builder}.
   *
   * @param fireType {@link ResourceLocation} of the new {@link Fire} to build.
   * @return a new {@link Fire.Builder}.
   */
  public static Fire.Builder fireBuilder(ResourceLocation fireType) {
    return new Fire.Builder(fireType);
  }

  /**
   * Attempts to register the given {@link Fire}.<br />
   * If the {@link Fire#fireType} is already registered, logs an error.
   *
   * @param fire {@link Fire} to register.
   * @return whether the registration is successful.
   */
  @Nullable
  public static synchronized Fire registerFire(Fire fire) {
    Fire previous = FIRES.computeIfAbsent(fire.getFireType(), key -> {
      // Need to manually set the fire type for blocks registered via data packs.
      Fire.Component.SOURCE_BLOCK.getOptionalValue(fire).ifPresent(block -> ((FireTypeChanger) block).setFireType(key));
      Fire.Component.CAMPFIRE_BLOCK.getOptionalValue(fire).ifPresent(block -> ((FireTypeChanger) block).setFireType(key));
      return fire;
    });
    if (previous != fire) {
      ResourceLocation fireType = fire.getFireType();
      Constants.LOGGER.error("Fire [{}] was already registered with the following value: {}", fireType, getFire(fireType));
      return null;
    }
    return fire;
  }

  /**
   * Attempts to register all the given {@link Fire}s.
   *
   * @param fires {@link Fire}s to register.
   * @return an {@link Map} with the outcome of each registration attempt.
   */
  public static synchronized Map<ResourceLocation, @Nullable Fire> registerFires(Fire... fires) {
    return registerFires(List.of(fires));
  }

  /**
   * Attempts to register all the given {@link Fire}s.
   *
   * @param fires {@link Fire}s to register.
   * @return an {@link Map} with the outcome of each registration attempt.
   */
  public static synchronized Map<ResourceLocation, @Nullable Fire> registerFires(List<Fire> fires) {
    HashMap<ResourceLocation, @Nullable Fire> outcomes = new HashMap<>();
    for (Fire fire : fires) {
      outcomes.put(fire.getFireType(), registerFire(fire));
    }
    return outcomes;
  }

  /**
   * Unregisters the specified fire.<br />
   * Internally use only, do not use elsewhere!
   *
   * @param fireType fire type.
   * @return whether the fire was previously registered.
   */
  @Nullable
  @ApiStatus.Internal
  public static synchronized Fire unregisterFire(ResourceLocation fireType) {
    return FIRES.remove(fireType);
  }

  /**
   * Registers the source block for the specified fire.
   *
   * @param fireType fire type.
   * @param base {@link BlockTags block tag} for blocks on which this fire can burn.
   * @param color light color.
   * @return supplier for the registered source block.
   */
  public static CobwebEntry<CustomFireBlock> registerFireSource(ResourceLocation fireType, TagKey<Block> base, MapColor color) {
    return registerFireSource(fireType, type -> new CustomFireBlock(type, base, color));
  }

  /**
   * Registers the source block for the specified fire.
   *
   * @param fireType fire type.
   * @param base {@link BlockTags block tag} for blocks on which this fire can burn.
   * @param properties {@link BlockBehaviour.Properties block properties}.
   * @return supplier for the registered source block.
   */
  public static CobwebEntry<CustomFireBlock> registerFireSource(ResourceLocation fireType, TagKey<Block> base, BlockBehaviour.Properties properties) {
    return registerFireSource(fireType, type -> new CustomFireBlock(type, base, properties));
  }

  /**
   * Registers the source block for the specified fire from the given supplier.
   *
   * @param fireType fire type.
   * @param supplier {@link CustomFireBlock} supplier.
   * @param <T> source block type.
   * @return supplier for the registered source block.
   */
  public static <T extends CustomFireBlock> CobwebEntry<T> registerFireSource(ResourceLocation fireType, Function<ResourceLocation, T> supplier) {
    Supplier<T> source = Suppliers.memoize(() -> supplier.apply(fireType));
    FIRE_SOURCE_TAGS.add(() -> DynamicTagBuilder.of(Registries.BLOCK, BlockTags.FIRE).addElement(source.get()));
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(FireManager.getComponentPath(fireType, Fire.Component.SOURCE_BLOCK), source);
  }

  /**
   * Registers the campfire block for the specified fire.
   *
   * @param fireType fire type.
   * @param spawnParticles whether to spawn crackling particles.
   * @return supplier for the registered campfire block.
   */
  public static CobwebEntry<CustomCampfireBlock> registerCampfire(ResourceLocation fireType, boolean spawnParticles) {
    return registerCampfire(fireType, type -> new CustomCampfireBlock(type, spawnParticles));
  }

  /**
   * Registers the campfire block for the specified fire.
   *
   * @param fireType fire type.
   * @param spawnParticles whether to spawn crackling particles.
   * @param properties {@link BlockBehaviour.Properties block properties}.
   * @return supplier for the registered campfire block.
   */
  public static CobwebEntry<CustomCampfireBlock> registerCampfire(ResourceLocation fireType, boolean spawnParticles, BlockBehaviour.Properties properties) {
    return registerCampfire(fireType, type -> new CustomCampfireBlock(type, spawnParticles, properties));
  }

  /**
   * Registers the campfire block for the specified fire from the given supplier.
   *
   * @param fireType fire type.
   * @param supplier {@link CustomCampfireBlock} supplier.
   * @param <T> campfire block type.
   * @return supplier for the registered campfire block.
   */
  public static <T extends CustomCampfireBlock> CobwebEntry<T> registerCampfire(ResourceLocation fireType, Function<ResourceLocation, T> supplier) {
    Supplier<T> campfire = Suppliers.memoize(() -> supplier.apply(fireType));
    CAMPFIRE_TAGS.add(() -> DynamicTagBuilder.of(Registries.BLOCK, BlockTags.CAMPFIRES).addElement(campfire.get()));
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(FireManager.getComponentPath(fireType, Fire.Component.CAMPFIRE_BLOCK), campfire);
  }

  /**
   * Registers the campfire item for the specified fire.<br />
   * Must be called <strong>after</strong> {@link #registerCampfire}.
   *
   * @param fireType fire type.
   * @return supplier for the registered campfire item.
   */
  public static CobwebEntry<BlockItem> registerCampfireItem(ResourceLocation fireType) {
    return registerCampfireItem(fireType, campfire -> new BlockItem(campfire, new Item.Properties()));
  }

  /**
   * Registers the campfire item for the specified fire from the given supplier.<br />
   * Must be called <strong>after</strong> {@link #registerCampfire}.
   *
   * @param fireType fire type.
   * @param supplier {@link BlockItem} supplier.
   * @param <T> campfire item type.
   * @return supplier for the registered campfire item.
   */
  public static <T extends BlockItem> CobwebEntry<T> registerCampfireItem(ResourceLocation fireType, Function<Block, T> supplier) {
    return CobwebRegistry.ofItems(fireType.getNamespace()).register(FireManager.getComponentPath(fireType, Fire.Component.CAMPFIRE_ITEM), () -> supplier.apply(FireManager.getRequiredComponent(fireType, Fire.Component.CAMPFIRE_BLOCK)));
  }

  /**
   * Registers the particle type for the specified fire.
   *
   * @param fireType fire type.
   * @return supplier for the registered particle type.
   */
  public static CobwebEntry<SimpleParticleType> registerParticle(ResourceLocation fireType) {
    return registerParticle(fireType, () -> new SimpleParticleType(false));
  }

  /**
   * Registers the particle type for the specified fire from the given supplier.
   *
   * @param fireType fire type.
   * @param supplier {@link SimpleParticleType} supplier.
   * @param <T> particle type.
   * @return supplier for the registered particle type.
   */
  public static <T extends SimpleParticleType> CobwebEntry<T> registerParticle(ResourceLocation fireType, Supplier<T> supplier) {
    return CobwebRegistry.of(Registries.PARTICLE_TYPE, fireType.getNamespace()).register(FireManager.getComponentPath(fireType, Fire.Component.FLAME_PARTICLE), supplier);
  }

  /**
   * Registers the pair of torch and wall torch blocks for the specified fire.<br />
   * Must be called <strong>after</strong> {@link #registerParticle}.
   *
   * @param fireType fire type.
   * @return supplier for the pair of torch and wall torch blocks.
   */
  public static Pair<CobwebEntry<CustomTorchBlock>, CobwebEntry<CustomWallTorchBlock>> registerTorch(ResourceLocation fireType) {
    return registerTorch(fireType, CustomTorchBlock::new, CustomWallTorchBlock::new);
  }

  /**
   * Registers the pair of torch and wall torch blocks for the specified fire from the given suppliers.<br />
   * Must be called <strong>after</strong> {@link #registerParticle}.
   *
   * @param fireType fire type.
   * @param torchSupplier {@link CustomTorchBlock} supplier.
   * @param wallTorchSupplier {@link CustomWallTorchBlock} supplier.
   * @param <T> torch block type.
   * @param <W> wall torch block type.
   * @return supplier for the pair of torch and wall torch blocks.
   */
  public static <T extends CustomTorchBlock, W extends CustomWallTorchBlock> Pair<CobwebEntry<T>, CobwebEntry<W>> registerTorch(
    ResourceLocation fireType,
    BiFunction<ResourceLocation, Supplier<SimpleParticleType>, T> torchSupplier,
    BiFunction<ResourceLocation, Supplier<SimpleParticleType>, W> wallTorchSupplier
  ) {
    CobwebRegister<Block> blocks = CobwebRegistry.ofBlocks(fireType.getNamespace());
    return Pair.of(
      blocks.register(FireManager.getComponentPath(fireType, Fire.Component.TORCH_BLOCK), () -> torchSupplier.apply(fireType, () -> getRequiredComponent(fireType, Fire.Component.FLAME_PARTICLE))),
      blocks.register(FireManager.getComponentPath(fireType, Fire.Component.WALL_TORCH_BLOCK), () -> wallTorchSupplier.apply(fireType, () -> getRequiredComponent(fireType, Fire.Component.FLAME_PARTICLE)))
    );
  }

  /**
   * Registers the torch item for the specified fire.<br />
   * Must be called <strong>after</strong> {@link #registerTorch}.
   *
   * @param fireType fire type.
   * @return supplier for the registered torch item.
   */
  public static CobwebEntry<StandingAndWallBlockItem> registerTorchItem(ResourceLocation fireType) {
    return registerTorchItem(fireType, (torch, wallTorch) -> new StandingAndWallBlockItem(torch, wallTorch, new Item.Properties(), Direction.DOWN));
  }

  /**
   * Registers the torch item for the specified fire from the given supplier.<br />
   * Must be called <strong>after</strong> {@link #registerTorch}.
   *
   * @param fireType fire type.
   * @param supplier {@link StandingAndWallBlockItem} supplier.
   * @param <T> torch item type.
   * @return supplier for the registered torch item.
   */
  public static <T extends StandingAndWallBlockItem> CobwebEntry<T> registerTorchItem(ResourceLocation fireType, BiFunction<Block, Block, T> supplier) {
    return CobwebRegistry.ofItems(fireType.getNamespace()).register(
      FireManager.getComponentPath(fireType, Fire.Component.TORCH_ITEM),
      () -> supplier.apply(FireManager.getRequiredComponent(fireType, Fire.Component.TORCH_BLOCK), FireManager.getRequiredComponent(fireType, Fire.Component.WALL_TORCH_BLOCK))
    );
  }

  /**
   * Registers the lantern block for the specified fire.
   *
   * @param fireType fire type.
   * @return supplier for the registered lantern block.
   */
  public static CobwebEntry<CustomLanternBlock> registerLantern(ResourceLocation fireType) {
    return registerLantern(fireType, CustomLanternBlock::new);
  }

  /**
   * Registers the lantern block for the specified fire from the given supplier.
   *
   * @param fireType fire type.
   * @param supplier {@link CustomLanternBlock} supplier.
   * @param <T> lantern block type.
   * @return supplier for the registered lantern block.
   */
  public static <T extends CustomLanternBlock> CobwebEntry<T> registerLantern(ResourceLocation fireType, Function<ResourceLocation, T> supplier) {
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(FireManager.getComponentPath(fireType, Fire.Component.LANTERN_BLOCK), () -> supplier.apply(fireType));
  }

  /**
   * Registers the lantern item for the specified fire.<br />
   * Must be called <strong>after</strong> {@link #registerLantern}.
   *
   * @param fireType fire type.
   * @return supplier for the registered lantern item.
   */
  public static CobwebEntry<BlockItem> registerLanternItem(ResourceLocation fireType) {
    return registerLanternItem(fireType, lantern -> new BlockItem(lantern, new Item.Properties()));
  }

  /**
   * Registers the lantern item for the specified fire from the given supplier.<br />
   * Must be called <strong>after</strong> {@link #registerLantern}.
   *
   * @param fireType fire type.
   * @param supplier {@link BlockItem} supplier.
   * @param <T> item type.
   * @return supplier for the registered lantern item.
   */
  public static <T extends BlockItem> CobwebEntry<T> registerLanternItem(ResourceLocation fireType, Function<Block, T> supplier) {
    return CobwebRegistry.ofItems(fireType.getNamespace()).register(
      FireManager.getComponentPath(fireType, Fire.Component.LANTERN_ITEM),
      () -> supplier.apply(FireManager.getRequiredComponent(fireType, Fire.Component.LANTERN_BLOCK))
    );
  }

  /**
   * Returns the {@link Fire} registered with the given {@code id}.<br />
   * Returns {@link #DEFAULT_FIRE} if no {@link Fire} is registered with the given {@code modId} and {@code fireId}.
   *
   * @param modId mod ID.
   * @param fireId fire ID.
   * @return registered {@link Fire} or {@link #DEFAULT_FIRE}.
   */
  public static Fire getFire(@Nullable String modId, @Nullable String fireId) {
    return isValidModId(modId) && isValidFireId(fireId) ? getFire(fireType(modId, fireId)) : DEFAULT_FIRE;
  }

  /**
   * Returns the {@link Fire} registered with the given {@code id}.<br />
   * Returns {@link #DEFAULT_FIRE} if no {@link Fire} is registered with the given {@code fireType}.
   *
   * @param fireType fire type.
   * @return registered {@link Fire} or {@link #DEFAULT_FIRE}.
   */
  public static Fire getFire(@Nullable ResourceLocation fireType) {
    return FIRES.getOrDefault(ensure(fireType), DEFAULT_FIRE);
  }

  /**
   * Returns the list of all registered {@link Fire}s.
   *
   * @return the list of all registered {@link Fire}s.
   */
  public static List<Fire> getFires() {
    return FIRES.values().stream().toList();
  }

  /**
   * Returns the specified property of the specified fire.<br />
   * Defaults to the property of the {@link #DEFAULT_FIRE} if the specified fire is not registered.
   *
   * @param fireType fire type.
   * @param getter property getter (use a method from the {@link Fire} class).
   * @param <T> property type.
   * @return property value.
   */
  public static <T> T getProperty(ResourceLocation fireType, Function<Fire, T> getter) {
    return getter.apply(getFire(fireType));
  }

  /**
   * Returns the specified damage source retrieved from the {@link Fire} damage source getter.
   *
   * @param entity entity.
   * @param fireType fire type.
   * @param getter damage source getter (use a method from the {@link Fire} class).
   * @return the correct damage source for the specified fire.
   */
  public static DamageSource getDamageSource(Entity entity, ResourceLocation fireType, BiFunction<Fire, Entity, DamageSource> getter) {
    return getter.apply(getFire(fireType), entity);
  }

  /**
   * Returns the specified component of the specified fire.<br />
   * Defaults to the component of the {@link #DEFAULT_FIRE} if the specified fire is not registered.
   *
   * @param fireType fire type.
   * @param component component.
   * @return component {@link ResourceLocation}.
   */
  @Nullable
  public static ResourceLocation getComponentId(ResourceLocation fireType, Fire.Component<?, ?> component) {
    return getFire(fireType).getComponent(component);
  }

  /**
   * Returns the specified component value of the specified fire.<br />
   * Defaults to the component value of the {@link #DEFAULT_FIRE} if the specified fire is not registered.
   *
   * @param fireType fire type.
   * @param component component.
   * @param <R> component registry.
   * @param <T> component value type.
   * @return component value.
   */
  @Nullable
  public static <R, T extends R> T getComponent(ResourceLocation fireType, Fire.Component<R, T> component) {
    return component.getValue(getComponentId(fireType, component));
  }

  /**
   * Returns the path of the component {@link ResourceLocation}.
   *
   * @param fireType fire type.
   * @param component component.
   * @return component {@link ResourceLocation} path.
   */
  @NotNull
  private static String getComponentPath(ResourceLocation fireType, Fire.Component<?, ?> component) {
    return Objects.requireNonNull(getComponentId(fireType, component)).getPath();
  }

  /**
   * Returns the specified component value of the specified fire.<br />
   * Defaults to the component value of the {@link #DEFAULT_FIRE} if the specified fire is not registered.
   *
   * @param fireType fire type.
   * @param component component.
   * @param <R> component registry.
   * @param <T> component value type.
   * @return component value.
   * @throws NullPointerException if the specified fire is registered but doesn't have the specified component.
   */
  @NotNull
  public static <R, T extends R> T getRequiredComponent(ResourceLocation fireType, Fire.Component<R, T> component) throws NullPointerException {
    return Objects.requireNonNull(component.getValue(getComponentId(fireType, component)));
  }

  /**
   * Returns the list of the specified property from all the registered fires.<br />
   * This list will have as many elements as there are registered fires.
   *
   * @param getter property getter.
   * @param <T> property type.
   * @return property list.
   */
  public static <T> List<T> getPropertyList(Function<Fire, T> getter) {
    return FIRES.values().stream().map(getter).toList();
  }

  /**
   * Returns the list of the specified component IDs from all the registered fires.<br />
   * This list won't necessarily have as many elements as there are registered fires because fires without the specified component are filtered out.
   *
   * @param component component.
   * @return component ID list.
   */
  public static List<ResourceLocation> getComponentIdList(Fire.Component<?, ?> component) {
    return FIRES.values().stream().map(fire -> fire.getComponent(component)).filter(Objects::nonNull).toList();
  }

  /**
   * Returns the list of the specified component values from all the registered fires.<br />
   * This list won't necessarily have as many elements as there are registered fires because fires without the specified component are filtered out.
   *
   * @param component component.
   * @param <R> component registry.
   * @param <T> component value type.
   * @return component value list.
   */
  public static <R, T extends R> List<T> getComponentList(Fire.Component<R, T> component) {
    return FIRES.values().stream().map(component::getValue).filter(Objects::nonNull).toList();
  }

  /**
   * Returns whether the given {@code modId} and {@code fireId} represent a valid fire type.
   *
   * @param modId mod ID.
   * @param fireId fire ID.
   * @return whether the given values represent a valid fire type.
   */
  public static boolean isValidType(@Nullable String modId, @Nullable String fireId) {
    return isValidModId(modId) && isValidFireId(fireId);
  }

  /**
   * Returns whether the given {@code modId} and {@code fireId} represent a valid fire type.
   *
   * @param fireType fire type.
   * @return whether the given values represent a valid fire type.
   */
  public static boolean isValidType(@Nullable ResourceLocation fireType) {
    return fireType != null && Strings.isNotBlank(fireType.getNamespace()) && Strings.isNotBlank(fireType.getPath());
  }

  /**
   * Returns whether a fire is registered with the given {@code modId} and {@code fireId}.
   *
   * @param modId mod ID.
   * @param fireId fire ID.
   * @return whether a fire is registered with the given values.
   */
  public static boolean isRegisteredType(@Nullable String modId, @Nullable String fireId) {
    return isValidModId(modId) && isValidFireId(fireId) && isRegisteredType(fireType(modId, fireId));
  }

  /**
   * Returns whether a fire is registered with the given {@code fireType}.
   *
   * @param fireType fire type.
   * @return whether a fire is registered with the given {@code fireType}.
   */
  public static boolean isRegisteredType(@Nullable ResourceLocation fireType) {
    return fireType != null && FIRES.containsKey(fireType);
  }

  /**
   * Returns whether the given fire ID is a valid fire ID.
   *
   * @param fireId fire ID.
   * @return whether the given fire ID is a valid fire ID.
   */
  public static boolean isValidFireId(@Nullable String fireId) {
    return Strings.isNotBlank(fireId) && ResourceLocation.isValidPath(fireId);
  }

  /**
   * Returns whether the given fire ID is a valid and registered fire ID.
   *
   * @param fireId fire ID.
   * @return whether the given fire ID is a valid and registered fire ID.
   */
  public static boolean isRegisteredFireId(@Nullable String fireId) {
    return isValidFireId(fireId) && FIRES.keySet().stream().anyMatch(fireType -> fireType.getPath().equals(fireId));
  }

  /**
   * Returns whether the given mod ID is a valid mod ID.
   *
   * @param modId mod ID
   * @return whether the given mod ID is a valid mod ID.
   */
  public static boolean isValidModId(@Nullable String modId) {
    return Strings.isNotBlank(modId) && ResourceLocation.isValidNamespace(modId);
  }

  /**
   * Returns whether the given mod ID is a valid, loaded and registered mod ID.
   *
   * @param modId mod ID.
   * @return whether the given mod ID is a valid, loaded and registered mod ID.
   */
  public static boolean isRegisteredModId(@Nullable String modId) {
    return isValidModId(modId) && FIRES.keySet().stream().anyMatch(fireType -> fireType.getNamespace().equals(modId));
  }

  /**
   * Returns the closest well-formed fire type from the given {@code modId} and {@code fireId}.
   *
   * @param modId mod ID.
   * @param fireId fire ID.
   * @return the closest well-formed fire type.
   */
  public static ResourceLocation sanitize(@Nullable String modId, @Nullable String fireId) {
    return isValidModId(modId) && isValidModId(fireId) ? sanitize(fireType(modId, fireId)) : DEFAULT_FIRE_TYPE;
  }

  /**
   * Returns the closest well-formed fire type from the given {@code fireType}.
   *
   * @param fireType fire type.
   * @return the closest well-formed fire type.
   */
  public static ResourceLocation sanitize(@Nullable ResourceLocation fireType) {
    return isValidType(fireType) ? fireType : DEFAULT_FIRE_TYPE;
  }

  /**
   * Returns the closest well-formed and registered fire type from the given {@code modId} and {@code fireId}.
   *
   * @param modId mod ID.
   * @param fireId fire ID.
   * @return the closest well-formed and registered fire type.
   */
  public static ResourceLocation ensure(@Nullable String modId, @Nullable String fireId) {
    String trimmedModId = modId == null ? "" : modId.trim();
    String trimmedFireId = fireId == null ? "" : fireId.trim();
    return isValidModId(trimmedModId) && isValidFireId(trimmedFireId) ? ensure(ResourceLocation.fromNamespaceAndPath(trimmedModId, trimmedFireId)) : DEFAULT_FIRE_TYPE;
  }

  /**
   * Returns the closest well-formed and registered fire type from the given {@code fireType}.
   *
   * @param fireType fire type.
   * @return the closest well-formed and registered fire type.
   */
  public static ResourceLocation ensure(@Nullable ResourceLocation fireType) {
    return isRegisteredType(fireType) ? fireType : DEFAULT_FIRE_TYPE;
  }

  /**
   * Returns the list of all fire types.
   *
   * @return the list of all fire types.
   */
  public static List<ResourceLocation> getFireTypes() {
    return FIRES.keySet().stream().toList();
  }

  /**
   * Returns the list of all registered fire IDs.
   *
   * @return the list of all registered fire IDs.
   */
  public static List<String> getFireIds() {
    return FIRES.keySet().stream().map(ResourceLocation::getPath).toList();
  }

  /**
   * Returns the list of all registered mod IDs.
   *
   * @return the list of all registered mod IDs.
   */
  public static List<String> getModIds() {
    return FIRES.keySet().stream().map(ResourceLocation::getNamespace).toList();
  }

  /**
   * Writes to the given {@link CompoundTag} the given {@code fireType}.<br />
   * If the given {@code fireType} is not registered, {@link #DEFAULT_FIRE_TYPE} will be used instead.
   *
   * @param tag {@link CompoundTag} to write to.
   * @param fireType fire type to save.
   */
  public static void writeTag(CompoundTag tag, @Nullable ResourceLocation fireType) {
    tag.putString(FIRE_TYPE_TAG, ensure(fireType).toString());
  }

  /**
   * Reads the fire type from the given {@link CompoundTag}.
   *
   * @param tag {@link CompoundTag} to read from.
   * @return the fire type read from the given {@link CompoundTag}.
   */
  public static ResourceLocation readTag(CompoundTag tag) {
    return ensure(ResourceLocation.tryParse(tag.getString(FIRE_TYPE_TAG)));
  }

  /**
   * Set on fire the given entity for the given seconds with the given fire type.
   *
   * @param entity {@link Entity} to set on fire.
   * @param seconds amount of seconds the fire should last for.
   * @param fireType fire type.
   */
  public static void setOnFire(Entity entity, float seconds, ResourceLocation fireType) {
    entity.igniteForSeconds(seconds);
    ((FireTypeChanger) entity).setFireType(ensure(fireType));
  }

  /**
   * Hurts or heals the given {@code entity}.<br />
   * Also applies the custom fire behavior.
   *
   * @param entity entity to hurt/heal.
   * @param fireType fire type.
   * @param damageSourceGetter getter for the damage source. See .
   * @return whether the {@code entity} was hurt.
   */
  public static boolean affect(Entity entity, ResourceLocation fireType, BiFunction<Fire, Entity, DamageSource> damageSourceGetter) {
    ((FireTypeChanger) entity).setFireType(ensure(fireType));
    return affect(entity, getDamageSource(entity, fireType, damageSourceGetter), FireManager.getProperty(fireType, Fire::getDamage), FireManager.getProperty(fireType, Fire::invertHealAndHarm));
  }

  /**
   * Hurts or heals the given {@code entity}.<br />
   * Also applies the custom fire behavior.
   *
   * @param entity entity to hurt/heal.
   * @param damageSource damage source.
   * @param damage damage/heal amount.
   * @param invertHealAndHarm whether to invert heal and harm.
   * @return whether the {@code entity} was hurt.
   */
  private static boolean affect(Entity entity, DamageSource damageSource, float damage, boolean invertHealAndHarm) {
    Predicate<Entity> behavior = FireManager.getProperty(((FireTyped) entity).getFireType(), Fire::getBehavior);
    if (behavior.test(entity) && Float.compare(damage, 0) != 0) {
      if (damage > 0) {
        if (entity instanceof LivingEntity livingEntity) {
          if (livingEntity.isInvertedHealAndHarm() && invertHealAndHarm) {
            livingEntity.heal(damage);
            return false;
          }
          return livingEntity.hurt(damageSource, damage);
        }
        return entity.hurt(damageSource, damage);
      }
      if (entity instanceof LivingEntity livingEntity) {
        if (livingEntity.isInvertedHealAndHarm() && invertHealAndHarm) {
          return livingEntity.hurt(damageSource, -damage);
        }
        livingEntity.heal(-damage);
        return false;
      }
    }
    return false;
  }

  /**
   * Safety methods to avoid data flow warnings for strings that are nullable but really aren't.
   *
   * @param modId mod ID.
   * @param fireId fire ID.
   * @return {@link ResourceLocation}.
   */
  private static ResourceLocation fireType(@Nullable String modId, @Nullable String fireId) {
    return ResourceLocation.fromNamespaceAndPath(Objects.requireNonNull(modId), Objects.requireNonNull(fireId));
  }
}
