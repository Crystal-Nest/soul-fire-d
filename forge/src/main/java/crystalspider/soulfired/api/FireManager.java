package crystalspider.soulfired.api;

import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.CampfireBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.ModList;

/**
 * Static manager for the registered Fires.
 */
public abstract class FireManager {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger();

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
  public static final Fire DEFAULT_FIRE = new Fire(DEFAULT_FIRE_TYPE, FireBuilder.DEFAULT_DAMAGE, FireBuilder.DEFAULT_INVERT_HEAL_AND_HARM, FireBuilder.DEFAULT_IN_FIRE, FireBuilder.DEFAULT_ON_FIRE, FireBuilder.DEFAULT_HURT_SOUND, FireBuilder.DEFAULT_SOURCE_BLOCK, null, null);

  /**
   * {@link ConcurrentHashMap} of all registered {@link Fire Fires}.
   */
  private static volatile ConcurrentHashMap<ResourceLocation, Fire> fires = new ConcurrentHashMap<>();

  /**
   * Utility to create a FireTyped {@link CampfireBlock}.
   * <p>
   * If you need a more fine grained control over your {@link CampfireBlock}, create your own class and implementation that extends {@link CampfireBlock} to suit your needs.
   * If you choose your own implementation, remember to set the Fire Type in the constructor of your {@link CampfireBlock} like so:
   * <pre>
   * <code>
   * CustomCampfireBlock() {
   *  super(emitParticles, 0, properties);
   *  (({@link FireTypeChanger}) this).setFireType(modId, fireId)
   * }
   * </code>
   * </pre>
   * 
   * @param modId
   * @param fireId
   * @param properties {@link Properties Block Properties}.
   * @return the new {@link CampfireBlock}.
   */
  public static final CampfireBlock createCampfireBlock(String modId, String fireId, Properties properties) {
    if (isValidType(modId, fireId)) {
      return createCampfireBlock(new ResourceLocation(modId, fireId), properties);
    }
    return new CampfireBlock(false, (int) DEFAULT_FIRE.getDamage(), properties);
  }

  /**
   * Utility to create a FireTyped {@link CampfireBlock}.
   * <p>
   * If you need a more fine grained control over your {@link CampfireBlock}, create your own class and implementation that extends {@link CampfireBlock} to suit your needs.
   * If you choose your own implementation, remember to set the Fire Type in the constructor of your {@link CampfireBlock} like so:
   * <pre>
   * <code>
   * CustomCampfireBlock() {
   *  super(emitParticles, 0, properties);
   *  (({@link FireTypeChanger}) this).setFireType(modId, fireId)
   * }
   * </code>
   * </pre>
   * 
   * @param fireType
   * @param properties {@link Properties Block Properties}.
   * @return the new {@link CampfireBlock}.
   */
  public static final CampfireBlock createCampfireBlock(ResourceLocation fireType, Properties properties) {
    CampfireBlock campfire = new CampfireBlock(false, 0, properties);
    ((FireTypeChanger) campfire).setFireType(fireType);
    return campfire;
  }

  /**
   * Returns a new {@link FireBuilder}.
   * 
   * @return a new {@link FireBuilder}.
   */
  public static final FireBuilder fireBuilder() {
    return new FireBuilder();
  }

  /**
   * Attempts to register the given {@link Fire}.
   * <p>
   * If the {@link Fire#fireType} is already registered, logs an error.
   * 
   * @param fire {@link Fire} to register.
   * @return whether the registration is successful.
   */
  public static final synchronized boolean registerFire(Fire fire) {
    ResourceLocation fireType = fire.getFireType();
    if (!fires.containsKey(fireType)) {
      fires.put(fireType, fire);
      ((FireTypeChanger) fire.getSourceBlock()).setFireType(fireType);
      return true;
    }
    LOGGER.error("Fire [" + fireType + "] was already registered with the following value: " + fires.get(fireType));
    return false;
  }

  /**
   * Returns the list of all registered {@link Fire Fires}.
   * 
   * @return the list of all registered {@link Fire Fires}.
   */
  public static final List<Fire> getFires() {
    return fires.values().stream().collect(Collectors.toList());
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
  public static final Fire getFire(String modId, String fireId) {
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
  public static final Fire getFire(ResourceLocation fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE);
  }

  /**
   * Returns whether the given {@code modId} and {@code fireId} represent a valid Fire Type.
   * 
   * @param modId
   * @param fireId
   * @return whether the given values represent a valid Fire Type.
   */
  public static final boolean isValidType(String modId, String fireId) {
    return isValidModId(modId) && isValidFireId(fireId);
  }

  /**
   * Returns whether a fire is registered with the given {@code modId} and {@code fireId}.
   * 
   * @param modId
   * @param fireId
   * @return whether a fire is registered with the given values.
   */
  public static final boolean isRegisteredType(String modId, String fireId) {
    return isValidModId(modId) && isValidFireId(fireId) && isRegisteredType(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns whether a fire is registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return whether a fire is registered with the given {@code fireType}.
   */
  public static final boolean isRegisteredType(ResourceLocation fireType) {
    return fireType != null && fires.containsKey(fireType);
  }

  /**
   * Returns whether the given {@code id} is a valid fire id.
   * 
   * @param id
   * @return whether the given {@code id} is a valid fire id.
   */
  public static final boolean isValidFireId(String id) {
    if (StringUtils.isNotBlank(id)) {
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
  public static final boolean isRegisteredFireId(String id) {
    return isValidFireId(id) && fires.keySet().stream().anyMatch(fireType -> fireType.getPath().equals(id));
  }

  /**
   * Returns whether the given {@code id} is a valid mod id.
   * 
   * @param id
   * @return whether the given {@code id} is a valid mod id.
   */
  public static final boolean isValidModId(String id) {
    if (StringUtils.isNotBlank(id)) {
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
  public static final boolean isRegisteredModId(String id) {
    return isValidModId(id) && ModList.get().isLoaded(id) && fires.keySet().stream().anyMatch(fireType -> fireType.getNamespace().equals(id));
  }

  /**
   * Returns whether the given {@link DamageSource} is registered with any {@link Fire}.
   * 
   * @param damageSource
   * @return whether the given {@link DamageSource} is registered with any {@link Fire}.
   */
  public static final boolean isFireDamageSource(DamageSource damageSource) {
    return fires.values().stream().anyMatch(fire -> fire.getInFire() == damageSource || fire.getOnFire() == damageSource);
  }

  /**
   * Returns the closest well-formed Fire Type from the given {@code modId} and {@code fireId}.
   * 
   * @param modId
   * @param fireId
   * @return the closest well-formed Fire Type.
   */
  public static final ResourceLocation sanitize(String modId, String fireId) {
    String trimmedModId = modId.trim(), trimmedFireId = fireId.trim();
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
  public static final ResourceLocation sanitize(ResourceLocation fireType) {
    if (StringUtils.isNotBlank(fireType.getNamespace()) && StringUtils.isNotBlank(fireType.getPath())) {
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
  public static final ResourceLocation ensure(String modId, String fireId) {
    String trimmedModId = modId.trim(), trimmedFireId = fireId.trim();
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
  public static final ResourceLocation ensure(ResourceLocation fireType) {
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
  public static final List<ResourceLocation> getFireTypes() {
    return fires.keySet().stream().collect(Collectors.toList());
  }

  /**
   * Returns the list of all registered fire ids.
   * 
   * @return the list of all registered fire ids.
   */
  public static final List<String> getFireIds() {
    return fires.keySet().stream().map(fireType -> fireType.getPath()).collect(Collectors.toList());
  }

  /**
   * Returns the list of all registered mod ids.
   * 
   * @return the list of all registered mod ids.
   */
  public static final List<String> getModIds() {
    return fires.keySet().stream().map(fireType -> fireType.getPath()).collect(Collectors.toList());
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
  public static final float getDamage(String modId, String fireId) {
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
  public static final float getDamage(ResourceLocation fireType) {
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
  public static final boolean getInvertHealAndHarm(String modId, String fireId) {
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
  public static final boolean getInvertHealAndHarm(ResourceLocation fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getInvertHealAndHarm();
  }

  /**
   * Returns the in damage source flag of the {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given values.
   * 
   * @param modId
   * @param fireId
   * @return the in damage source flag of the {@link Fire}.
   */
  public static final DamageSource getInFireDamageSource(String modId, String fireId) {
    return getInFireDamageSource(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the in damage source flag of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the in damage source flag of the {@link Fire}.
   */
  public static final DamageSource getInFireDamageSource(ResourceLocation fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getInFire();
  }

  /**
   * Returns the on damage source flag of the {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given values.
   * 
   * @param modId
   * @param fireId
   * @return the on damage source flag of the {@link Fire}.
   */
  public static final DamageSource getOnFireDamageSource(String modId, String fireId) {
    return getOnFireDamageSource(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the on damage source flag of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the on damage source flag of the {@link Fire}.
   */
  public static final DamageSource getOnFireDamageSource(ResourceLocation fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getOnFire();
  }

  /**
   * Returns the hurt sound of the {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given values.
   * 
   * @param modId
   * @param fireId
   * @return the hurt sound of the {@link Fire}.
   */
  public static final SoundEvent getHurtSound(String modId, String fireId) {
    return getHurtSound(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the hurt sound of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the hurt sound of the {@link Fire}.
   */
  public static final SoundEvent getHurtSound(ResourceLocation fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getHurtSound();
  }

  /**
   * Returns the source block of the {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given values.
   * 
   * @param modId
   * @param fireId
   * @return the source block of the {@link Fire}.
   */
  public static final Block getSourceBlock(String modId, String fireId) {
    return getSourceBlock(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the source block of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the source block of the {@link Fire}.
   */
  public static final Block getSourceBlock(ResourceLocation fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getSourceBlock();
  }

  /**
   * Returns the Fire Type associated with the given {@code sourceBlock}.
   * <p>
   * Returns the {@link #DEFAULT_FIRE_TYPE default Fire Type} if no {@link Fire} is associated with the given {@code sourceBlock}.
   * 
   * @param sourceBlock
   * @return Fire Type associated with the given {@code sourceBlock}.
   */
  public static final ResourceLocation getFireTypeFromBlock(Block sourceBlock) {
    return fires.entrySet().stream().filter(entry -> (entry.getValue().getSourceBlock() == sourceBlock)).findFirst().orElse(new AbstractMap.SimpleEntry<ResourceLocation, Fire>(DEFAULT_FIRE_TYPE, DEFAULT_FIRE)).getKey();
  }

  /**
   * Returns the list of all Fire Aspect enchantments registered.
   * 
   * @return the list of all Fire Aspect enchantments registered.
   */
  public static final List<Enchantment> getFireAspects() {
    return fires.values().stream().map(fire -> fire.getFireAspect()).collect(Collectors.toList());
  }

  /**
   * Returns the list of all Flame enchantments registered.
   * 
   * @return the list of all Flame enchantments registered.
   */
  public static final List<Enchantment> getFlames() {
    return fires.values().stream().map(fire -> fire.getFlame()).collect(Collectors.toList());
  }

  /**
   * Returns the Fire Aspect enchantment of the {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns the {@code null} if no {@link Fire} was registered with the given values.
   * 
   * @param modId
   * @param fireId
   * @return the Fire Aspect enchantment of the {@link Fire}.
   */
  public static final Enchantment getFireAspect(String modId, String fireId) {
    return getFireAspect(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the Fire Aspect enchantment of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the {@code null} if no {@link Fire} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the Fire Aspect enchantment of the {@link Fire}.
   */
  public static final Enchantment getFireAspect(ResourceLocation fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getFireAspect();
  }

  /**
   * Returns the Flame enchantment of the {@link Fire} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns the {@code null} if no {@link Fire} was registered with the given values.
   * 
   * @param modId
   * @param fireId
   * @return the Flame enchantment of the {@link Fire}.
   */
  public static final Enchantment getFlame(String modId, String fireId) {
    return getFlame(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the Flame enchantment of the {@link Fire} registered with the given {@code fireType}.
   * <p>
   * Returns the {@code null} if no {@link Fire} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the Flame enchantment of the {@link Fire}.
   */
  public static final Enchantment getFlame(ResourceLocation fireType) {
    return fires.getOrDefault(fireType, DEFAULT_FIRE).getFlame();
  }

  /**
   * Set on fire the given entity for the given seconds with the given Fire Type.
   * 
   * @param entity {@link Entity} to set on fire.
   * @param seconds amount of seconds the fire should last for.
   * @param modId mod id of the fire.
   * @param fireId fire id of the fire.
   */
  public static final void setOnFire(Entity entity, int seconds, String modId, String fireId) {
    setOnFire(entity, seconds, new ResourceLocation(modId, fireId));
  }

  /**
   * Set on fire the given entity for the given seconds with the given Fire Type.
   * 
   * @param entity {@link Entity} to set on fire.
   * @param seconds amount of seconds the fire should last for.
   * @param fireType {@link ResourceLocation} of the fire.
   */
  public static final void setOnFire(Entity entity, int seconds, ResourceLocation fireType) {
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
  public static final boolean damageInFire(Entity entity, String fireId, String modId) {
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
  public static final boolean damageInFire(Entity entity, ResourceLocation fireType) {
    if (isRegisteredType(fireType)) {
      ((FireTypeChanger) entity).setFireType(fireType);
      if (entity.tickCount % 20 == 0) {
        return harmOrHeal(entity, getInFireDamageSource(fireType), getDamage(fireType), getInvertHealAndHarm(fireType));
      }
      return false;
    }
    ((FireTypeChanger) entity).setFireType(DEFAULT_FIRE_TYPE);
    return harmOrHeal(entity, DEFAULT_FIRE.getInFire(), DEFAULT_FIRE.getDamage(), DEFAULT_FIRE.getInvertHealAndHarm());
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
  public static final boolean damageOnFire(Entity entity, String fireId, String modId) {
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
  public static final boolean damageOnFire(Entity entity, ResourceLocation fireType) {
    if (isRegisteredType(fireType)) {
      ((FireTypeChanger) entity).setFireType(fireType);
      return harmOrHeal(entity, getOnFireDamageSource(fireType), getDamage(fireType), getInvertHealAndHarm(fireType));
    }
    ((FireTypeChanger) entity).setFireType(DEFAULT_FIRE_TYPE);
    return harmOrHeal(entity, DEFAULT_FIRE.getOnFire(), DEFAULT_FIRE.getDamage(), DEFAULT_FIRE.getInvertHealAndHarm());
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
  private static final boolean harmOrHeal(Entity entity, DamageSource damageSource, float damage, boolean invertHealAndHarm) {
    if (damage > 0) {
      if (entity instanceof LivingEntity) {
        LivingEntity livingEntity = (LivingEntity) entity;
        if (livingEntity.isInvertedHealAndHarm() && invertHealAndHarm) {
          livingEntity.heal(damage);
          return false;
        }
        return livingEntity.hurt(damageSource, damage);
      }
      return entity.hurt(damageSource, damage);
    }
    if (entity instanceof LivingEntity) {
      LivingEntity livingEntity = (LivingEntity) entity;
      if (livingEntity.isInvertedHealAndHarm() && invertHealAndHarm) {
        return livingEntity.hurt(damageSource, -damage);
      }
      livingEntity.heal(-damage);
      return false;
    }
    return false;
  }
}
