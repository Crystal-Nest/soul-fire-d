package it.crystalnest.soul_fire_d.network.packet;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Networking packet to register the given {@link Fire}.
 *
 * @param fire fire.
 */
public record RegisterFirePacket(Fire fire) implements CustomPacketPayload {
  /**
   * Packet type.
   */
  public static final Type<RegisterFirePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "register_fire"));

  public static final StreamCodec<FriendlyByteBuf, RegisterFirePacket> CODEC = StreamCodec.composite(
    Fire.STREAM_CODEC,
    RegisterFirePacket::fire,
    RegisterFirePacket::new
  );

  @NotNull
  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
