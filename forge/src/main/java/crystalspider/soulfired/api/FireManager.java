package crystalspider.soulfired.api;

import java.util.HashMap;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.resources.model.Material;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.block.state.BlockState;

public class FireManager {
  /**
   * Logger.
   */
  public static final Logger LOGGER = LogUtils.getLogger();

  private static final HashMap<String, Fire> fires = new HashMap<>();

  public static final FireBuilder getFireBuilder() {
    return new FireBuilder();
  }

  public static final void registerFire(Fire fire) {
    String fireId = fire.getId();
    if (!fires.containsKey(fireId)) {
      fires.put(fireId, fire);
    } else {
      LOGGER.error("Fire [" + fireId + "] was already registered with the following value: " + fires.get(fireId));
    }
  }

  public static final boolean isValidFireId(String id) {
    return !(id == null || id.isBlank());
  }

  public static final boolean isFireId(String id) {
    return isValidFireId(id) && fires.containsKey(id);
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

  public static final BlockState getSourceBlock(String fireId) {
    if (isFireId(fireId)) {
      return fires.get(fireId).getBlockState();
    }
    return null;
  }
}
