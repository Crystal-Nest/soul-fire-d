package crystalspider.soulfired.api.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireManager;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

/**
 * Static manager for the registered Fires, client side only.
 */
public final class FireClientManager {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger();

  /**
   * {@link ConcurrentHashMap} of all registered {@link FireClient Fires}.
   */
  private static volatile ConcurrentHashMap<Identifier, FireClient> fires = new ConcurrentHashMap<>();
  
  private FireClientManager() {}

  /**
   * Attempts to a new {@link FireClient} from the given {@link Fire}.
   * 
   * @param fire {@link Fire} to derive a new {@link FireClient}.
   * @return whether the registration is successful.
   */
  public static synchronized boolean registerFire(Fire fire) {
    return registerFire(new FireClient(fire.getFireType()));
  }

  /**
   * Attempts to register new {@link FireClient} for all the given {@link Fire fires}.
   * 
   * @param fires {@link Fire fires} to derive {@link FireClient} to register.
   * @return an {@link HashMap} with the outcome of each registration attempt.
   */
  public static synchronized HashMap<Identifier, Boolean> registerFires(Fire... fires) {
    return registerFires(Arrays.asList(fires));
  }

  /**
   * Attempts to register all the given {@link Fire fires}.
   * 
   * @param fires {@link Fire fires} to register.
   * @return an {@link HashMap} with the outcome of each registration attempt.
   */
  public static synchronized HashMap<Identifier, Boolean> registerFires(List<Fire> fires) {
    HashMap<Identifier, Boolean> outcomes = new HashMap<>();
    for (Fire fire : fires) {
      outcomes.put(fire.getFireType(), registerFire(fire));
    }
    return outcomes;
  }

  /**
   * Unregisters the specified fire.
   * <p>
   * To be used only internally, do not use elsewhere!
   * 
   * @param fireType
   * @return whether the fire was previously registered.
   */
  @Deprecated
  public static synchronized boolean unregisterFire(Identifier fireType) {
    return fires.remove(fireType) != null;
  }

  /**
   * Attempts to register the given {@link FireClient}.
   * <p>
   * If the {@link FireClient#fireType} is already registered, logs an error.
   * 
   * @param fire {@link FireClient} to register.
   * @return whether the registration is successful.
   */
  public static final synchronized boolean registerFire(FireClient fire) {
    Identifier fireType = fire.getFireType();
    if (!fires.containsKey(fireType)) {
      fires.put(fireType, fire);
      return true;
    }
    LOGGER.error("FireClient [" + fireType + "] was already registered by mod with the following value: " + fires.get(fireType));
    return false;
  }

  /**
   * Returns the {@link FireClient#spriteIdentifier0} of the {@link FireClient} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns {@link ModelLoader#FIRE_0} if no {@link FireClient} was registered with the given values.
   * 
   * @param modId
   * @param fireId
   * @return the {@link FireClient#spriteIdentifier0} of the {@link FireClient}.
   */
  public static final SpriteIdentifier getSpriteIdentifier0(String modId, String fireId) {
    return getSpriteIdentifier0(new Identifier(modId, fireId));
  }

  /**
   * Returns the {@link FireClient#spriteIdentifier0} of the {@link FireClient} registered with the given {@code fireType}.
   * <p>
   * Returns {@link ModelLoader#FIRE_0} if no {@link FireClient} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the {@link FireClient#spriteIdentifier0} of the {@link FireClient}.
   */
  public static final SpriteIdentifier getSpriteIdentifier0(Identifier fireType) {
    if (FireManager.isRegisteredType(fireType) && fires.containsKey(fireType)) {
      return fires.get(fireType).getSpriteIdentifier0();
    }
    return ModelLoader.FIRE_0;
  }

  /**
   * Returns the {@link FireClient#spriteIdentifier1} of the {@link FireClient} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns {@link ModelLoader#FIRE_1} if no {@link FireClient} was registered with the given values.
   * 
   * @param modId
   * @param fireId
   * @return the {@link FireClient#spriteIdentifier1} of the {@link FireClient}.
   */
  public static final SpriteIdentifier getSpriteIdentifier1(String modId, String fireId) {
    return getSpriteIdentifier1(new Identifier(modId, fireId));
  }

  /**
   * Returns the {@link FireClient#spriteIdentifier1} of the {@link FireClient} registered with the given {@code fireType}.
   * <p>
   * Returns {@link ModelLoader#FIRE_1} if no {@link FireClient} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the {@link FireClient#spriteIdentifier1} of the {@link FireClient}.
   */
  public static final SpriteIdentifier getSpriteIdentifier1(Identifier fireType) {
    if (FireManager.isRegisteredType(fireType) && fires.containsKey(fireType)) {
      return fires.get(fireType).getSpriteIdentifier1();
    }
    return ModelLoader.FIRE_1;
  }

  /**
   * Returns the sprite 0 of the {@link FireClient} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns {@link ModelLoader#FIRE_0} sprite if no {@link FireClient} was registered with the given values.
   * 
   * @param id
   * @return the sprite 0 of the {@link FireClient}ì.
   */
  public static final Sprite getSprite0(String modId, String fireId) {
    return getSprite0(new Identifier(modId, fireId));
  }

  /**
   * Returns the sprite 0 of the {@link FireClient} registered with the given {@code fireType}.
   * <p>
   * Returns {@link ModelLoader#FIRE_0} sprite if no {@link FireClient} was registered with the given {@code fireType}.
   * 
   * @param id
   * @return the sprite 0 of the {@link FireClient}.
   */
  public static final Sprite getSprite0(Identifier fireType) {
    if (FireManager.isRegisteredType(fireType) && fires.containsKey(fireType)) {
      return fires.get(fireType).getSprite0();
    }
    return ModelLoader.FIRE_0.getSprite();
  }

  /**
   * Returns the sprite 1 of the {@link FireClient} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns {@link ModelLoader#FIRE_1} sprite if no {@link FireClient} was registered with the given values.
   * 
   * @param id
   * @return the sprite 1 of the {@link FireClient}.
   */
  public static final Sprite getSprite1(String modId, String fireId) {
    return getSprite1(new Identifier(modId, fireId));
  }

  /**
   * Returns the sprite 1 of the {@link FireClient} registered with the given {@code fireType}.
   * <p>
   * Returns {@link ModelLoader#FIRE_1} sprite if no {@link FireClient} was registered with the given {@code fireType}.
   * 
   * @param id
   * @return the sprite 1 of the {@link FireClient} registered with the given {@code fireType}.
   */
  public static final Sprite getSprite1(Identifier fireType) {
    if (FireManager.isRegisteredType(fireType) && fires.containsKey(fireType)) {
      return fires.get(fireType).getSprite1();
    }
    return ModelLoader.FIRE_1.getSprite();
  }
}
