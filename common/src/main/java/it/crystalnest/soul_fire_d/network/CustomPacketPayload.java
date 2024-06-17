package it.crystalnest.soul_fire_d.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Backport of Minecraft CustomPacketPayload from later versions.
 */
public interface CustomPacketPayload {
  /**
   * Write data into the buffer.
   *
   * @param buffer buffer.
   */
  void write(FriendlyByteBuf buffer);

  /**
   * Packet identifier.
   *
   * @return packet identifier.
   */
  ResourceLocation id();
}
