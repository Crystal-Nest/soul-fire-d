package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.soul_fire_d.Constants;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Resource (datapack) reload listener.
 */
public final class FabricFireResourceReloadListener extends FireResourceReloadListener implements IdentifiableResourceReloadListener {
  /**
   * @param player player to sync data to.
   * @param joined whether the syncing is happening because the player is joining.
   */
  public static void handle(ServerPlayer player, boolean joined) {
    handle(player);
  }

  @Override
  public ResourceLocation getFabricId() {
    return new ResourceLocation(Constants.MOD_ID, Constants.DDFIRES);
  }
}
