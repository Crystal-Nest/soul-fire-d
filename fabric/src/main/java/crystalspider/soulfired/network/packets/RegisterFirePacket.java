package crystalspider.soulfired.network.packets;

import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireBuilder;
import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

/**
 * Network packet to register a {@link Fire}.
 */
public final class RegisterFirePacket {
  /**
   * Handles the packet.
   * 
   * @param client
   * @param handle
   * @param buffer
   * @param sender
  */
  public static void handle(MinecraftClient client, ClientPlayNetworkHandler handle, PacketByteBuf buffer, PacketSender sender) {
    Fire fire = decode(buffer);
    // Fire will already be registered with FireManager if in single-player.
    if (!FireManager.isRegisteredType(fire.getFireType())) {
      FireManager.registerFire(fire);
    }
    FireClientManager.registerFire(fire);
  }

  /**
   * Decodes data to instantiate a RegisterFirePacket from the network buffer.
   * 
   * @param buffer {@link PacketByteBuf} from which to read data to get the {@link Fire} to register.
   */
  public static Fire decode(PacketByteBuf buffer) {
    FireBuilder fireBuilder = FireManager.fireBuilder(buffer.readIdentifier()).setDamage(buffer.readFloat()).setInvertHealAndHarm(buffer.readBoolean());
    if (buffer.readBoolean()) {
      fireBuilder.setSource(buffer.readIdentifier());
    }
    if (buffer.readBoolean()) {
      fireBuilder.setCampfire(buffer.readIdentifier());
    }
    fireBuilder.removeFireAspect();
    fireBuilder.removeFlame();
    return fireBuilder.build();
  }
}
