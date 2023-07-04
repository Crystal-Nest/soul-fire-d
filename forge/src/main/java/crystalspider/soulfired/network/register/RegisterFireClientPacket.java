package crystalspider.soulfired.network.register;

import java.util.function.Supplier;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import net.minecraftforge.fml.network.NetworkEvent.Context;

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
  final static void handle(RegisterFirePacket packet, Supplier<Context> context) {
    // Fire will already be registered with FireManager if in single-player.
    if (!FireManager.isRegisteredType(packet.fire.getFireType())) {
      FireManager.registerFire(packet.fire);
    }
    FireClientManager.registerFire(packet.fire);
  }
}
