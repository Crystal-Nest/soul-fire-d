package crystalspider.soulfired;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.config.SoulFiredConfig;
import crystalspider.soulfired.handlers.FireResourceReloadListener;
import crystalspider.soulfired.handlers.LootTableEventsHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraftforge.api.ModLoadingContext;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraftforge.fml.config.ModConfig.Type;

/**
 * Soul fire'd mod loader.
 */
public final class SoulFiredLoader implements ModInitializer {
  /**
   * ID of this mod.
   */
  public static final String MODID = "soulfired";

  @Override
  public void onInitialize() {
    ModLoadingContext.registerConfig(MODID, Type.COMMON, SoulFiredConfig.SPEC);
    LootTableLoadingCallback.EVENT.register(LootTableEventsHandler::handle);
    ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new FireResourceReloadListener());
    FireManager.registerFire(
      FireManager.fireBuilder(FireManager.SOUL_FIRE_TYPE)
        .setDamage(2)
        .setFireAspectConfig(builder -> builder
          .setEnabled(SoulFiredConfig::getEnableSoulFireAspect)
          .setIsDiscoverable(SoulFiredConfig::getEnableSoulFireAspectDiscovery)
          .setIsTradeable(SoulFiredConfig::getEnableSoulFireAspectTrades)
          .setIsTreasure(SoulFiredConfig::getEnableSoulFireAspectTreasure)
        )
        .setFlameConfig(builder -> builder
          .setEnabled(SoulFiredConfig::getEnableSoulFlame)
          .setIsDiscoverable(SoulFiredConfig::getEnableSoulFlameDiscovery)
          .setIsTradeable(SoulFiredConfig::getEnableSoulFlameTrades)
          .setIsTreasure(SoulFiredConfig::getEnableSoulFlameTreasure)
        )
      .build()
    );
  }
}
