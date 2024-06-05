package it.crystalnest.soul_fire_d.network.handler;

import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public final class FabricFirePacketHandler {
  private FabricFirePacketHandler() {}

  public static void handleRegister(Minecraft minecraft, ClientPacketListener listener, FriendlyByteBuf buffer, PacketSender sender) {
    FirePacketHandler.handle(new RegisterFirePacket(buffer));
  }

  public static void handleUnregister(Minecraft minecraft, ClientPacketListener listener, FriendlyByteBuf buffer, PacketSender sender) {
    FirePacketHandler.handle(new UnregisterFirePacket(buffer));
  }
}
