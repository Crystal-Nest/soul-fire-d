package crystalspider.soulfired.api;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Static manager for the registered Fires.
 */
public abstract class FireManager {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogUtils.getLogger();

  /**
   * Fire Id of Vanilla Fire.
   */
  public static final String BASE_FIRE_ID = "";

  /**
   * Fire Id of Soul Fire.
   */
  public static final String SOUL_FIRE_ID = "soul";

  /**
   * {@link ConcurrentHashMap} of all registered {@link Fire Fires}.
   */
  private static volatile ConcurrentHashMap<String, Fire> fires = new ConcurrentHashMap<>();

  /**
   * Utility to create a FireTyped {@link CampfireBlock}.
   * 
   * @param fireId Fire Id of the fire the campfire burns from.
   * @param settings {@link Settings Block Settings}.
   * @return the new {@link CampfireBlock}.
   */
  public static final CampfireBlock createCampfireBlock(String fireId, Settings settings) {
    if (isValidFireId(fireId)) {
      CampfireBlock campfire = new CampfireBlock(false, 0, settings);
      ((FireTypeChanger) campfire).setFireId(fireId);
      return campfire;
    }
    return new CampfireBlock(false, (int) FireBuilder.DEFAULT_DAMAGE, settings);
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
   * Registers the given {@link Fire}.
   * <p>
   * If the {@link Fire#id} is already registered, logs an error.
   * 
   * @param fire {@link Fire} to register.
   * @return whether the registration has been successful.
   */
  public static final synchronized boolean registerFire(Fire fire) {
    String fireId = fire.getId();
    if (!fires.containsKey(fireId)) {
      fires.put(fireId, fire);
      Registry.register(Registry.ENCHANTMENT, new Identifier(fire.getModId(), fireId + "_fire_aspect"), fire.getFireAspect());
      Registry.register(Registry.ENCHANTMENT, new Identifier(fire.getModId(), fireId + "_flame"), fire.getFlame());
      return true;
    }
    LOGGER.error("Fire [" + fireId + "] was already registered by mod " + fires.get(fireId).getModId() + " with the following value: " + fires.get(fireId));
    return false;
  }

  /**
   * Returns a copy of the list of all registered {@link Fire Fires}.
   * 
   * @return a copy of the list of all registered {@link Fire Fires}.
   */
  public static final List<Fire> getFires() {
    return List.copyOf(fires.values());
  }

  /**
   * Returns the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns {@code null} if no {@link Fire} is registered with the given {@code id}.
   * 
   * @param id
   * @return registered {@link Fire} or {@code null}.
   */
  public static final Fire getFire(String id) {
    if (isFireId(id)) {
      return fires.get(id);
    }
    return null;
  }

  /**
   * Returns whether the given {@code id} is a valid fire id.
   * 
   * @param id
   * @return whether the given {@code id} is a valid fire id.
   */
  public static final boolean isValidFireId(String id) {
    return !(id == null || id.isBlank());
  }

  /**
   * Returns whether the given {@code id} is a valid and registered fire id.
   * 
   * @param id
   * @return whether the given {@code id} is a valid and registered fire id.
   */
  public static final boolean isFireId(String id) {
    return isValidFireId(id) && fires.containsKey(id);
  }

  /**
   * Returns the closest well-formed fire id from the given {@code id}.
   * 
   * @param id
   * @return the closest well-formed fire id.
   */
  public static final String sanitizeFireId(String id) {
    if (isValidFireId(id)) {
      return id.trim();
    }
    return BASE_FIRE_ID;
  }

  /**
   * Returns the closest well-formed and registered fire id from the given {@code id}.
   * 
   * @param id
   * @return the closest well-formed and registered fire id.
   */
  public static final String ensureFireId(String id) {
    if (fires.containsKey(sanitizeFireId(id))) {
      return id;
    }
    return BASE_FIRE_ID;
  }

  /**
   * Returns a copy of the list of all registered fire ids.
   * 
   * @return a copy of the list of all registered fire ids.
   */
  public static final List<String> getFireIds() {
    return List.copyOf(Collections.list(fires.keys()));
  }

  /**
   * Returns the damage of the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code id}.
   * 
   * @param id
   * @return the damage of the {@link Fire} registered with the given {@code id}.
   */
  public static final float getDamage(String id) {
    if (isFireId(id)) {
      return fires.get(id).getDamage();
    }
    return FireBuilder.DEFAULT_DAMAGE;
  }

  /**
   * Returns the invertHealAndHarm flag of the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code id}.
   * 
   * @param id
   * @return the invertHealAndHarm flag of the {@link Fire} registered with the given {@code id}.
   */
  public static final boolean getInvertHealAndHarm(String id) {
    if (isFireId(id)) {
      return fires.get(id).getInvertHealAndHarm();
    }
    return FireBuilder.DEFAULT_INVERT_HEAL_AND_HARM;
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
   * Returns the in damage source of the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code id}.
   * 
   * @param id
   * @return the in damage source of the {@link Fire} registered with the given {@code id}.
   */
  public static final DamageSource getInFireDamageSource(String id) {
    if (isFireId(id)) {
      return fires.get(id).getInFire();
    }
    return FireBuilder.DEFAULT_IN_FIRE;
  }

  /**
   * Returns the on damage source of the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code id}.
   * 
   * @param id
   * @return the on damage source of the {@link Fire} registered with the given {@code id}.
   */
  public static final DamageSource getOnFireDamageSource(String id) {
    if (isFireId(id)) {
      return fires.get(id).getOnFire();
    }
    return FireBuilder.DEFAULT_ON_FIRE;
  } 

  /**
   * Returns the hurt sound of the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code id}.
   * 
   * @param id
   * @return the hurt sound of the {@link Fire} registered with the given {@code id}.
   */
  public static final SoundEvent getHurtSound(String id) {
    if (isFireId(id)) {
      return fires.get(id).getHurtSound();
    }
    return FireBuilder.DEFAULT_HURT_SOUND;
  }

  /**
   * Returns the source block of the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns the default value if no {@link Fire} was registered with the given {@code id}.
   * 
   * @param id
   * @return the source block of the {@link Fire} registered with the given {@code id}.
   */
  public static final BlockState getSourceBlock(String id) {
    if (isFireId(id)) {
      return fires.get(id).getSourceBlock();
    }
    return FireBuilder.DEFAULT_BLOCKSTATE;
  }

  /**
   * Returns the list of all Fire Aspect enchantments registered.
   * 
   * @return the list of all Fire Aspect enchantments registered.
   */
  public static final List<Enchantment> getFireAspects() {
    return fires.values().stream().map(fire -> fire.getFireAspect()).toList();
  }

  /**
   * Returns the list of all Flame enchantments registered.
   * 
   * @return the list of all Flame enchantments registered.
   */
  public static final List<Enchantment> getFlames() {
    return fires.values().stream().map(fire -> fire.getFlame()).toList();
  }

  /**
   * Returns the Fire Aspect enchantment of the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns {@code null} if no {@link Fire} was registered with the given {@code id}.
   * 
   * @param id
   * @return the Fire Aspect enchantment of the {@link Fire} registered with the given {@code id}.
   */
  public static final Enchantment getFireAspect(String id) {
    if (isFireId(id)) {
      return fires.get(id).getFireAspect();
    }
    return null;
  }

  /**
   * Returns the Flame enchantment of the {@link Fire} registered with the given {@code id}.
   * <p>
   * Returns {@code null} if no {@link Fire} was registered with the given {@code id}.
   * 
   * @param id
   * @return the Flame enchantment of the {@link Fire} registered with the given {@code id}.
   */
  public static final Enchantment getFlame(String id) {
    if (isFireId(id)) {
      return fires.get(id).getFlame();
    }
    return null;
  }

  /**
   * Harms (or heals) the given {@code entity} based on the {@link Fire} registered with {@code fireId}.
   * <p>
   * If no {@link Fire} was registered with {@code fireId}, uses the provided {@code damageSource} and {@code damage} to harm (or heal) the {@code entity}.
   * 
   * @param entity {@link Entity} to harm or heal.
   * @param fireId fire id used to set the {@code entity} on fire.
   * @param damageSource default {@link DamageSource} to use when no {@link Fire} is registered with {@code fireId}.
   * @param damage  default {@code damage} to use when no {@link Fire} is registered with {@code fireId}.
   * @return whether the {@code entity} has been harmed.
   */
  public static final boolean damageInFire(Entity entity, String fireId, DamageSource damageSource, float damage) {
    if (isFireId(fireId)) {
      ((FireTypeChanger) entity).setFireId(fireId);
      if (entity.age % 20 == 0) {
        return harmOrHeal(entity, getInFireDamageSource(fireId), getDamage(fireId), getInvertHealAndHarm(fireId));
      }
      return false;
    }
    ((FireTypeChanger) entity).setFireId(BASE_FIRE_ID);
    return harmOrHeal(entity, damageSource, damage, FireBuilder.DEFAULT_INVERT_HEAL_AND_HARM);
  }

  /**
   * Harms (or heals) the given {@code entity} based on the {@link Fire} registered with {@code fireId}.
   * <p>
   * If no {@link Fire} was registered with {@code fireId}, uses the provided {@code damageSource} and {@code damage} to harm (or heal) the {@code entity}.
   * 
   * @param entity {@link Entity} to harm or heal.
   * @param fireId fire id used to set the {@code entity} on fire.
   * @param damageSource default {@link DamageSource} to use when no {@link Fire} is registered with {@code fireId}.
   * @param damage  default {@code damage} to use when no {@link Fire} is registered with {@code fireId}.
   * @return whether the {@code entity} has been harmed.
   */
  public static final boolean damageOnFire(Entity entity, String fireId, DamageSource damageSource, float damage) {
    if (isFireId(fireId)) {
      ((FireTypeChanger) entity).setFireId(fireId);
      return harmOrHeal(entity, getOnFireDamageSource(fireId), getDamage(fireId), getInvertHealAndHarm(fireId));
    }
    ((FireTypeChanger) entity).setFireId(BASE_FIRE_ID);
    return harmOrHeal(entity, damageSource, damage, FireBuilder.DEFAULT_INVERT_HEAL_AND_HARM);
  }

  /**
   * Harms (or heals) the given {@code entity} based on the {@link Fire} registered with {@code fireId}.
   * <p>
   * If no {@link Fire} was registered with {@code fireId}, uses the provided {@code damageSource} and {@code damage} to harm (or heal) the {@code entity}.
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
}
