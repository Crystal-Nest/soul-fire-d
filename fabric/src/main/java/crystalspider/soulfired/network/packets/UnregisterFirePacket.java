package crystalspider.soulfired.network.packets;

import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

/**
 * Network packet to unregister a Fire.
 */
public final class UnregisterFirePacket {
  /**
   * Handles the packet.
   * 
   * @param client
   * @param handle
   * @param buffer
   * @param sender
   */
  @SuppressWarnings("deprecation")
  public static void handle(MinecraftClient client, ClientPlayNetworkHandler handle, PacketByteBuf buffer, PacketSender sender) {
    Identifier fireType = decode(buffer);
    FireManager.unregisterFire(fireType);
    FireClientManager.unregisterFire(fireType);
  }

  /**
   * Decodes data to instantiate an UnregisterFirePacket from the network buffer.
   * 
   * @param buffer {@link PacketByteBuf} from which to read data to get the {@link Fire} to register.
   */
  public static Identifier decode(PacketByteBuf buffer) {
    return buffer.readIdentifier();
  }
}
