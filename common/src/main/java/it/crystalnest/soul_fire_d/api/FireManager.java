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
import it.crystalnest.soul_fire_d.api.block.entity.DynamicBlockEntityType;
import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFireAspectEnchantment;
import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFlameEnchantment;
import it.crystalnest.soul_fire_d.api.type.FireTypeChanger;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
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
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Static manager for registered Fires.
 */
public final class FireManager {
  /**
   * ID of the tag used to save Fire Type.
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
    entity -> true,
    BuiltInRegistries.BLOCK.getKey(Blocks.FIRE),
    BuiltInRegistries.BLOCK.getKey(Blocks.CAMPFIRE),
    BuiltInRegistries.ENCHANTMENT.getKey(Enchantments.FIRE_ASPECT),
    BuiltInRegistries.ENCHANTMENT.getKey(Enchantments.FLAMING_ARROWS)
  );

  public static final Supplier<DynamicBlockEntityType<CampfireBlockEntity>> CUSTOM_CAMPFIRE_ENTITY_TYPE = CobwebRegistry.of(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).register(
    "custom_campfire",
    () -> new DynamicBlockEntityType<>(CampfireBlockEntity::new, Set.of(), null)
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

  public static <T> T getProperty(ResourceLocation fireType, Function<Fire, T> getter) {
    return getter.apply(FIRES.getOrDefault(fireType, DEFAULT_FIRE));
  }

  @Nullable
  public static <T> T getOptionalProperty(ResourceLocation fireType, Function<Fire, Optional<T>> getter) {
    return getter.apply(FIRES.getOrDefault(fireType, DEFAULT_FIRE)).orElse(null);
  }

  public static <T> T getDefaultedProperty(ResourceLocation fireType, Function<Fire, Optional<T>> getter) {
    return getter.apply(FIRES.getOrDefault(fireType, DEFAULT_FIRE)).orElse(getter.apply(DEFAULT_FIRE).orElseThrow());
  }

  public static <T> List<T> getPropertyList(Function<Fire, T> getter) {
    return FIRES.values().stream().map(getter).toList();
  }

  public static <T> List<T> getOptionalPropertyList(Function<Fire, Optional<T>> getter) {
    return FIRES.values().stream().map(getter).filter(Optional::isPresent).map(Optional::get).toList();
  }

  public static <T> List<T> getDefaultedPropertyList(Function<Fire, Optional<T>> getter) {
    return FIRES.values().stream().map(fire -> getter.apply(fire).orElse(getter.apply(DEFAULT_FIRE).orElseThrow())).toList();
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
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(FireManager.getProperty(fireType, Fire::getCampfire).orElseThrow().getPath(), campfire);
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

  public static Supplier<SimpleParticleType> registerParticle(ResourceLocation fireType) {
    return CobwebRegistry.of(Registries.PARTICLE_TYPE, fireType.getNamespace()).register(getFlameName(fireType.getPath()), () -> new SimpleParticleType(false));
  }

  public static Supplier<CustomTorchBlock> registerTorch(ResourceLocation fireType, Supplier<SimpleParticleType> particle) {
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(
      getTorchName(fireType.getPath()),
      () -> new CustomTorchBlock(fireType, particle, BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY))
    );
  }

  public static Supplier<CustomWallTorchBlock> registerTorch(ResourceLocation fireType, Supplier<SimpleParticleType> particle, Supplier<CustomTorchBlock> torch) {
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(
      getWallTorchName(fireType.getPath()),
      () -> new CustomWallTorchBlock(fireType, particle, BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.WOOD).dropsLike(torch.get()).pushReaction(PushReaction.DESTROY))
    );
  }

  public static Supplier<StandingAndWallBlockItem> registerTorchItem(ResourceLocation fireType, Supplier<CustomTorchBlock> torch, Supplier<CustomWallTorchBlock> wallTorch) {
    return CobwebRegistry.ofItems(fireType.getNamespace()).register(getTorchName(fireType.getPath()), () -> new StandingAndWallBlockItem(torch.get(), wallTorch.get(), new Item.Properties(), Direction.DOWN));
  }

  public static Supplier<CustomLanternBlock> registerLantern(ResourceLocation fireType) {
    return CobwebRegistry.ofBlocks(fireType.getNamespace()).register(
      getLanternName(fireType.getPath()),
      () -> new CustomLanternBlock(fireType, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).forceSolidOn().requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).noOcclusion().pushReaction(PushReaction.DESTROY))
    );
  }

  public static Supplier<BlockItem> registerLanternItem(ResourceLocation fireType, Supplier<? extends LanternBlock> lantern) {
    return CobwebRegistry.ofItems(fireType.getNamespace()).register(getLanternName(fireType.getPath()), () -> new BlockItem(lantern.get(), new Item.Properties()));
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
  public static Fire getFire(@Nullable String modId, @Nullable String fireId) {
    return isValidModId(modId) && isValidFireId(fireId) ? getFire(new ResourceLocation(modId, fireId)) : DEFAULT_FIRE;
  }

  /**
   * Returns the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns {@link #DEFAULT_FIRE} if no {@link Fire} is registered with the given {@code fireType}.
   *
   * @param fireType
   * @return registered {@link Fire} or {@link #DEFAULT_FIRE}.
   */
  public static Fire getFire(@Nullable ResourceLocation fireType) {
    return isValidType(fireType) ? FIRES.getOrDefault(fireType, DEFAULT_FIRE) : DEFAULT_FIRE;
  }

  /**
   * Returns whether the given {@code modId} and {@code fireId} represent a valid Fire Type.
   *
   * @param modId
   * @param fireId
   * @return whether the given values represent a valid Fire Type.
   */
  public static boolean isValidType(@Nullable String modId, @Nullable String fireId) {
    return isValidModId(modId) && isValidFireId(fireId);
  }

  /**
   * Returns whether the given {@code modId} and {@code fireId} represent a valid Fire Type.
   *
   * @param fireType
   * @return whether the given values represent a valid Fire Type.
   */
  public static boolean isValidType(@Nullable ResourceLocation fireType) {
    return fireType != null && Strings.isNotBlank(fireType.getNamespace()) && Strings.isNotBlank(fireType.getPath());
  }

  /**
   * Returns whether a fire is registered with the given {@code modId} and {@code fireId}.
   *
   * @param modId
   * @param fireId
   * @return whether a fire is registered with the given values.
   */
  public static boolean isRegisteredType(@Nullable String modId, @Nullable String fireId) {
    return isValidModId(modId) && isValidFireId(fireId) && isRegisteredType(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns whether a fire is registered with the given {@code fireType}.
   *
   * @param fireType
   * @return whether a fire is registered with the given {@code fireType}.
   */
  public static boolean isRegisteredType(@Nullable ResourceLocation fireType) {
    return fireType != null && FIRES.containsKey(fireType);
  }

  /**
   * Returns whether the given {@code id} is a valid fire id.
   *
   * @param id
   * @return whether the given {@code id} is a valid fire id.
   */
  public static boolean isValidFireId(@Nullable String id) {
    return Strings.isNotBlank(id) && ResourceLocation.isValidPath(id);
  }

  /**
   * Returns whether the given {@code id} is a valid and registered fire id.
   *
   * @param id
   * @return whether the given {@code id} is a valid and registered fire id.
   */
  public static boolean isRegisteredFireId(@Nullable String id) {
    return isValidFireId(id) && FIRES.keySet().stream().anyMatch(fireType -> fireType.getPath().equals(id));
  }

  /**
   * Returns whether the given {@code id} is a valid mod id.
   *
   * @param id
   * @return whether the given {@code id} is a valid mod id.
   */
  public static boolean isValidModId(@Nullable String id) {
    return Strings.isNotBlank(id) && ResourceLocation.isValidNamespace(id);
  }

  /**
   * Returns whether the given {@code id} is a valid, loaded and registered mod id.
   *
   * @param id
   * @return whether the given {@code id} is a valid, loaded and registered mod id.
   */
  public static boolean isRegisteredModId(@Nullable String id) {
    return isValidModId(id) && FIRES.keySet().stream().anyMatch(fireType -> fireType.getNamespace().equals(id));
  }

  /**
   * Returns the closest well-formed Fire Type from the given {@code modId} and {@code fireId}.
   *
   * @param modId
   * @param fireId
   * @return the closest well-formed Fire Type.
   */
  public static ResourceLocation sanitize(@Nullable String modId, @Nullable String fireId) {
    return isValidModId(modId) && isValidModId(fireId) ? sanitize(new ResourceLocation(modId, fireId)) : DEFAULT_FIRE_TYPE;
  }

  /**
   * Returns the closest well-formed Fire Type from the given {@code fireType}.
   *
   * @param fireType
   * @return the closest well-formed Fire Type.
   */
  public static ResourceLocation sanitize(@Nullable ResourceLocation fireType) {
    return isValidType(fireType) ? fireType : DEFAULT_FIRE_TYPE;
  }

  /**
   * Returns the closest well-formed and registered Fire Type from the given {@code modId} and {@code fireId}.
   *
   * @param modId
   * @param fireId
   * @return the closest well-formed and registered Fire Type.
   */
  public static ResourceLocation ensure(@Nullable String modId, @Nullable String fireId) {
    String trimmedModId = modId == null ? "" : modId.trim();
    String trimmedFireId = fireId == null ? "" : fireId.trim();
    return isValidModId(trimmedModId) && isValidFireId(trimmedFireId) ? ensure(new ResourceLocation(trimmedModId, trimmedFireId)) : DEFAULT_FIRE_TYPE;
  }

  /**
   * Returns the closest well-formed and registered Fire Type from the given {@code fireType}.
   *
   * @param fireType
   * @return the closest well-formed and registered Fire Type.
   */
  public static ResourceLocation ensure(@Nullable ResourceLocation fireType) {
    return isRegisteredType(fireType) ? fireType : DEFAULT_FIRE_TYPE;
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

  @Nullable
  public static Block getLantern(ResourceLocation fireType) {
    return BuiltInRegistries.BLOCK.getOptional(new ResourceLocation(fireType.getNamespace(), getLanternName(fireType.getPath()))).orElse(null);
  }

  public static List<Block> getSources() {
    return FIRES.keySet().stream().map(FireManager::getSource).filter(Objects::nonNull).toList();
  }

  public static List<Block> getCampfires() {
    return FIRES.keySet().stream().map(FireManager::getCampfire).filter(Objects::nonNull).toList();
  }

  /**
   * Returns the fire source block associated with {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   *
   * @param fireType
   * @return the fire source block associated with {@link Fire}.
   */
  @Nullable
  public static Block getSource(ResourceLocation fireType) {
    return BuiltInRegistries.BLOCK.getOptional(getOptionalProperty(fireType, Fire::getSource)).orElse(null);
  }

  /**
   * Returns the source block associated with {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   *
   * @param fireType
   * @return the source block associated with {@link Fire}.
   */
  @Nullable
  public static Block getCampfire(ResourceLocation fireType) {
    return BuiltInRegistries.BLOCK.getOptional(getOptionalProperty(fireType, Fire::getCampfire)).orElse(null);
  }

  /**
   * Returns the list of all Fire Aspect enchantments registered.
   *
   * @return the list of all Fire Aspect enchantments registered.
   */
  public static List<FireTypedFireAspectEnchantment> getFireAspects() {
    return FIRES.keySet().stream().map(FireManager::getFireAspect).filter(Objects::nonNull).toList();
  }

  /**
   * Returns the list of all Flame enchantments registered.
   *
   * @return the list of all Flame enchantments registered.
   */
  public static List<FireTypedFlameEnchantment> getFlames() {
    return FIRES.keySet().stream().map(FireManager::getFlame).filter(Objects::nonNull).toList();
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
    return (FireTypedFireAspectEnchantment) BuiltInRegistries.ENCHANTMENT.get(getOptionalProperty(fireType, Fire::getFireAspect));
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
    return (FireTypedFlameEnchantment) BuiltInRegistries.ENCHANTMENT.get(getOptionalProperty(fireType, Fire::getFlame));
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
      return harmOrHeal(entity, getInFireDamageSourceFor(entity, fireType), FireManager.getProperty(fireType, Fire::getDamage), FireManager.getProperty(fireType, Fire::invertHealAndHarm));
    }
    ((FireTypeChanger) entity).setFireType(DEFAULT_FIRE_TYPE);
    return harmOrHeal(entity, DEFAULT_FIRE.getInFire(entity), DEFAULT_FIRE.getDamage(), DEFAULT_FIRE.invertHealAndHarm());
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
      return harmOrHeal(entity, getOnFireDamageSourceFor(entity, fireType), FireManager.getProperty(fireType, Fire::getDamage), FireManager.getProperty(fireType, Fire::invertHealAndHarm));
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
    Optional<Predicate<Entity>> behavior = FireManager.getProperty(((FireTyped) entity).getFireType(), Fire::getBehavior);
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
   * Writes to the given {@link CompoundTag} the given {@code fireType}.<br />
   * If the given {@code fireType} is not registered, {@link #DEFAULT_FIRE_TYPE} will be used instead.
   *
   * @param tag {@link CompoundTag} to write to.
   * @param fireType Fire Type to save.
   */
  public static void writeTag(CompoundTag tag, @Nullable ResourceLocation fireType) {
    tag.putString(FIRE_TYPE_TAG, ensure(fireType).toString());
  }

  /**
   * Reads the Fire Type from the given {@link CompoundTag}.
   *
   * @param tag {@link CompoundTag} to read from.
   * @return the Fire Type read from the given {@link CompoundTag}.
   */
  public static ResourceLocation readTag(CompoundTag tag) {
    return ensure(ResourceLocation.tryParse(tag.getString(FIRE_TYPE_TAG)));
  }

  public static String getLanternName(String fireId) {
    return fireId + "_lantern";
  }

  public static String getTorchName(String fireId) {
    return fireId + "_torch";
  }

  public static String getWallTorchName(String fireId) {
    return fireId + "_wall_torch";
  }

  public static String getFlameName(String fireId) {
    return fireId + "_flame";
  }
}
