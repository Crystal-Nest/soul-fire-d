package crystalspider.soulfired.api.client;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public abstract class FireClientManager {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger();

  /**
   * {@link ConcurrentHashMap} of all registered {@link FireClient Fires}.
   */
  private static volatile ConcurrentHashMap<String, FireClient> fires = new ConcurrentHashMap<>();

  /**
   * Registers a new {@link FireClient} from the given {@link Fire}.
   * 
   * @param fire {@link Fire} to use to create a new {@link FireClient}.
   * @return  whether the registration has been successful.
   */
  public static final synchronized boolean registerFire(Fire fire) {
    return registerFire(new FireClient(fire.getModId(), fire.getId()));
  }

  /**
   * Registers the given {@link FireClient}.
   * <p>
   * If the {@link FireClient#id} is already registered, logs an error.
   * 
   * @param fire {@link FireClient} to register.
   * @return whether the registration has been successful.
   */
  public static final synchronized boolean registerFire(FireClient fire) {
    String fireId = fire.getId();
    if (!fires.containsKey(fireId)) {
      fires.put(fireId, fire);
      return true;
    }
    LOGGER.error("FireClient [" + fireId + "] was already registered by mod " + fires.get(fireId).getModId() + " with the following value: " + fires.get(fireId));
    return false;
  }

  /**
   * Returns the sprite 0 of the {@link FireClient} registered with the given {@code id}.
   * <p>
   * Returns {@code null} if no {@link FireClient} was registered with the given {@code id}.
   * 
   * @param id
   * @return the sprite 0 of the {@link FireClient} registered with the given {@code id}.
   */
  public static final TextureAtlasSprite getSprite0(String id) {
    if (FireManager.isFireId(id) && fires.containsKey(id)) {
      return fires.get(id).getSprite0();
    }
    return null;
  }

  /**
   * Returns the sprite 1 of the {@link FireClient} registered with the given {@code id}.
   * <p>
   * Returns {@code null} if no {@link FireClient} was registered with the given {@code id}.
   * 
   * @param id
   * @return the sprite 1 of the {@link FireClient} registered with the given {@code id}.
   */
  public static final TextureAtlasSprite getSprite1(String id) {
    if (FireManager.isFireId(id) && fires.containsKey(id)) {
      return fires.get(id).getSprite1();
    }
    return null;
  }
}
