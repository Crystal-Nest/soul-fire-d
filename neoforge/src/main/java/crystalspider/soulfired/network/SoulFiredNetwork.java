package crystalspider.soulfired.network;

import org.jetbrains.annotations.Nullable;

import crystalspider.soulfired.SoulFiredLoader;
import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.network.register.RegisterFirePacket;
import crystalspider.soulfired.network.unregister.UnregisterFirePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.NetworkRegistry.ChannelBuilder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.simple.SimpleChannel;

/**
 * Soul Fire'd network.
 */
public final class SoulFiredNetwork {
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  private static final SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(SoulFiredLoader.MODID, "ddfires")).networkProtocolVersion(() -> SoulFiredLoader.PROTOCOL_VERSION).serverAcceptedVersions(SoulFiredLoader.PROTOCOL_VERSION::equals).clientAcceptedVersions(SoulFiredLoader.PROTOCOL_VERSION::equals).simpleChannel();
  
  /**
   * Last packet id used to register a packet kind.
   */
  private static int id = 0;

  /**
   * Get the current id to use to register a new packet kind.
   * 
   * @return current id.
   */
  private static int id() {
    return id++;
  }

  /**
   * Registers all kind of packets.
   */
  public static void register() {
    INSTANCE.messageBuilder(RegisterFirePacket.class, id()).encoder(RegisterFirePacket::encode).decoder(RegisterFirePacket::new).consumerNetworkThread(RegisterFirePacket::handle);
    INSTANCE.messageBuilder(UnregisterFirePacket.class, id()).encoder(UnregisterFirePacket::encode).decoder(UnregisterFirePacket::new).consumerNetworkThread(UnregisterFirePacket::handle);
  }

  /**
   * Sends a new {@link RegisterFirePacket}.
   * 
   * @param player the player to send data to, {@code null} when sending to all players.
   * @param fire
   */
  public static void sendToClient(@Nullable ServerPlayer player, Fire fire) {
    if (player == null) {
      INSTANCE.send(PacketDistributor.ALL.noArg(), new RegisterFirePacket(fire));
    } else {
      INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RegisterFirePacket(fire));
    }
  }

  /**
   * Sends a new {@link UnregisterFirePacket}.
   * 
   * @param player the player to send data to, {@code null} when sending to all players.
   * @param fireType
   */
  public static void sendToClient(@Nullable ServerPlayer player, ResourceLocation fireType) {
    if (player == null) {
      INSTANCE.send(PacketDistributor.ALL.noArg(), new UnregisterFirePacket(fireType));
    } else {
      INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new UnregisterFirePacket(fireType));
    }
  }
}
