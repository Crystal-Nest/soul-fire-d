package crystalspider.soulfired.api;

import net.minecraft.client.resources.model.Material;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
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

  private final FireEnchantData fireEnchantData;

  /**
   * @param id {@link #id}.
   * @param damage {@link #damage}.
   * @param material0 {@link #material0}.
   * @param material1 {@link #material1}.
   * @param inFire {@link #inFire}.
   * @param onFire {@link #onFire}.
   * @param hurtSound {@link #hurtSound}.
   * @param blockState {@link #blockState}.
   * @param fireEnchantData {@link #fireEnchantData}.
   */
  Fire(String id, float damage, Material material0, Material material1, DamageSource inFire, DamageSource onFire, SoundEvent hurtSound, BlockState blockState, FireEnchantData fireEnchantData) {
    this.id = id;
    this.damage = damage;
    this.material0 = material0;
    this.material1 = material1;
    this.inFire = inFire;
    this.onFire = onFire;
    this.hurtSound = hurtSound;
    this.blockState = blockState;
    this.fireEnchantData = fireEnchantData;
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

  /**
   * Returns this {@link #fireEnchantData}.
   * 
   * @return this {@link #fireEnchantData}.
   */
  public FireEnchantData getEnchantData() {
    return fireEnchantData;
  }

  @Override
  public String toString() {
    return "Fire [id=" + id + ", damage=" + damage + ", material0=" + material0 + ", material1=" + material1 + ", inFire=" + inFire + ", onFire=" + onFire + ", hurtSound=" + hurtSound + ", blockState=" + blockState + ", enchantData=" + fireEnchantData + "]";
  }

  static class FireEnchantData {
    final Rarity rarity;
  
    final boolean isTreasure;
    final boolean isCurse;
    final boolean isTradeable;
    final boolean isDiscoverable;
  
    public FireEnchantData(Rarity rarity, boolean isTreasure, boolean isCurse, boolean isTradeable, boolean isDiscoverable) {
      this.rarity = rarity;
      this.isTreasure = isTreasure;    
      this.isCurse = isCurse;
      this.isTradeable = isTradeable;
      this.isDiscoverable = isDiscoverable;
    }
  
    public FireEnchantData(Rarity rarity) {
      this(rarity, true, false, true, true);
    }

    public Rarity getRarity() {
      return rarity;
    }

    public boolean isTreasure() {
      return isTreasure;
    }

    public boolean isCurse() {
      return isCurse;
    }

    public boolean isTradeable() {
      return isTradeable;
    }

    public boolean isDiscoverable() {
      return isDiscoverable;
    }

    @Override
    public String toString() {
      return "FireEnchantData [rarity=" + rarity + ", isCurse=" + isCurse + ", isDiscoverable=" + isDiscoverable + ", isTradeable=" + isTradeable + ", isTreasure=" + isTreasure + "]";
    }    
  }
}
