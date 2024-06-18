package it.crystalnest.soul_fire_d.platform;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.network.handler.ForgeFirePacketHandler;
import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import it.crystalnest.soul_fire_d.platform.services.NetworkHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;

/**
 * Forge networking helper.
 */
public class ForgeNetworkHelper implements NetworkHelper {
  /**
   * Channel version.
   */
  private static final String CHANNEL_VERSION = "1.19.4-4";

  /**
   * {@link SimpleChannel} instance.
   */
  private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
    .named(new ResourceLocation(Constants.MOD_ID, Constants.DDFIRES))
    .networkProtocolVersion(() -> CHANNEL_VERSION)
    .clientAcceptedVersions(CHANNEL_VERSION::equals)
    .serverAcceptedVersions(CHANNEL_VERSION::equals)
    .simpleChannel();

  /**
   * Latest packet ID.
   */
  private static int id = 0;

  /**
   * Get the current available packet ID.
   *
   * @return current available packet ID.
   */
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
      INSTANCE.send(PacketDistributor.ALL.noArg(), new RegisterFirePacket(fire));
    } else {
      INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RegisterFirePacket(fire));
    }
  }

  @Override
  public void sendToClient(@Nullable ServerPlayer player, ResourceLocation fireType) {
    if (player == null) {
      INSTANCE.send(PacketDistributor.ALL.noArg(), new UnregisterFirePacket(fireType));
    } else {
      INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new UnregisterFirePacket(fireType));
    }
  }
}
