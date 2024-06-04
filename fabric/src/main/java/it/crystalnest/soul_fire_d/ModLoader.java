package it.crystalnest.soul_fire_d;

import it.crystalnest.soul_fire_d.handler.FabricFireResourceReloadListener;
import it.crystalnest.soul_fire_d.handler.LootTableEventsHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.ApiStatus;

/**
 * Mod loader.
 */
@ApiStatus.Internal
public class ModLoader implements ModInitializer {
  @Override
  public void onInitialize() {
    CommonModLoader.init();
    LootTableEvents.MODIFY.register(LootTableEventsHandler::handle);
    ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(FabricFireResourceReloadListener::handle);
    ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricFireResourceReloadListener());
  }
}
