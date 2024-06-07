package it.crystalnest.soul_fire_d.api;

import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFireAspectEnchantment;
import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFlameEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;

import java.util.Optional;
import java.util.function.Function;

/**
 * Fire.
 */
public final class Fire {
  /**
   * {@link ResourceLocation} to uniquely identify this Fire.
   */
  private final ResourceLocation fireType;

  private final int light;

  /**
   * Fire damage per second.
   */
  private final float damage;

  /**
   * Whether to invert harm and heal for mobs that have potion effects inverted (e.g. undeads).
   */
  private final boolean invertHealAndHarm;

  /**
   * {@link DamageSource} getter for when the entity is in or on a block providing fire.
   */
  private final Function<Entity, DamageSource> inFireGetter;

  /**
   * {@link DamageSource} getter for when the entity is burning.
   */
  private final Function<Entity, DamageSource> onFireGetter;

  /**
   * {@link Optional} {@link ResourceLocation} of the {@link Block} of the Fire Block considered as the source for this Fire.
   */
  private final Optional<ResourceLocation> source;

  /**
   * {@link Optional} {@link ResourceLocation} of the {@link CampfireBlock} associated with this Fire.
   */
  private final Optional<ResourceLocation> campfire;

  /**
   * {@link Optional} Fire Aspect {@link FireTypedFireAspectEnchantment} {@link ResourceLocation}.
   */
  private final Optional<ResourceLocation> fireAspect;

  /**
   * {@link Optional} Flame {@link FireTypedFlameEnchantment} {@link ResourceLocation}.
   */
  private final Optional<ResourceLocation> flame;

  /**
   * @param fireType {@link #fireType}.
   * @param damage {@link #damage}.
   * @param invertHealAndHarm {@link #invertHealAndHarm}.
   * @param inFireGetter {@link #inFireGetter}.
   * @param onFireGetter {@link #onFireGetter}.
   * @param source {@link #source}.
   * @param campfire {@link #campfire}.
   * @param fireAspect {@link #fireAspect}.
   * @param flame {@link #flame}.
   */
  Fire(
    ResourceLocation fireType,
    int light,
    float damage,
    boolean invertHealAndHarm,
    Function<Entity, DamageSource> inFireGetter,
    Function<Entity, DamageSource> onFireGetter,
    ResourceLocation source,
    ResourceLocation campfire,
    ResourceLocation fireAspect,
    ResourceLocation flame
  ) {
    this.fireType = fireType;
    this.light = light;
    this.damage = damage;
    this.invertHealAndHarm = invertHealAndHarm;
    this.inFireGetter = inFireGetter;
    this.onFireGetter = onFireGetter;
    this.source = Optional.ofNullable(source);
    this.campfire = Optional.ofNullable(campfire);
    this.fireAspect = Optional.ofNullable(fireAspect);
    this.flame = Optional.ofNullable(flame);
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
  public boolean getInvertHealAndHarm() {
    return invertHealAndHarm;
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
   * Returns this {@link #source}.
   *
   * @return this {@link #source}.
   */
  public Optional<ResourceLocation> getSource() {
    return source;
  }

  /**
   * Returns this {@link #campfire}.
   *
   * @return this {@link #campfire}.
   */
  public Optional<ResourceLocation> getCampfire() {
    return campfire;
  }

  /**
   * Returns this {@link #fireAspect}.
   *
   * @return this {@link #fireAspect}.
   */
  public Optional<ResourceLocation> getFireAspect() {
    return fireAspect;
  }

  /**
   * Returns this {@link #flame}.
   *
   * @return this {@link #flame}.
   */
  public Optional<ResourceLocation> getFlame() {
    return flame;
  }

  @Override
  public String toString() {
    return "Fire [fireType=" + fireType + ", damage=" + damage + ", invertedHealAndHarm=" + invertHealAndHarm + ", source=" + source + ", campfire=" + campfire + "]";
  }
}
