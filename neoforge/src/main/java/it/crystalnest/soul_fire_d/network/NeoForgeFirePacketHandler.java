package it.crystalnest.soul_fire_d.network;

import it.crystalnest.soul_fire_d.network.handler.FirePacketHandler;
import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import net.neoforged.neoforge.network.NetworkEvent.Context;

/**
 * NeoForge fire packet handler.
 */
public final class NeoForgeFirePacketHandler {
  private NeoForgeFirePacketHandler() {}

  /**
   * Handles a {@link RegisterFirePacket}.
   *
   * @param packet {@link RegisterFirePacket}.
   * @param context payload context.
   */
  public static void handle(RegisterFirePacket packet, Context context) {
    FirePacketHandler.handle(packet);
  }

  /**
   * Handles an {@link UnregisterFirePacket}.
   *
   * @param packet {@link UnregisterFirePacket}.
   * @param context payload context.
   */
  public static void handle(UnregisterFirePacket packet, Context context) {
    FirePacketHandler.handle(packet);
  }
}
