package it.crystalnest.soul_fire_d.platform;

import it.crystalnest.soul_fire_d.ModLoader;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.network.handler.FirePacketHandler;
import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import it.crystalnest.soul_fire_d.platform.services.NetworkHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.Nullable;

/**
 * NeoForge networking helper.
 */
public class NeoForgeNetworkHelper implements NetworkHelper {
  /**
   * Registers the custom packets and their handlers.
   *
   * @param event {@link RegisterPayloadHandlersEvent}.
   */
  private static void registerPackets(RegisterPayloadHandlersEvent event) {
    PayloadRegistrar registrar = event.registrar("1.21-4.0");
    registrar.playToClient(RegisterFirePacket.TYPE, RegisterFirePacket.CODEC, (packet, context) -> FirePacketHandler.handle(packet));
    registrar.playToClient(UnregisterFirePacket.TYPE, UnregisterFirePacket.CODEC, (packet, context) -> FirePacketHandler.handle(packet));
  }

  @Override
  public void sendToClient(@Nullable ServerPlayer player, Fire fire) {
    if (player == null) {
      PacketDistributor.sendToAllPlayers(new RegisterFirePacket(fire));
    } else {
      PacketDistributor.sendToPlayer(player, new RegisterFirePacket(fire));
    }
  }

  @Override
  public void sendToClient(@Nullable ServerPlayer player, ResourceLocation fireType) {
    if (player == null) {
      PacketDistributor.sendToAllPlayers(new UnregisterFirePacket(fireType));
    } else {
      PacketDistributor.sendToPlayer(player, new UnregisterFirePacket(fireType));
    }
  }

  @Override
  public void register() {
    ModLoader.getBus().addListener(NeoForgeNetworkHelper::registerPackets);
  }
}
