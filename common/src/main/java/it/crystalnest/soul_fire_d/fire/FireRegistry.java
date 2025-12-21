package it.crystalnest.soul_fire_d.fire;

import it.crystalnest.prometheus.api.Fire;
import it.crystalnest.prometheus.api.FireManager;
import it.crystalnest.prometheus.api.FireRegistrar;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fire registry.
 */
public final class FireRegistry {
  /**
   * Fire type of Soul Fire (from {@link FireManager#SOUL_FIRE_TYPE}).
   */
  @ApiStatus.Internal
  public static final Identifier SOUL_FIRE_TYPE = FireManager.SOUL_FIRE_TYPE;

  static {
    // noinspection DataFlowIssue: key of SOUL_FIRE_FLAME is sure to be defined.
    FireManager.registerFire(
      FireManager.fireBuilder(SOUL_FIRE_TYPE)
        .setDefaultComponents()
        .setComponent(Fire.Component.FLAME_PARTICLE, BuiltInRegistries.PARTICLE_TYPE.getKey(ParticleTypes.SOUL_FIRE_FLAME))
        .setLight(10)
        .setDamage(2)
        .build()
    );
    FireRegistrar.registerFireCharge(SOUL_FIRE_TYPE);
  }

  private FireRegistry() {}

  /**
   * Called outside to load the class and register.
   */
  public static void register() {}
}
