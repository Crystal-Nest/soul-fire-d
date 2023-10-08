package crystalspider.soulfired.network.unregister;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import net.minecraftforge.event.network.CustomPayloadEvent.Context;

/**
 * Unregister Fire Client Packet.
 */
final class UnregisterFireClientPacket {
  /**
   * Handles a {@link UnregisterFirePacket} client-side.
   * <p>
   * Unregisters the Fire specified by the packet.
   * 
   * @param packet
   * @param context
   */
  @SuppressWarnings("deprecation")
  final static void handle(UnregisterFirePacket packet, Context context) {
    FireManager.unregisterFire(packet.fireType);
    FireClientManager.unregisterFire(packet.fireType);
  }
}
