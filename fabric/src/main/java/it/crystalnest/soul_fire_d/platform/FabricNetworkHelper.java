package it.crystalnest.soul_fire_d.platform;

import it.crystalnest.soul_fire_d.ClientModLoader;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import it.crystalnest.soul_fire_d.platform.services.NetworkHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

/**
 * Fabric networking helper.
 */
public class FabricNetworkHelper implements NetworkHelper {
  /**
   * Encodes a packet into a buffer.
   *
   * @param packet packet constructor.
   * @param data packet data.
   * @param <D> packet data type.
   * @param <P> packet type.
   * @return buffer with encoded packet data.
   */
  private static <D, P extends CustomPacketPayload> FriendlyByteBuf encode(Function<D, P> packet, D data) {
    FriendlyByteBuf buffer = PacketByteBufs.create();
    packet.apply(data).write(buffer);
    return buffer;
  }

  /**
   * Registration is done in {@link ClientModLoader} to avoid crashes server-side.
   */
  @Override
  public void register() {
    // Registration done elsewhere.
  }

  @Override
  public void sendToClient(ServerPlayer player, Fire fire) {
    ServerPlayNetworking.send(player, RegisterFirePacket.ID, encode(RegisterFirePacket::new, fire));
  }

  @Override
  public void sendToClient(ServerPlayer player, ResourceLocation fireType) {
    ServerPlayNetworking.send(player, UnregisterFirePacket.ID, encode(UnregisterFirePacket::new, fireType));
  }
}
