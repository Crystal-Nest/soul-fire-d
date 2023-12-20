package crystalspider.soulfired.network;

import org.jetbrains.annotations.Nullable;

import crystalspider.soulfired.ModLoader;
import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.network.register.RegisterFirePacket;
import crystalspider.soulfired.network.unregister.UnregisterFirePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Soul Fire'd network.
 */
public final class SoulFiredNetwork {
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ModLoader.MOD_ID, "ddfires"), () -> ModLoader.PROTOCOL_VERSION, ModLoader.PROTOCOL_VERSION::equals, ModLoader.PROTOCOL_VERSION::equals);
  
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
