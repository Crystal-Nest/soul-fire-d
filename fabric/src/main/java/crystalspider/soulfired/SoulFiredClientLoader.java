package crystalspider.soulfired;

import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import net.fabricmc.api.ClientModInitializer;

/**
 * Soul fire'd mod client loader.
 */
public class SoulFiredClientLoader implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    for (Fire fire : FireManager.getFires()) {
      FireClientManager.registerFire(fire);
    }
  }
}
