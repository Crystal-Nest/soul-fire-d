package it.crystalnest.soul_fire_d.network.packet;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Networking packet to unregister the specified {@link Fire}.
 *
 * @param fireType fire reference.
 */
public record UnregisterFirePacket(ResourceLocation fireType) implements CustomPacketPayload {
  /**
   * Packet type.
   */
  public static final Type<UnregisterFirePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "unregister_fire"));

  public static final StreamCodec<FriendlyByteBuf, UnregisterFirePacket> CODEC = StreamCodec.composite(
    ResourceLocation.STREAM_CODEC,
    UnregisterFirePacket::fireType,
    UnregisterFirePacket::new
  );

  @NotNull
  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
