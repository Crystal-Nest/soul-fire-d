package crystalspider.soulfired.api;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvent;

/**
 * Fire
 */
public class Fire {
  /**
   * Mod ID.
   */
  private final String modId;
  /**
   * Fire unique ID.
   */
  private final String id;

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
   * {@link BlockState} of the Fire Block considered as the source for this Fire.
   */
  private final BlockState sourceBlock;

  /**
   * Fire Aspect {@link Enchantment}.
   */
  private final Enchantment fireAspect;
  /**
   * Flame {@link Enchantment}.
   */
  private final Enchantment flame;

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
   * @param sourceBlock {@link #sourceBlock}.
   * @param fireAspect {@link #fireAspect}.
   * @param flame {@link #flame}.
   */
  Fire(String modId, String id, float damage, boolean invertHealAndHarm, DamageSource inFire, DamageSource onFire, SoundEvent hurtSound, BlockState sourceBlock, Enchantment fireAspect, Enchantment flame) {
    this.modId = modId;
    this.id = id;
    this.damage = damage;
    this.invertHealAndHarm = invertHealAndHarm;
    this.inFire = inFire;
    this.onFire = onFire;
    this.hurtSound = hurtSound;
    this.sourceBlock = sourceBlock;
    this.fireAspect = fireAspect;
    this.flame = flame;
  }

  /**
   * Returns this {@link #modId}.
   * 
   * @return this {@link #modId}.
   */
  public String getModId() {
    return modId;
  }

  /**
   * Returns this {@link #id}.
   * 
   * @return this {@link #id}.
   */
  public String getId() {
    return id;
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
   * Returns this {@link #sourceBlock}.
   * 
   * @return this {@link #sourceBlock}.
   */
  public BlockState getSourceBlock() {
    return sourceBlock;
  }

  /**
   * Returns this {@link #fireAspect}.
   * 
   * @return this {@link #fireAspect}.
   */
  public Enchantment getFireAspect() {
    return fireAspect;
  }

  /**
   * Returns this {@link #flame}.
   * 
   * @return this {@link #flame}.
   */
  public Enchantment getFlame() {
    return flame;
  }

  @Override
  public String toString() {
    return "Fire [id=" + id + ", damage=" + damage + ", invertedHealAndHarm=" + invertHealAndHarm + ", inFire=" + inFire + ", onFire=" + onFire + ", hurtSound=" + hurtSound + ", blockState=" + sourceBlock + "]";
  }
}
