package it.crystalnest.soul_fire_d.network.handler;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.client.FireClientManager;
import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;

/**
 * Handler for fire packets.
 */
public final class FirePacketHandler {
  private FirePacketHandler() {}

  /**
   * Handles a {@link RegisterFirePacket}.
   *
   * @param packet {@link RegisterFirePacket}.
   */
  public static void handle(RegisterFirePacket packet) {
    // Fire will already be registered with FireManager if in single-player.
    if (!FireManager.isRegisteredType(packet.fire().getFireType())) {
      FireManager.registerFire(packet.fire());
    }
    FireClientManager.registerFire(packet.fire());
  }

  /**
   * Handles a {@link UnregisterFirePacket}.
   *
   * @param packet {@link UnregisterFirePacket}.
   */
  public static void handle(UnregisterFirePacket packet) {
    FireManager.unregisterFire(packet.fireType());
    FireClientManager.unregisterFire(packet.fireType());
  }
}
