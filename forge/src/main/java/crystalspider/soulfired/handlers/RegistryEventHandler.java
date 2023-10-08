package crystalspider.soulfired.handlers;

import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegisterEvent;

/**
 * Handles the registry events.
 */
@EventBusSubscriber(bus = Bus.MOD)
public final class RegistryEventHandler {
  private RegistryEventHandler() {}

  /**
   * Handles the {@link Register} event for {@link Enchantment Enchantments}.
   * <p>
   * Registers Fire Aspect and Flame enchantments for all registered {@link Fire Fires} with such enchantments.
   * 
   * @param event
   */
  @SubscribeEvent
  public static void handle(RegisterEvent event) {
    for (Fire fire : FireManager.getFires()) {
      ResourceLocation fireType = fire.getFireType();
      event.register(Keys.ENCHANTMENTS, helper -> {
        if (fire.getFireAspect().isPresent()) {
          helper.register(new ResourceLocation(fireType.getNamespace(), fireType.getPath() + "_fire_aspect"), fire.getFireAspect().get());
        }
        if (fire.getFlame().isPresent()) {
          helper.register(new ResourceLocation(fireType.getNamespace(), fireType.getPath() + "_flame"), fire.getFlame().get());
        }
      });
    }
  }
}
