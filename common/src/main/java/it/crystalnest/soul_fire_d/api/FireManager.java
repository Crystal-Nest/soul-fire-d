package it.crystalnest.soul_fire_d.api;

import com.google.common.base.Suppliers;
import it.crystalnest.cobweb.api.pack.DynamicDataPack;
import it.crystalnest.cobweb.api.pack.DynamicTagBuilder;
import it.crystalnest.cobweb.api.registry.CobwebRegistry;
import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.block.CustomCampfireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomFireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomLanternBlock;
import it.crystalnest.soul_fire_d.api.block.CustomTorchBlock;
import it.crystalnest.soul_fire_d.api.block.CustomWallTorchBlock;
import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFireAspectEnchantment;
import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFlameEnchantment;
import it.crystalnest.soul_fire_d.api.type.FireTypeChanger;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import it.crystalnest.soul_fire_d.platform.Services;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Static manager for registered Fires.
 */
public final class FireManager {
  /**
   * Id of the tag used to save Fire Type.
   */
  public static final String FIRE_TYPE_TAG = "FireType";

  /**
   * Fire Type of Vanilla Fire.
   */
  public static final ResourceLocation DEFAULT_FIRE_TYPE = new ResourceLocation("");

  /**
   * Fire Type of Soul Fire.
   */
  public static final ResourceLocation SOUL_FIRE_TYPE = new ResourceLocation("soul");

  /**
   * Default {@link Fire} used as fallback to retrieve default properties.
   */
  public static final Fire DEFAULT_FIRE = new Fire(
    DEFAULT_FIRE_TYPE,
    FireBuilder.DEFAULT_LIGHT,
    FireBuilder.DEFAULT_DAMAGE,
    FireBuilder.DEFAULT_INVERT_HEAL_AND_HARM,
    true,
    FireBuilder.DEFAULT_IN_FIRE_GETTER,
    FireBuilder.DEFAULT_ON_FIRE_GETTER,
    null,
    new ResourceLocation("fire"),
    new ResourceLocation("campfire"),
    null,
    null
  );

  private static final DynamicDataPack FIRE_SOURCE_TAGS = DynamicDataPack.named(new ResourceLocation(Constants.MOD_ID, "fire_source_tags"));

  private static final DynamicDataPack CAMPFIRE_TAGS = DynamicDataPack.named(new ResourceLocation(Constants.MOD_ID, "campfire_tags"));

  /**
   * {@link ConcurrentHashMap} of all registered {@link Fire Fires}.
   */
  private static final ConcurrentHashMap<ResourceLocation, Fire> FIRES = new ConcurrentHashMap<>();

  static {
    FIRE_SOURCE_TAGS.register();
    CAMPFIRE_TAGS.register();
  }

  private FireManager() {}

  public static <T> T getFireProperty(ResourceLocation fireType, Function<Fire, T> getter) {
    return getter.apply(FIRES.getOrDefault(fireType, DEFAULT_FIRE));
  }

  public static <T extends CustomFireBlock> Supplier<T> registerFireSource(ResourceLocation fireType, Function<ResourceLocation, T> supplier) {
    Supplier<T> source = Suppliers.memoize(() -> supplier.apply(fireType));
    FIRE_SOURCE_TAGS.add(() -> DynamicTagBuilder.of(Registries.BLOCK, BlockTags.FIRE).addElement(source.get()));
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(FireManager.getFire(fireType).getSource().orElseThrow().getPath(), source);
  }

  public static Supplier<CustomFireBlock> registerFireSource(ResourceLocation fireType, TagKey<Block> base, MapColor color) {
    return registerFireSource(fireType, type -> new CustomFireBlock(type, base, color));
  }

  public static Supplier<CustomFireBlock> registerFireSource(ResourceLocation fireType, TagKey<Block> base, BlockBehaviour.Properties properties) {
    return registerFireSource(fireType, type -> new CustomFireBlock(type, base, properties));
  }

  public static <T extends CustomCampfireBlock> Supplier<T> registerCampfire(ResourceLocation fireType, Function<ResourceLocation, T> supplier) {
    Supplier<T> campfire = Suppliers.memoize(() -> supplier.apply(fireType));
    CAMPFIRE_TAGS.add(() -> DynamicTagBuilder.of(Registries.BLOCK, BlockTags.CAMPFIRES).addElement(campfire.get()));
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(FireManager.getFire(fireType).getCampfire().orElseThrow().getPath(), campfire);
  }

  public static Supplier<CustomCampfireBlock> registerCampfire(ResourceLocation fireType, boolean spawnParticles) {
    return registerCampfire(fireType, type -> new CustomCampfireBlock(type, spawnParticles));
  }

  public static Supplier<CustomCampfireBlock> registerCampfire(ResourceLocation fireType, boolean spawnParticles, BlockBehaviour.Properties properties) {
    return registerCampfire(fireType, type -> new CustomCampfireBlock(type, spawnParticles, properties));
  }

  public static Supplier<BlockItem> registerCampfireItem(ResourceLocation fireType, Supplier<? extends Block> campfire) {
    return CobwebRegistry.ofItems(fireType.getNamespace()).register(FireManager.getFire(fireType).getCampfire().orElseThrow().getPath(), () -> new BlockItem(campfire.get(), new Item.Properties()));
  }

  @SafeVarargs
  public static Supplier<BlockEntityType<CampfireBlockEntity>> registerCampfireBlockEntity(ResourceLocation fireType, Supplier<? extends Block>... campfires) {
    return CobwebRegistry.of(Registries.BLOCK_ENTITY_TYPE, fireType.getNamespace()).register(
      FireManager.getFire(fireType).getCampfire().orElseThrow().getPath(),
      () -> BlockEntityType.Builder.of(CampfireBlockEntity::new, Arrays.stream(campfires).map(Supplier::get).toArray(Block[]::new)).build(null)
    );
  }

  public static Supplier<SimpleParticleType> registerParticle(ResourceLocation fireType) {
    return CobwebRegistry.of(Registries.PARTICLE_TYPE, fireType.getNamespace()).register(fireType.getPath() + "_flame", () -> new SimpleParticleType(false));
  }

  public static Supplier<CustomTorchBlock> registerTorch(ResourceLocation fireType, Supplier<SimpleParticleType> particle) {
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(
      fireType.getPath() + "_torch",
      () -> new CustomTorchBlock(fireType, particle, BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY))
    );
  }

  public static Supplier<CustomWallTorchBlock> registerTorch(ResourceLocation fireType, Supplier<SimpleParticleType> particle, Supplier<CustomTorchBlock> torch) {
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(
      fireType.getPath() + "_wall_torch",
      () -> new CustomWallTorchBlock(fireType, particle, BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.WOOD).dropsLike(torch.get()).pushReaction(PushReaction.DESTROY))
    );
  }

  public static Supplier<StandingAndWallBlockItem> registerTorchItem(ResourceLocation fireType, Supplier<CustomTorchBlock> torch, Supplier<CustomWallTorchBlock> wallTorch) {
    return CobwebRegistry.ofItems(fireType.getNamespace()).register(fireType.getPath() + "_torch", () -> new StandingAndWallBlockItem(torch.get(), wallTorch.get(), new Item.Properties(), Direction.DOWN));
  }

  public static Supplier<CustomLanternBlock> registerLantern(ResourceLocation fireType) {
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(
      fireType.getPath() + "_lantern",
      () -> new CustomLanternBlock(fireType, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).forceSolidOn().requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).noOcclusion().pushReaction(PushReaction.DESTROY))
    );
  }

  public static Supplier<BlockItem> registerLanternItem(ResourceLocation fireType, Supplier<? extends LanternBlock> lantern) {
    return CobwebRegistry.ofItems(fireType.getNamespace()).register(fireType.getPath() + "_lantern", () -> new BlockItem(lantern.get(), new Item.Properties()));
  }

  /**
   * Returns a new {@link FireBuilder}.
   *
   * @param modId {@code modId} of the new {@link Fire} to build.
   * @param fireId {@code fireId} of the new {@link Fire} to build.
   * @return a new {@link FireBuilder}.
   */
  public static FireBuilder fireBuilder(String modId, String fireId) {
    return new FireBuilder(modId, fireId);
  }

  /**
   * Returns a new {@link FireBuilder}.
   *
   * @param fireType {@link ResourceLocation} of the new {@link Fire} to build.
   * @return a new {@link FireBuilder}.
   */
  public static FireBuilder fireBuilder(ResourceLocation fireType) {
    return new FireBuilder(fireType);
  }

  /**
   * Attempts to register the given {@link Fire}.
   * <p>
   * If the {@link Fire#fireType} is already registered, logs an error.
   *
   * @param fire {@link Fire} to register.
   * @return whether the registration is successful.
   */
  @Nullable
  public static synchronized Fire registerFire(Fire fire) {
    Fire previous = FIRES.computeIfAbsent(fire.getFireType(), key -> {
      fire.getSource().flatMap(BuiltInRegistries.BLOCK::getOptional).ifPresent(block -> ((FireTypeChanger) block).setFireType(key));
      fire.getCampfire().flatMap(BuiltInRegistries.BLOCK::getOptional).ifPresent(block -> ((FireTypeChanger) block).setFireType(key));
      return fire;
    });
    if (previous != fire) {
      ResourceLocation fireType = fire.getFireType();
      Constants.LOGGER.error("Fire [{}] was already registered with the following value: {}", fireType, FIRES.get(fireType));
      return null;
    }
    return fire;
  }

  /**
   * Attempts to register all the given {@link Fire fires}.
   *
   * @param fires {@link Fire fires} to register.
   * @return an {@link Map} with the outcome of each registration attempt.
   */
  public static synchronized Map<ResourceLocation, @Nullable Fire> registerFires(Fire... fires) {
    return registerFires(List.of(fires));
  }

  /**
   * Attempts to register all the given {@link Fire fires}.
   *
   * @param fires {@link Fire fires} to register.
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
   * Unregisters the specified fire.
   * <p>
   * To be used only internally, do not use elsewhere!
   *
   * @param fireType
   * @return whether the fire was previously registered.
   */
  @ApiStatus.Internal
  public static synchronized boolean unregisterFire(ResourceLocation fireType) {
    return FIRES.remove(fireType) != null;
  }

  /**
   * Returns the list of all registered {@link Fire Fires}.
   *
   * @return the list of all registered {@link Fire Fires}.
   */
  public static List<Fire> getFires() {
    return FIRES.values().stream().toList();
  }

  /**
   * Returns the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns {@link #DEFAULT_FIRE} if no {@link Fire} is registered with the given {@code modId} and {@code fireId}.
   *
   * @param modId
   * @param fireId
   * @return registered {@link Fire} or {@link #DEFAULT_FIRE}.
   */
  public static Fire getFire(String modId, String fireId) {
    return getFire(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns {@link #DEFAULT_FIRE} if no {@link Fire} is registered with the given {@code fireType}.
   *
   * @param fireType
   * @return registered {@link Fire} or {@link #DEFAULT_FIRE}.
   */
  public static Fire getFire(ResourceLocation fireType) {
    return FIRES.getOrDefault(fireType, DEFAULT_FIRE);
  }

  /**
   * Returns whether the given {@code modId} and {@code fireId} represent a valid Fire Type.
   *
   * @param modId
   * @param fireId
   * @return whether the given values represent a valid Fire Type.
   */
  public static boolean isValidType(String modId, String fireId) {
    return isValidModId(modId) && isValidFireId(fireId);
  }

  /**
   * Returns whether a fire is registered with the given {@code modId} and {@code fireId}.
   *
   * @param modId
   * @param fireId
   * @return whether a fire is registered with the given values.
   */
  public static boolean isRegisteredType(String modId, String fireId) {
    return isValidModId(modId) && isValidFireId(fireId) && isRegisteredType(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns whether a fire is registered with the given {@code fireType}.
   *
   * @param fireType
   * @return whether a fire is registered with the given {@code fireType}.
   */
  public static boolean isRegisteredType(ResourceLocation fireType) {
    return fireType != null && FIRES.containsKey(fireType);
  }

  /**
   * Returns whether the given {@code id} is a valid fire id.
   *
   * @param id
   * @return whether the given {@code id} is a valid fire id.
   */
  public static boolean isValidFireId(String id) {
    if (Strings.isNotBlank(id)) {
      try {
        new ResourceLocation(id);
        return true;
      } catch (ResourceLocationException e) {
        return false;
      }
    }
    return false;
  }

  /**
   * Returns whether the given {@code id} is a valid and registered fire id.
   *
   * @param id
   * @return whether the given {@code id} is a valid and registered fire id.
   */
  public static boolean isRegisteredFireId(String id) {
    return isValidFireId(id) && FIRES.keySet().stream().anyMatch(fireType -> fireType.getPath().equals(id));
  }

  /**
   * Returns whether the given {@code id} is a valid mod id.
   *
   * @param id
   * @return whether the given {@code id} is a valid mod id.
   */
  public static boolean isValidModId(String id) {
    if (Strings.isNotBlank(id)) {
      try {
        new ResourceLocation(id, "");
        return true;
      } catch (ResourceLocationException e) {
        return false;
      }
    }
    return false;
  }

  /**
   * Returns whether the given {@code id} is a valid, loaded and registered mod id.
   *
   * @param id
   * @return whether the given {@code id} is a valid, loaded and registered mod id.
   */
  public static boolean isRegisteredModId(String id) {
    return isValidModId(id) && Services.PLATFORM.isModLoaded(id) && FIRES.keySet().stream().anyMatch(fireType -> fireType.getNamespace().equals(id));
  }

  /**
   * Returns the closest well-formed Fire Type from the given {@code modId} and {@code fireId}.
   *
   * @param modId
   * @param fireId
   * @return the closest well-formed Fire Type.
   */
  public static ResourceLocation sanitize(String modId, String fireId) {
    String trimmedModId = modId.trim();
    String trimmedFireId = fireId.trim();
    try {
      return sanitize(new ResourceLocation(trimmedModId, trimmedFireId));
    } catch (ResourceLocationException e) {
      return DEFAULT_FIRE_TYPE;
    }
  }

  /**
   * Returns the closest well-formed Fire Type from the given {@code fireType}.
   *
   * @param fireType
   * @return the closest well-formed Fire Type.
   */
  public static ResourceLocation sanitize(ResourceLocation fireType) {
    if (Strings.isNotBlank(fireType.getNamespace()) && Strings.isNotBlank(fireType.getPath())) {
      return fireType;
    }
    return DEFAULT_FIRE_TYPE;
  }

  /**
   * Returns the closest well-formed and registered Fire Type from the given {@code modId} and {@code fireId}.
   *
   * @param modId
   * @param fireId
   * @return the closest well-formed and registered Fire Type.
   */
  public static ResourceLocation ensure(String modId, String fireId) {
    String trimmedModId = modId.trim();
    String trimmedFireId = fireId.trim();
    try {
      return ensure(new ResourceLocation(trimmedModId, trimmedFireId));
    } catch (ResourceLocationException e) {
      return DEFAULT_FIRE_TYPE;
    }
  }

  /**
   * Returns the closest well-formed and registered Fire Type from the given {@code fireType}.
   *
   * @param fireType
   * @return the closest well-formed and registered Fire Type.
   */
  public static ResourceLocation ensure(ResourceLocation fireType) {
    if (isRegisteredType(fireType)) {
      return fireType;
    }
    return DEFAULT_FIRE_TYPE;
  }

  /**
   * Returns the list of all Fire Types.
   *
   * @return the list of all Fire Types.
   */
  public static List<ResourceLocation> getFireTypes() {
    return FIRES.keySet().stream().toList();
  }

  /**
   * Returns the list of all registered fire ids.
   *
   * @return the list of all registered fire ids.
   */
  public static List<String> getFireIds() {
    return FIRES.keySet().stream().map(ResourceLocation::getPath).toList();
  }

  /**
   * Returns the list of all registered mod ids.
   *
   * @return the list of all registered mod ids.
   */
  public static List<String> getModIds() {
    return FIRES.keySet().stream().map(ResourceLocation::getNamespace).toList();
  }

  /**
   * Returns the light level of the {@link Fire} registered with the given {@code modId} and {@code fireId}.<br />
   * Returns the default value if no {@link Fire} was registered with the given values.
   *
   * @param modId
   * @param fireId
   * @return the light level of the {@link Fire}.
   */
  public static int getLight(String modId, String fireId) {
    return getLight(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the light level of the {@link Fire} registered with the given {@code fireType}.<br />
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   *
   * @param fireType
   * @return the light level of the {@link Fire}.
   */
  public static int getLight(ResourceLocation fireType) {
    return FIRES.getOrDefault(fireType, DEFAULT_FIRE).getLight();
  }

  /**
   * Returns the damage of the {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given values.
   *
   * @param modId
   * @param fireId
   * @return the damage of the {@link Fire}.
   */
  public static float getDamage(String modId, String fireId) {
    return getDamage(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the damage of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   *
   * @param fireType
   * @return the damage of the {@link Fire}.
   */
  public static float getDamage(ResourceLocation fireType) {
    return FIRES.getOrDefault(fireType, DEFAULT_FIRE).getDamage();
  }

  /**
   * Returns the invertHealAndHarm flag of the {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given values.
   *
   * @param modId
   * @param fireId
   * @return the invertHealAndHarm flag of the {@link Fire}.
   */
  public static boolean getInvertHealAndHarm(String modId, String fireId) {
    return getInvertHealAndHarm(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the invertHealAndHarm flag of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   *
   * @param fireType
   * @return the invertHealAndHarm flag of the {@link Fire}.
   */
  public static boolean getInvertHealAndHarm(ResourceLocation fireType) {
    return FIRES.getOrDefault(fireType, DEFAULT_FIRE).invertHealAndHarm();
  }

  /**
   * Returns the in damage source of the {@link Fire} registered with the given {@code modId} and {@code fireId} for the given {@link Entity}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given values.
   *
   * @param entity
   * @param modId
   * @param fireId
   * @return the in damage source of the {@link Fire} for the {@link Entity}.
   */
  public static DamageSource getInFireDamageSourceFor(Entity entity, String modId, String fireId) {
    return getInFireDamageSourceFor(entity, new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the in damage source of the {@link Fire} registered with the given {@code fireType} for the given {@link Entity}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   *
   * @param entity
   * @param fireType
   * @return the in damage source of the {@link Fire} for the {@link Entity}.
   */
  public static DamageSource getInFireDamageSourceFor(Entity entity, ResourceLocation fireType) {
    return FIRES.getOrDefault(fireType, DEFAULT_FIRE).getInFire(entity);
  }

  /**
   * Returns the on damage source of the {@link Fire} registered with the given {@code modId} and {@code fireId} for the given {@link Entity}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given values.
   *
   * @param entity
   * @param modId
   * @param fireId
   * @return the on damage source of the {@link Fire} for the {@link Entity}.
   */
  public static DamageSource getOnFireDamageSourceFor(Entity entity, String modId, String fireId) {
    return getOnFireDamageSourceFor(entity, new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the on damage source of the {@link Fire} registered with the given {@code fireType} for the given {@link Entity}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   *
   * @param entity
   * @param fireType
   * @return the on damage source of the {@link Fire} for the {@link Entity}.
   */
  public static DamageSource getOnFireDamageSourceFor(Entity entity, ResourceLocation fireType) {
    return FIRES.getOrDefault(fireType, DEFAULT_FIRE).getOnFire(entity);
  }

  /**
   * Returns the source block associated with {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given values.
   *
   * @param modId
   * @param fireId
   * @return the source block associated with {@link Fire}.
   */
  public static Block getSourceBlock(String modId, String fireId) {
    return getSourceBlock(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the fire source block associated with {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   *
   * @param fireType
   * @return the fire source block associated with {@link Fire}.
   */
  public static Block getSourceBlock(ResourceLocation fireType) {
    return BuiltInRegistries.BLOCK.getOptional(FIRES.getOrDefault(fireType, DEFAULT_FIRE).getSource().orElse(DEFAULT_FIRE.getSource().orElseThrow())).orElse(Blocks.FIRE);
  }

  /**
   * Returns the campfire block associated with {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given values.
   *
   * @param modId
   * @param fireId
   * @return the campfire block associated with {@link Fire}.
   */
  public static Block getCampfireBlock(String modId, String fireId) {
    return getCampfireBlock(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the source block associated with {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   *
   * @param fireType
   * @return the source block associated with {@link Fire}.
   */
  public static Block getCampfireBlock(ResourceLocation fireType) {
    return BuiltInRegistries.BLOCK.getOptional(FIRES.getOrDefault(fireType, DEFAULT_FIRE).getCampfire().orElse(DEFAULT_FIRE.getCampfire().orElseThrow())).orElse(Blocks.CAMPFIRE);
  }

  /**
   * Returns the list of all Fire Aspect enchantments registered.
   *
   * @return the list of all Fire Aspect enchantments registered.
   */
  public static List<FireTypedFireAspectEnchantment> getFireAspects() {
    return FIRES.values().stream().map(fire -> getFireAspect(fire.getFireType())).filter(Objects::nonNull).toList();
  }

  /**
   * Returns the list of all Flame enchantments registered.
   *
   * @return the list of all Flame enchantments registered.
   */
  public static List<FireTypedFlameEnchantment> getFlames() {
    return FIRES.values().stream().map(fire -> getFlame(fire.getFireType())).filter(Objects::nonNull).toList();
  }

  /**
   * Returns the Fire Aspect enchantment of the {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns {@code null} if no {@link Fire} was registered with the given values.
   *
   * @param modId
   * @param fireId
   * @return the Fire Aspect enchantment of the {@link Fire}.
   */
  @Nullable
  public static FireTypedFireAspectEnchantment getFireAspect(String modId, String fireId) {
    return getFireAspect(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the Fire Aspect enchantment of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns {@code null} if no {@link Fire} was registered with the given {@code fireType}.
   *
   * @param fireType
   * @return the Fire Aspect enchantment of the {@link Fire}.
   */
  @Nullable
  public static FireTypedFireAspectEnchantment getFireAspect(ResourceLocation fireType) {
    return getFire(fireType).getFireAspect().map(resourceLocation -> (FireTypedFireAspectEnchantment) BuiltInRegistries.ENCHANTMENT.getOptional(resourceLocation).orElse(null)).orElse(null);
  }

  /**
   * Returns the Flame enchantment of the {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns {@code null} if no {@link Fire} was registered with the given values.
   *
   * @param modId
   * @param fireId
   * @return the Flame enchantment of the {@link Fire}.
   */
  @Nullable
  public static FireTypedFlameEnchantment getFlame(String modId, String fireId) {
    return getFlame(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the Flame enchantment of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns {@code null} if no {@link Fire} was registered with the given {@code fireType}.
   *
   * @param fireType
   * @return the Flame enchantment of the {@link Fire}.
   */
  @Nullable
  public static FireTypedFlameEnchantment getFlame(ResourceLocation fireType) {
    return getFire(fireType).getFlame().map(resourceLocation -> (FireTypedFlameEnchantment) BuiltInRegistries.ENCHANTMENT.getOptional(resourceLocation).orElse(null)).orElse(null);
  }

  /**
   * Set on fire the given entity for the given seconds with the given Fire Type.
   *
   * @param entity {@link Entity} to set on fire.
   * @param seconds amount of seconds the fire should last for.
   * @param modId mod id of the fire.
   * @param fireId fire id of the fire.
   */
  public static void setOnFire(Entity entity, int seconds, String modId, String fireId) {
    setOnFire(entity, seconds, new ResourceLocation(modId, fireId));
  }

  /**
   * Set on fire the given entity for the given seconds with the given Fire Type.
   *
   * @param entity {@link Entity} to set on fire.
   * @param seconds amount of seconds the fire should last for.
   * @param fireType {@link ResourceLocation} of the fire.
   */
  public static void setOnFire(Entity entity, int seconds, ResourceLocation fireType) {
    entity.setSecondsOnFire(seconds);
    ((FireTypeChanger) entity).setFireType(ensure(fireType));
  }

  /**
   * Harms (or heals) the given {@code entity} based on the {@link Fire} registered with the given {@code fireId} and {@code modId}.
   * <p>
   * If no {@link Fire} was registered with the given {@code fireId} and {@code modId}, defaults to the default {@code damageSource} and {@code damage} to harm the {@code entity}.
   *
   * @param entity {@link Entity} to harm or heal.
   * @param fireId
   * @param modId
   * @return whether the {@code entity} has been harmed.
   */
  public static boolean damageInFire(Entity entity, String fireId, String modId) {
    return damageInFire(entity, new ResourceLocation(fireId, modId));
  }

  /**
   * Harms (or heals) the given {@code entity} based on the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * If no {@link Fire} was registered with the given {@code fireType}, defaults to the default {@code damageSource} and {@code damage} to harm the {@code entity}.
   *
   * @param entity {@link Entity} to harm or heal.
   * @param fireType
   * @return whether the {@code entity} has been harmed.
   */
  public static boolean damageInFire(Entity entity, ResourceLocation fireType) {
    if (isRegisteredType(fireType)) {
      ((FireTypeChanger) entity).setFireType(fireType);
      return harmOrHeal(entity, getInFireDamageSourceFor(entity, fireType), getDamage(fireType), getInvertHealAndHarm(fireType));
    }
    ((FireTypeChanger) entity).setFireType(DEFAULT_FIRE_TYPE);
    return harmOrHeal(entity, DEFAULT_FIRE.getInFire(entity), DEFAULT_FIRE.getDamage(), DEFAULT_FIRE.invertHealAndHarm());
  }

  /**
   * Harms (or heals) the given {@code entity} based on the {@link Fire} registered with the given {@code fireId} and {@code modId}.
   * <p>
   * If no {@link Fire} was registered with the given {@code fireId} and {@code modId}, defaults to the default {@code damageSource} and {@code damage} to harm the {@code entity}.
   *
   * @param entity {@link Entity} to harm or heal.
   * @param fireId
   * @param modId
   * @return whether the {@code entity} has been harmed.
   */
  public static boolean damageOnFire(Entity entity, String fireId, String modId) {
    return damageOnFire(entity, new ResourceLocation(fireId, modId));
  }

  /**
   * Harms (or heals) the given {@code entity} based on the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * If no {@link Fire} was registered with the given {@code fireType}, defaults to the default {@code damageSource} and {@code damage} to harm the {@code entity}.
   *
   * @param entity {@link Entity} to harm or heal.
   * @param fireType
   * @return whether the {@code entity} has been harmed.
   */
  public static boolean damageOnFire(Entity entity, ResourceLocation fireType) {
    if (isRegisteredType(fireType)) {
      ((FireTypeChanger) entity).setFireType(fireType);
      return harmOrHeal(entity, getOnFireDamageSourceFor(entity, fireType), getDamage(fireType), getInvertHealAndHarm(fireType));
    }
    ((FireTypeChanger) entity).setFireType(DEFAULT_FIRE_TYPE);
    return harmOrHeal(entity, DEFAULT_FIRE.getOnFire(entity), DEFAULT_FIRE.getDamage(), DEFAULT_FIRE.invertHealAndHarm());
  }

  /**
   * Harms or heals the given {@code entity}.
   *
   * @param entity
   * @param damageSource
   * @param damage
   * @param invertHealAndHarm
   * @return whether the {@code entity} has been harmed.
   */
  private static boolean harmOrHeal(Entity entity, DamageSource damageSource, float damage, boolean invertHealAndHarm) {
    Optional<Predicate<Entity>> behavior = FireManager.getFireProperty(((FireTyped) entity).getFireType(), Fire::getBehavior);
    if (behavior.isEmpty() || behavior.get().test(entity)) {
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
   * Writes to the given {@link CompoundTag} the given {@code fireType}.
   * <p>
   * If the given {@code fireType} is not registered, the {@link #DEFAULT_FIRE} will be written instead.
   *
   * @param tag {@link CompoundTag} to write to.
   * @param fireType Fire Type to save.
   */
  public static void writeNbt(CompoundTag tag, ResourceLocation fireType) {
    tag.putString(FIRE_TYPE_TAG, ensure(fireType).toString());
  }

  /**
   * Reads the Fire Type from the given {@link CompoundTag}.
   *
   * @param tag {@link CompoundTag} to read from.
   * @return the Fire Type read from the given {@link CompoundTag}.
   */
  public static ResourceLocation readNbt(CompoundTag tag) {
    return ensure(ResourceLocation.tryParse(tag.getString(FIRE_TYPE_TAG)));
  }
}

