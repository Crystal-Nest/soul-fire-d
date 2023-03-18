package crystalspider.soulfired.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import crystalspider.soulfired.api.enchantment.FireTypedFireAspectEnchantment;
import crystalspider.soulfired.api.enchantment.FireTypedFlameEnchantment;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

/**
 * Static manager for registered Fires.
 */
public final class FireManager {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger();

  /**
   * Id of the tag used to save Fire Type.
   */
  public static final String FIRE_TYPE_TAG = "FireType";

  /**
   * Fire Type of Vanilla Fire.
   */
  public static final Identifier DEFAULT_FIRE_TYPE = new Identifier("");

  /**
   * Fire Type of Soul Fire.
   */
  public static final Identifier SOUL_FIRE_TYPE = new Identifier("soul");

  /**
   * Default {@link Fire} used as fallback to retrieve default properties.
   */
  public static final Fire DEFAULT_FIRE = new Fire(DEFAULT_FIRE_TYPE, FireBuilder.DEFAULT_DAMAGE, FireBuilder.DEFAULT_INVERT_HEAL_AND_HARM, FireBuilder.DEFAULT_IN_FIRE_GETTER, FireBuilder.DEFAULT_ON_FIRE_GETTER, new Identifier("fire"), new Identifier("campfire"), null, null);

  /**
   * {@link ConcurrentHashMap} of all registered {@link Fire Fires}.
   */
  private static volatile ConcurrentHashMap<Identifier, Fire> fires = new ConcurrentHashMap<>();

  private FireManager() {}

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
   * @param fireType {@link Identifier} of the new {@link Fire} to build.
   * @return a new {@link FireBuilder}.
   */
  public static FireBuilder fireBuilder(Identifier fireType) {
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
  public static synchronized boolean registerFire(Fire fire) {
    Identifier fireType = fire.getFireType();
    if (!fires.containsKey(fireType)) {
      fires.put(fireType, fire);
      Optional<FireTypedFireAspectEnchantment> fireAspect = fire.getFireAspect();
      Optional<FireTypedFlameEnchantment> flame = fire.getFlame();
      if (fireAspect.isPresent()) {
        Registry.register(Registries.ENCHANTMENT, new Identifier(fireType.getNamespace(), fireType.getPath() + "_fire_aspect"), fireAspect.get());
      }
      if (flame.isPresent()) {
        Registry.register(Registries.ENCHANTMENT, new Identifier(fireType.getNamespace(), fireType.getPath() + "_flame"), flame.get());
      }
      Optional<Identifier> source = fire.getSource();
      Optional<Identifier> campfire = fire.getCampfire();
      if (source.isPresent()) {
        Block sourceBlock = Registries.BLOCK.get(source.get());
        if (sourceBlock != null) {
          ((FireTypeChanger) sourceBlock).setFireType(fireType);
        }
      }
      if (campfire.isPresent()) {
        Block campfireBlock = Registries.BLOCK.get(campfire.get());
        if (campfireBlock != null) {
          ((FireTypeChanger) campfireBlock).setFireType(fireType);
        }
      }
      return true;
    }
    LOGGER.error("Fire [" + fireType + "] was already registered with the following value: " + fires.get(fireType));
    return false;
  }

  /**
   * Attempts to register all the given {@link Fire fires}.
   * 
   * @param fires {@link Fire fires} to register.
   * @return an {@link HashMap} with the outcome of each registration attempt.
   */
  public static synchronized HashMap<Identifier, Boolean> registerFires(Fire... fires) {
    return registerFires(Arrays.asList(fires));
  }

  /**
   * Attempts to register all the given {@link Fire fires}.
   * 
   * @param fires {@link Fire fires} to register.
   * @return an {@link HashMap} with the outcome of each registration attempt.
   */
  public static synchronized HashMap<Identifier, Boolean> registerFires(List<Fire> fires) {
    HashMap<Identifier, Boolean> outcomes = new HashMap<>();
    for (Fire fire : fires) {
      outcomes.put(fire.getFireType(), registerFire(fire));
    }
    return outcomes;
  }

  /**
   * Returns the list of all registered {@link Fire Fires}.
   * 
   * @return the list of all registered {@link Fire Fires}.
   */
  public static List<Fire> getFires() {
    return fires.values().stream().toList();
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
    return getFire(new Identifier(modId, fireId));
  }

  /**
   * Returns the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns {@link #DEFAULT_FIRE} if no {@link Fire} is registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return registered {@link Fire} or {@link #DEFAULT_FIRE}.
   */
  public static Fire getFire(Identifier fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE);
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
    return isValidModId(modId) && isValidFireId(fireId) && isRegisteredType(new Identifier(modId, fireId));
  }

  /**
   * Returns whether a fire is registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return whether a fire is registered with the given {@code fireType}.
   */
  public static boolean isRegisteredType(Identifier fireType) {
    return fireType != null && fires.containsKey(fireType);
  }

  /**
   * Returns whether the given {@code id} is a valid fire id.
   * 
   * @param id
   * @return whether the given {@code id} is a valid fire id.
   */
  public static boolean isValidFireId(String id) {
    if (isNotBlank(id)) {
      try {
        new Identifier(id);
        return true;
      } catch (InvalidIdentifierException e) {
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
    return isValidFireId(id) && fires.keySet().stream().anyMatch(fireType -> fireType.getPath().equals(id));
  }

  /**
   * Returns whether the given {@code id} is a valid mod id.
   * 
   * @param id
   * @return whether the given {@code id} is a valid mod id.
   */
  public static boolean isValidModId(String id) {
    if (isNotBlank(id)) {
      try {
        new Identifier(id, "");
        return true;
      } catch (InvalidIdentifierException e) {
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
    return isValidModId(id) && FabricLoader.getInstance().isModLoaded(id) && fires.keySet().stream().anyMatch(fireType -> fireType.getNamespace().equals(id));
  }

  /**
   * Returns the closest well-formed Fire Type from the given {@code modId} and {@code fireId}.
   * 
   * @param modId
   * @param fireId
   * @return the closest well-formed Fire Type.
   */
  public static Identifier sanitize(String modId, String fireId) {
    String trimmedModId = modId.trim(), trimmedFireId = fireId.trim();
    try {
      return sanitize(new Identifier(trimmedModId, trimmedFireId));
    } catch (InvalidIdentifierException e) {
      return DEFAULT_FIRE_TYPE;
    }
  }

  /**
   * Returns the closest well-formed Fire Type from the given {@code fireType}.
   * 
   * @param fireType
   * @return the closest well-formed Fire Type.
   */
  public static Identifier sanitize(Identifier fireType) {
    if (isNotBlank(fireType.getNamespace()) && isNotBlank(fireType.getPath())) {
      return fireType;
    }
    return DEFAULT_FIRE_TYPE;
  }

  /**
   * Returns the closest well-formed and registered Fire Type from the given {@code modId} and {@code fireId}.
   * 
   * @param modId
   * @param fireId
   * @return the closest well-formed and registered fire Fire Type.
   */
  public static Identifier ensure(String modId, String fireId) {
    String trimmedModId = modId.trim(), trimmedFireId = fireId.trim();
    try {
      return ensure(new Identifier(trimmedModId, trimmedFireId));
    } catch (InvalidIdentifierException e) {
      return DEFAULT_FIRE_TYPE;
    }
  }

  /**
   * Returns the closest well-formed and registered Fire Type from the given {@code fireType}.
   * 
   * @param fireType
   * @return the closest well-formed and registered Fire Type.
   */
  public static Identifier ensure(Identifier fireType) {
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
  public static List<Identifier> getFireTypes() {
    return fires.keySet().stream().toList();
  }

  /**
   * Returns the list of all registered fire ids.
   * 
   * @return the list of all registered fire ids.
   */
  public static List<String> getFireIds() {
    return fires.keySet().stream().map(fireType -> fireType.getPath()).toList();
  }

  /**
   * Returns the list of all registered mod ids.
   * 
   * @return the list of all registered mod ids.
   */
  public static List<String> getModIds() {
    return fires.keySet().stream().map(fireType -> fireType.getPath()).toList();
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
    return getDamage(new Identifier(modId, fireId));
  }

  /**
   * Returns the damage of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the damage of the {@link Fire}.
   */
  public static float getDamage(Identifier fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getDamage();
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
    return getInvertHealAndHarm(new Identifier(modId, fireId));
  }

  /**
   * Returns the invertHealAndHarm flag of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the invertHealAndHarm flag of the {@link Fire}.
   */
  public static boolean getInvertHealAndHarm(Identifier fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getInvertHealAndHarm();
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
    return getInFireDamageSourceFor(entity, new Identifier(modId, fireId));
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
  public static DamageSource getInFireDamageSourceFor(Entity entity, Identifier fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getInFire(entity);
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
    return getOnFireDamageSourceFor(entity, new Identifier(modId, fireId));
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
  public static DamageSource getOnFireDamageSourceFor(Entity entity, Identifier fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getOnFire(entity);
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
    return getSourceBlock(new Identifier(modId, fireId));
  }

  /**
   * Returns the fire source block associated with {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the fire source block associated with {@link Fire}.
   */
  public static Block getSourceBlock(Identifier fireType) {
    Block sourceBlock = Registries.BLOCK.get(fires.getOrDefault(fireType, DEFAULT_FIRE).getSource().orElse(DEFAULT_FIRE.getSource().get()));
    return sourceBlock != null ? sourceBlock : Blocks.FIRE;
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
    return getCampfireBlock(new Identifier(modId, fireId));
  }

  /**
   * Returns the source block associated with {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the source block associated with {@link Fire}.
   */
  public static Block getCampfireBlock(Identifier fireType) {
    Block campfireBlock = Registries.BLOCK.get(fires.getOrDefault(fireType, DEFAULT_FIRE).getCampfire().orElse(DEFAULT_FIRE.getSource().get()));
    return campfireBlock != null ? campfireBlock : Blocks.CAMPFIRE;
  }

  /**
   * Returns the list of all Fire Aspect enchantments registered.
   * 
   * @return the list of all Fire Aspect enchantments registered.
   */
  public static List<FireTypedFireAspectEnchantment> getFireAspects() {
    return fires.values().stream().map(fire -> fire.getFireAspect()).filter(optional -> optional.isPresent()).map(optional -> optional.get()).toList();
  }

  /**
   * Returns the list of all Flame enchantments registered.
   * 
   * @return the list of all Flame enchantments registered.
   */
  public static List<FireTypedFlameEnchantment> getFlames() {
    return fires.values().stream().map(fire -> fire.getFlame()).filter(optional -> optional.isPresent()).map(optional -> optional.get()).toList();
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
    return getFireAspect(new Identifier(modId, fireId));
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
  public static FireTypedFireAspectEnchantment getFireAspect(Identifier fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getFireAspect().orElse(null);
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
    return getFlame(new Identifier(modId, fireId));
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
  public static FireTypedFlameEnchantment getFlame(Identifier fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getFlame().orElse(null);
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
    setOnFire(entity, seconds, new Identifier(modId, fireId));
  }

  /**
   * Set on fire the given entity for the given seconds with the given Fire Type.
   * 
   * @param entity {@link Entity} to set on fire.
   * @param seconds amount of seconds the fire should last for.
   * @param fireType {@link Identifier} of the fire.
   */
  public static void setOnFire(Entity entity, int seconds, Identifier fireType) {
    entity.setOnFireFor(seconds);
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
    return damageInFire(entity, new Identifier(fireId, modId));
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
  public static boolean damageInFire(Entity entity, Identifier fireType) {
    if (isRegisteredType(fireType)) {
      ((FireTypeChanger) entity).setFireType(fireType);
      if (entity.age % 20 == 0) {
        return harmOrHeal(entity, getInFireDamageSourceFor(entity, fireType), getDamage(fireType), getInvertHealAndHarm(fireType));
      }
      return false;
    }
    ((FireTypeChanger) entity).setFireType(DEFAULT_FIRE_TYPE);
    return harmOrHeal(entity, DEFAULT_FIRE.getInFire(entity), DEFAULT_FIRE.getDamage(), DEFAULT_FIRE.getInvertHealAndHarm());
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
    return damageOnFire(entity, new Identifier(fireId, modId));
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
  public static boolean damageOnFire(Entity entity, Identifier fireType) {
    if (isRegisteredType(fireType)) {
      ((FireTypeChanger) entity).setFireType(fireType);
      return harmOrHeal(entity, getOnFireDamageSourceFor(entity, fireType), getDamage(fireType), getInvertHealAndHarm(fireType));
    }
    ((FireTypeChanger) entity).setFireType(DEFAULT_FIRE_TYPE);
    return harmOrHeal(entity, DEFAULT_FIRE.getOnFire(entity), DEFAULT_FIRE.getDamage(), DEFAULT_FIRE.getInvertHealAndHarm());
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
    if (damage > 0) {
      if (entity instanceof LivingEntity) {
        LivingEntity livingEntity = (LivingEntity) entity;
        if (livingEntity.isUndead() && invertHealAndHarm) {
          livingEntity.heal(damage);
          return false;
        }
        return livingEntity.damage(damageSource, damage);
      }
      return entity.damage(damageSource, damage);
    }
    if (entity instanceof LivingEntity) {
      LivingEntity livingEntity = (LivingEntity) entity;
      if (livingEntity.isUndead() && invertHealAndHarm) {
        return livingEntity.damage(damageSource, -damage);
      }
      livingEntity.heal(-damage);
      return false;
    }
    return false;
  }

  /**
   * Returns whether a given string is not blank, meaning it's not null and it's not made up only of whitespaces.
   * 
   * @param string
   * @return whether a given string is not blank, meaning it's not null and it's not made up only of whitespaces.
   */
  private static boolean isNotBlank(String string) {
    return !(string == null || string.isBlank());
  }

  /**
   * Writes to the given {@link NbtCompound} the given {@code fireType}.
   * <p>
   * If the given {@code fireType} is not registered, the {@link DEFAULT_FIRE_TYPE} will be written instead.
   * 
   * @param tag {@link NbtCompound} to write to.
   * @param fireType Fire Type to save.
   */
  public static void writeNbt(NbtCompound tag, Identifier fireType) {
    tag.putString(FIRE_TYPE_TAG, ensure(fireType).toString());
  }

  /**
   * Reads the Fire Type from the given {@link NbtCompound}.
   * 
   * @param tag {@link NbtCompound} to read from.
   * @return the Fire Type read from the given {@link NbtCompound}.
   */
  public static Identifier readNbt(NbtCompound tag) {
    return ensure(Identifier.tryParse(tag.getString(FIRE_TYPE_TAG)));
  }
}
