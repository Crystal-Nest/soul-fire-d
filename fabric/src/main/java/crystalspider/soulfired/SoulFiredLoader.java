package crystalspider.soulfired;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.config.SoulFiredConfig;
import crystalspider.soulfired.handlers.LootTableEventsHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;

/**
 * Soul fire'd mod loader.
 */
public class SoulFiredLoader implements ModInitializer {
  /**
   * ID of this mod.
   */
  public static final String MODID = "soulfired";

  @Override
  public void onInitialize() {
    LootTableLoadingCallback.EVENT.register(LootTableEventsHandler::handle);
    FireManager.registerFire(
      FireManager.fireBuilder(FireManager.SOUL_FIRE_TYPE)
        .setDamage(2)
        .setFireAspectConfig(builder -> builder.setEnabled(SoulFiredConfig::getEnableSoulFireAspect))
        .setFlameConfig(builder -> builder.setEnabled(SoulFiredConfig::getEnableSoulFlame))
      .build()
    );
  }
}