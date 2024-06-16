package it.crystalnest.soul_fire_d.network.handler;

import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Fabric fire packet handler.
 */
public final class FabricFirePacketHandler {
  private FabricFirePacketHandler() {}

  /**
   * Handles a {@link RegisterFirePacket}.
   *
   * @param minecraft Minecraft client instance.
   * @param listener client packet listener.
   * @param buffer buffer.
   * @param sender packet sender.
   */
  public static void handleRegister(Minecraft minecraft, ClientPacketListener listener, FriendlyByteBuf buffer, PacketSender sender) {
    FirePacketHandler.handle(new RegisterFirePacket(buffer));
  }

  /**
   * Handles an {@link UnregisterFirePacket}.
   *
   * @param minecraft Minecraft client instance.
   * @param listener client packet listener.
   * @param buffer buffer.
   * @param sender packet sender.
   */
  public static void handleUnregister(Minecraft minecraft, ClientPacketListener listener, FriendlyByteBuf buffer, PacketSender sender) {
    FirePacketHandler.handle(new UnregisterFirePacket(buffer));
  }
}
