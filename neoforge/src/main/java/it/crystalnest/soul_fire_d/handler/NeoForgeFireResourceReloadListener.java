package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.soul_fire_d.Constants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

/**
 * Handles datapack reload events.
 */
@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class NeoForgeFireResourceReloadListener extends FireResourceReloadListener {
  /**
   * Handles the {@link AddServerReloadListenersEvent}.
   *
   * @param event {@link AddServerReloadListenersEvent}.
   */
  @SubscribeEvent
  public static void handle(AddServerReloadListenersEvent event) {
    event.addListener(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, Constants.DDFIRES), new NeoForgeFireResourceReloadListener());
  }

  /**
   * Handles the {@link OnDatapackSyncEvent}.
   *
   * @param event {@link OnDatapackSyncEvent}.
   */
  @SubscribeEvent
  public static void handle(OnDatapackSyncEvent event) {
    handle(event.getPlayer());
  }
}
