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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Builder for {@link Fire} instances.
 */
public final class FireBuilder {
  /**
   * Default value for {@link #light}.
   */
  public static final int DEFAULT_LIGHT = 15;

  /**
   * Default value for {@link #damage}.
   */
  public static final float DEFAULT_DAMAGE = 1;

  /**
   * Default value for {@link #invertHealAndHarm}.
   */
  public static final boolean DEFAULT_INVERT_HEAL_AND_HARM = false;

  /**
   * Default value for {@link #canRainDouse}.
   */
  public static final boolean DEFAULT_CAN_RAIN_DOUSE = false;

  /**
   * Default value for {@link #inFireGetter}.
   */
  public static final Function<Entity, DamageSource> DEFAULT_IN_FIRE_GETTER = entity -> entity.damageSources().inFire();

  /**
   * Default value for {@link #onFireGetter}.
   */
  public static final Function<Entity, DamageSource> DEFAULT_ON_FIRE_GETTER = entity -> entity.damageSources().onFire();

  /**
   * Default value for {@link #behavior}.
   */
  public static final Predicate<Entity> DEFAULT_BEHAVIOR = entity -> true;

  /**
   * {@link Fire} instance modId.<br />
   * Required.
   */
  private String modId;

  /**
   * {@link Fire} instance fireId.<br />
   * Required.
   */
  private String fireId;

  /**
   * {@link Fire} instance {@link Fire#light light}.<br />
   * Optional, defaults to {@link #DEFAULT_LIGHT}.
   */
  private int light;

  /**
   * {@link Fire} instance {@link Fire#damage damage}.<br />
   * Optional, defaults to {@link #DEFAULT_DAMAGE}.
   */
  private float damage;

  /**
   * {@link Fire} instance {@link Fire#invertHealAndHarm invertedHealAndHarm}.<br />
   * Optional, defaults to {@link #DEFAULT_INVERT_HEAL_AND_HARM}.
   */
  private boolean invertHealAndHarm;

  /**
   * {@link Fire} instance {@link Fire#canRainDouse canRainDouse}.<br />
   * Optional, defaults to {@link #DEFAULT_CAN_RAIN_DOUSE}.
   */
  private boolean canRainDouse;

  /**
   * {@link Fire} instance {@link Fire#inFireGetter inFireGetter}.<br />
   * Optional, defaults to {@link #DEFAULT_IN_FIRE_GETTER}.<br />
   * If changed from the default, remember to add translations for the new death messages!
   */
  private Function<Entity, DamageSource> inFireGetter;

  /**
   * {@link Fire} instance {@link Fire#onFireGetter onFireGetter}.<br />
   * Optional, defaults to {@link #DEFAULT_ON_FIRE_GETTER}.<br />
   * If changed from the default, remember to add translations for the new death messages!
   */
  private Function<Entity, DamageSource> onFireGetter;

  /**
   * {@link Fire} instance {@link Fire#behavior behavior}.<br />
   * Optional, defaults to {@link #DEFAULT_BEHAVIOR}.
   */
  private Predicate<Entity> behavior;

  /**
   *
   */
  private Map<FireComponent<?, ?>, ResourceLocation> components;

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
  @Nullable
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
  @Nullable
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

  public FireBuilder setLight(int light) {
    if (light >= 0 && light <= 15) {
      this.light = light;
    }
    return this;
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

  public FireBuilder setCanRainDouse(boolean canRainDouse) {
    this.canRainDouse = canRainDouse;
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
   * @param behavior
   * @return
   */
  public FireBuilder setBehavior(@NotNull Predicate<Entity> behavior) {
    this.behavior = behavior;
    return this;
  }

  /**
   *
   *
   * @param behavior
   * @return
   */
  public FireBuilder setBehavior(@NotNull Consumer<Entity> behavior) {
    this.behavior = entity -> {
      behavior.accept(entity);
      return true;
    };
    return this;
  }

  /**
   *
   *
   * @param component
   * @param id
   * @return
   */
  public FireBuilder setComponent(FireComponent<?, ?> component, ResourceLocation id) {
    this.components.put(component, id);
    return this;
  }

  /**
   *
   *
   * @param component
   * @return
   */
  public FireBuilder removeComponent(FireComponent<?, ?> component) {
    this.components.remove(component);
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
    light = DEFAULT_LIGHT;
    damage = DEFAULT_DAMAGE;
    invertHealAndHarm = DEFAULT_INVERT_HEAL_AND_HARM;
    canRainDouse = DEFAULT_CAN_RAIN_DOUSE;
    inFireGetter = DEFAULT_IN_FIRE_GETTER;
    onFireGetter = DEFAULT_ON_FIRE_GETTER;
    behavior = DEFAULT_BEHAVIOR;
    components = new HashMap<>(Map.ofEntries(
      FireComponent.SOURCE_BLOCK.getEntry(modId, fireId),
      FireComponent.CAMPFIRE_BLOCK.getEntry(modId, fireId),
      FireComponent.CAMPFIRE_ITEM.getEntry(modId, fireId),
      FireComponent.LANTERN_BLOCK.getEntry(modId, fireId),
      FireComponent.LANTERN_ITEM.getEntry(modId, fireId),
      FireComponent.TORCH_BLOCK.getEntry(modId, fireId),
      FireComponent.TORCH_ITEM.getEntry(modId, fireId),
      FireComponent.WALL_TORCH_BLOCK.getEntry(modId, fireId),
      FireComponent.FLAME_PARTICLE.getEntry(modId, fireId)
    ));
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
      if (fireAspectConfigurator != null && fireAspectConfigurator.isEmpty()) {
        fireAspectConfigurator = Optional.of(builder -> builder);
      }
      if (flameConfigurator != null && flameConfigurator.isEmpty()) {
        flameConfigurator = Optional.of(builder -> builder);
      }
      components.put(FireComponent.FIRE_ASPECT_ENCHANTMENT, register(fireType, fireAspectConfigurator, FireAspectBuilder::new));
      components.put(FireComponent.FLAME_ENCHANTMENT, register(fireType, flameConfigurator, FlameBuilder::new));
      return new Fire(fireType, light, damage, invertHealAndHarm, canRainDouse, inFireGetter, onFireGetter, behavior, components);
    }
    throw new IllegalStateException("Attempted to build a Fire with a non-valid fireId [" + fireId + "] or modId [" + modId + "].");
  }
}
