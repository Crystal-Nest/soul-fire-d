package crystalspider.soulfired.network;

import org.jetbrains.annotations.Nullable;

import crystalspider.soulfired.ModLoader;
import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.network.register.RegisterFirePacket;
import crystalspider.soulfired.network.unregister.UnregisterFirePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

/**
 * Soul Fire'd network.
 */
public final class SoulFiredNetwork {
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  private static final SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(ModLoader.MOD_ID, "ddfires")).networkProtocolVersion(ModLoader.PROTOCOL_VERSION).acceptedVersions((status, version) -> version == ModLoader.PROTOCOL_VERSION).simpleChannel();

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
      INSTANCE.send(new RegisterFirePacket(fire), PacketDistributor.ALL.noArg());
    } else {
      INSTANCE.send(new RegisterFirePacket(fire), PacketDistributor.PLAYER.with(player));
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
      INSTANCE.send(new UnregisterFirePacket(fireType), PacketDistributor.ALL.noArg());
    } else {
      INSTANCE.send(new UnregisterFirePacket(fireType), PacketDistributor.PLAYER.with(player));
    }
  }
}
