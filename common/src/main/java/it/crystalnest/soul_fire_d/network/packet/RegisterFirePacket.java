package it.crystalnest.soul_fire_d.network.packet;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireBuilder;
import it.crystalnest.soul_fire_d.api.FireComponent;
import it.crystalnest.soul_fire_d.api.FireManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record RegisterFirePacket(Fire fire) implements CustomPacketPayload {
  public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "register_fire");

  public RegisterFirePacket(FriendlyByteBuf buffer) {
    this(decode(buffer));
  }

  private static Fire decode(FriendlyByteBuf buffer) {
    FireBuilder fireBuilder = FireManager.fireBuilder(buffer.readResourceLocation())
      .setDamage(buffer.readFloat())
      .setInvertHealAndHarm(buffer.readBoolean())
      .removeComponent(FireComponent.CAMPFIRE_ITEM)
      .removeComponent(FireComponent.LANTERN_BLOCK)
      .removeComponent(FireComponent.LANTERN_ITEM)
      .removeComponent(FireComponent.TORCH_BLOCK)
      .removeComponent(FireComponent.TORCH_ITEM)
      .removeComponent(FireComponent.WALL_TORCH_BLOCK)
      .removeComponent(FireComponent.FLAME_PARTICLE)
      .removeFireAspect()
      .removeFlame();
    if (buffer.readBoolean()) {
      fireBuilder.setComponent(FireComponent.SOURCE_BLOCK, buffer.readResourceLocation());
    } else {
      fireBuilder.removeComponent(FireComponent.SOURCE_BLOCK);
    }
    if (buffer.readBoolean()) {
      fireBuilder.setComponent(FireComponent.CAMPFIRE_BLOCK, buffer.readResourceLocation());
    } else {
      fireBuilder.removeComponent(FireComponent.CAMPFIRE_BLOCK);
    }
    return fireBuilder.build();
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buffer) {
    buffer.writeResourceLocation(fire.getFireType());
    buffer.writeFloat(fire.getDamage());
    buffer.writeBoolean(fire.invertHealAndHarm());
    @Nullable ResourceLocation source = fire.getComponent(FireComponent.SOURCE_BLOCK);
    buffer.writeBoolean(source != null);
    if (source != null) {
      buffer.writeResourceLocation(source);
    }
    @Nullable ResourceLocation campfire = fire.getComponent(FireComponent.CAMPFIRE_BLOCK);
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
