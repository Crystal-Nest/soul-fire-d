package it.crystalnest.soul_fire_d.platform.services;

import it.crystalnest.soul_fire_d.api.Fire;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface NetworkHelper {
  /**
   * Registers the networking handlers.
   */
  void register();

  void sendToClient(@Nullable ServerPlayer player, Fire fire);

  void sendToClient(@Nullable ServerPlayer player, ResourceLocation fireType);
}
