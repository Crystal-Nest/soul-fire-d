package crystalspider.soulfired.handlers;

import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Handles the registry events.
 */
@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.MOD)
public class FMLClientSetupEventHandler {
  /**
   * Handles the {@link FMLClientSetupEvent} event.
   * <p>
   * Registers all {@link Fire Fires} to the client.
   * 
   * @param event
   */
  @SubscribeEvent
  public static void handle(FMLClientSetupEvent event) {
    FireClientManager.registerFires(FireManager.getFires());
  }
}
