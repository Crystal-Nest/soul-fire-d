package it.crystalnest.soul_fire_d.network.handler;

import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;

public final class ForgeFirePacketHandler {
  private ForgeFirePacketHandler() {}

  public static void handleRegister(RegisterFirePacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> FirePacketHandler.handle(packet)));
    context.setPacketHandled(true);
  }

  public static void handleUnregister(UnregisterFirePacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> FirePacketHandler.handle(packet)));
    context.setPacketHandled(true);
  }
}
