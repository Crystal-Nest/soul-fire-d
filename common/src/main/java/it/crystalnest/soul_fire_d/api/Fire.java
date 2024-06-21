package it.crystalnest.soul_fire_d.api;

import com.google.common.collect.ImmutableMap;
import it.crystalnest.soul_fire_d.api.enchantment.FireAspectBuilder;
import it.crystalnest.soul_fire_d.api.enchantment.FireEnchantmentBuilder;
import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFireAspectEnchantment;
import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFlameEnchantment;
import it.crystalnest.soul_fire_d.api.enchantment.FlameBuilder;
import it.crystalnest.soul_fire_d.api.type.FireTypedEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.enchantment.ArrowFireEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.FireAspectEnchantment;
import net.minecraft.world.level.block.Block;
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
   * {@link ImmutableMap} of {@link Component}s associated with their IDs for this fire.
   */
  private final ImmutableMap<Component<?, ?>, ResourceLocation> components;

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
    Map<Component<?, ?>, ResourceLocation> components
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

  /**
   * Returns this {@link #light}.
   *
   * @return this {@link #light}.
   */
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
   * @param entity entity.
   * @return the In Fire {@link DamageSource} from the given {@link Entity}.
   */
  public DamageSource getInFire(Entity entity) {
    return inFireGetter.apply(entity);
  }

  /**
   * Returns the On Fire {@link DamageSource} from the given {@link Entity}.
   *
   * @param entity entity.
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
   * Returns {@link ResourceLocation} associated with specified {@link Component}.<br />
   * Might be {@code null} if this fire doesn't have the specified component.
   *
   * @param component {@link Component}.
   * @return the {@link ResourceLocation} associated with specified {@link Component}.
   */
  @Nullable
  public ResourceLocation getComponent(Component<?, ?> component) {
    return components.get(component);
  }

  @Override
  public String toString() {
    return "Fire{" + "fireType=" + fireType + ", light=" + light + ", damage=" + damage + ", invertHealAndHarm=" + invertHealAndHarm + ", canRainDouse=" + canRainDouse + ", components=" + components + "}";
  }

  /**
   * Fire component to associate a component to a {@link ResourceLocation} and easy retrieve the value registered with it.
   *
   * @param <R> {@link Registry} type.
   * @param <T> value type.
   */
  public static final class Component<R, T extends R> {
    /**
     * Source block component.
     */
    public static final Component<Block, Block> SOURCE_BLOCK = new Component<>(Registries.BLOCK, "_fire");

    /**
     * Campfire block component.
     */
    public static final Component<Block, Block> CAMPFIRE_BLOCK = new Component<>(Registries.BLOCK, "_campfire");

    /**
     * Campfire item component.
     */
    public static final Component<Item, BlockItem> CAMPFIRE_ITEM = new Component<>(Registries.ITEM, "_campfire");

    /**
     * Lantern block component.
     */
    public static final Component<Block, Block> LANTERN_BLOCK = new Component<>(Registries.BLOCK, "_lantern");

    /**
     * Lantern item component.
     */
    public static final Component<Item, BlockItem> LANTERN_ITEM = new Component<>(Registries.ITEM, "_lantern");

    /**
     * Torch block component.
     */
    public static final Component<Block, Block> TORCH_BLOCK = new Component<>(Registries.BLOCK, "_torch");

    /**
     * Torch item component.
     */
    public static final Component<Item, StandingAndWallBlockItem> TORCH_ITEM = new Component<>(Registries.ITEM, "_torch");

    /**
     * Wall torch block component.
     */
    public static final Component<Block, Block> WALL_TORCH_BLOCK = new Component<>(Registries.BLOCK, "_wall_torch");

    /**
     * Flame particle component.
     */
    public static final Component<ParticleType<?>, SimpleParticleType> FLAME_PARTICLE = new Component<>(Registries.PARTICLE_TYPE, "_flame");

    /**
     * Fire Aspect enchantment component.
     */
    public static final Component<Enchantment, FireAspectEnchantment> FIRE_ASPECT_ENCHANTMENT = new Component<>(Registries.ENCHANTMENT, "_fire_aspect");

    /**
     * Flame enchantment component.
     */
    public static final Component<Enchantment, ArrowFireEnchantment> FLAME_ENCHANTMENT = new Component<>(Registries.ENCHANTMENT, "_flame");

    /**
     * Registry key where the value associated to this component is stored.
     */
    private final ResourceKey<? extends Registry<R>> key;

    /**
     * Component default suffix for the associated {@link ResourceLocation}.
     */
    private final String suffix;

    /**
     * @param key {@link #key}.
     * @param suffix {@link #suffix}.
     */
    private Component(ResourceKey<? extends Registry<R>> key, String suffix) {
      this.key = key;
      this.suffix = suffix;
    }

    /**
     * Returns the {@link Registry} where the value associated to this component is stored.
     *
     * @return the {@link Registry} where the value associated to this component is stored.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    Registry<R> getRegistry() {
      return (Registry<R>) BuiltInRegistries.REGISTRY.get(key.location());
    }

    /**
     * Returns the value associated to this component.<br />
     * Might be {@code null} if no value was registered with the given ID.
     *
     * @param id value ID.
     * @return the value associated to this component.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    T getValue(ResourceLocation id) {
      return (T) getRegistry().get(id);
    }

    /**
     * Returns the value associated to this component by retrieving the ID from the given {@link Fire}.<br />
     * Might be {@code null} if no value was registered with the given ID.
     *
     * @param fire {@link Fire}.
     * @return the value associated to this component.
     */
    @Nullable
    T getValue(Fire fire) {
      return getValue(fire.getComponent(this));
    }

    /**
     * Returns the value associated to this component.
     *
     * @param id value ID.
     * @return the value associated to this component.
     */
    @SuppressWarnings("unchecked")
    Optional<T> getOptionalValue(ResourceLocation id) {
      return (Optional<T>) getRegistry().getOptional(id);
    }

    /**
     * Returns the value associated to this component by retrieving the ID from the given {@link Fire}.
     *
     * @param fire {@link Fire}.
     * @return the value associated to this component.
     */
    Optional<T> getOptionalValue(Fire fire) {
      return getOptionalValue(fire.getComponent(this));
    }

    /**
     * Returns the default {@link Map#entry(Object, Object) Map.entry} for this component from the given {@code modId} and {@code fireId}.
     *
     * @param modId mod ID.
     * @param fireId fire ID.
     * @return the default {@link Map#entry(Object, Object) Map.entry} for this component.
     */
    Map.Entry<Component<R, T>, ResourceLocation> getEntry(String modId, String fireId) {
      return Map.entry(this, new ResourceLocation(modId, fireId + suffix));
    }
  }

  /**
   * Builder for {@link Fire} instances.
   */
  public static final class Builder {
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
     * {@link Fire} instance {@link Fire#components components}.<br />
     * Optional, defaults to a map with every component, each associated to the default {@link ResourceLocation} made by {@link #modId} and {@link #fireId} with the component default {@link Component#suffix suffix}.
     */
    private Map<Component<?, ?>, ResourceLocation> components;

    /**
     * {@link Function} to configure the {@link FireAspectBuilder}.<br />
     * Optional, defaults to a configuration to build a new {@link FireTypedFireAspectEnchantment} with {@link Enchantment.Rarity#VERY_RARE}.<br />
     * Default value is recommended.<br />
     * If your Fire should have a Fire Aspect enchantment, but with a different value than default, use {@link #setFireAspectConfig(UnaryOperator)}.<br />
     * If your Fire should not have a Fire Aspect enchantment, set this to null with {@link #removeFireAspect()}.
     */
    @Nullable
    private Optional<UnaryOperator<FireAspectBuilder>> fireAspectConfigurator;

    /**
     * {@link UnaryOperator} to configure the {@link FlameBuilder}.<br />
     * Optional, defaults to a configuration to build a new {@link FireTypedFlameEnchantment} with {@link Enchantment.Rarity#VERY_RARE}.<br />
     * Default value is recommended.<br />
     * If your Fire should have a Flame enchantment, but with a different value than default, use {@link #setFlameConfig(UnaryOperator)}.<br />
     * If your Fire should not have a Flame enchantment, set this to null with {@link #removeFlame()}.
     */
    @Nullable
    private Optional<UnaryOperator<FlameBuilder>> flameConfigurator;

    /**
     * @param modId {@link #modId}.
     * @param fireId {@link #fireId}.
     */
    Builder(String modId, String fireId) {
      reset(modId, fireId);
    }

    /**
     * @param fireType {@link ResourceLocation} to set both {@link #modId} and {@link #fireId}.
     */
    Builder(ResourceLocation fireType) {
      reset(fireType);
    }

    /**
     * Returns the value of the given {@link Optional}.<br />
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
     * Returns the value obtained by calling {@link FireEnchantmentBuilder#register()} for the given Builder.<br />
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
     * Sets the {@link #light}.<br />
     * Accepted values are only between {@code 0} and {@code 15} inclusive.
     *
     * @param light {@link #light}.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder setLight(int light) {
      if (light >= 0 && light <= 15) {
        this.light = light;
      }
      return this;
    }

    /**
     * Sets the {@link #damage}.<br />
     * If the {@code damage} passed is {@code >= 0} the fire will harm entities, otherwise it will heal them.<br />
     * Whether the fire heals or harms depends also on {@link #invertHealAndHarm}.
     *
     * @param damage {@link #damage}.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder setDamage(float damage) {
      this.damage = damage;
      return this;
    }

    /**
     * Sets the {@link #invertHealAndHarm} flag.<br />
     * If set to true, entities that have heal and harm inverted (e.g. undeads) will have heal and harm inverted for this fire too.
     *
     * @param invertHealAndHarm {@link #invertHealAndHarm}.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder setInvertHealAndHarm(boolean invertHealAndHarm) {
      this.invertHealAndHarm = invertHealAndHarm;
      return this;
    }

    /**
     * Sets the {@link #canRainDouse}.
     *
     * @param canRainDouse {@link #canRainDouse}.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder setCanRainDouse(boolean canRainDouse) {
      this.canRainDouse = canRainDouse;
      return this;
    }

    /**
     * Sets the {@link DamageSource} {@link #inFireGetter}.
     *
     * @param getter damage source getter.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder setInFire(Function<Entity, DamageSource> getter) {
      this.inFireGetter = getter;
      return this;
    }

    /**
     * Sets the {@link DamageSource} {@link #onFireGetter}.
     *
     * @param getter damage source getter.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder setOnFire(Function<Entity, DamageSource> getter) {
      this.onFireGetter = getter;
      return this;
    }

    /**
     * Sets the {@link #behavior}.
     *
     * @param behavior {@link #behavior}.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder setBehavior(@NotNull Predicate<Entity> behavior) {
      this.behavior = behavior;
      return this;
    }

    /**
     * Sets the {@link #behavior}.<br />
     * Shorthand for custom behaviors that don't prevent the default heal/harm behavior, thus returning {@code true}.
     *
     * @param behavior {@link #behavior}.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder setBehavior(@NotNull Consumer<Entity> behavior) {
      this.behavior = entity -> {
        behavior.accept(entity);
        return true;
      };
      return this;
    }

    /**
     * Sets the specified {@link Component}.<br />
     * It's strongly recommended that you use all the default values for each component. Use this only when you don't have control over the values.
     * Do not use this to add Fire Aspect and Flame enchantments: use {@link #setFireAspectConfig(UnaryOperator)} and {@link #setFlameConfig(UnaryOperator)} instead.<br />
     *
     * @param component component.
     * @param id {@link ResourceLocation}.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder setComponent(Component<?, ?> component, ResourceLocation id) {
      this.components.put(component, id);
      return this;
    }

    /**
     * Removes the specified {@link Component}.<br />
     * Do not use this to remove Fire Aspect and Flame enchantments: use {@link #removeFireAspect()} and {@link #removeFlame()} instead.<br />
     *
     * @param component component.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder removeComponent(Component<?, ?> component) {
      this.components.remove(component);
      return this;
    }

    /**
     * Sets the {@link #fireAspectConfigurator}.
     *
     * @param fireAspectConfigurator {@link UnaryOperator} to configure the {@link FireAspectBuilder}.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder setFireAspectConfig(UnaryOperator<FireAspectBuilder> fireAspectConfigurator) {
      this.fireAspectConfigurator = Optional.of(fireAspectConfigurator);
      return this;
    }

    /**
     * Marks the Fire Aspect enchantment for removal: it will not be registered.
     *
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder removeFireAspect() {
      this.fireAspectConfigurator = null;
      return this;
    }

    /**
     * Sets the {@link #flameConfigurator}.
     *
     * @param flameConfigurator {@link UnaryOperator} to configure the {@link FlameBuilder}.
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder setFlameConfig(UnaryOperator<FlameBuilder> flameConfigurator) {
      this.flameConfigurator = Optional.of(flameConfigurator);
      return this;
    }

    /**
     * Marks the Flame enchantment for removal: it will not be registered.
     *
     * @return this Builder to either set other properties or {@link #register}.
     */
    public Builder removeFlame() {
      this.flameConfigurator = null;
      return this;
    }

    /**
     * Resets the state of the Builder.<br />
     * Used to avoid getting new Builders from the {@link FireManager manager} and instead use the same instance to build different {@link Fire Fires}.
     *
     * @param fireType {@link ResourceLocation} of the new {@link Fire} to build.
     * @return this Builder, reset.
     */
    public Builder reset(ResourceLocation fireType) {
      return reset(fireType.getNamespace(), fireType.getPath());
    }

    /**
     * Resets the state of the Builder.<br />
     * Used to avoid getting new Builders from the {@link FireManager manager} and instead use the same instance to build different {@link Fire Fires}.
     *
     * @param modId {@code modId} of the new {@link Fire} to build.
     * @param fireId {@code fireId} of the new {@link Fire} to build.
     * @return this Builder, reset.
     */
    public Builder reset(String modId, String fireId) {
      this.modId = modId;
      return reset(fireId);
    }

    /**
     * Resets the state of the Builder.<br />
     * Used to avoid getting new Builders from the {@link FireManager manager} and instead use the same instance to build different {@link Fire Fires}.<br />
     * Note that, for ease of use, the {@link #modId} is not reset.
     *
     * @param fireId {@code fireId} of the new {@link Fire} to build.
     * @return this Builder, reset.
     */
    public Builder reset(String fireId) {
      this.fireId = fireId;
      light = DEFAULT_LIGHT;
      damage = DEFAULT_DAMAGE;
      invertHealAndHarm = DEFAULT_INVERT_HEAL_AND_HARM;
      canRainDouse = DEFAULT_CAN_RAIN_DOUSE;
      inFireGetter = DEFAULT_IN_FIRE_GETTER;
      onFireGetter = DEFAULT_ON_FIRE_GETTER;
      behavior = DEFAULT_BEHAVIOR;
      components = new HashMap<>(Map.ofEntries(
        Component.SOURCE_BLOCK.getEntry(modId, fireId),
        Component.CAMPFIRE_BLOCK.getEntry(modId, fireId),
        Component.CAMPFIRE_ITEM.getEntry(modId, fireId),
        Component.LANTERN_BLOCK.getEntry(modId, fireId),
        Component.LANTERN_ITEM.getEntry(modId, fireId),
        Component.TORCH_BLOCK.getEntry(modId, fireId),
        Component.TORCH_ITEM.getEntry(modId, fireId),
        Component.WALL_TORCH_BLOCK.getEntry(modId, fireId),
        Component.FLAME_PARTICLE.getEntry(modId, fireId)
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
        if (fireAspectConfigurator != null) {
          if (fireAspectConfigurator.isEmpty()) {
            fireAspectConfigurator = Optional.of(builder -> builder);
          }
          components.put(Component.FIRE_ASPECT_ENCHANTMENT, register(fireType, fireAspectConfigurator, FireAspectBuilder::new));
        }
        if (flameConfigurator != null) {
          if (flameConfigurator.isEmpty()) {
            flameConfigurator = Optional.of(builder -> builder);
          }
          components.put(Component.FLAME_ENCHANTMENT, register(fireType, flameConfigurator, FlameBuilder::new));
        }
        return new Fire(fireType, light, damage, invertHealAndHarm, canRainDouse, inFireGetter, onFireGetter, behavior, components);
      }
      throw new IllegalStateException("Attempted to build a Fire with a non-valid fireId [" + fireId + "] or modId [" + modId + "].");
    }
  }
}
