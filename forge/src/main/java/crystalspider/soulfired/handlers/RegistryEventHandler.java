package crystalspider.soulfired.handlers;

import crystalspider.soulfired.SoulFiredLoader;
import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireEnchantmentBuilder;
import crystalspider.soulfired.api.FireManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handles the registry events.
 */
public class RegistryEventHandler {
  /**
   * Handles the {@link Register} event for {@link Enchantment Enchantments}.
   * <p>
   * Registers default Fire Aspect and Flame enchantments for all registered {@link Fire Fires}.
   * 
   * @param event
   */
  @SubscribeEvent
  public void handle(Register<Enchantment> event) {
    for (Fire fire : FireManager.getFires()) {
      FireEnchantmentBuilder fireEnchantmentBuilder = FireManager.fireEnchantBuilder(SoulFiredLoader.MODID, fire);
      event.getRegistry().register(FireManager.registerFireAspect(fireEnchantmentBuilder.buildFireAspect()));
      event.getRegistry().register(FireManager.registerFlame(fireEnchantmentBuilder.buildFlame()));
    }
  }
}
