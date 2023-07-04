package crystalspider.soulfired.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crystalspider.soulfired.api.events.ServerLifecycleEvents;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Injects into {@link PlayerManager} to add hooks for datapack reloads.
 */
@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
  /**
   * Shadowed {@link PlayerManager#getPlayerList()}.
   */
  @Shadow
  public abstract List<ServerPlayerEntity> getPlayerList();

  /**
   * Injects in the method {@link PlayerManager#onPlayerConnect(ClientConnection, ServerPlayerEntity)} before creating a new {@link SynchronizeRecipesS2CPacket}.
   * <p>
   * Triggers a new {@link ServerLifecycleEvents#SYNC_DATA_PACK_CONTENTS} event.
   * 
   * @param connection
   * @param player
   * @param ci {@link CallbackInfo}.
   */
	@Inject(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "net/minecraft/network/packet/s2c/play/SynchronizeRecipesS2CPacket.<init>(Ljava/util/Collection;)V"))
	private void hookOnPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.invoker().onSyncDataPackContents(player, true);
	}

  /**
   * Injects in the method {@link PlayerManager#onDataPacksReloaded()} before creating a new {@link SynchronizeRecipesS2CPacket}.
   * <p>
   * Triggers a new {@link ServerLifecycleEvents#SYNC_DATA_PACK_CONTENTS} event.
   * 
   * @param ci {@link CallbackInfo}.
   */
	@Inject(method = "onDataPacksReloaded", at = @At(value = "INVOKE", target = "net/minecraft/network/packet/s2c/play/SynchronizeTagsS2CPacket.<init>(Ljava/util/Map;)V"))
	private void hookOnDataPacksReloaded(CallbackInfo ci) {
		for (ServerPlayerEntity player : getPlayerList()) {
			ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.invoker().onSyncDataPackContents(player, false);
		}
	}
}
