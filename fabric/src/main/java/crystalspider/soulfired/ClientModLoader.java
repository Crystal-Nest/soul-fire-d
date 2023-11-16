package crystalspider.soulfired;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import crystalspider.soulfired.network.SoulFiredNetwork;
import net.fabricmc.api.ClientModInitializer;

/**
 * Soul fire'd mod client loader.
 */
public final class ClientModLoader implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    FireClientManager.registerFires(FireManager.getFires());
    SoulFiredNetwork.register();
  }
}
