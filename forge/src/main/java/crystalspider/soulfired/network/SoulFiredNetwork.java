package crystalspider.soulfired.network;

import javax.annotation.Nullable;

import crystalspider.soulfired.SoulFiredLoader;
import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.network.register.RegisterFirePacket;
import crystalspider.soulfired.network.unregister.UnregisterFirePacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Soul Fire'd network.
 */
public final class SoulFiredNetwork {
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(SoulFiredLoader.MODID, "ddfires"), () -> SoulFiredLoader.PROTOCOL_VERSION, SoulFiredLoader.PROTOCOL_VERSION::equals, SoulFiredLoader.PROTOCOL_VERSION::equals);
  
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
    INSTANCE.registerMessage(id(), RegisterFirePacket.class, RegisterFirePacket::encode, RegisterFirePacket::new, RegisterFirePacket::handle);
    INSTANCE.registerMessage(id(), UnregisterFirePacket.class, UnregisterFirePacket::encode, UnregisterFirePacket::new, UnregisterFirePacket::handle);
  }

  /**
   * Sends a new {@link RegisterFirePacket}.
   * 
   * @param player the player to send data to, {@code null} when sending to all players.
   * @param fire
   */
  public static void sendToClient(@Nullable ServerPlayerEntity player, Fire fire) {
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
  public static void sendToClient(@Nullable ServerPlayerEntity player, ResourceLocation fireType) {
    if (player == null) {
      INSTANCE.send(PacketDistributor.ALL.noArg(), new UnregisterFirePacket(fireType));
    } else {
      INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new UnregisterFirePacket(fireType));
    }
  }
}
