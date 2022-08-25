package crystalspider.soulfired.api;

import java.util.HashMap;
import java.util.function.Function;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.resources.model.Material;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public class FireManager {
  /**
   * Logger.
   */
  public static final Logger LOGGER = LogUtils.getLogger();

  /**
   * {@link HashMap} of all registered {@link Fire Fires}.
   */
  private static volatile HashMap<String, Fire> fires = new HashMap<>();

  public static final synchronized FireBuilder getFireBuilder() {
    return new FireBuilder();
  }

  public static final synchronized void registerFire(Fire fire) {
    String fireId = fire.getId();
    if (!fires.containsKey(fireId)) {
      fires.put(fireId, fire);
    } else {
      LOGGER.error("Fire [" + fireId + "] was already registered with the following value: " + fires.get(fireId));
    }
  }

  public static final synchronized boolean isValidFireId(String id) {
    return !(id == null || id.isBlank());
  }

  public static final synchronized boolean isFireId(String id) {
    return isValidFireId(id) && fires.containsKey(id);
  }

  public static final synchronized float getDamage(String id) {
    if (isFireId(id)) {
      return fires.get(id).getDamage();
    }
    return 0;
  }

  public static final synchronized boolean isFireDamageSource(DamageSource damageSource) {
    return fires.values().stream().anyMatch(fire -> fire.getInFire() == damageSource || fire.getOnFire() == damageSource);
  }

  public static final synchronized DamageSource getInFireDamageSource(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getInFire();
    }
    return null;
  }

  public static final synchronized DamageSource getOnFireDamageSource(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getOnFire();
    }
    return null;
  }

  public static final synchronized Material getMaterial0(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getMaterial0();
    }
    return null;
  }

  public static final synchronized Material getMaterial1(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getMaterial1();
    }
    return null;
  }

  public static final synchronized SoundEvent getHurtSound(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getHurtSound();
    }
    return null;
  }

  public static final synchronized BlockState getSourceBlock(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getBlockState();
    }
    return null;
  }

  public static final synchronized boolean hurtEntityInFire(Entity entity, String fireId, DamageSource damageSource, float damage) {
    return hurtEntity(entity, fireId, damageSource, damage, FireManager::getInFireDamageSource);
  }

  public static final synchronized boolean hurtEntityOnFire(Entity entity, String fireId, DamageSource damageSource, float damage) {
    return hurtEntity(entity, fireId, damageSource, damage, FireManager::getOnFireDamageSource);
  }

  private static final synchronized boolean hurtEntity(Entity entity, String fireId, DamageSource damageSource, float damage, Function<String, DamageSource> damageSourceGetter) {
    if (isFireId(fireId)) {
      ((FireTyped) entity).setFireId(fireId);
      return entity.hurt(damageSourceGetter.apply(fireId), getDamage(fireId));
    }
    ((FireTyped) entity).setFireId(null);
    return entity.hurt(damageSource, damage);
  }
}
