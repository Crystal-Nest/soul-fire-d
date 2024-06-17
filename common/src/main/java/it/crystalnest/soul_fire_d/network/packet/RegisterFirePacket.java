package it.crystalnest.soul_fire_d.network.packet;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.network.CustomPacketPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Networking packet to register the given {@link Fire}.
 *
 * @param fire fire.
 */
public record RegisterFirePacket(Fire fire) implements CustomPacketPayload {
  /**
   * Packet ID.
   */
  public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "register_fire");

  /**
   * @param buffer buffer.
   */
  public RegisterFirePacket(FriendlyByteBuf buffer) {
    this(decode(buffer));
  }

  /**
   * Decodes the given buffer into a {@link Fire}.
   *
   * @param buffer buffer.
   * @return decoded {@link Fire}.
   */
  private static Fire decode(FriendlyByteBuf buffer) {
    Fire.Builder builder = FireManager.fireBuilder(buffer.readResourceLocation())
      .setDamage(buffer.readFloat())
      .setInvertHealAndHarm(buffer.readBoolean())
      .removeComponent(Fire.Component.CAMPFIRE_ITEM)
      .removeComponent(Fire.Component.LANTERN_BLOCK)
      .removeComponent(Fire.Component.LANTERN_ITEM)
      .removeComponent(Fire.Component.TORCH_BLOCK)
      .removeComponent(Fire.Component.TORCH_ITEM)
      .removeComponent(Fire.Component.WALL_TORCH_BLOCK)
      .removeComponent(Fire.Component.FLAME_PARTICLE)
      .removeFireAspect()
      .removeFlame();
    if (buffer.readBoolean()) {
      builder.setComponent(Fire.Component.SOURCE_BLOCK, buffer.readResourceLocation());
    } else {
      builder.removeComponent(Fire.Component.SOURCE_BLOCK);
    }
    if (buffer.readBoolean()) {
      builder.setComponent(Fire.Component.CAMPFIRE_BLOCK, buffer.readResourceLocation());
    } else {
      builder.removeComponent(Fire.Component.CAMPFIRE_BLOCK);
    }
    return builder.build();
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buffer) {
    buffer.writeResourceLocation(fire.getFireType());
    buffer.writeFloat(fire.getDamage());
    buffer.writeBoolean(fire.invertHealAndHarm());
    @Nullable ResourceLocation source = fire.getComponent(Fire.Component.SOURCE_BLOCK);
    buffer.writeBoolean(source != null);
    if (source != null) {
      buffer.writeResourceLocation(source);
    }
    @Nullable ResourceLocation campfire = fire.getComponent(Fire.Component.CAMPFIRE_BLOCK);
    buffer.writeBoolean(campfire != null);
    if (campfire != null) {
      buffer.writeResourceLocation(campfire);
    }
  }

  @NotNull
  @Override
  public ResourceLocation id() {
    return ID;
  }
}
