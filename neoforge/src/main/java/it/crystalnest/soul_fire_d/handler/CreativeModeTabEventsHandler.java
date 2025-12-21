package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.prometheus.api.Fire;
import it.crystalnest.prometheus.api.FireManager;
import it.crystalnest.soul_fire_d.Constants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/**
 * Handler for creative mode tabs related events.
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public final class CreativeModeTabEventsHandler {
  /**
   * Handles the {@link BuildCreativeModeTabContentsEvent} event.<br>
   * Adds the copper campfire right after the normal campfire.
   *
   * @param event {@link BuildCreativeModeTabContentsEvent}.
   */
  @SubscribeEvent
  public static void handle(BuildCreativeModeTabContentsEvent event) {
    ResourceKey<CreativeModeTab> key = event.getTabKey();
    if (key == CreativeModeTabs.INGREDIENTS || key == CreativeModeTabs.TOOLS_AND_UTILITIES) {
      event.insertAfter(
        Items.FIRE_CHARGE.getDefaultInstance(),
        FireManager.getRequiredComponent(FireManager.SOUL_FIRE_TYPE, Fire.Component.FIRE_CHARGE_ITEM).getDefaultInstance(),
        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
      );
    }
  }
}
