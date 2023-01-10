package crystalspider.soulfired;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import net.fabricmc.api.ClientModInitializer;

/**
 * Soul fire'd mod client loader.
 */
public final class SoulFiredClientLoader implements ClientModInitializer {
  private SoulFiredClientLoader() {}

  @Override
  public void onInitializeClient() {
    FireClientManager.registerFires(FireManager.getFires());
  }
}
