package it.crystalnest.soul_fire_d;

import it.crystalnest.soul_fire_d.loot.LootRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.ApiStatus;

/**
 * Mod loader.
 */
@ApiStatus.Internal
@Mod(Constants.MOD_ID)
public final class ModLoader {
  /**
   * Mod initialization.
   */
  public ModLoader() {
    CommonModLoader.init();
    LootRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
  }
}
