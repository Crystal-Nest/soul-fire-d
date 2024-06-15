package it.crystalnest.soul_fire_d.api;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Fire.
 */
public final class Fire {
  /**
   * {@link ResourceLocation} to uniquely identify this Fire.
   */
  private final ResourceLocation fireType;

  /**
   * Emitted light level by fire related blocks such as fire source, campfire, torch, and lantern.
   */
  private final int light;

  /**
   * Fire damage per second.<br />
   * Positive will hurt, negative will heal, {@code 0} will do nothing.
   */
  private final float damage;

  /**
   * Whether to invert harm and heal for mobs that have potion effects inverted (e.g. undead).
   */
  private final boolean invertHealAndHarm;

  /**
   * Whether rain can douse this Fire.
   */
  private final boolean canRainDouse;

  /**
   * {@link DamageSource} getter for when the entity is in or on a block providing fire.
   */
  private final Function<Entity, DamageSource> inFireGetter;

  /**
   * {@link DamageSource} getter for when the entity is burning.
   */
  private final Function<Entity, DamageSource> onFireGetter;

  /**
   * Custom behavior to apply before the entity takes damage or heals.<br />
   * The returned value determines whether the default hurt/heal behavior should still apply.
   */
  private final Predicate<Entity> behavior;

  /**
   *
   */
  private final ImmutableMap<FireComponent<?, ?>, ResourceLocation> components;

  /**
   * @param fireType {@link #fireType}.
   * @param light {@link #light}.
   * @param damage {@link #damage}.
   * @param invertHealAndHarm {@link #invertHealAndHarm}.
   * @param canRainDouse {@link #canRainDouse}.
   * @param inFireGetter {@link #inFireGetter}.
   * @param onFireGetter {@link #onFireGetter}.
   * @param behavior {@link #behavior}.
   * @param components {@link #components}.
   */
  Fire(
    ResourceLocation fireType,
    int light,
    float damage,
    boolean invertHealAndHarm,
    boolean canRainDouse,
    Function<Entity, DamageSource> inFireGetter,
    Function<Entity, DamageSource> onFireGetter,
    Predicate<Entity> behavior,
    Map<FireComponent<?, ?>, ResourceLocation> components
  ) {
    this.fireType = fireType;
    this.light = light;
    this.damage = damage;
    this.invertHealAndHarm = invertHealAndHarm;
    this.canRainDouse = canRainDouse;
    this.inFireGetter = inFireGetter;
    this.onFireGetter = onFireGetter;
    this.behavior = behavior;
    this.components = ImmutableMap.copyOf(components);
  }

  /**
   * Returns this {@link #fireType}.
   *
   * @return this {@link #fireType}.
   */
  public ResourceLocation getFireType() {
    return fireType;
  }

  public int getLight() {
    return light;
  }

  /**
   * Returns this {@link #damage}.
   *
   * @return this {@link #damage}.
   */
  public float getDamage() {
    return damage;
  }

  /**
   * Returns this {@link #invertHealAndHarm}.
   *
   * @return this {@link #invertHealAndHarm}.
   */
  public boolean invertHealAndHarm() {
    return invertHealAndHarm;
  }

  /**
   * Returns this {@link #canRainDouse}.
   *
   * @return this {@link #canRainDouse}.
   */
  public boolean canRainDouse() {
    return canRainDouse;
  }

  /**
   * Returns the In Fire {@link DamageSource} from the given {@link Entity}.
   *
   * @return the In Fire {@link DamageSource} from the given {@link Entity}.
   */
  public DamageSource getInFire(Entity entity) {
    return inFireGetter.apply(entity);
  }

  /**
   * Returns the On Fire {@link DamageSource} from the given {@link Entity}.
   *
   * @return the On Fire {@link DamageSource} from the given {@link Entity}.
   */
  public DamageSource getOnFire(Entity entity) {
    return onFireGetter.apply(entity);
  }

  /**
   * Returns this {@link #behavior}.
   *
   * @return this {@link #behavior}.
   */
  public Predicate<Entity> getBehavior() {
    return behavior;
  }

  /**
   *
   *
   * @param component
   * @return
   */
  @Nullable
  public ResourceLocation getComponent(FireComponent<?, ?> component) {
    return components.get(component);
  }

  @Override
  public String toString() {
    return "Fire{" + "fireType=" + fireType + ", light=" + light + ", damage=" + damage + ", invertHealAndHarm=" + invertHealAndHarm + ", canRainDouse=" + canRainDouse + ", components=" + components + "}";
  }
}
