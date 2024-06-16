package it.crystalnest.soul_fire_d.platform.services;

import it.crystalnest.soul_fire_d.api.Fire;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

/**
 * Networking helper.
 */
public interface NetworkHelper {
  /**
   * Registers the networking handlers.
   */
  void register();

  /**
   * Send a fire register packet to the given player.
   *
   * @param player player to send the packet to.
   * @param fire fire data.
   */
  void sendToClient(@Nullable ServerPlayer player, Fire fire);

  /**
   * Send a fire unregister packet to the given player.
   *
   * @param player player to send the packet to.
   * @param fireType fire reference.
   */
  void sendToClient(@Nullable ServerPlayer player, ResourceLocation fireType);
}
