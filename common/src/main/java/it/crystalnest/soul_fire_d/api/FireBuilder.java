package it.crystalnest.soul_fire_d.api;

import it.crystalnest.soul_fire_d.api.enchantment.FireAspectBuilder;
import it.crystalnest.soul_fire_d.api.enchantment.FireEnchantmentBuilder;
import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFireAspectEnchantment;
import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFlameEnchantment;
import it.crystalnest.soul_fire_d.api.enchantment.FlameBuilder;
import it.crystalnest.soul_fire_d.api.type.FireTypedEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Builder for {@link Fire} instances.
 */
public final class FireBuilder {
  /**
   * Default value for {@link #damage}.
   */
  public static final float DEFAULT_DAMAGE = 1.0F;

  /**
   * Default value for {@link #invertHealAndHarm}.
   */
  public static final boolean DEFAULT_INVERT_HEAL_AND_HARM = false;

  /**
   * Default value for {@link #inFireGetter}.
   */
  public static final Function<Entity, DamageSource> DEFAULT_IN_FIRE_GETTER = entity -> entity.damageSources().inFire();

  /**
   * Default value for {@link #onFireGetter}.
   */
  public static final Function<Entity, DamageSource> DEFAULT_ON_FIRE_GETTER = entity -> entity.damageSources().onFire();

  /**
   * {@link Fire} instance modId.
   * <p>
   * Required.
   */
  private String modId;

  /**
   * {@link Fire} instance fireId.
   * <p>
   * Required.
   */
  private String fireId;

  /**
   * {@link Fire} instance {@link Fire#damage damage}.
   * <p>
   * Optional, defaults to {@link #DEFAULT_DAMAGE}.
   */
  private float damage;

  /**
   * {@link Fire} instance {@link Fire#invertHealAndHarm invertedHealAndHarm}.
   * <p>
   * Optional, defaults to {@link #DEFAULT_INVERT_HEAL_AND_HARM}.
   */
  private boolean invertHealAndHarm;

  /**
   * {@link Fire} instance {@link Fire#inFireGetter inFireGetter}.
   * <p>
   * Optional, defaults to {@link #DEFAULT_IN_FIRE_GETTER}.
   * <p>
   * If changed from the default, remember to add translations for the new death messages!
   */
  private Function<Entity, DamageSource> inFireGetter;

  /**
   * {@link Fire} instance {@link Fire#onFireGetter onFireGetter}.
   * <p>
   * Optional, defaults to {@link #DEFAULT_ON_FIRE_GETTER}.
   * <p>
   * If changed from the default, remember to add translations for the new death messages!
   */
  private Function<Entity, DamageSource> onFireGetter;

  /**
   * {@link Fire} instance {@link Fire#source source}.
   * <p>
   * Optional, defaults to a new {@link ResourceLocation} with {@link #modId} as namespace and {@code fireId + "_fire"} as path.
   * <p>
   * Default value is recommended.
   * <p>
   * If your Fire is associated with a fire source, but with a different {@link ResourceLocation} than default, use {@link #setSource(ResourceLocation)}.
   * <p>
   * If your Fire is not associated with any fire source, set this to null with {@link #removeSource()}.
   */
  private Optional<ResourceLocation> source;

  /**
   * {@link Fire} instance {@link Fire#campfire campfire}.
   * <p>
   * Optional, defaults to a new {@link ResourceLocation} with {@link #modId} as namespace and {@code fireId + "_campfire"} as path.
   * <p>
   * Default value is recommended.
   * <p>
   * If your Fire is associated with a campfire, but with a different {@link ResourceLocation} than default, use {@link #setCampfire(ResourceLocation)}.
   * <p>
   * If your Fire is not associated with any campfire, set this to null with {@link #removeCampfire()}.
   */
  private Optional<ResourceLocation> campfire;

  /**
   * {@link Function} to configure the {@link FireAspectBuilder}.
   * <p>
   * Optional, defaults to a configuration to build a new {@link FireTypedFireAspectEnchantment} with {@link Enchantment.Rarity#VERY_RARE}.
   * <p>
   * Default value is recommended.
   * <p>
   * If your Fire should have a Fire Aspect enchantment, but with a different value than default, use {@link #setFireAspectConfig(UnaryOperator)}.
   * <p>
   * If your Fire should not have a Fire Aspect enchantment, set this to null with {@link #removeFireAspect()}.
   */
  private Optional<UnaryOperator<FireAspectBuilder>> fireAspectConfigurator;

  /**
   * {@link UnaryOperator} to configure the {@link FlameBuilder}.
   * <p>
   * Optional, defaults to a configuration to build a new {@link FireTypedFlameEnchantment} with {@link Enchantment.Rarity#VERY_RARE}.
   * <p>
   * Default value is recommended.
   * <p>
   * If your Fire should have a Flame enchantment, but with a different value than default, use {@link #setFlameConfig(UnaryOperator)}.
   * <p>
   * If your Fire should not have a Flame enchantment, set this to null with {@link #removeFlame()}.
   */
  private Optional<UnaryOperator<FlameBuilder>> flameConfigurator;

  /**
   * @param modId {@link #modId}.
   * @param fireId {@link #fireId}.
   */
  FireBuilder(String modId, String fireId) {
    reset(modId, fireId);
  }

  /**
   * @param fireType {@link ResourceLocation} to set both {@link #modId} and {@link #fireId}.
   */
  FireBuilder(ResourceLocation fireType) {
    reset(fireType);
  }

  /**
   * Returns the value of the given {@link Optional}.
   * <p>
   * Returns {@code null} if the {@code optional} is either {@code null} or empty.
   *
   * @param <T> value type.
   * @param optional
   * @return the value of the given {@link Optional}.
   */
  @Nullable
  private static <T> T get(@Nullable Optional<T> optional) {
    return optional != null && optional.isPresent() ? optional.get() : null;
  }

  /**
   * Returns the value obtained by calling {@link FireEnchantmentBuilder#register()} for the given Builder.
   * <p>
   * Returns {@code null} if the {@code optional} is either {@code null} or empty.
   *
   * @param <B> builder type.
   * @param <E> enchantment type.
   * @param optional
   * @param builder
   * @return the value obtained by calling {@link FireEnchantmentBuilder#register()}.
   */
  private static <B extends FireEnchantmentBuilder<E>, E extends Enchantment & FireTypedEnchantment> ResourceLocation register(ResourceLocation fireType, @Nullable Optional<UnaryOperator<B>> optional, Function<ResourceLocation, B> builder) {
    UnaryOperator<B> configurator = get(optional);
    if (configurator != null) {
      return configurator.apply(builder.apply(fireType)).register();
    }
    return null;
  }

  /**
   * Sets the {@link #damage}.
   * <p>
   * If the {@code damage} passed is {@code >= 0} the fire will harm entities, otherwise it will heal them.
   * <p>
   * Whether the fire heals or harms depends also on {@link #invertHealAndHarm}.
   *
   * @param damage
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder setDamage(float damage) {
    this.damage = damage;
    return this;
  }

  /**
   * Sets the {@link #invertHealAndHarm} flag.
   * <p>
   * If set to true, entities that have heal and harm inverted (e.g. undeads) will have heal and harm inverted for this fire too.
   *
   * @param invertHealAndHarm
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder setInvertHealAndHarm(boolean invertHealAndHarm) {
    this.invertHealAndHarm = invertHealAndHarm;
    return this;
  }

  /**
   * Sets the {@link DamageSource} {@link #inFireGetter}.
   *
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder setInFire(Function<Entity, DamageSource> getter) {
    this.inFireGetter = getter;
    return this;
  }

  /**
   * Sets the {@link DamageSource} {@link #inFireGetter}.
   *
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder setOnFire(Function<Entity, DamageSource> getter) {
    this.onFireGetter = getter;
    return this;
  }

  /**
   * Sets the {@link #source}.
   *
   * @param source
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder setSource(ResourceLocation source) {
    this.source = Optional.of(source);
    return this;
  }

  /**
   * Removes the {@link #source}.
   *
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder removeSource() {
    this.source = null;
    return this;
  }

  /**
   * Sets the {@link #campfire}.
   *
   * @param campfire
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder setCampfire(ResourceLocation campfire) {
    this.campfire = Optional.of(campfire);
    return this;
  }

  /**
   * Removes the {@link #campfire}.
   *
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder removeCampfire() {
    this.campfire = null;
    return this;
  }

  /**
   * Sets the {@link #fireAspectConfigurator}.
   *
   * @param fireAspectConfigurator {@link UnaryOperator} to configure the {@link FireAspectBuilder}.
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder setFireAspectConfig(UnaryOperator<FireAspectBuilder> fireAspectConfigurator) {
    this.fireAspectConfigurator = Optional.of(fireAspectConfigurator);
    return this;
  }

  /**
   * Marks the Fire Aspect enchantment for removal: it will not be registered.
   *
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder removeFireAspect() {
    this.fireAspectConfigurator = null;
    return this;
  }

  /**
   * Sets the {@link #flameConfigurator}.
   *
   * @param flameConfigurator {@link UnaryOperator} to configure the {@link FlameBuilder}.
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder setFlameConfig(UnaryOperator<FlameBuilder> flameConfigurator) {
    this.flameConfigurator = Optional.of(flameConfigurator);
    return this;
  }

  /**
   * Marks the Flame enchantment for removal: it will not be registered.
   *
   * @return this Builder to either set other properties or {@link #register}.
   */
  public FireBuilder removeFlame() {
    this.flameConfigurator = null;
    return this;
  }

  /**
   * Resets the state of the Builder.
   * <p>
   * Used to avoid getting new Builders from the {@link FireManager manager} and instead use the same instance to build different {@link Fire Fires}.
   *
   * @param fireType {@link ResourceLocation} of the new {@link Fire} to build.
   * @return this Builder, reset.
   */
  public FireBuilder reset(ResourceLocation fireType) {
    return reset(fireType.getNamespace(), fireType.getPath());
  }

  /**
   * Resets the state of the Builder.
   * <p>
   * Used to avoid getting new Builders from the {@link FireManager manager} and instead use the same instance to build different {@link Fire Fires}.
   *
   * @param modId {@code modId} of the new {@link Fire} to build.
   * @param fireId {@code fireId} of the new {@link Fire} to build.
   * @return this Builder, reset.
   */
  public FireBuilder reset(String modId, String fireId) {
    this.modId = modId;
    return reset(fireId);
  }

  /**
   * Resets the state of the Builder.
   * <p>
   * Used to avoid getting new Builders from the {@link FireManager manager} and instead use the same instance to build different {@link Fire Fires}.
   * <p>
   * Note that, for ease of use, the {@link #modId} is not reset.
   *
   * @param fireId {@code fireId} of the new {@link Fire} to build.
   * @return this Builder, reset.
   */
  public FireBuilder reset(String fireId) {
    this.fireId = fireId;
    damage = DEFAULT_DAMAGE;
    invertHealAndHarm = DEFAULT_INVERT_HEAL_AND_HARM;
    inFireGetter = DEFAULT_IN_FIRE_GETTER;
    onFireGetter = DEFAULT_ON_FIRE_GETTER;
    source = Optional.empty();
    campfire = Optional.empty();
    fireAspectConfigurator = Optional.empty();
    flameConfigurator = Optional.empty();
    return this;
  }

  /**
   * Builds a {@link Fire} instance.
   *
   * @return {@link Fire} instance.
   * @throws IllegalStateException if the either {@link #fireId} or {@link #modId} is not valid.
   */
  public Fire build() throws IllegalStateException {
    if (FireManager.isValidFireId(fireId) && FireManager.isValidModId(modId)) {
      ResourceLocation fireType = FireManager.sanitize(modId, fireId);
      if (source != null && source.isEmpty()) {
        source = Optional.of(new ResourceLocation(modId, fireId + "_fire"));
      }
      if (campfire != null && campfire.isEmpty()) {
        campfire = Optional.of(new ResourceLocation(modId, fireId + "_campfire"));
      }
      if (fireAspectConfigurator != null && fireAspectConfigurator.isEmpty()) {
        fireAspectConfigurator = Optional.of(builder -> builder);
      }
      if (flameConfigurator != null && flameConfigurator.isEmpty()) {
        flameConfigurator = Optional.of(builder -> builder);
      }
      return new Fire(fireType, damage, invertHealAndHarm, inFireGetter, onFireGetter, get(source), get(campfire), register(fireType, fireAspectConfigurator, FireAspectBuilder::new), register(fireType, flameConfigurator, FlameBuilder::new));
    }
    throw new IllegalStateException("Attempted to build a Fire with a non-valid fireId or modId");
  }
}
