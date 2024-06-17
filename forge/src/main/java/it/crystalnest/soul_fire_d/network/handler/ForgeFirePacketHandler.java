package it.crystalnest.soul_fire_d.network.handler;

import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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
  public static void handleRegister(RegisterFirePacket packet, Supplier<NetworkEvent.Context>  context) {
    context.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> FirePacketHandler.handle(packet)));
    context.get().setPacketHandled(true);
  }

  /**
   * Handles an {@link UnregisterFirePacket}.
   *
   * @param packet {@link UnregisterFirePacket}.
   * @param context custom payload context.
   */
  public static void handleUnregister(UnregisterFirePacket packet, Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> FirePacketHandler.handle(packet)));
    context.get().setPacketHandled(true);
  }
}
