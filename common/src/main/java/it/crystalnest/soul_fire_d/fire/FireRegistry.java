package it.crystalnest.soul_fire_d.fire;

import it.crystalnest.prometheus.api.Fire;
import it.crystalnest.prometheus.api.FireManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

/**
 * Fire registry.
 */
public final class FireRegistry {
  /**
   * Fire type of Soul Fire (from {@link FireManager#SOUL_FIRE_TYPE}).
   */
  public static final ResourceLocation SOUL_FIRE_TYPE = FireManager.SOUL_FIRE_TYPE;

  static {
    FireManager.registerFire(
      FireManager.fireBuilder(SOUL_FIRE_TYPE)
        .setDefaultComponents()
        .setComponent(Fire.Component.FLAME_PARTICLE, BuiltInRegistries.PARTICLE_TYPE.getKey(ParticleTypes.SOUL_FIRE_FLAME))
        .setLight(10)
        .setDamage(2)
        .build()
    );
  }

  private FireRegistry() {}

  /**
   * Called outside to load the class and register.
   */
  public static void register() {}
}
