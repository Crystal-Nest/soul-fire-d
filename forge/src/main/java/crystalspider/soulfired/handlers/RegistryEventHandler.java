package crystalspider.soulfired.handlers;

import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Handles the registry events.
 */
@EventBusSubscriber(bus = Bus.MOD)
public class RegistryEventHandler {
  /**
   * Handles the {@link Register} event for {@link Enchantment Enchantments}.
   * <p>
   * Registers Fire Aspect and Flame enchantments for all registered {@link Fire Fires} with such enchantments.
   * 
   * @param event
   */
  @SubscribeEvent
  public static void handle(Register<Enchantment> event) {
    for (Fire fire : FireManager.getFires()) {
      if (fire.getFireAspect().isPresent()) {
        event.getRegistry().register(fire.getFireAspect().get());
      }
      if (fire.getFlame().isPresent()) {
        event.getRegistry().register(fire.getFlame().get());
      }
    }
  }
}
