package it.crystalnest.soul_fire_d.fire;

import it.crystalnest.prometheus.api.FireManager;
import net.minecraft.resources.ResourceLocation;

/**
 * Fire registry.
 */
public final class FireRegistry {
  /**
   * Fire type of Soul Fire.
   */
  public static final ResourceLocation SOUL_FIRE_TYPE = ResourceLocation.withDefaultNamespace("soul");

  static {
    FireManager.registerFire(FireManager.fireBuilder(SOUL_FIRE_TYPE).setDefaultComponents().setLight(10).setDamage(2).build());
  }

  private FireRegistry() {}

  /**
   * Called outside to load the class and register.
   */
  public static void register() {}
}
