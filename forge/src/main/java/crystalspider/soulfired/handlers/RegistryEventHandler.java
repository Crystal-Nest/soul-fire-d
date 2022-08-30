package crystalspider.soulfired.handlers;

import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegisterEvent;

/**
 * Handles the registry events.
 */
public class RegistryEventHandler {
  /**
   * Handles the {@link RegisterEvent}.
   * <p>
   * Registers default Fire Aspect and Flame enchantments for all registered {@link Fire Fires}.
   * 
   * @param event
   */
  @SubscribeEvent
  public void handle(RegisterEvent event) {
    for (Fire fire : FireManager.getFires()) {
      event.register(Keys.ENCHANTMENTS, helper -> {
        helper.register(new ResourceLocation(fire.getModId(), fire.getId() + "_fire_aspect"), fire.getFireAspect());
        helper.register(new ResourceLocation(fire.getModId(), fire.getId() + "_flame"), fire.getFlame());
      });
    }
  }
}
