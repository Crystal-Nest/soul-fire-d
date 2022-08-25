package crystalspider.soulfired.api;

import net.minecraft.client.resources.model.Material;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
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
  private final BlockState blockState;

  /**
   * @param id {@link #id}.
   * @param damage {@link #damage}.
   * @param material0 {@link #material0}.
   * @param material1 {@link #material1}.
   * @param inFire {@link #inFire}.
   * @param onFire {@link #onFire}.
   * @param hurtSound {@link #hurtSound}.
   * @param blockState {@link #blockState}.
   */
  Fire(String id, float damage, Material material0, Material material1, DamageSource inFire, DamageSource onFire, SoundEvent hurtSound, BlockState blockState) {
    this.id = id;
    this.damage = damage;
    this.material0 = material0;
    this.material1 = material1;
    this.inFire = inFire;
    this.onFire = onFire;
    this.hurtSound = hurtSound;
    this.blockState = blockState;
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
   * Returns this {@link #blockState}.
   * 
   * @return this {@link #blockState}.
   */
  public BlockState getBlockState() {
    return blockState;
  }

  @Override
  public String toString() {
    return "Fire [id=" + id + ", damage=" + damage + ", material0=" + material0 + ", material1=" + material1 + ", inFire=" + inFire + ", onFire=" + onFire + ", hurtSound=" + hurtSound + ", blockState=" + blockState + "]";
  }
}
