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

public class FabricNetworkHelper implements NetworkHelper {
  /**
   * Registration is done in {@link ClientModLoader} to avoid crashes server-side.
   */
  @Override
  public void register() {}

  @Override
  public void sendToClient(ServerPlayer player, Fire fire) {
    ServerPlayNetworking.send(player, RegisterFirePacket.ID, encode(RegisterFirePacket::new, fire));
  }

  @Override
  public void sendToClient(ServerPlayer player, ResourceLocation fireType) {
    ServerPlayNetworking.send(player, UnregisterFirePacket.ID, encode(UnregisterFirePacket::new, fireType));
  }

  private <D, P extends CustomPacketPayload> FriendlyByteBuf encode(Function<D, P> packet, D data) {
    FriendlyByteBuf buffer = PacketByteBufs.create();
    packet.apply(data).write(buffer);
    return buffer;
  }
}
