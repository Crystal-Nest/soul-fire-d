package it.crystalnest.soul_fire_d.api.client;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static manager for registered Fires, client side only.
 */
public final class FireClientManager {
  /**
   * {@link ConcurrentHashMap} of all registered {@link FireClient Fires}.
   */
  private static final ConcurrentHashMap<ResourceLocation, FireClient> fires = new ConcurrentHashMap<>();

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
   * @return an {@link Map} with the outcome of each registration attempt.
   */
  public static synchronized Map<ResourceLocation, Boolean> registerFires(Fire... fires) {
    return registerFires(List.of(fires));
  }

  /**
   * Attempts to register all the given {@link Fire fires}.
   *
   * @param fires {@link Fire fires} to register.
   * @return an {@link Map} with the outcome of each registration attempt.
   */
  public static synchronized Map<ResourceLocation, Boolean> registerFires(List<Fire> fires) {
    HashMap<ResourceLocation, Boolean> outcomes = new HashMap<>();
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
  @ApiStatus.Internal
  public static synchronized boolean unregisterFire(ResourceLocation fireType) {
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
  private static synchronized boolean registerFire(FireClient fire) {
    if (fires.putIfAbsent(fire.getFireType(), fire) != null) {
      ResourceLocation fireType = fire.getFireType();
      Constants.LOGGER.error("FireClient [{}] was already registered with the following value: {}", fireType, fires.get(fireType));
      return false;
    }
    return true;
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
  public static Material getMaterial0(String modId, String fireId) {
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
  public static Material getMaterial0(ResourceLocation fireType) {
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
  public static Material getMaterial1(String modId, String fireId) {
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
  public static Material getMaterial1(ResourceLocation fireType) {
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
   * @param modId
   * @param fireId
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
   * @param fireType
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
   * @param modId
   * @param fireId
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
   * @param fireType
   * @return the sprite 1 of the {@link FireClient} registered with the given {@code fireType}.
   */
  public static TextureAtlasSprite getSprite1(ResourceLocation fireType) {
    if (FireManager.isRegisteredType(fireType) && fires.containsKey(fireType)) {
      return fires.get(fireType).getSprite1();
    }
    return ModelBakery.FIRE_1.sprite();
  }
}
