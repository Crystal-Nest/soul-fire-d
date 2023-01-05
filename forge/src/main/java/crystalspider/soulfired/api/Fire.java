package crystalspider.soulfired.api;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.CampfireBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * Fire
 */
public class Fire {
  /**
   * {@link ResourceLocation} to uniquely identify this Fire.
   */
  private final ResourceLocation fireType;

  /**
   * Fire damage per second.
   */
  private final float damage;

  /**
   * Whether to invert harm and heal for mobs that have potion effects inverted (e.g. undeads).
   */
  private final boolean invertHealAndHarm;

  /**
   * Fire {@link DamageSource} for when the entity is in or on a block providing fire.
   */
  private final DamageSource inFire;
  /**
   * Fire {@link DamageSource} for when the entity is burning.
   */
  private final DamageSource onFire;

  /**
   * Fire {@link SoundEvent} to use when the player is taking fire damage.
   */
  private final SoundEvent hurtSound;

  /**
   * {@link Optional} {@link ResourceLocation} of the {@link Block} of the Fire Block considered as the source for this Fire.
   */
  private final Optional<ResourceLocation> source;
  /**
   * {@link Optional} {@link ResourceLocation} of the {@link CampfireBlock} associated with this Fire.
   */
  private final Optional<ResourceLocation> campfire;

  /**
   * {@link Optional} Fire Aspect {@link Enchantment}.
   */
  private final Optional<Enchantment> fireAspect;
  /**
   * {@link Optional} Flame {@link Enchantment}.
   */
  private final Optional<Enchantment> flame;

  /**
   * @param modId {@link #modId}.
   * @param id {@link #id}.
   * @param damage {@link #damage}.
   * @param invertHealAndHarm {@link #invertHealAndHarm}.
   * @param material0 {@link #material0}.
   * @param material1 {@link #material1}.
   * @param inFire {@link #inFire}.
   * @param onFire {@link #onFire}.
   * @param hurtSound {@link #hurtSound}.
   * @param source {@link #source}.
   * @param campfire {@link #campfire}.
   * @param fireAspect {@link #fireAspect}.
   * @param flame {@link #flame}.
   */
  Fire(ResourceLocation fireType, float damage, boolean invertHealAndHarm, DamageSource inFire, DamageSource onFire, SoundEvent hurtSound, ResourceLocation source, ResourceLocation campfire, Enchantment fireAspect, Enchantment flame) {
    this.fireType = fireType;
    this.damage = damage;
    this.invertHealAndHarm = invertHealAndHarm;
    this.inFire = inFire;
    this.onFire = onFire;
    this.hurtSound = hurtSound;
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
   * Returns this {@link #inFire}.
   * 
   * @return this {@link #inFire}.
   */
  public DamageSource getInFire() {
    return inFire;
  }

  /**
   * Returns this {@link #onFire}.
   * 
   * @return this {@link #onFire}.
   */
  public DamageSource getOnFire() {
    return onFire;
  }

  /**
   * Returns this {@link #hurtSound}.
   * 
   * @return this {@link #hurtSound}.
   */
  public SoundEvent getHurtSound() {
    return hurtSound;
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
  public Optional<Enchantment> getFireAspect() {
    return fireAspect;
  }

  /**
   * Returns this {@link #flame}.
   * 
   * @return this {@link #flame}.
   */
  public Optional<Enchantment> getFlame() {
    return flame;
  }

  @Override
  public String toString() {
    return "Fire [fireType=" + fireType + ", damage=" + damage + ", invertedHealAndHarm=" + invertHealAndHarm + ", inFire=" + inFire + ", onFire=" + onFire + ", hurtSound=" + hurtSound + ", source=" + source + ", campfire=" + campfire + "]";
  }
}
