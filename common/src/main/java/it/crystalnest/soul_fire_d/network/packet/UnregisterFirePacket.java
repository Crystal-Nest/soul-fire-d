package it.crystalnest.soul_fire_d.network.packet;

import it.crystalnest.soul_fire_d.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record UnregisterFirePacket(ResourceLocation fireType) implements CustomPacketPayload {
  public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "unregister_fire");

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
