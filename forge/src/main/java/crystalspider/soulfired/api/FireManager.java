package crystalspider.soulfired.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import crystalspider.soulfired.api.enchantment.FireTypedArrowEnchantment;
import crystalspider.soulfired.api.enchantment.FireTypedAspectEnchantment;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.client.resources.model.Material;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;

public abstract class FireManager {
  /**
   * Logger.
   */
  public static final Logger LOGGER = LogUtils.getLogger();

  public static final String DEFAULT_FIRE_ID = "";

  public static final String SOUL_FIRE_ID = "soul";

  /**
   * {@link HashMap} of all registered {@link Fire Fires}.
   */
  private static volatile ConcurrentHashMap<String, Fire> fires = new ConcurrentHashMap<>();

  private static volatile ConcurrentHashMap<String, FireTypedAspectEnchantment> fireAspectEnchants = new ConcurrentHashMap<>();
  private static volatile ConcurrentHashMap<String, FireTypedArrowEnchantment> flameEnchants = new ConcurrentHashMap<>();

  public static final synchronized Fire registerFire(Fire fire) {
    String fireId = fire.getId();
    if (!fires.containsKey(fireId)) {
      fires.put(fireId, fire);
      return fire;
    }
    LOGGER.error("Fire [" + fireId + "] was already registered with the following value: " + fires.get(fireId));
    return fires.get(fireId);
  }

  public static final synchronized FireTypedAspectEnchantment registerFireAspect(FireTypedAspectEnchantment fireAspectEnchant) {
    String fireId = fireAspectEnchant.getFireId();
    if (isFireId(fireId)) {
      if (!fireAspectEnchants.containsKey(fireId)) {
        fireAspectEnchants.put(fireId, fireAspectEnchant);
        return fireAspectEnchant;
      }
      LOGGER.error("Fire Aspect Enchantment with fireId [" + fireId + "] was already registered");
      return fireAspectEnchants.get(fireId);
    }
    LOGGER.error("Tried to register a Fire Aspect Enchantment with an non-valid or not mapped fireId [" + fireId + "]");
    return null;
  }

  public static final synchronized FireTypedArrowEnchantment registerFlame(FireTypedArrowEnchantment flameEnchant) {
    String fireId = flameEnchant.getFireId();
    if (isFireId(fireId)) {
      if (!flameEnchants.containsKey(fireId)) {
        flameEnchants.put(fireId, flameEnchant);
        return flameEnchant;
      }
      LOGGER.error("Flame Enchantment with fireId [" + fireId + "] was already registered");
      return flameEnchants.get(fireId);
    }
    LOGGER.error("Tried to register a Flame Enchantment with an non-valid or not mapped fireId [" + fireId + "]");
    return null;
  }

  public static final CampfireBlock createCampfireBlock(String fireId, boolean spawnParticles, Properties properties) {
    if (isValidFireId(fireId)) {
      CampfireBlock campfire = new CampfireBlock(spawnParticles, 0, properties);
      ((FireTypeChanger) campfire).setFireId(fireId);
      return campfire;
    }
    return new CampfireBlock(spawnParticles, 1, properties);
  }

  public static final FireBuilder fireBuilder() {
    return new FireBuilder();
  }

  public static final FireEnchantmentBuilder fireEnchantBuilder(String modId) {
    return new FireEnchantmentBuilder(modId);
  }

  public static final FireEnchantmentBuilder fireEnchantBuilder(String modId, String fireId) {
    return new FireEnchantmentBuilder(modId, fireId);
  }

  public static final FireEnchantmentBuilder fireEnchantBuilder(String modId, Fire fire) {
    return new FireEnchantmentBuilder(modId, fire);
  }

  public static final boolean isValidFireId(String id) {
    return !(id == null || id.isBlank());
  }

  public static final boolean isFireId(String id) {
    return isValidFireId(id) && fires.containsKey(id);
  }

  public static final String sanitizeFireId(String id) {
    if (isValidFireId(id)) {
      return id.trim();
    }
    return DEFAULT_FIRE_ID;
  }

  public static final String ensureFireId(String id) {
    if (fires.containsKey(sanitizeFireId(id))) {
      return id;
    }
    return DEFAULT_FIRE_ID;
  }

  public static final List<String> getFireIds() {
    return List.copyOf(Collections.list(fires.keys()));
  }

  public static final List<Fire> getFires() {
    return List.copyOf(fires.values());
  }

  public static final float getDamage(String id) {
    if (isFireId(id)) {
      return fires.get(id).getDamage();
    }
    return 0;
  }

  public static final boolean isFireDamageSource(DamageSource damageSource) {
    return fires.values().stream().anyMatch(fire -> fire.getInFire() == damageSource || fire.getOnFire() == damageSource);
  }

  public static final DamageSource getInFireDamageSource(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getInFire();
    }
    return null;
  }

  public static final DamageSource getOnFireDamageSource(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getOnFire();
    }
    return null;
  }

  public static final Material getMaterial0(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getMaterial0();
    }
    return null;
  }

  public static final Material getMaterial1(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getMaterial1();
    }
    return null;
  }

  public static final SoundEvent getHurtSound(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getHurtSound();
    }
    return null;
  }

  public static final BlockState getSourceBlock(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getBlockState();
    }
    return null;
  }

  public static final List<FireTypedAspectEnchantment> getFireAspectEnchants() {
    return List.copyOf(fireAspectEnchants.values());
  }

  public static final List<FireTypedArrowEnchantment> getFlameEnchants() {
    return List.copyOf(flameEnchants.values());
  }

  public static final FireTypedAspectEnchantment getFireAspectEnchant(String fireId) {
    if (isFireId(fireId)) {
      return fireAspectEnchants.get(fireId);
    }
    return null;
  }

  public static final FireTypedArrowEnchantment getFlameEnchant(String fireId) {
    if (isFireId(fireId)) {
      return flameEnchants.get(fireId);
    }
    return null;
  }

  public static final boolean hurtEntityInFire(Entity entity, String fireId, DamageSource damageSource, float damage) {
    return hurtEntity(entity, fireId, damageSource, damage, FireManager::getInFireDamageSource);
  }

  public static final boolean hurtEntityOnFire(Entity entity, String fireId, DamageSource damageSource, float damage) {
    return hurtEntity(entity, fireId, damageSource, damage, FireManager::getOnFireDamageSource);
  }

  private static final boolean hurtEntity(Entity entity, String fireId, DamageSource damageSource, float damage, Function<String, DamageSource> damageSourceGetter) {
    if (isFireId(fireId)) {
      ((FireTypeChanger) entity).setFireId(fireId);
      return entity.hurt(damageSourceGetter.apply(fireId), getDamage(fireId));
    }
    ((FireTypeChanger) entity).setFireId(DEFAULT_FIRE_ID);
    return entity.hurt(damageSource, damage);
  }
}
