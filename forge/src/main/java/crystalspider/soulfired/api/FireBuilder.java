package crystalspider.soulfired.api;

import java.util.Optional;

import javax.annotation.Nullable;

import crystalspider.soulfired.api.enchantment.FireTypedArrowEnchantment;
import crystalspider.soulfired.api.enchantment.FireTypedAspectEnchantment;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

/**
 * Builder for {@link Fire} instances.
 */
public class FireBuilder {
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
  public static final SoundEvent DEFAULT_HURT_SOUND = SoundEvents.PLAYER_HURT_ON_FIRE;

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
   * {@link Fire} instance {@link Fire#fireAspect fireAspect}.
   * <p>
   * Optional, defaults to a new {@link FireTypedAspectEnchantment} with {@link Rarity#VERY_RARE}.
   * <p>
   * Default value is recommended.
   * <p>
   * If your Fire should have a Fire Aspect enchantment, but with a different value than default, use {@link #setFireAspect(Enchantment)}.
   * <p>
   * If your Fire should not have a Fire Aspect enchantment, set this to null with {@link #removeFireAspect()}.
   */
  private Optional<Enchantment> fireAspect;
  /**
   * {@link Fire} instance {@link Fire#flame flame}.
   * <p>
   * Optional, defaults to a new {@link FireTypedArrowEnchantment} with {@link Rarity#VERY_RARE}.
   * <p>
   * Default value is recommended.
   * <p>
   * If your Fire should have a Flame enchantment, but with a different value than default, use {@link #setFlame(Enchantment)}.
   * <p>
   * If your Fire should not have a Flame enchantment, set this to null with {@link #removeFlame()}.
   */
  private Optional<Enchantment> flame;

  FireBuilder() {
    reset();
  }

  /**
   * Sets the {@link #modId}.
   * <p>
   * {@link #modId} will be set only if the given {@code id} is valid.
   * 
   * @param modId
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setModId(String modId) {
    if (FireManager.isValidModId(modId)) {
      this.modId = modId;
    }
    return this;
  }

  /**
   * Sets the {@link #fireId}.
   * <p>
   * {@link #fireId} will be set only if the given {@code id} is valid.
   * 
   * @param fireId
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setFireId(String fireId) {
    if (FireManager.isValidFireId(fireId)) {
      this.fireId = fireId;
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
   * Sets the {@link DamageSource} {@link #inFire}.
   * <p>
   * Prefer {@link #setInFire(String)}.
   * 
   * @param inFire
   * @return this Builder to either set other properties or {@link #build}.
   */
  private FireBuilder setInFire(DamageSource inFire) {
    this.inFire = inFire;
    return this;
  }

  /**
   * Sets the {@link DamageSource} {@link #inFire} by creating a new {@link DamageSource} with the given {@code messageId}.
   * <p>
   * The {@link DamageSource} will have both {@link DamageSource#bypassArmor bypassArmor} and {@link DamageSource#isFireSource isFireSource} set to {@code true}.
   * <p>
   * Prefer the use of {@link #setInFire()} after setting both {@link #modId} and {@link #fireId}.
   * 
   * @param messageId
   * @return this Builder to either set other properties or {@link #build}.
   */
  @Deprecated
  public FireBuilder setInFire(String messageId) {
    return setInFire((new DamageSource(messageId)).bypassArmor().setIsFire());
  }

  /**
   * Sets the {@link DamageSource} {@link #inFire} by creating a new {@link DamageSource} with the {@code messageId} equal to {@code "in_" + id + "_fire"}.
   * <p>
   * The {@link DamageSource} will have both {@link DamageSource#bypassArmor bypassArmor} and {@link DamageSource#isFireSource isFireSource} set to {@code true}.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   * @throws IllegalStateException if the either {@link #modId} or {@link #fireId} is not valid.
   */
  public FireBuilder setInFire() throws IllegalStateException {
    if (FireManager.isValidFireId(fireId)) {
      return setInFire("in_" + fireId + "_fire");
    }
    throw new IllegalStateException("Attempted to create inFire DamageSource before setting a valid id");
  }

  /**
   * Sets the {@link DamageSource} {@link #onFire}.
   * <p>
   * Prefer {@link #setOnFire(String)}.
   * 
   * @param onFire
   * @return this Builder to either set other properties or {@link #build}.
   */
  private FireBuilder setOnFire(DamageSource onFire) {
    this.onFire = onFire;
    return this;
  }

  /**
   * Sets the {@link DamageSource} {@link #onFire} by creating a new {@link DamageSource} with the given {@code messageId}.
   * <p>
   * The {@link DamageSource} will have both {@link DamageSource#bypassArmor bypassArmor} and {@link DamageSource#isFireSource isFireSource} set to {@code true}.
   * <p>
   * Prefer the use of {@link #setOnFire()} after setting both {@link #modId} and {@link #fireId}.
   * 
   * @param messageId
   * @return this Builder to either set other properties or {@link #build}.
   */
  @Deprecated
  public FireBuilder setOnFire(String messageId) {
    return setOnFire((new DamageSource(messageId)).bypassArmor().setIsFire());
  }

  /**
   * Sets the {@link DamageSource} {@link #inFire} by creating a new {@link DamageSource} with the {@code messageId} equal to {@code "on_" + id + "_fire"}.
   * <p>
   * The {@link DamageSource} will have both {@link DamageSource#bypassArmor bypassArmor} and {@link DamageSource#isFireSource isFireSource} set to {@code true}.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   * @throws IllegalStateException if the either {@link #modId} or {@link #fireId} is not valid.
   */
  public FireBuilder setOnFire() throws IllegalStateException {
    if (FireManager.isValidFireId(fireId)) {
      return setOnFire("on_" + fireId + "_fire");
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
  public FireBuilder setSource(ResourceLocation source) {
    this.source = Optional.of(source);
    return this;
  }

  /**
   * Removes the fire source.
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
  public FireBuilder setCampfire(ResourceLocation campfire) {
    this.campfire = Optional.of(campfire);
    return this;
  }

  /**
   * Removes the campfire.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder removeCampfire() {
    this.campfire = null;
    return this;
  }

  /**
   * Sets the {@link #fireAspect} enchantment.
   * <p>
   * The {@link Enchantment} passed as parameter <b>MUST</b> implement the {@link FireTyped} interface, otherwise the value will not be set.
   * 
   * @param fireAspect
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setFireAspect(Enchantment fireAspect) {
    if (fireAspect instanceof FireTyped) {
      this.fireAspect = Optional.of(fireAspect);
    }
    return this;
  }

  /**
   * Removes the Fire Aspect enchantment.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder removeFireAspect() {
    this.fireAspect = null;
    return this;
  }

  /**
   * Sets the {@link #fireAspect} enchantment.
   * <p>
   * The {@link Enchantment} passed as parameter <b>MUST</b> implement the {@link FireTyped} interface, otherwise the value will not be set.
   * 
   * @param fireAspect
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setFlame(Enchantment flame) {
    if (flame instanceof FireTyped) {
      this.flame = Optional.of(flame);
    }
    return this;
  }

  /**
   * Removes the Flame enchantment.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder removeFlame() {
    this.flame = null;
    return this;
  }

  /**
   * Resets the state of the Builder.
   * <p>
   * Used to avoid getting new Builders from the {@link FireManager manager} and instead use the same instance to build different {@link Fire Fires}.
   * <p>
   * Note that, for ease of use, the {@link #modId} is not reset.
   * 
   * @return this Builder, reset.
   */
  public FireBuilder reset() {
    fireId = null;
    damage = DEFAULT_DAMAGE;
    invertHealAndHarm = DEFAULT_INVERT_HEAL_AND_HARM;
    inFire = DEFAULT_IN_FIRE;
    onFire = DEFAULT_ON_FIRE;
    hurtSound = DEFAULT_HURT_SOUND;
    source = Optional.empty();
    campfire = Optional.empty();
    fireAspect = Optional.empty();
    flame = Optional.empty();
    return this;
  }

  /**
   * Builds a {@link Fire} instance.
   * <p>
   * If {@link #fireAspect} is not set, it will default to a new {@link FireTypedAspectEnchantment} with {@link Rarity#VERY_RARE}.
   * <p>
   * If {@link #flame} is not set, it will default to a new {@link FireTypedArrowEnchantment} with {@link Rarity#VERY_RARE}.
   * 
   * @return {@link Fire} instance.
   * @throws IllegalStateException if the either {@link #fireId} or {@link #modId} is not valid.
   */
  public Fire build() throws IllegalStateException {
    if (FireManager.isValidFireId(fireId) && FireManager.isValidModId(modId)) {
      ResourceLocation fireType = FireManager.sanitize(modId, fireId);
      if (source != null && !source.isPresent()) {
        source = Optional.of(new ResourceLocation(modId, fireId + "_fire"));
      }
      if (campfire != null && !campfire.isPresent()) {
        campfire = Optional.of(new ResourceLocation(modId, fireId + "_campfire"));
      }
      if (fireAspect != null && !fireAspect.isPresent()) {
        fireAspect = Optional.of(new FireTypedAspectEnchantment(fireType, Rarity.VERY_RARE));
      }
      if (flame != null && !flame.isPresent()) {
        flame = Optional.of(new FireTypedArrowEnchantment(fireType, Rarity.VERY_RARE));
      }
      return new Fire(fireType, damage, invertHealAndHarm, inFire, onFire, hurtSound, get(source), get(campfire), get(fireAspect), get(flame));
    }
    throw new IllegalStateException("Attempted to build a Fire with a not valid fireId or modId");
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
  private final <T> T get(@Nullable Optional<T> optional) {
    return optional == null || !optional.isPresent() ? null : optional.get();
  }
}
