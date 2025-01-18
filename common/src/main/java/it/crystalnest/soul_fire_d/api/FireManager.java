package it.crystalnest.soul_fire_d.api;

import it.crystalnest.cobweb.api.pack.dynamic.DynamicDataPack;
import it.crystalnest.cobweb.api.pack.dynamic.DynamicTagBuilder;
import it.crystalnest.cobweb.api.registry.CobwebEntry;
import it.crystalnest.cobweb.api.registry.CobwebRegister;
import it.crystalnest.cobweb.api.registry.CobwebRegistry;
import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.QuadriFunction;
import it.crystalnest.soul_fire_d.api.block.CustomCampfireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomFireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomLanternBlock;
import it.crystalnest.soul_fire_d.api.block.CustomTorchBlock;
import it.crystalnest.soul_fire_d.api.block.CustomWallTorchBlock;
import it.crystalnest.soul_fire_d.api.block.entity.CustomCampfireBlockEntity;
import it.crystalnest.soul_fire_d.api.block.entity.DynamicBlockEntityType;
import it.crystalnest.soul_fire_d.api.type.FireTypeChanger;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import org.apache.commons.lang3.function.TriFunction;
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
import java.util.function.BiConsumer;
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
   * Attempts to register the given {@link Fire}.<br>
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
   * Unregisters the specified fire.<br>
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
   * Registers the source block for the specified fire from the given constructor.
   *
   * @param fireType fire type.
   * @param color light color.
   * @param constructor {@link CustomFireBlock} constructor.
   * @param <T> source block type.
   * @return {@link CobwebEntry} for the source block.
   */
  public static <T extends CustomFireBlock> CobwebEntry<T> registerFireSource(ResourceLocation fireType, MapColor color, BiFunction<ResourceLocation, BlockBehaviour.Properties, T> constructor) {
    CobwebEntry<T> source = CobwebRegistry.ofBlocks(fireMod(fireType)).registerBlock(FireManager.getComponentPath(fireType, Fire.Component.SOURCE_BLOCK), properties -> constructor.apply(fireType, properties.mapColor(color)));
    FIRE_SOURCE_TAGS.add(() -> DynamicTagBuilder.of(Registries.BLOCK, BlockTags.FIRE).addElement(source.get()));
    return source;
  }

  /**
   * Registers the source block for the specified fire from the given constructor.
   *
   * @param fireType fire type.
   * @param base {@link CustomFireBlock#base}.
   * @param color light color.
   * @param constructor {@link CustomFireBlock} constructor.
   * @param <T> source block type.
   * @return {@link CobwebEntry} for the source block.
   */
  public static <T extends CustomFireBlock> CobwebEntry<T> registerFireSource(ResourceLocation fireType, TagKey<Block> base, MapColor color, TriFunction<ResourceLocation, TagKey<Block>, BlockBehaviour.Properties, T> constructor) {
    return registerFireSource(fireType, color, (type, properties) -> constructor.apply(type, base, properties));
  }

  /**
   * Registers the source block for the specified fire from the given constructor.
   *
   * @param fireType fire type.
   * @param constructor {@link CustomCampfireBlock} constructor.
   * @param <T> campfire block type.
   * @return {@link CobwebEntry} for the campfire block.
   */
  public static <T extends CustomCampfireBlock> CobwebEntry<T> registerCampfire(ResourceLocation fireType, BiFunction<ResourceLocation, BlockBehaviour.Properties, T> constructor) {
    CobwebEntry<T> campfire = CobwebRegistry.ofBlocks(fireMod(fireType)).registerBlock(FireManager.getComponentPath(fireType, Fire.Component.CAMPFIRE_BLOCK), properties -> constructor.apply(fireType, properties));
    FIRE_SOURCE_TAGS.add(() -> DynamicTagBuilder.of(Registries.BLOCK, BlockTags.CAMPFIRES).addElement(campfire.get()));
    return campfire;
  }

  /**
   * Registers the source block for the specified fire from the given constructor.
   *
   * @param fireType fire type.
   * @param spawnParticles whether to spawn crackling particles.
   * @param constructor {@link CustomCampfireBlock} constructor.
   * @param <T> campfire block type.
   * @return {@link CobwebEntry} for the campfire block.
   */
  public static <T extends CustomCampfireBlock> CobwebEntry<T> registerCampfire(ResourceLocation fireType, boolean spawnParticles, TriFunction<ResourceLocation, Boolean, BlockBehaviour.Properties, T> constructor) {
    return registerCampfire(fireType, (type, properties) -> constructor.apply(type, spawnParticles, properties));
  }

  /**
   * Registers the campfire item for the specified fire.<br>
   * Must be called <strong>after</strong> {@link #registerCampfire}.
   *
   * @param fireType fire type.
   * @return {@link CobwebEntry} for the campfire item.
   */
  public static CobwebEntry<BlockItem> registerCampfireItem(ResourceLocation fireType) {
    return registerCampfireItem(fireType, BlockItem::new);
  }

  /**
   * Registers the campfire item for the specified fire from the given constructor.<br>
   * Must be called <strong>after</strong> {@link #registerCampfire}.
   *
   * @param fireType fire type.
   * @param constructor {@link BlockItem} constructor.
   * @param <T> item type.
   * @return {@link CobwebEntry} for the campfire item.
   */
  public static <T extends BlockItem> CobwebEntry<T> registerCampfireItem(ResourceLocation fireType, BiFunction<Block, Item.Properties, T> constructor) {
    return registerCampfireItem(fireType, constructor, new Item.Properties());
  }

  /**
   * Registers the campfire item for the specified fire from the given constructor.<br>
   * Must be called <strong>after</strong> {@link #registerCampfire}.
   *
   * @param fireType fire type.
   * @param constructor {@link BlockItem} constructor.
   * @param properties item properties.
   * @param <T> item type.
   * @return {@link CobwebEntry} for the campfire item.
   */
  public static <T extends BlockItem> CobwebEntry<T> registerCampfireItem(ResourceLocation fireType, BiFunction<Block, Item.Properties, T> constructor, Item.Properties properties) {
    return CobwebRegistry.ofItems(fireMod(fireType)).registerBlockItemLike(FireManager.getComponentPath(fireType, Fire.Component.CAMPFIRE_ITEM), () -> FireManager.getRequiredComponent(fireType, Fire.Component.CAMPFIRE_BLOCK), properties, constructor);
  }

  /**
   * Registers the particle type for the specified fire.
   *
   * @param fireType fire type.
   * @return {@link CobwebEntry} for the particle type.
   */
  public static CobwebEntry<SimpleParticleType> registerParticle(ResourceLocation fireType) {
    return registerParticle(fireType, () -> new SimpleParticleType(false));
  }

  /**
   * Registers the particle type for the specified fire from the given supplier.<br>
   * Make sure your particle implements {@link ParticleOptions} if you are going to register a custom torch too.<br>
   * If it's not a subclass of {@link SimpleParticleType}, you also need to register a {@link ParticleProvider} for your particle.
   *
   * @param fireType fire type.
   * @param supplier {@link SimpleParticleType} supplier.
   * @param <T> particle type.
   * @return {@link CobwebEntry} for the particle type.
   */
  public static <T extends SimpleParticleType> CobwebEntry<T> registerParticle(ResourceLocation fireType, Supplier<T> supplier) {
    return CobwebRegistry.of(Registries.PARTICLE_TYPE, fireMod(fireType)).register(FireManager.getComponentPath(fireType, Fire.Component.FLAME_PARTICLE), supplier);
  }

  /**
   * Registers the pair of torch and wall torch blocks for the specified fire.<br>
   * Must be called <strong>after</strong> {@link #registerParticle}.<br>
   * Make sure your registered particle implements {@link ParticleOptions}.
   *
   * @param fireType fire type.
   * @return pair of {@link CobwebEntry}s for the torch and wall torch blocks.
   */
  public static Pair<CobwebEntry<CustomTorchBlock>, CobwebEntry<CustomWallTorchBlock>> registerTorch(ResourceLocation fireType) {
    return registerTorch(fireType, CustomTorchBlock::new, CustomWallTorchBlock::new);
  }

  /**
   * Registers the pair of torch and wall torch blocks for the specified fire from the given constructors.<br>
   * Must be called <strong>after</strong> {@link #registerParticle}.<br>
   * Make sure your registered particle implements {@link ParticleOptions}.
   *
   * @param fireType fire type.
   * @param torchSupplier {@link CustomTorchBlock} constructor.
   * @param wallTorchSupplier {@link CustomWallTorchBlock} constructor.
   * @param <T> torch block type.
   * @param <W> wall torch block type.
   * @return pair of {@link CobwebEntry}s for torch and wall torch blocks.
   */
  public static <T extends CustomTorchBlock, W extends CustomWallTorchBlock> Pair<CobwebEntry<T>, CobwebEntry<W>> registerTorch(
    ResourceLocation fireType,
    TriFunction<ResourceLocation, Supplier<SimpleParticleType>, BlockBehaviour.Properties, T> torchSupplier,
    TriFunction<ResourceLocation, Supplier<SimpleParticleType>, BlockBehaviour.Properties, W> wallTorchSupplier
  ) {
    CobwebRegister.Blocks blocks = CobwebRegistry.ofBlocks(fireMod(fireType));
    return Pair.of(
      blocks.registerBlock(FireManager.getComponentPath(fireType, Fire.Component.TORCH_BLOCK), properties -> torchSupplier.apply(fireType, () -> getRequiredComponent(fireType, Fire.Component.FLAME_PARTICLE), properties)),
      blocks.registerBlock(FireManager.getComponentPath(fireType, Fire.Component.WALL_TORCH_BLOCK), properties -> wallTorchSupplier.apply(fireType, () -> getRequiredComponent(fireType, Fire.Component.FLAME_PARTICLE), properties))
    );
  }

  /**
   * Registers the torch item for the specified fire.<br>
   * Must be called <strong>after</strong> {@link #registerTorch}.
   *
   * @param fireType fire type.
   * @return {@link CobwebEntry} for the torch item.
   */
  public static CobwebEntry<StandingAndWallBlockItem> registerTorchItem(ResourceLocation fireType) {
    return registerTorchItem(fireType, (torch, wallTorch, properties) -> new StandingAndWallBlockItem(torch, wallTorch, Direction.DOWN, properties));
  }

  /**
   * Registers the torch item for the specified fire from the given constructor.<br>
   * Must be called <strong>after</strong> {@link #registerTorch}.
   *
   * @param fireType fire type.
   * @param constructor {@link StandingAndWallBlockItem} constructor.
   * @param <T> torch item type.
   * @return {@link CobwebEntry} for the torch item.
   */
  public static <T extends StandingAndWallBlockItem> CobwebEntry<T> registerTorchItem(ResourceLocation fireType, TriFunction<Block, Block, Item.Properties, T> constructor) {
    return registerTorchItem(fireType, constructor, new Item.Properties());
  }

  /**
   * Registers the torch item for the specified fire from the given constructor and properties.<br>
   * Must be called <strong>after</strong> {@link #registerTorch}.
   *
   * @param fireType fire type.
   * @param constructor {@link StandingAndWallBlockItem} constructor.
   * @param properties item properties.
   * @param <T> torch item type.
   * @return {@link CobwebEntry} for the torch item.
   */
  public static <T extends StandingAndWallBlockItem> CobwebEntry<T> registerTorchItem(ResourceLocation fireType, TriFunction<Block, Block, Item.Properties, T> constructor, Item.Properties properties) {
    return CobwebRegistry.ofItems(fireMod(fireType)).registerBlockItemLike(
      FireManager.getComponentPath(fireType, Fire.Component.TORCH_ITEM),
      () -> FireManager.getRequiredComponent(fireType, Fire.Component.TORCH_BLOCK),
      properties,
      (torch, props) -> constructor.apply(torch, FireManager.getRequiredComponent(fireType, Fire.Component.WALL_TORCH_BLOCK), props)
    );
  }

  /**
   * Registers the lantern block for the specified fire.
   *
   * @param fireType fire type.
   * @return {@link CobwebEntry} for the lantern block.
   */
  public static CobwebEntry<CustomLanternBlock> registerLantern(ResourceLocation fireType) {
    return registerLantern(fireType, CustomLanternBlock::new);
  }

  /**
   * Registers the lantern block for the specified fire from the given constructor.
   *
   * @param fireType fire type.
   * @param constructor {@link CustomLanternBlock} constructor.
   * @param <T> lantern block type.
   * @return {@link CobwebEntry} for the lantern block.
   */
  public static <T extends CustomLanternBlock> CobwebEntry<T> registerLantern(ResourceLocation fireType, BiFunction<ResourceLocation, BlockBehaviour.Properties, T> constructor) {
    return CobwebRegistry.ofBlocks(fireMod(fireType)).registerBlock(FireManager.getComponentPath(fireType, Fire.Component.LANTERN_BLOCK), properties -> constructor.apply(fireType, properties));
  }

  /**
   * Registers the lantern item for the specified fire.<br>
   * Must be called <strong>after</strong> {@link #registerLantern}.
   *
   * @param fireType fire type.
   * @return {@link CobwebEntry} for the lantern item.
   */
  public static CobwebEntry<BlockItem> registerLanternItem(ResourceLocation fireType) {
    return registerLanternItem(fireType, BlockItem::new);
  }

  /**
   * Registers the lantern item for the specified fire from the given constructor.<br>
   * Must be called <strong>after</strong> {@link #registerLantern}.
   *
   * @param fireType fire type.
   * @param constructor {@link BlockItem} constructor.
   * @param <T> item type.
   * @return {@link CobwebEntry} for the lantern item.
   */
  public static <T extends BlockItem> CobwebEntry<T> registerLanternItem(ResourceLocation fireType, BiFunction<Block, Item.Properties, T> constructor) {
    return registerLanternItem(fireType, constructor, new Item.Properties());
  }

  /**
   * Registers the lantern item for the specified fire from the given constructor.<br>
   * Must be called <strong>after</strong> {@link #registerLantern}.
   *
   * @param fireType fire type.
   * @param constructor {@link BlockItem} constructor.
   * @param properties item properties.
   * @param <T> item type.
   * @return {@link CobwebEntry} for the lantern item.
   */
  public static <T extends BlockItem> CobwebEntry<T> registerLanternItem(ResourceLocation fireType, BiFunction<Block, Item.Properties, T> constructor, Item.Properties properties) {
    return CobwebRegistry.ofItems(fireMod(fireType)).registerBlockItemLike(FireManager.getComponentPath(fireType, Fire.Component.LANTERN_ITEM), () -> FireManager.getRequiredComponent(fireType, Fire.Component.LANTERN_BLOCK), properties, constructor);
  }

  /**
   * Returns the {@link Fire} registered with the given {@code id}.<br>
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
   * Returns the {@link Fire} registered with the given {@code id}.<br>
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
   * Returns the specified property of the specified fire.<br>
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
   * Returns the specified component of the specified fire.<br>
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
   * Returns the specified component value of the specified fire.<br>
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
   * Returns the specified component value of the specified fire.<br>
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
   * Returns the list of the specified property from all the registered fires.<br>
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
   * Returns the list of the specified component IDs from all the registered fires.<br>
   * This list won't necessarily have as many elements as there are registered fires because fires without the specified component are filtered out.
   *
   * @param component component.
   * @return component ID list.
   */
  public static List<ResourceLocation> getComponentIdList(Fire.Component<?, ?> component) {
    return FIRES.values().stream().map(fire -> fire.getComponent(component)).filter(Objects::nonNull).toList();
  }

  /**
   * Returns the list of the specified component values from all the registered fires.<br>
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
    return fireType != null && Strings.isNotBlank(fireMod(fireType)) && Strings.isNotBlank(fireType.getPath());
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
    return isValidModId(modId) && FIRES.keySet().stream().anyMatch(fireType -> fireMod(fireType).equals(modId));
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
   * Writes to the given {@link CompoundTag} the given {@code fireType}.<br>
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
    setOnFire(entity, seconds, fireType, Entity::igniteForSeconds);
  }

  /**
   * Set on fire the given entity for the given seconds with the given fire type.<br>
   * This is for internal use only (or for mixin usage). Use {@link #setOnFire(Entity, float, ResourceLocation)} instead.
   *
   * @param entity {@link Entity} to set on fire.
   * @param seconds amount of seconds the fire should last for.
   * @param fireType fire type.
   * @param setOnFireFunction how to set the entity on fire.
   */
  @ApiStatus.Internal
  public static void setOnFire(Entity entity, float seconds, ResourceLocation fireType, BiConsumer<Entity, Float> setOnFireFunction) {
    setOnFireFunction.accept(entity, seconds);
    ((FireTypeChanger) entity).setFireType(ensure(fireType));
  }

  /**
   * Hurts or heals the given {@code entity}.<br>
   * Also applies the custom fire behavior.
   *
   * @param entity entity to hurt/heal.
   * @param fireType fire type.
   * @param damageSourceGetter getter for the damage source. See .
   * @return whether the {@code entity} was hurt.
   */
  public static boolean affect(Entity entity, ResourceLocation fireType, BiFunction<Fire, Entity, DamageSource> damageSourceGetter) {
    return affect(entity, fireType, damageSourceGetter, Entity::hurtServer);
  }

  /**
   * Hurts or heals the given {@code entity}.<br>
   * Also applies the custom fire behavior.<br>
   * This is for internal use only (or for mixin usage). Use {@link #affect(Entity, ResourceLocation, BiFunction)} instead.
   *
   * @param entity entity to hurt/heal.
   * @param fireType fire type.
   * @param damageSourceGetter getter for the damage source. See {@link #getDamageSource(Entity, ResourceLocation, BiFunction)}.
   * @return whether the {@code entity} was hurt.
   */
  @ApiStatus.Internal
  public static boolean affect(Entity entity, ResourceLocation fireType, BiFunction<Fire, Entity, DamageSource> damageSourceGetter, TriFunction<Entity, DamageSource, Float, Void> hurtFunction) {
    return affect(entity, fireType, damageSourceGetter, (e, l, ds, d) -> {
      hurtFunction.apply(e, ds, d);
      return true;
    });
  }

  /**
   * Hurts or heals the given {@code entity}.<br>
   * Also applies the custom fire behavior.<br>
   * This is for internal use only (or for mixin usage). Use {@link #affect(Entity, ResourceLocation, BiFunction)} instead.
   *
   * @param entity entity to hurt/heal.
   * @param fireType fire type.
   * @param damageSourceGetter getter for the damage source. See {@link #getDamageSource(Entity, ResourceLocation, BiFunction)}.
   * @return whether the {@code entity} was hurt.
   */
  @ApiStatus.Internal
  public static boolean affect(Entity entity, ResourceLocation fireType, BiFunction<Fire, Entity, DamageSource> damageSourceGetter, QuadriFunction<Entity, ServerLevel, DamageSource, Float, Boolean> hurtFunction) {
    ((FireTypeChanger) entity).setFireType(ensure(fireType));
    return affect(entity, getDamageSource(entity, fireType, damageSourceGetter), FireManager.getProperty(fireType, Fire::getDamage), FireManager.getProperty(fireType, Fire::invertHealAndHarm), hurtFunction);
  }

  /**
   * Hurts or heals the given {@code entity}.<br>
   * Also applies the custom fire behavior.<br>
   * This is for internal use only (or for mixin usage). Use {@link #affect(Entity, ResourceLocation, BiFunction)} instead.
   *
   * @param entity entity to hurt/heal.
   * @param damageSource damage source.
   * @param damage damage/heal amount.
   * @param invertHealAndHarm whether to invert heal and harm.
   * @param hurtFunction how to harm the {@code entity}.
   * @return whether the {@code entity} was hurt.
   */
  private static boolean affect(Entity entity, DamageSource damageSource, float damage, boolean invertHealAndHarm, QuadriFunction<Entity, ServerLevel, DamageSource, Float, Boolean> hurtFunction) {
    Predicate<Entity> behavior = FireManager.getProperty(((FireTyped) entity).getFireType(), Fire::getBehavior);
    if (entity.level() instanceof ServerLevel level && behavior.test(entity) && Float.compare(damage, 0) != 0) {
      if (damage > 0) {
        if (entity instanceof LivingEntity livingEntity) {
          if (livingEntity.isInvertedHealAndHarm() && invertHealAndHarm) {
            livingEntity.heal(damage);
            return false;
          }
          return hurtFunction.apply(livingEntity, level, damageSource, damage);
        }
        return hurtFunction.apply(entity, level, damageSource, damage);
      }
      if (entity instanceof LivingEntity livingEntity) {
        if (livingEntity.isInvertedHealAndHarm() && invertHealAndHarm) {
          return hurtFunction.apply(livingEntity, level, damageSource, -damage);
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

  /**
   * Returns the namespace (mod ID) of the given fire type.
   *
   * @param fireType fire type.
   * @return fire mod ID.
   */
  private static String fireMod(ResourceLocation fireType) {
    return fireType.getNamespace();
  }

  /**
   * Shorthand for getting the {@link Fire#light} property of the specified fire.
   *
   * @param fireType fire type.
   * @return fire light property.
   */
  public static int light(ResourceLocation fireType) {
    return FireManager.getProperty(fireType, Fire::getLight);
  }
}
