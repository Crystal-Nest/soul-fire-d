package it.crystalnest.soul_fire_d;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.enchantment.EnchantmentRegistry;
import it.crystalnest.soul_fire_d.config.ModConfig;
import it.crystalnest.soul_fire_d.platform.Services;
import org.jetbrains.annotations.ApiStatus;

/**
 * Common mod loader.
 */
@ApiStatus.Internal
public final class CommonModLoader {
  static {
    FireManager.registerFire(FireManager.fireBuilder(FireManager.SOUL_FIRE_TYPE)
      .setLight(10)
      .setDamage(2)
      .build()
    );
  }

  private CommonModLoader() {}

  /**
   * Initialize common operations across loaders.
   */
  public static void init() {
    ModConfig.CONFIG.register();
    Services.NETWORK.register();
    EnchantmentRegistry.register();
  }
}
