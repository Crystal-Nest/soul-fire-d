package crystalspider.soulfired.handlers;

import crystalspider.soulfired.SoulFiredLoader;
import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireEnchantmentBuilder;
import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.enchantment.FireTypedArrowEnchantment;
import crystalspider.soulfired.api.enchantment.FireTypedAspectEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegistryEventHandler {
  @SubscribeEvent
  public void register(RegistryEvent.Register<Enchantment> event) {
    for (Fire fire : FireManager.getFires()) {
      FireEnchantmentBuilder fireEnchantmentBuilder = FireManager.fireEnchantBuilder(SoulFiredLoader.MODID, fire);
      FireTypedAspectEnchantment fireAspect = fireEnchantmentBuilder.buildFireAspect();
      FireTypedArrowEnchantment flame = fireEnchantmentBuilder.buildFlame();
      event.getRegistry().register(fireAspect);
      event.getRegistry().register(flame);
      FireManager.registerFireAspect(fireAspect);
      FireManager.registerFlame(flame);
    }
  }
}
