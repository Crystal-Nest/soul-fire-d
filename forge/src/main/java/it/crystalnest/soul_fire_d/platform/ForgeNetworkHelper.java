package it.crystalnest.soul_fire_d.platform;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.network.handler.ForgeFirePacketHandler;
import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import it.crystalnest.soul_fire_d.platform.services.NetworkHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import org.jetbrains.annotations.Nullable;

public class ForgeNetworkHelper implements NetworkHelper {
  private static final SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(Constants.MOD_ID, Constants.DDFIRES)).networkProtocolVersion(1_20_4__3_2).acceptedVersions((status, version) -> version == 1_20_4__3_2).simpleChannel();

  private static int id = 0;

  private static int id() {
    return id++;
  }

  @Override
  public void register() {
    INSTANCE.messageBuilder(RegisterFirePacket.class, id()).encoder(RegisterFirePacket::write).decoder(RegisterFirePacket::new).consumerNetworkThread(ForgeFirePacketHandler::handleRegister);
    INSTANCE.messageBuilder(UnregisterFirePacket.class, id()).encoder(UnregisterFirePacket::write).decoder(UnregisterFirePacket::new).consumerNetworkThread(ForgeFirePacketHandler::handleUnregister);
  }

  @Override
  public void sendToClient(@Nullable ServerPlayer player, Fire fire) {
    if (player == null) {
      INSTANCE.send(new RegisterFirePacket(fire), PacketDistributor.ALL.noArg());
    } else {
      INSTANCE.send(new RegisterFirePacket(fire), PacketDistributor.PLAYER.with(player));
    }
  }

  @Override
  public void sendToClient(@Nullable ServerPlayer player, ResourceLocation fireType) {
    if (player == null) {
      INSTANCE.send(new UnregisterFirePacket(fireType), PacketDistributor.ALL.noArg());
    } else {
      INSTANCE.send(new UnregisterFirePacket(fireType), PacketDistributor.PLAYER.with(player));
    }
  }
}
