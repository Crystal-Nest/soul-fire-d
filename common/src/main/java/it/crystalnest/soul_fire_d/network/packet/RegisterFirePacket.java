package it.crystalnest.soul_fire_d.network.packet;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireBuilder;
import it.crystalnest.soul_fire_d.api.FireManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record RegisterFirePacket(Fire fire) implements CustomPacketPayload {
  public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "register_fire");

  public RegisterFirePacket(FriendlyByteBuf buffer) {
    this(decode(buffer));
  }

  private static Fire decode(FriendlyByteBuf buffer) {
    FireBuilder fireBuilder = FireManager.fireBuilder(buffer.readResourceLocation()).setDamage(buffer.readFloat()).setInvertHealAndHarm(buffer.readBoolean()).removeFireAspect().removeFlame();
    if (buffer.readBoolean()) {
      fireBuilder.setSource(buffer.readResourceLocation());
    } else {
      fireBuilder.removeSource();
    }
    if (buffer.readBoolean()) {
      fireBuilder.setCampfire(buffer.readResourceLocation());
    } else {
      fireBuilder.removeCampfire();
    }
    return fireBuilder.build();
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buffer) {
    buffer.writeResourceLocation(fire.getFireType());
    buffer.writeFloat(fire.getDamage());
    buffer.writeBoolean(fire.invertHealAndHarm());
    buffer.writeBoolean(fire.getSource().isPresent());
    if (fire.getSource().isPresent()) {
      buffer.writeResourceLocation(fire.getSource().get());
    }
    buffer.writeBoolean(fire.getCampfire().isPresent());
    if (fire.getCampfire().isPresent()) {
      buffer.writeResourceLocation(fire.getCampfire().get());
    }
  }

  @NotNull
  @Override
  public ResourceLocation id() {
    return ID;
  }
}
