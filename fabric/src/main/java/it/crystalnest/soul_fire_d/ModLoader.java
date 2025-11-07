package it.crystalnest.soul_fire_d;

import it.crystalnest.soul_fire_d.handler.LootTableEventsHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import org.jetbrains.annotations.ApiStatus;

/**
 * Mod loader.
 */
@ApiStatus.Internal
public final class ModLoader implements ModInitializer {
  @Override
  public void onInitialize() {
    CommonModLoader.init();
    LootTableEvents.MODIFY.register(LootTableEventsHandler::handle);
  }
}
