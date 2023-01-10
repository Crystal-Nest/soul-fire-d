package crystalspider.soulfired;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.config.SoulFiredConfig;
import crystalspider.soulfired.handlers.LootTableEventsHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.api.ModLoadingContext;

/**
 * Soul fire'd mod loader.
 */
public final class SoulFiredLoader implements ModInitializer {
  /**
   * ID of this mod.
   */
  public static final String MODID = "soulfired";

  private SoulFiredLoader() {}

  @Override
  public void onInitialize() {
    ModLoadingContext.registerConfig(MODID, Type.COMMON, SoulFiredConfig.SPEC);
    LootTableEvents.MODIFY.register(LootTableEventsHandler::handle);
    FireManager.registerFire(
      FireManager.fireBuilder(FireManager.SOUL_FIRE_TYPE)
        .setDamage(2)
        .setFireAspectConfig(builder -> builder.setEnabled(SoulFiredConfig::getEnableSoulFireAspect))
        .setFlameConfig(builder -> builder.setEnabled(SoulFiredConfig::getEnableSoulFlame))
      .build()
    );
  }
}