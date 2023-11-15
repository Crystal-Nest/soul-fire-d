package crystalspider.soulfired.network.register;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import net.neoforged.neoforge.network.NetworkEvent.Context;

/**
 * Register Fire Client Packet.
 */
final class RegisterFireClientPacket {
  /**
   * Handles a {@link RegisterFirePacket} client-side.
   * <p>
   * Registers the Fire contained in the packet.
   * 
   * @param packet
   * @param context
   */
  final static void handle(RegisterFirePacket packet, Context context) {
    // Fire will already be registered with FireManager if in single-player.
    if (!FireManager.isRegisteredType(packet.fire.getFireType())) {
      FireManager.registerFire(packet.fire);
    }
    FireClientManager.registerFire(packet.fire);
  }
}
