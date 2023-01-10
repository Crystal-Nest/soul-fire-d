package crystalspider.soulfired.api;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import crystalspider.soulfired.api.enchantment.FireAspectBuilder;
import crystalspider.soulfired.api.enchantment.FireEnchantmentBuilder;
import crystalspider.soulfired.api.enchantment.FireTypedFireAspectEnchantment;
import crystalspider.soulfired.api.enchantment.FireTypedFlameEnchantment;
import crystalspider.soulfired.api.enchantment.FlameBuilder;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

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
   * Default value for {@link #inFire}.
   */
  public static final DamageSource DEFAULT_IN_FIRE = DamageSource.IN_FIRE;
  /**
   * Default value for {@link #onFire}.
   */
  public static final DamageSource DEFAULT_ON_FIRE = DamageSource.ON_FIRE;
  /**
   * Default value for {@link #hurtSound}.
   */
  public static final SoundEvent DEFAULT_HURT_SOUND = SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE;

  /**
   * {@link Fire} instance {@link Fire#modId modId}.
   * <p>
   * Required.
   */
  private String modId;
  /**
   * {@link Fire} instance {@link Fire#fireId id}.
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
   * {@link Fire} instance {@link Fire#inFire inFire}.
   * <p>
   * Optional, defaults to {@link #DEFAULT_IN_FIRE}.
   * <p>
   * If changed from the default, remember to add translations for the new death messages!
   */
  private DamageSource inFire;
  /**
   * {@link Fire} instance {@link Fire#onFire onFire}.
   * <p>
   * Optional, defaults to {@link #DEFAULT_ON_FIRE}.
   * <p>
   * If changed from the default, remember to add translations for the new death messages!
   */
  private DamageSource onFire;

  /**
   * {@link Fire} instance {@link Fire#hurtSound hurtSound}.
   * <p>
   * Optional, defaults to {@link #DEFAULT_HURT_SOUND}.
   * <p>
   * Default value is recommended.
   */
  private SoundEvent hurtSound;

  /**
   * {@link Fire} instance {@link Fire#source source}.
   * <p>
   * Optional, defaults to a new {@link Identifier} with {@link #modId} as namespace and {@code fireId + "_fire"} as path.
   * <p>
   * Default value is recommended.
   * <p>
   * If your Fire is associated with a fire source, but with a different {@link Identifier} than default, use {@link #setSource(Identifier)}.
   * <p>
   * If your Fire is not associated with any fire source, set this to null with {@link #removeSource()}.
   */
  private Optional<Identifier> source;
  /**
   * {@link Fire} instance {@link Fire#campfire campfire}.
   * <p>
   * Optional, defaults to a new {@link Identifier} with {@link #modId} as namespace and {@code fireId + "_campfire"} as path.
   * <p>
   * Default value is recommended.
   * <p>
   * If your Fire is associated with a campfire, but with a different {@link Identifier} than default, use {@link #setCampfire(Identifier)}.
   * <p>
   * If your Fire is not associated with any campfire, set this to null with {@link #removeCampfire()}.
   */
  private Optional<Identifier> campfire;

  /**
   * {@link Function} to configure the {@link FireAspectBuilder}.
   * <p>
   * Optional, defaults to a configuration to build a new {@link FireTypedFireAspectEnchantment} with {@link Rarity#VERY_RARE}.
   * <p>
   * Default value is recommended.
   * <p>
   * If your Fire should have a Fire Aspect enchantment, but with a different value than default, use {@link #setFireAspectConfig(Function)}.
   * <p>
   * If your Fire should not have a Fire Aspect enchantment, set this to null with {@link #removeFireAspect()}.
   */
  private Optional<Function<FireAspectBuilder, FireAspectBuilder>> fireAspectConfigurator;
  /**
   * {@link Function} to configure the {@link FlameBuilder}.
   * <p>
   * Optional, defaults to a configuration to build a new {@link FireTypedFlameEnchantment} with {@link Rarity#VERY_RARE}.
   * <p>
   * Default value is recommended.
   * <p>
   * If your Fire should have a Flame enchantment, but with a different value than default, use {@link #setFlameConfig(Function)}.
   * <p>
   * If your Fire should not have a Flame enchantment, set this to null with {@link #removeFlame()}.
   */
  private Optional<Function<FlameBuilder, FlameBuilder>> flameConfigurator;

  /**
   * @param modId {@link #modId}.
   * @param fireId {@link #fireId}.
   */
  FireBuilder(String modId, String fireId) {
    reset(modId, fireId);
  }

  /**
   * @param fireType {@link Identifier} to set both {@link #modId} and {@link #fireId}.
   */
  FireBuilder(Identifier fireType) {
    reset(fireType);
  }

  /**
   * Sets the {@link #damage}.
   * <p>
   * If the {@code damage} passed is {@code >= 0} the fire will harm entities, otherwise it will heal them.
   * <p>
   * Whether the fire heals or harms depends also on {@link #invertHealAndHarm}.
   * 
   * @param damage
   * @return this Builder to either set other properties or {@link #build}.
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
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setInvertHealAndHarm(boolean invertHealAndHarm) {
    this.invertHealAndHarm = invertHealAndHarm;
    return this;
  }

  /**
   * Sets the {@link DamageSource} {@link #inFire} by creating a new {@link DamageSource} with the {@code messageId} equal to {@code "in_" + id + "_fire"}.
   * <p>
   * The {@link DamageSource} will have both {@link DamageSource#bypassesArmor bypassesArmor} and {@link DamageSource#fire fire} set to {@code true}.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   * @throws IllegalStateException if the either {@link #modId} or {@link #fireId} is not valid.
   */
  public FireBuilder setInFire() throws IllegalStateException {
    if (FireManager.isValidFireId(fireId)) {
      this.inFire = (new FireDamageSource("in_" + fireId + "_fire")).setBypassesArmor().setFire();
      return this;
    }
    throw new IllegalStateException("Attempted to create inFire DamageSource before setting a valid id");
  }

  /**
   * Sets the {@link DamageSource} {@link #inFire} by creating a new {@link DamageSource} with the {@code messageId} equal to {@code "on_" + id + "_fire"}.
   * <p>
   * The {@link DamageSource} will have both {@link DamageSource#bypassesArmor bypassesArmor} and {@link DamageSource#fire fire} set to {@code true}.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   * @throws IllegalStateException if the either {@link #modId} or {@link #fireId} is not valid.
   */
  public FireBuilder setOnFire() throws IllegalStateException {
    if (FireManager.isValidFireId(fireId)) {
      this.onFire = (new FireDamageSource("on_" + fireId + "_fire")).setBypassesArmor().setFire();
      return this;
    }
    throw new IllegalStateException("Attempted to create onFire DamageSource before setting a valid id");
  }

  /**
   * Sets the {@link #hurtSound}.
   * 
   * @param hurtSound
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setHurtSound(SoundEvent hurtSound) {
    this.hurtSound = hurtSound;
    return this;
  }

  /**
   * Sets the {@link #source}.
   * 
   * @param source
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setSource(Identifier source) {
    this.source = Optional.of(source);
    return this;
  }

  /**
   * Removes the {@link #source}.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder removeSource() {
    this.source = null;
    return this;
  }

  /**
   * Sets the {@link #campfire}.
   * 
   * @param campfire
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setCampfire(Identifier campfire) {
    this.campfire = Optional.of(campfire);
    return this;
  }

  /**
   * Removes the {@link #campfire}.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder removeCampfire() {
    this.campfire = null;
    return this;
  }

  /**
   * Sets the {@link #fireAspectConfigurator}.
   * 
   * @param fireAspectConfigurator {@link Function} to configure the {@link FireAspectBuilder}.
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setFireAspectConfig(Function<FireAspectBuilder, FireAspectBuilder> fireAspectConfigurator) {
    this.fireAspectConfigurator = Optional.of(fireAspectConfigurator);
    return this;
  }

  /**
   * Marks the Fire Aspect enchantment for removal: it will not be registered.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder removeFireAspect() {
    this.fireAspectConfigurator = null;
    return this;
  }

  /**
   * Sets the {@link #flameConfigurator}.
   * 
   * @param flameConfigurator {@link Function} to configure the {@link FlameBuilder}.
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setFlameConfig(Function<FlameBuilder, FlameBuilder> flameConfigurator) {
    this.flameConfigurator = Optional.of(flameConfigurator);
    return this;
  }

  /**
   * Marks the Flame enchantment for removal: it will not be registered.
   * 
   * @return this Builder to either set other properties or {@link #build}.
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
   * @param fireType {@link Identifier} of the new {@link Fire} to build.
   * @return this Builder, reset.
   */
  public FireBuilder reset(Identifier fireType) {
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
    this.fireId = fireId;
    damage = DEFAULT_DAMAGE;
    invertHealAndHarm = DEFAULT_INVERT_HEAL_AND_HARM;
    inFire = DEFAULT_IN_FIRE;
    onFire = DEFAULT_ON_FIRE;
    hurtSound = DEFAULT_HURT_SOUND;
    source = Optional.empty();
    campfire = Optional.empty();
    fireAspectConfigurator = Optional.empty();
    flameConfigurator = Optional.empty();
    return this;
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
    inFire = DEFAULT_IN_FIRE;
    onFire = DEFAULT_ON_FIRE;
    hurtSound = DEFAULT_HURT_SOUND;
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
      Identifier fireType = FireManager.sanitize(modId, fireId);
      if (source != null && source.isEmpty()) {
        source = Optional.of(new Identifier(modId, fireId + "_fire"));
      }
      if (campfire != null && campfire.isEmpty()) {
        campfire = Optional.of(new Identifier(modId, fireId + "_campfire"));
      }
      if (fireAspectConfigurator != null && fireAspectConfigurator.isEmpty()) {
        fireAspectConfigurator = Optional.of(builder -> builder);
      }
      if (flameConfigurator != null && flameConfigurator.isEmpty()) {
        flameConfigurator = Optional.of(builder -> builder);
      }
      return new Fire(fireType, damage, invertHealAndHarm, inFire, onFire, hurtSound, get(source), get(campfire), build(fireAspectConfigurator, () -> new FireAspectBuilder(fireType)), build(flameConfigurator, () -> new FlameBuilder(fireType)));
    }
    throw new IllegalStateException("Attempted to build a Fire with a non-valid fireId or modId");
  }

  /**
   * Returns the value of the given {@link Optional}.
   * <p>
   * Returns {@code null} if the {@code optional} is either {@code null} or empty.
   * 
   * @param <T>
   * @param optional
   * @return the value of the given {@link Optional}.
   */
  @Nullable
  private <T> T get(@Nullable Optional<T> optional) {
    return optional != null && optional.isPresent() ? optional.get() : null;
  }

  /**
   * Returns the value obtained by calling {@link FireEnchantmentBuilder#build()} for the given Builder.
   * <p>
   * Returns {@code null} if the {@code optional} is either {@code null} or empty.
   * 
   * @param <B>
   * @param <E>
   * @param optional
   * @param supplier
   * @return the value obtained by calling {@link FireEnchantmentBuilder#build()}.
   */
  @Nullable
  private <B extends FireEnchantmentBuilder<E>, E extends Enchantment & FireTyped> E build(@Nullable Optional<Function<B, B>> optional, Supplier<B> supplier) {
    Function<B, B> configurator = get(optional);
    if (configurator != null) {
      return configurator.apply(supplier.get()).build();
    }
    return null;
  }
}
