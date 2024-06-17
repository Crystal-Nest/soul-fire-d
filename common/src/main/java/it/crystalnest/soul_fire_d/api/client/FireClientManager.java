package it.crystalnest.soul_fire_d.api.client;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

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
  private static final ConcurrentHashMap<ResourceLocation, FireClient> FIRES = new ConcurrentHashMap<>();

  private FireClientManager() {}

  /**
   * Attempts to register the given {@link FireClient}.<br />
   * If the {@link FireClient#fireType} is already registered, logs an error.
   *
   * @param fire {@link FireClient} to register.
   * @return the registered fire or {@code null} if the registration was unsuccessful.
   */
  @Nullable
  private static synchronized FireClient registerFire(FireClient fire) {
    if (FIRES.putIfAbsent(fire.getFireType(), fire) != null) {
      ResourceLocation fireType = fire.getFireType();
      Constants.LOGGER.error("FireClient [{}] was already registered with the following value: {}", fireType, FIRES.get(fireType));
      return null;
    }
    return fire;
  }

  /**
   * Attempts to a new {@link FireClient} from the given {@link Fire}.
   *
   * @param fire {@link Fire} to derive a new {@link FireClient}.
   * @return the registered fire or {@code null} if the registration was unsuccessful.
   */
  @Nullable
  public static synchronized FireClient registerFire(Fire fire) {
    return registerFire(new FireClient(fire.getFireType()));
  }

  /**
   * Attempts to register all the given {@link Fire fires}.
   *
   * @param fires {@link Fire fires} to register.
   * @return an {@link Map} with the outcome of each registration attempt.
   */
  public static synchronized Map<ResourceLocation, @Nullable FireClient> registerFires(List<Fire> fires) {
    HashMap<ResourceLocation, @Nullable FireClient> outcomes = new HashMap<>();
    for (Fire fire : fires) {
      outcomes.put(fire.getFireType(), registerFire(fire));
    }
    return outcomes;
  }

  /**
   * Attempts to register new {@link FireClient} for all the given {@link Fire fires}.
   *
   * @param fires {@link Fire fires} to derive {@link FireClient} to register.
   * @return an {@link Map} with the outcome of each registration attempt.
   */
  public static synchronized Map<ResourceLocation, @Nullable FireClient> registerFires(Fire... fires) {
    return registerFires(List.of(fires));
  }

  /**
   * Unregisters the specified fire.<br />
   * Internally use only, do not use elsewhere!
   *
   * @param fireType fire type.
   * @return whether the fire was previously registered.
   */
  @Nullable
  @ApiStatus.Internal
  public static synchronized FireClient unregisterFire(ResourceLocation fireType) {
    return FIRES.remove(fireType);
  }

  /**
   * Returns the {@link FireClient#material0} of the {@link FireClient} registered with the given {@code fireType}.<br />
   * Returns {@link ModelBakery#FIRE_0} if no {@link FireClient} was registered with the given {@code fireType}.
   *
   * @param fireType fire type.
   * @return the {@link FireClient#material0} of the {@link FireClient}.
   */
  public static Material getMaterial0(ResourceLocation fireType) {
    if (FireManager.isRegisteredType(fireType) && FIRES.containsKey(fireType)) {
      return FIRES.get(fireType).getMaterial0();
    }
    return ModelBakery.FIRE_0;
  }

  /**
   * Returns the {@link FireClient#material1} of the {@link FireClient} registered with the given {@code fireType}.<br />
   * Returns {@link ModelBakery#FIRE_1} if no {@link FireClient} was registered with the given {@code fireType}.
   *
   * @param fireType fire type.
   * @return the {@link FireClient#material1} of the {@link FireClient}.
   */
  public static Material getMaterial1(ResourceLocation fireType) {
    if (FireManager.isRegisteredType(fireType) && FIRES.containsKey(fireType)) {
      return FIRES.get(fireType).getMaterial1();
    }
    return ModelBakery.FIRE_1;
  }

  /**
   * Returns the sprite 0 of the {@link FireClient} registered with the given {@code fireType}.<br />
   * Returns {@link ModelBakery#FIRE_0} sprite if no {@link FireClient} was registered with the given {@code fireType}.
   *
   * @param fireType fire type.
   * @return the sprite 0 of the {@link FireClient}.
   */
  public static TextureAtlasSprite getSprite0(ResourceLocation fireType) {
    if (FireManager.isRegisteredType(fireType) && FIRES.containsKey(fireType)) {
      return FIRES.get(fireType).getSprite0();
    }
    return ModelBakery.FIRE_0.sprite();
  }

  /**
   * Returns the sprite 1 of the {@link FireClient} registered with the given {@code fireType}.<br />
   * Returns {@link ModelBakery#FIRE_1} sprite if no {@link FireClient} was registered with the given {@code fireType}.
   *
   * @param fireType fire type.
   * @return the sprite 1 of the {@link FireClient} registered with the given {@code fireType}.
   */
  public static TextureAtlasSprite getSprite1(ResourceLocation fireType) {
    if (FireManager.isRegisteredType(fireType) && FIRES.containsKey(fireType)) {
      return FIRES.get(fireType).getSprite1();
    }
    return ModelBakery.FIRE_1.sprite();
  }
}
