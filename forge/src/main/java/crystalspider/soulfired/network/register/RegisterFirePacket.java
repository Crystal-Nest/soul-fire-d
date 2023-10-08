package crystalspider.soulfired.network.register;

import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireBuilder;
import crystalspider.soulfired.api.FireManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent.Context;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;

/**
 * Network packet to register a {@link Fire}.
 */
public final class RegisterFirePacket {
  /**
   * {@link Fire} to register.
   */
  final Fire fire;

  /**
   * @param fire {@link Fire} to register.
   */
  public RegisterFirePacket(Fire fire) {
    this.fire = fire;
  }

  /**
   * Decodes data to instantiate a RegisterFirePacket from the network buffer.
   * 
   * @param buffer {@link FriendlyByteBuf} from which to read data to get the {@link Fire} to register.
   */
  public RegisterFirePacket(FriendlyByteBuf buffer) {
    FireBuilder fireBuilder = FireManager.fireBuilder(buffer.readResourceLocation()).setDamage(buffer.readFloat()).setInvertHealAndHarm(buffer.readBoolean());
    if (buffer.readBoolean()) {
      fireBuilder.setSource(buffer.readResourceLocation());
    }
    if (buffer.readBoolean()) {
      fireBuilder.setCampfire(buffer.readResourceLocation());
    }
    fireBuilder.removeFireAspect();
    fireBuilder.removeFlame();
    fire = fireBuilder.build();
  }

  /**
   * Encodes a RegisterFirePacket into the network buffer.
   * 
   * @param buffer {@link FriendlyByteBuf} to save data to.
   */
  public void encode(FriendlyByteBuf buffer) {
    buffer.writeResourceLocation(fire.getFireType());
    buffer.writeFloat(fire.getDamage());
    buffer.writeBoolean(fire.getInvertHealAndHarm());
    buffer.writeBoolean(fire.getSource().isPresent());
    if (fire.getSource().isPresent()) {
      buffer.writeResourceLocation(fire.getSource().get());
    }
    buffer.writeBoolean(fire.getCampfire().isPresent());
    if (fire.getCampfire().isPresent()) {
      buffer.writeResourceLocation(fire.getCampfire().get());
    }
  }

  /**
   * Handles the packet server-side.
   * 
   * @param context
   */
  public void handle(Context context) {
    context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new SafeRunnable() { @Override public void run() { RegisterFireClientPacket.handle(RegisterFirePacket.this, context); } }));
    context.setPacketHandled(true);
  }
}
