package crystalspider.soulfired.api;

import net.minecraft.client.resources.model.Material;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fire
 */
public class Fire {
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
   * Fire {@link Material} for the sprite0.
   * <p>
   * Used only in rendering the Fire of an entity.
   */
  private final Material material0;
  /**
   * Fire {@link Material} for the sprite1.
   * <p>
   * Used both for rendering the Fire of an entity and the player overlay.
   */
  private final Material material1;

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
  Fire(String id, float damage, boolean invertHealAndHarm, Material material0, Material material1, DamageSource inFire, DamageSource onFire, SoundEvent hurtSound, BlockState sourceBlock, Enchantment fireAspect, Enchantment flame) {
    this.id = id;
    this.damage = damage;
    this.invertHealAndHarm = invertHealAndHarm;
    this.material0 = material0;
    this.material1 = material1;
    this.inFire = inFire;
    this.onFire = onFire;
    this.hurtSound = hurtSound;
    this.sourceBlock = sourceBlock;
    this.fireAspect = fireAspect;
    this.flame = flame;
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
   * Returns this {@link #material0}.
   * 
   * @return this {@link #material0}.
   */
  public Material getMaterial0() {
    return material0;
  }

  /**
   * Returns this {@link #material1}.
   * 
   * @return this {@link #material1}.
   */
  public Material getMaterial1() {
    return material1;
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
    return "Fire [id=" + id + ", damage=" + damage + ", invertedHealAndHarm=" + invertHealAndHarm + ", material0=" + material0 + ", material1=" + material1 + ", inFire=" + inFire + ", onFire=" + onFire + ", hurtSound=" + hurtSound + ", blockState=" + sourceBlock + "]";
  }
}
