package it.crystalnest.soul_fire_d.network;

import it.crystalnest.soul_fire_d.network.handler.FirePacketHandler;
import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public final class NeoForgeFirePacketHandler {
  private NeoForgeFirePacketHandler() {}

  public static void handle(RegisterFirePacket packet, PlayPayloadContext context) {
    FirePacketHandler.handle(packet);
  }

  public static void handle(UnregisterFirePacket packet, PlayPayloadContext context) {
    FirePacketHandler.handle(packet);
  }
}
