package crystalspider.soulfired.network.unregister;

import crystalspider.soulfired.api.Fire;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent.Context;

/**
 * Network packet to unregister a Fire.
 */
public final class UnregisterFirePacket {
  /**
   * Fire type of the Fire to unregister.
   */
  final ResourceLocation fireType;

  /**
   * @param fireType fire type of the Fire to unregister.
   */
  public UnregisterFirePacket(ResourceLocation fireType) {
    this.fireType = fireType;
  }

  /**
   * Decodes data to instantiate an UnregisterFirePacket from the network buffer.
   * 
   * @param buffer {@link FriendlyByteBuf} from which to read data to get the {@link Fire} to register.
   */
  public UnregisterFirePacket(FriendlyByteBuf buffer) {
    fireType = buffer.readResourceLocation();
  }

  /**
   * Encodes an UnregisterFirePacket into the network buffer.
   * 
   * @param buffer {@link FriendlyByteBuf} to save data to.
   */
  public void encode(FriendlyByteBuf buffer) {
    buffer.writeResourceLocation(fireType);
  }

  /**
   * Handles the packet server-side.
   * 
   * @param context
   */
  public void handle(Context context) {
    if (FMLEnvironment.dist == Dist.CLIENT) {
      UnregisterFireClientPacket.handle(UnregisterFirePacket.this, context);
    }
    context.setPacketHandled(true);
  }
}
