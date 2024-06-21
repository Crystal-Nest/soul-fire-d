package it.crystalnest.soul_fire_d.network.handler;

import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;

/**
 * Forge fire packet handler.
 */
public final class ForgeFirePacketHandler {
  private ForgeFirePacketHandler() {}

  /**
   * Handles a {@link RegisterFirePacket}.
   *
   * @param packet {@link RegisterFirePacket}.
   * @param context custom payload context.
   */
  public static void handleRegister(RegisterFirePacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() { @Override public void run() { FirePacketHandler.handle(packet); } }));
    context.setPacketHandled(true);
  }

  /**
   * Handles an {@link UnregisterFirePacket}.
   *
   * @param packet {@link UnregisterFirePacket}.
   * @param context custom payload context.
   */
  public static void handleUnregister(UnregisterFirePacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() { @Override public void run() { FirePacketHandler.handle(packet); } }));
    context.setPacketHandled(true);
  }
}
