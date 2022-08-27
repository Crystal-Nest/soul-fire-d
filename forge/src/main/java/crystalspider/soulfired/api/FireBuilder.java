package crystalspider.soulfired.api;

import crystalspider.soulfired.api.Fire.FireEnchantData;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Builder for {@link Fire} instances.
 */
public class FireBuilder {
  /**
   * Default atlas location.
   */
  @SuppressWarnings("deprecation")
  private final static ResourceLocation BASE_ATLAS_LOCATION = TextureAtlas.LOCATION_BLOCKS;

  /**
   * {@link Fire} instance {@link Fire#id id}.
   * <p>
   * Required.
   */
  private String id;

  /**
   * {@link Fire} instance {@link Fire#damage damage}.
   * <p>
   * Optional, defaults to {@code 1.0F}.
   */
  private float damage;

  /**
   * {@link Fire} instance {@link Fire#material0 material0}.
   * <p>
   * Optional, defaults to calling {@link #setMaterial0(String)} with {@code "block/" + id + "_fire_0"}.
   */
  private Material material0;
  /**
   * {@link Fire} instance {@link Fire#material1 material1}.
   * <p>
   * Optional, defaults to calling {@link #setMaterial0(String)} with {@code "block/" + id + "_fire_1"}.
   */
  private Material material1;

  /**
   * {@link Fire} instance {@link Fire#inFire inFire}.
   * <p>
   * Optional, defaults to {@link DamageSource#IN_FIRE}.
   */
  private DamageSource inFire;
  /**
   * {@link Fire} instance {@link Fire#onFire onFire}.
   * <p>
   * Optional, defaults to {@link DamageSource#ON_FIRE}.
   */
  private DamageSource onFire;

  /**
   * {@link Fire} instance {@link Fire#hurtSound hurtSound}.
   * <p>
   * Optional, defaults to {@link SoundEvents#PLAYER_HURT_ON_FIRE}.
   * <p>
   * Default value is recommended.
   */
  private SoundEvent hurtSound;

  /**
   * {@link Fire} instance {@link Fire#blockState blockState}.
   * <p>
   * Optional, defaults to {@code Blocks.FIRE.defaultBlockState()}.
   * <p>
   * Default value is <b>not</b> recommended.
   */
  private BlockState blockState;

  private Rarity rarity;

  private boolean isTreasure;
  private boolean isCurse;
  private boolean isTradeable;
  private boolean isDiscoverable;

  FireBuilder() {
    reset();
  }

  /**
   * Sets the {@link #id}.
   * <p>
   * {@link #id} will be set only if the given {@code id} is valid. In addition, a valid {@code id} will be trimmed.
   * 
   * @param id
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setId(String id) {
    if (FireManager.isValidFireId(id)) {
      this.id = id;
    }
    return this;
  }

  /**
   * Sets the {@link #damage}.
   * <p>
   * The {@code damage} passed must be {@code >= 0}, otherwise the call to this method won't change the {@link #damage} value.
   * 
   * @param damage
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setDamage(float damage) {
    if (damage >= 0) {
      this.damage = damage;
    }
    return this;
  }

  /**
   * Sets the {@link #material0}.
   * 
   * @param sprite0
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setMaterial0(Material sprite0) {
    this.material0 = sprite0;
    return this;
  }

  /**
   * Sets the {@link #material0} by creating a new {@link Material} with the given {@link Material#atlasLocation atlasLocation} and {@link Material#texture texture}.
   * 
   * @param atlasLocation
   * @param texture
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setMaterial0(ResourceLocation atlasLocation, ResourceLocation texture) {
    return setMaterial0(new Material(atlasLocation, texture));
  }

  /**
   * Sets the {@link #material0} by creating a new {@link Material} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link Material#atlasLocation atlasLocation} and the given {@code texture} as its {@link Material#texture texture}.
   * 
   * @param texture
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setMaterial0(ResourceLocation texture) {
    return setMaterial0(BASE_ATLAS_LOCATION, texture);
  }

  /**
   * Sets the {@link #material0} by creating a new {@link Material} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link Material#atlasLocation atlasLocation} and a new {@link ResourceLocation} with the given {@code id} as its {@link Material#texture texture}.
   * 
   * @param id
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setMaterial0(String id) {
    return setMaterial0(new ResourceLocation(id));
  }

  /**
   * Sets the {@link #material1}.
   * 
   * @param sprite1
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setMaterial1(Material sprite1) {
    this.material1 = sprite1;
    return this;
  }

  /**
   * Sets the {@link #material1} by creating a new {@link Material} with the given {@link Material#atlasLocation atlasLocation} and {@link Material#texture texture}.
   * 
   * @param atlasLocation
   * @param texture
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setMaterial1(ResourceLocation atlasLocation, ResourceLocation texture) {
    return setMaterial1(new Material(atlasLocation, texture));
  }

  /**
   * Sets the {@link #material1} by creating a new {@link Material} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link Material#atlasLocation atlasLocation} and the given {@code texture} as its {@link Material#texture texture}.
   * 
   * @param texture
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setMaterial1(ResourceLocation texture) {
    return setMaterial1(BASE_ATLAS_LOCATION, texture);
  }

  /**
   * Sets the {@link #material1} by creating a new {@link Material} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link Material#atlasLocation atlasLocation} and a new {@link ResourceLocation} with the given {@code id} as its {@link Material#texture texture}.
   * 
   * @param id
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setMaterial1(String id) {
    return setMaterial1(new ResourceLocation(id));
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
   * The {@link DamageSource} will have both {@link DamageSource#bypassArmor bypassArmor} and {@link DamageSource#isFireSource isFireSource} set to {@code true}, and {@link DamageSource#exhaustion exhaustion} set to {@code 0.0F}.
   * 
   * @param messageId
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setInFire(String messageId) {
    return setInFire((new DamageSource(messageId)).bypassArmor().setIsFire());
  }

  /**
   * Sets the {@link DamageSource} {@link #inFire} by creating a new {@link DamageSource} with the {@code messageId} equal to {@code "in_" + id + "_fire"}.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   * @throws IllegalStateException if the {@link #id} is invalid (not set or blank).
   */
  public FireBuilder setInFire() throws IllegalStateException {
    if (FireManager.isValidFireId(id)) {
      return setInFire("in_" + id + "_fire");
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
   * The {@link DamageSource} will have both {@link DamageSource#bypassArmor bypassArmor} and {@link DamageSource#isFireSource isFireSource} set to {@code true}, and {@link DamageSource#exhaustion exhaustion} set to {@code 0.0F}.   * 
   * 
   * @param messageId
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setOnFire(String messageId) {
    return setOnFire((new DamageSource(messageId)).bypassArmor().setIsFire());
  }

  /**
   * Sets the {@link DamageSource} {@link #inFire} by creating a new {@link DamageSource} with the {@code messageId} equal to {@code "on_" + id + "_fire"}.
   * 
   * @return this Builder to either set other properties or {@link #build}.
   * @throws IllegalStateException if the {@link #id} is invalid (not set or blank).
   */
  public FireBuilder setOnFire() throws IllegalStateException {
    if (FireManager.isValidFireId(id)) {
      return setOnFire("on_" + id + "_fire");
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
   * Sets the {@link #blockState}.
   * <p>
   * Use this only if you need fine grained control on the BlockState associated to the Fire, otherwise prefer {@link #setSourceBlock(BaseFireBlock)}
   * 
   * @param blockState
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setSourceBlock(BlockState blockState) {
    this.blockState = blockState;
    return this;
  }

  /**
   * Sets the {@link #blockState} to the {@link Block#defaultBlockState() default BlockState} of the given {@code sourceBlock}.
   * 
   * @param sourceBlock
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setSourceBlock(Block sourceBlock) {
    return setSourceBlock(sourceBlock.defaultBlockState());
  }

  public FireBuilder setRarity(Rarity rarity) {
    this.rarity = rarity;
    return this;
  }

  public FireBuilder setTreasure(boolean isTreasure) {
    this.isTreasure = isTreasure;
    return this;
  }

  public FireBuilder setCurse(boolean isCurse) {
    this.isCurse = isCurse;
    return this;
  }

  public FireBuilder setTradeable(boolean isTradeable) {
    this.isTradeable = isTradeable;
    return this;
  }

  public FireBuilder setDiscoverable(boolean isDiscoverable) {
    this.isDiscoverable = isDiscoverable;
    return this;
  }

  /**
   * Resets the state of the Builder.
   * <p>
   * Used to avoid getting new Builders from the {@link FireManager manager} and instead use the same instance to build different {@link Fire Fires}.
   * 
   * @return this Builder, reset.
   */
  public FireBuilder reset() {
    id = null;
    damage = 1.0F;
    material0 = null;
    material1 = null;
    inFire = DamageSource.IN_FIRE;
    onFire = DamageSource.ON_FIRE;
    hurtSound = SoundEvents.PLAYER_HURT_ON_FIRE;
    blockState = Blocks.FIRE.defaultBlockState();
    rarity = Rarity.VERY_RARE;
    isTreasure = true;
    isCurse = false;
    isTradeable = true;
    isDiscoverable = true;
    return this;
  }

  /**
   * Build a {@link Fire} instance.
   * <p>
   * If {@link #material0} is not set, it will default to a new {@link Material} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link Material#atlasLocation atlasLocation} and {@code "block/" + id + "_fire_0"} as its {@link Material#texture texture}.
   * <p>
   * If {@link #material1} is not set, it will default to a new {@link Material} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link Material#atlasLocation atlasLocation} and {@code "block/" + id + "_fire_1"} as its {@link Material#texture texture}.
   * 
   * @return {@link Fire} instance.
   * @throws IllegalStateException if the {@link #id} is invalid (not set or blank).
   */
  public Fire build() throws IllegalStateException {
    if (FireManager.isValidFireId(id)) {
      if (material0 == null) {
        setMaterial0("block/" + id + "_fire_0");
      }
      if (material1 == null) {
        setMaterial1("block/" + id + "_fire_1");
      }
      return new Fire(FireManager.sanitizeFireId(id), damage, material0, material1, inFire, onFire, hurtSound, blockState, new FireEnchantData(rarity, isTreasure, isCurse, isTradeable, isDiscoverable));
    }
    throw new IllegalStateException("Attempted to build a Fire with a non-valid id");
  }
}
