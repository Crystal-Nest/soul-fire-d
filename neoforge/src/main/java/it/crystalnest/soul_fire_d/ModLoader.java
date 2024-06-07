package it.crystalnest.soul_fire_d;

import it.crystalnest.soul_fire_d.loot.LootRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

/**
 * Mod loader.
 */
@ApiStatus.Internal
@Mod(Constants.MOD_ID)
public final class ModLoader {
  /**
   * NeoForge mod event bus.
   */
  private static IEventBus bus;

  /**
   * Mod initialization.
   *
   * @param bus Event bus.
   */
  public ModLoader(IEventBus bus) {
    ModLoader.bus = bus;
    CommonModLoader.init();
    LootRegistry.register(bus);
  }

  /**
   * Returns the event {@link #bus}.
   *
   * @return the event {@link #bus}.
   */
  public static IEventBus getBus() {
    return bus;
  }
}
