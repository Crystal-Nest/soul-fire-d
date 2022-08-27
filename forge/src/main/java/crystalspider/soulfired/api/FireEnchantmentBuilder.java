package crystalspider.soulfired.api;

import crystalspider.soulfired.api.Fire.FireEnchantData;
import crystalspider.soulfired.api.enchantment.FireTypedArrowEnchantment;
import crystalspider.soulfired.api.enchantment.FireTypedAspectEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class FireEnchantmentBuilder {
  private final String modId;
  private String fireId;

  private Rarity rarity;

  private boolean isTreasure;
  private boolean isCurse;
  private boolean isTradeable;
  private boolean isDiscoverable;

  FireEnchantmentBuilder(String modId) {
    this.modId = modId;
    reset();
  }

  FireEnchantmentBuilder(String modId, String fireId) {
    this.modId = modId;
    reset(fireId);
  }

  FireEnchantmentBuilder(String modId, Fire fire) {
    this.modId = modId;
    reset(fire);
  }

  /**
   * Resets the state of the Builder.
   * <p>
   * Used to avoid getting new Builders from the {@link FireManager manager} and instead use the same instance to build different Fire Enchantments.
   * <p>
   * To build a pair of Fire Enchantments (Fire Aspect & Flame) resetting is not necessary if the same context can be reused.
   * 
   * @return this Builder, reset.
   */
  public FireEnchantmentBuilder reset() {
    fireId = null;
    rarity = null;
    isTreasure = true;
    isCurse = false;
    isTradeable = true;
    isDiscoverable = true;
    return this;
  }

  /**
   * Resets the state of the Builder and sets the {@link #fireId}.
   * <p>
   * Used to avoid getting new Builders from the {@link FireManager manager} and instead use the same instance to build different Fire Enchantments.
   * <p>
   * To build a pair of Fire Enchantments (Fire Aspect & Flame) resetting is not necessary if the same context can be reused.
   * 
   * @return this Builder, reset.
   */
  public FireEnchantmentBuilder reset(String fireId) {
    setId(fireId);
    rarity = null;
    isTreasure = true;
    isCurse = false;
    isTradeable = true;
    isDiscoverable = true;
    return this;
  }

  /**
   * Resets the state of the Builder and all data from the given {@link Fire}.
   * <p>
   * Used to avoid getting new Builders from the {@link FireManager manager} and instead use the same instance to build different Fire Enchantments.
   * <p>
   * To build a pair of Fire Enchantments (Fire Aspect & Flame) resetting is not necessary if the same context can be reused.
   * 
   * @return this Builder, reset.
   */
  public FireEnchantmentBuilder reset(Fire fire) {
    setId(fire.getId());
    FireEnchantData enchantData = fire.getEnchantData();
    rarity = enchantData.getRarity();
    isTreasure = enchantData.isTreasure();
    isCurse = enchantData.isCurse();
    isTradeable = enchantData.isTradeable();
    isDiscoverable = enchantData.isDiscoverable();
    return this;
  }

  public FireEnchantmentBuilder setId(String fireId) {
    if (FireManager.isValidFireId(fireId)) {
      this.fireId = fireId;
    }
    return this;
  }

  public FireEnchantmentBuilder setRarity(Rarity rarity) {
    this.rarity = rarity;
    return this;
  }

  public FireEnchantmentBuilder setTreasure(boolean isTreasure) {
    this.isTreasure = isTreasure;
    return this;
  }

  public FireEnchantmentBuilder setCurse(boolean isCurse) {
    this.isCurse = isCurse;
    return this;
  }

  public FireEnchantmentBuilder setTradeable(boolean isTradeable) {
    this.isTradeable = isTradeable;
    return this;
  }

  public FireEnchantmentBuilder setDiscoverable(boolean isDiscoverable) {
    this.isDiscoverable = isDiscoverable;
    return this;
  }

  public FireTypedAspectEnchantment buildFireAspect() {
    if (FireManager.isValidFireId(fireId)) {
      if (rarity != null) {
        return buildEnchantment(FireTypedAspectEnchantment::new, "fire_aspect");
      }
      throw new IllegalStateException("Attempted to build a Fire Enchantment with a non-valid rarity");
    }
    throw new IllegalStateException("Attempted to build a Fire Enchantment with a non-valid id");
  }

  public FireTypedArrowEnchantment buildFlame() {
    if (FireManager.isValidFireId(fireId)) {
      if (rarity != null) {
        return buildEnchantment(FireTypedArrowEnchantment::new, "flame");
      }
      throw new IllegalStateException("Attempted to build a Fire Enchantment with a non-valid rarity");
    }
    throw new IllegalStateException("Attempted to build a Fire Enchantment with a non-valid id");
  }

  @SuppressWarnings("unchecked")
  private <T extends Enchantment> T buildEnchantment(FireEnchantmentConstructor<T> enchantmentConstructor, String name) {
    return (T) enchantmentConstructor.apply(fireId, rarity, isTreasure, isCurse, isTradeable, isDiscoverable).setRegistryName(modId, fireId + "_" + name);
  }

  private static interface FireEnchantmentConstructor<T extends Enchantment> {
    public abstract T apply(String fireId, Rarity rarity, boolean isTreasure, boolean isCurse, boolean isTradeable, boolean isDiscoverable);
  }
}
