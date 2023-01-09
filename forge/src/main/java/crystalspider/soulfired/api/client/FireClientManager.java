package crystalspider.soulfired.api.client;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireManager;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

/**
 * Static manager for registered Fires, client side only.
 */
public final class FireClientManager {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger();

  /**
   * {@link ConcurrentHashMap} of all registered {@link FireClient Fires}.
   */
  private static volatile ConcurrentHashMap<ResourceLocation, FireClient> fires = new ConcurrentHashMap<>();

  private FireClientManager() {}

  /**
   * Registers a new {@link FireClient} from the given {@link Fire}.
   * 
   * @param fire {@link Fire} to derive a new {@link FireClient}.
   * @return whether the registration is successful.
   */
  public static synchronized boolean registerFire(Fire fire) {
    return registerFire(new FireClient(fire.getFireType()));
  }

  /**
   * Attempts to register the given {@link FireClient}.
   * <p>
   * If the {@link FireClient#fireType} is already registered, logs an error.
   * 
   * @param fire {@link FireClient} to register.
   * @return whether the registration is successful.
   */
  private static synchronized boolean registerFire(FireClient fire) {
    ResourceLocation fireType = fire.getFireType();
    if (!fires.containsKey(fireType)) {
      fires.put(fireType, fire);
      return true;
    }
    LOGGER.error("FireClient [" + fireType + "] was already registered by mod with the following value: " + fires.get(fireType));
    return false;
  }

  /**
   * Returns the {@link FireClient#material0} of the {@link FireClient} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns {@link ModelBakery#FIRE_0} if no {@link FireClient} was registered with the given values.
   * 
   * @param modId
   * @param fireId
   * @return the {@link FireClient#material0} of the {@link FireClient}.
   */
  public static RenderMaterial getMaterial0(String modId, String fireId) {
    return getMaterial0(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the {@link FireClient#material0} of the {@link FireClient} registered with the given {@code fireType}.
   * <p>
   * Returns {@link ModelBakery#FIRE_0} if no {@link FireClient} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the {@link FireClient#material0} of the {@link FireClient}.
   */
  public static RenderMaterial getMaterial0(ResourceLocation fireType) {
    if (FireManager.isRegisteredType(fireType) && fires.containsKey(fireType)) {
      return fires.get(fireType).getMaterial0();
    }
    return ModelBakery.FIRE_0;
  }

  /**
   * Returns the {@link FireClient#material1} of the {@link FireClient} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns {@link ModelBakery#FIRE_1} if no {@link FireClient} was registered with the given values.
   * 
   * @param modId
   * @param fireId
   * @return the {@link FireClient#material1} of the {@link FireClient}.
   */
  public static RenderMaterial getMaterial1(String modId, String fireId) {
    return getMaterial1(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the {@link FireClient#material1} of the {@link FireClient} registered with the given {@code fireType}.
   * <p>
   * Returns {@link ModelBakery#FIRE_1} if no {@link FireClient} was registered with the given {@code fireType}.
   * 
   * @param fireType
   * @return the {@link FireClient#material1} of the {@link FireClient}.
   */
  public static RenderMaterial getMaterial1(ResourceLocation fireType) {
    if (FireManager.isRegisteredType(fireType) && fires.containsKey(fireType)) {
      return fires.get(fireType).getMaterial1();
    }
    return ModelBakery.FIRE_1;
  }

  /**
   * Returns the sprite 0 of the {@link FireClient} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns {@link ModelBakery#FIRE_0} sprite if no {@link FireClient} was registered with the given values.
   * 
   * @param id
   * @return the sprite 0 of the {@link FireClient}ì.
   */
  public static TextureAtlasSprite getSprite0(String modId, String fireId) {
    return getSprite0(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the sprite 0 of the {@link FireClient} registered with the given {@code fireType}.
   * <p>
   * Returns {@link ModelBakery#FIRE_0} sprite if no {@link FireClient} was registered with the given {@code fireType}.
   * 
   * @param id
   * @return the sprite 0 of the {@link FireClient}.
   */
  public static TextureAtlasSprite getSprite0(ResourceLocation fireType) {
    if (FireManager.isRegisteredType(fireType) && fires.containsKey(fireType)) {
      return fires.get(fireType).getSprite0();
    }
    return ModelBakery.FIRE_0.sprite();
  }

  /**
   * Returns the sprite 1 of the {@link FireClient} registered with the given {@code modId} and {@code fireId}.
   * <p>
   * Returns {@link ModelBakery#FIRE_1} sprite if no {@link FireClient} was registered with the given values.
   * 
   * @param id
   * @return the sprite 1 of the {@link FireClient}.
   */
  public static TextureAtlasSprite getSprite1(String modId, String fireId) {
    return getSprite1(new ResourceLocation(modId, fireId));
  }

  /**
   * Returns the sprite 1 of the {@link FireClient} registered with the given {@code fireType}.
   * <p>
   * Returns {@link ModelBakery#FIRE_1} sprite if no {@link FireClient} was registered with the given {@code fireType}.
   * 
   * @param id
   * @return the sprite 1 of the {@link FireClient} registered with the given {@code fireType}.
   */
  public static TextureAtlasSprite getSprite1(ResourceLocation fireType) {
    if (FireManager.isRegisteredType(fireType) && fires.containsKey(fireType)) {
      return fires.get(fireType).getSprite1();
    }
    return ModelBakery.FIRE_1.sprite();
  }
}
