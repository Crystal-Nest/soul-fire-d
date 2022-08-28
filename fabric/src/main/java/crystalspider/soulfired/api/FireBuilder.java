package crystalspider.soulfired.api;

import crystalspider.soulfired.api.enchantment.FireTypedArrowEnchantment;
import crystalspider.soulfired.api.enchantment.FireTypedAspectEnchantment;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

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
  public static final SoundEvent DEFAULT_HURT_SOUND = SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE;
  /**
   * Default value for {@link #blockState}.
   */
  public static final BlockState DEFAULT_BLOCKSTATE = Blocks.FIRE.getDefaultState();

  /**
   * Default atlas location.
   */
  @SuppressWarnings("deprecation")
  private static final Identifier BASE_ATLAS_LOCATION = SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;

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
   * {@link Fire} instance {@link Fire#invertHealAndHarm invertedHealAndHarm}.
   * <p>
   * Optional, defaults to {@code false}.
   */
  private boolean invertHealAndHarm;

  /**
   * {@link Fire} instance {@link Fire#spriteIdentifier0 spriteIdentifier0}.
   * <p>
   * Optional, defaults to calling {@link #setSpriteIdentifier0(String)} with {@code "block/" + id + "_fire_0"}.
   */
  private SpriteIdentifier spriteIdentifier0;
  /**
   * {@link Fire} instance {@link Fire#spriteIdentifier1 spriteIdentifier1}.
   * <p>
   * Optional, defaults to calling {@link #setSpriteIdentifier0(String)} with {@code "block/" + id + "_fire_1"}.
   */
  private SpriteIdentifier spriteIdentifier1;

  /**
   * {@link Fire} instance {@link Fire#inFire inFire}.
   * <p>
   * Optional, defaults to {@link DamageSource#IN_FIRE}.
   * <p>
   * If changed from the default, remember to add translations for the new death messages!
   */
  private DamageSource inFire;
  /**
   * {@link Fire} instance {@link Fire#onFire onFire}.
   * <p>
   * Optional, defaults to {@link DamageSource#ON_FIRE}.
   * <p>
   * If changed from the default, remember to add translations for the new death messages!
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

  /**
   * {@link Fire} instance {@link Fire#fireAspect fireAspect}.
   * <p>
   * Optional, defaults to a new {@link FireTypedAspectEnchantment} with {@link Rarity#VERY_RARE}.
   */
  private Enchantment fireAspect;
  /**
   * {@link Fire} instance {@link Fire#flame flame}.
   * <p>
   * Optional, defaults to a new {@link FireTypedArrowEnchantment} with {@link Rarity#VERY_RARE}.
   */
  private Enchantment flame;

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
   * Sets the {@link #spriteIdentifier0}.
   * 
   * @param spriteIdentifier0
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setSpriteIdentifier0(SpriteIdentifier spriteIdentifier0) {
    this.spriteIdentifier0 = spriteIdentifier0;
    return this;
  }

  /**
   * Sets the {@link #spriteIdentifier0} by creating a new {@link SpriteIdentifier} with the given {@link SpriteIdentifier#atlasLocation atlasLocation} and {@link SpriteIdentifier#texture texture}.
   * 
   * @param atlasLocation
   * @param texture
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setSpriteIdentifier0(Identifier atlasLocation, Identifier texture) {
    return setSpriteIdentifier0(new SpriteIdentifier(atlasLocation, texture));
  }

  /**
   * Sets the {@link #spriteIdentifier0} by creating a new {@link SpriteIdentifier} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link SpriteIdentifier#atlasLocation atlasLocation} and the given {@code texture} as its {@link SpriteIdentifier#texture texture}.
   * 
   * @param texture
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setSpriteIdentifier0(Identifier texture) {
    return setSpriteIdentifier0(BASE_ATLAS_LOCATION, texture);
  }

  /**
   * Sets the {@link #spriteIdentifier0} by creating a new {@link SpriteIdentifier} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link SpriteIdentifier#atlasLocation atlasLocation} and a new {@link Identifier} with the given {@code id} as its {@link SpriteIdentifier#texture texture}.
   * 
   * @param id
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setSpriteIdentifier0(String id) {
    return setSpriteIdentifier0(new Identifier(id));
  }

  /**
   * Sets the {@link #spriteIdentifier1}.
   * 
   * @param spriteIdentifier1
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setSpriteIdentifier1(SpriteIdentifier spriteIdentifier1) {
    this.spriteIdentifier1 = spriteIdentifier1;
    return this;
  }

  /**
   * Sets the {@link #spriteIdentifier1} by creating a new {@link SpriteIdentifier} with the given {@link SpriteIdentifier#atlasLocation atlasLocation} and {@link SpriteIdentifier#texture texture}.
   * 
   * @param atlasLocation
   * @param texture
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setSpriteIdentifier1(Identifier atlasLocation, Identifier texture) {
    return setSpriteIdentifier1(new SpriteIdentifier(atlasLocation, texture));
  }

  /**
   * Sets the {@link #spriteIdentifier1} by creating a new {@link SpriteIdentifier} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link SpriteIdentifier#atlasLocation atlasLocation} and the given {@code texture} as its {@link SpriteIdentifier#texture texture}.
   * 
   * @param texture
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setSpriteIdentifier1(Identifier texture) {
    return setSpriteIdentifier1(BASE_ATLAS_LOCATION, texture);
  }

  /**
   * Sets the {@link #spriteIdentifier1} by creating a new {@link SpriteIdentifier} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link SpriteIdentifier#atlasLocation atlasLocation} and a new {@link Identifier} with the given {@code id} as its {@link SpriteIdentifier#texture texture}.
   * 
   * @param id
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setSpriteIdentifier1(String id) {
    return setSpriteIdentifier1(new Identifier(id));
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
   * Sets the {@link DamageSource} {@link #inFire} by creating a new {@link DamageSource} with the given {@code name}.
   * <p>
   * The {@link DamageSource} will have both {@link DamageSource#setBypassesArmor setBypassesArmor} and {@link DamageSource#isFireSource isFireSource} set to {@code true}, and {@link DamageSource#exhaustion exhaustion} set to {@code 0.0F}.
   * 
   * @param name
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setInFire(String name) {
    return setInFire((new FireDamageSource(name)).setBypassesArmor().setFire());
  }

  /**
   * Sets the {@link DamageSource} {@link #inFire} by creating a new {@link DamageSource} with the {@code name} equal to {@code "in_" + id + "_fire"}.
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
   * Sets the {@link DamageSource} {@link #onFire} by creating a new {@link DamageSource} with the given {@code name}.
   * <p>
   * The {@link DamageSource} will have both {@link DamageSource#setBypassesArmor setBypassesArmor} and {@link DamageSource#isFireSource isFireSource} set to {@code true}, and {@link DamageSource#exhaustion exhaustion} set to {@code 0.0F}.   * 
   * 
   * @param name
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setOnFire(String name) {
    return setOnFire((new FireDamageSource(name)).setBypassesArmor().setFire());
  }

  /**
   * Sets the {@link DamageSource} {@link #inFire} by creating a new {@link DamageSource} with the {@code name} equal to {@code "on_" + id + "_fire"}.
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
    return setSourceBlock(sourceBlock.getDefaultState());
  }

  /**
   * Sets the {@link #fireAspect} enchantment.
   * <p>
   * The {@link Enchantment} passed as parameter MUST implement the {@link FireTyped} interface, otherwise the value will not be set.
   * 
   * @param fireAspect
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setFireAspect(Enchantment fireAspect) {
    if (flame instanceof FireTyped) {
      this.fireAspect = fireAspect;
    }
    return this;
  }

  /**
   * Sets the {@link #fireAspect} enchantment.
   * <p>
   * The {@link Enchantment} passed as parameter MUST implement the {@link FireTyped} interface, otherwise the value will not be set.
   * 
   * @param fireAspect
   * @return this Builder to either set other properties or {@link #build}.
   */
  public FireBuilder setFlame(Enchantment flame) {
    if (flame instanceof FireTyped) {
      this.flame = flame;
    }
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
    damage = DEFAULT_DAMAGE;
    invertHealAndHarm = DEFAULT_INVERT_HEAL_AND_HARM;
    spriteIdentifier0 = null;
    spriteIdentifier1 = null;
    inFire = DEFAULT_IN_FIRE;
    onFire = DEFAULT_ON_FIRE;
    hurtSound = DEFAULT_HURT_SOUND;
    blockState = DEFAULT_BLOCKSTATE;
    fireAspect = null;
    flame = null;
    return this;
  }

  /**
   * Build a {@link Fire} instance.
   * <p>
   * If {@link #spriteIdentifier0} is not set, it will default to a new {@link SpriteIdentifier} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link SpriteIdentifier#atlas atlas} and {@code "block/" + id + "_fire_0"} as its {@link SpriteIdentifier#texture texture}.
   * <p>
   * If {@link #spriteIdentifier1} is not set, it will default to a new {@link SpriteIdentifier} with {@link #BASE_ATLAS_LOCATION BlockLocation} as its {@link SpriteIdentifier#atlas atlas} and {@code "block/" + id + "_fire_1"} as its {@link SpriteIdentifier#texture texture}.
   * 
   * @return {@link Fire} instance.
   * @throws IllegalStateException if the {@link #id} is invalid (not set or blank).
   */
  public Fire build() throws IllegalStateException {
    if (FireManager.isValidFireId(id)) {
      if (spriteIdentifier0 == null) {
        setSpriteIdentifier0("block/" + id + "_fire_0");
      }
      if (spriteIdentifier1 == null) {
        setSpriteIdentifier1("block/" + id + "_fire_1");
      }
      if (fireAspect == null) {
        fireAspect = new FireTypedAspectEnchantment(id, Rarity.VERY_RARE);
      }
      if (flame == null) {
        flame = new FireTypedArrowEnchantment(id, Rarity.VERY_RARE);
      }
      return new Fire(FireManager.sanitizeFireId(id), damage, invertHealAndHarm, spriteIdentifier0, spriteIdentifier1, inFire, onFire, hurtSound, blockState, fireAspect, flame);
    }
    throw new IllegalStateException("Attempted to build a Fire with a non-valid id");
  }
}
