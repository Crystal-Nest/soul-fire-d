package crystalspider.soulfired.api.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Port to 1.16.5 of part of the original {@link net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents} available only from 1.18 onwards.
 */
public final class ServerLifecycleEvents {
  private ServerLifecycleEvents() {}

  /**
	 * Called when a Minecraft server is about to send tag and recipe data to a player.
	 * @see SyncDataPackContents
	 */
	public static final Event<SyncDataPackContents> SYNC_DATA_PACK_CONTENTS = EventFactory.createArrayBacked(SyncDataPackContents.class, callbacks -> (player, joined) -> {
		for (SyncDataPackContents callback : callbacks) {
			callback.onSyncDataPackContents(player, joined);
		}
	});

  @FunctionalInterface
	public interface SyncDataPackContents {
		/**
		 * Called right before tags and recipes are sent to a player,
		 * either because the player joined, or because the server reloaded resources.
		 * The {@linkplain MinecraftServer#getResourceManager() server resource manager} is up-to-date when this is called.
		 *
		 * <p>For example, this event can be used to sync data loaded with custom resource reloaders.
		 *
		 * @param player Player to which the data is being sent.
		 * @param joined True if the player is joining the server, false if the server finished a successful resource reload.
		 */
		void onSyncDataPackContents(ServerPlayerEntity player, boolean joined);
	}
}
