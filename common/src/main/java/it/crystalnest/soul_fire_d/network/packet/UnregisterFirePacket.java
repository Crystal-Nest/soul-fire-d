package it.crystalnest.soul_fire_d.network.packet;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.network.CustomPacketPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Networking packet to unregister the specified {@link Fire}.
 *
 * @param fireType fire reference.
 */
public record UnregisterFirePacket(ResourceLocation fireType) implements CustomPacketPayload {
  /**
   * Packet ID.
   */
  public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "unregister_fire");

  /**
   * @param buffer buffer.
   */
  public UnregisterFirePacket(FriendlyByteBuf buffer) {
    this(buffer.readResourceLocation());
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buffer) {
    buffer.writeResourceLocation(fireType);
  }

  @NotNull
  @Override
  public ResourceLocation id() {
    return ID;
  }
}
