package crystalspider.soulfired;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.config.ModConfig;
import crystalspider.soulfired.handler.FireResourceReloadListener;
import crystalspider.soulfired.handler.LootTableEventsHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

/**
 * Soul fire'd mod loader.
 */
public final class ModLoader implements ModInitializer {
  /**
   * ID of this mod.
   */
  public static final String MOD_ID = "soulfired";

  @Override
  public void onInitialize() {
    ModLoadingContext.registerConfig(MOD_ID, Type.COMMON, ModConfig.SPEC);
    LootTableEvents.MODIFY.register(LootTableEventsHandler::handle);
    ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new FireResourceReloadListener());
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
