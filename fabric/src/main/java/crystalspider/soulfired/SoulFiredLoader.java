package crystalspider.soulfired;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.handlers.LootTableEventsHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;

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
    FireManager.registerFire(
      FireManager.fireBuilder()
        .setModId(MODID)
        .setId(FireManager.SOUL_FIRE_ID)
        .setDamage(2)
        .setSourceBlock(Blocks.SOUL_FIRE)
      .build()
    );
    LootTableLoadingCallback.EVENT.register(LootTableEventsHandler::handle);
  }
}