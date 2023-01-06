package crystalspider.soulfired.handlers;

import crystalspider.soulfired.api.FireBuilder;
import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.config.SoulFiredConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Loading;

/**
 * Handles configuration events.
 */
@EventBusSubscriber(bus = Bus.MOD)
public class ModConfigEventHandler {
  /**
   * Handles the {@link Loading} event.
   * <p>
   * Registers Soul Fire.
   * 
   * @param event
   */
  @SubscribeEvent
  public static void handle(Loading event) {
    FireBuilder fireBuilder = FireManager.fireBuilder().setModId(FireManager.SOUL_FIRE_TYPE.getNamespace()).setFireId(FireManager.SOUL_FIRE_TYPE.getPath()).setDamage(2);
    if (!SoulFiredConfig.getEnableSoulFireAspect()) {
      fireBuilder.removeFireAspect();
    }
    if (!SoulFiredConfig.getEnableSoulFlame()) {
      fireBuilder.removeFlame();
    }
    FireManager.registerFire(fireBuilder.build());
  }
}
