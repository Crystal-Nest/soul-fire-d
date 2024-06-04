package it.crystalnest.soul_fire_d;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.config.ModConfig;
import it.crystalnest.soul_fire_d.platform.Services;
import org.jetbrains.annotations.ApiStatus;

/**
 * Common mod loader.
 */
@ApiStatus.Internal
public final class CommonModLoader {
  private CommonModLoader() {}

  /**
   * Initialize common operations across loaders.
   */
  public static void init() {
    ModConfig.CONFIG.register();
    Services.NETWORK.register();
    FireManager.registerFire(
      FireManager.fireBuilder(FireManager.SOUL_FIRE_TYPE)
        .setDamage(2)
        .setFireAspectConfig(builder -> builder
          .setEnabled(ModConfig::getEnableSoulFireAspect)
          .setIsDiscoverable(ModConfig::getEnableSoulFireAspectDiscovery)
          .setIsTradeable(ModConfig::getEnableSoulFireAspectTrades)
          .setIsTreasure(ModConfig::getEnableSoulFireAspectTreasure)
        )
        .setFlameConfig(builder -> builder
          .setEnabled(ModConfig::getEnableSoulFlame)
          .setIsDiscoverable(ModConfig::getEnableSoulFlameDiscovery)
          .setIsTradeable(ModConfig::getEnableSoulFlameTrades)
          .setIsTreasure(ModConfig::getEnableSoulFlameTreasure)
        )
        .build()
    );
  }
}
