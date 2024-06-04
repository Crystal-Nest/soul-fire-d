package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.soul_fire_d.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

/**
 * Handles datapack reload events.
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class NeoForgeFireResourceReloadListener extends FireResourceReloadListener {
  /**
   * Handles the {@link AddReloadListenerEvent}.
   *
   * @param event
   */
  @SubscribeEvent
  public static void handle(AddReloadListenerEvent event) {
    event.addListener(new NeoForgeFireResourceReloadListener());
  }

  /**
   * Handles the {@link OnDatapackSyncEvent}.
   *
   * @param event
   */
  @SubscribeEvent
  public static void handle(OnDatapackSyncEvent event) {
    handle(event.getPlayer());
  }
} 
