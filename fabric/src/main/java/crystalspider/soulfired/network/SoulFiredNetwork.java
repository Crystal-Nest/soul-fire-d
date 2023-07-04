package crystalspider.soulfired.network;

import crystalspider.soulfired.SoulFiredLoader;
import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.events.ServerLifecycleEvents;
import crystalspider.soulfired.handlers.FireResourceReloadListener;
import crystalspider.soulfired.network.packets.RegisterFirePacket;
import crystalspider.soulfired.network.packets.UnregisterFirePacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * Soul Fire'd network.
 */
public final class SoulFiredNetwork {
  /**
   * {@link RegisterFirePacket} {@link Identifier}.
   */
  public static final Identifier REGISTER_FIRE = new Identifier(SoulFiredLoader.MODID, "register_fire");
  /**
   * {@link UnregisterFirePacket} {@link Identifier}.
   */
  public static final Identifier UNREGISTER_FIRE = new Identifier(SoulFiredLoader.MODID, "unregister_fire");

  /**
   * Register packets identifiers and event to send them.1
   */
  public static void register() {
    ClientPlayNetworking.registerGlobalReceiver(REGISTER_FIRE, RegisterFirePacket::handle);
    ClientPlayNetworking.registerGlobalReceiver(UNREGISTER_FIRE, UnregisterFirePacket::handle);
    ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(FireResourceReloadListener::handle);
  }

  /**
   * Sends a new {@link RegisterFirePacket}.
   * 
   * @param player the player to send data to.
   * @param fire
   */
  public static void sendToClient(ServerPlayerEntity player, Fire fire) {
    PacketByteBuf buffer = PacketByteBufs.create();
    buffer.writeIdentifier(fire.getFireType());
    buffer.writeFloat(fire.getDamage());
    buffer.writeBoolean(fire.getInvertHealAndHarm());
    buffer.writeBoolean(fire.getSource().isPresent());
    if (fire.getSource().isPresent()) {
      buffer.writeIdentifier(fire.getSource().get());
    }
    buffer.writeBoolean(fire.getCampfire().isPresent());
    if (fire.getCampfire().isPresent()) {
      buffer.writeIdentifier(fire.getCampfire().get());
    }
    ServerPlayNetworking.send(player, REGISTER_FIRE, buffer);
  }

  /**
   * Sends a new {@link UnregisterFirePacket}.
   * 
   * @param player the player to send data to.
   * @param fireType
   */
  public static void sendToClient(ServerPlayerEntity player, Identifier fireType) {
    PacketByteBuf buffer = PacketByteBufs.create();
    buffer.writeIdentifier(fireType);
    ServerPlayNetworking.send(player, UNREGISTER_FIRE, buffer);
  }
}
