package crystalspider.soulfired;

import crystalspider.soulfired.api.FireManager;
import net.fabricmc.api.ModInitializer;
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
  }
}