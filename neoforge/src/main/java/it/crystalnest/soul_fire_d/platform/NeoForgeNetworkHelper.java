package it.crystalnest.soul_fire_d.platform;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.ModLoader;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.network.NeoForgeFirePacketHandler;
import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import it.crystalnest.soul_fire_d.platform.services.NetworkHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.jetbrains.annotations.Nullable;

/**
 * NeoForge networking helper.
 */
public class NeoForgeNetworkHelper implements NetworkHelper {
  /**
   * Registers the custom packets and their handlers.
   *
   * @param event {@link RegisterPayloadHandlerEvent}.
   */
  private static void registerPackets(RegisterPayloadHandlerEvent event) {
    IPayloadRegistrar registrar = event.registrar(Constants.MOD_ID);
    registrar.play(RegisterFirePacket.ID, RegisterFirePacket::new, handler -> handler.client(NeoForgeFirePacketHandler::handle));
    registrar.play(UnregisterFirePacket.ID, UnregisterFirePacket::new, handler -> handler.client(NeoForgeFirePacketHandler::handle));
  }

  @Override
  public void sendToClient(@Nullable ServerPlayer player, Fire fire) {
    if (player == null) {
      PacketDistributor.ALL.noArg().send(new RegisterFirePacket(fire));
    } else {
      PacketDistributor.PLAYER.with(player).send(new RegisterFirePacket(fire));
    }
  }

  @Override
  public void sendToClient(@Nullable ServerPlayer player, ResourceLocation fireType) {
    if (player == null) {
      PacketDistributor.ALL.noArg().send(new UnregisterFirePacket(fireType));
    } else {
      PacketDistributor.PLAYER.with(player).send(new UnregisterFirePacket(fireType));
    }
  }

  @Override
  public void register() {
    ModLoader.getBus().addListener(NeoForgeNetworkHelper::registerPackets);
  }
}
