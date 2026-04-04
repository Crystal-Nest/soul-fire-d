package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.prometheus.api.Fire;
import it.crystalnest.prometheus.api.FireManager;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;

/**
 * Handler for creative mode tabs related events.
 */
public final class CreativeModeTabEventsHandler {
  private CreativeModeTabEventsHandler() {}

  /**
   * Handles the {@link CreativeModeTabEvents#MODIFY_OUTPUT_ALL} event.<br>
   * Adds the copper campfire right after the normal campfire.
   *
   * @param tab creative mode tab.
   * @param entries item group entries.
   */
  public static void handle(CreativeModeTab tab, FabricCreativeModeTabOutput entries) {
    BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).ifPresent(key -> {
      if (key == CreativeModeTabs.INGREDIENTS || key == CreativeModeTabs.TOOLS_AND_UTILITIES) {
        entries.insertAfter(Items.FIRE_CHARGE, FireManager.getRequiredComponent(FireManager.SOUL_FIRE_TYPE, Fire.Component.FIRE_CHARGE_ITEM));
      }
    });
  }
}
