package it.crystalnest.soul_fire_d.network.handler;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.client.FireClientManager;
import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;

public final class FirePacketHandler {
  private FirePacketHandler() {}

  public static void handle(RegisterFirePacket packet) {
    // Fire will already be registered with FireManager if in single-player.
    if (!FireManager.isRegisteredType(packet.fire().getFireType())) {
      FireManager.registerFire(packet.fire());
    }
    FireClientManager.registerFire(packet.fire());
  }

  public static void handle(UnregisterFirePacket packet) {
    FireManager.unregisterFire(packet.fireType());
    FireClientManager.unregisterFire(packet.fireType());
  }
}
