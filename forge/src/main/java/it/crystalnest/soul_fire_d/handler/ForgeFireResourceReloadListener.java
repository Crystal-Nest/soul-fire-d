package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.soul_fire_d.Constants;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Handles datapack reload events.
 */
@EventBusSubscriber(modid = Constants.MOD_ID, bus = Bus.FORGE)
public final class ForgeFireResourceReloadListener extends FireResourceReloadListener {
  /**
   * Handles the {@link AddReloadListenerEvent}.
   *
   * @param event
   */
  @SubscribeEvent
  public static void handle(AddReloadListenerEvent event) {
    event.addListener(new ForgeFireResourceReloadListener());
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
