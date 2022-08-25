package crystalspider.soulfired.api.enchantment;

import crystalspider.soulfired.api.FireManager;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class FireEnchantmentBuilder {
  private String fireId;

  private Rarity rarity;

  private boolean isTreasure;
  private boolean isCurse;
  private boolean isTradeable;
  private boolean isDiscoverable;

  public FireEnchantmentBuilder() {
    reset();
  }

  public FireEnchantmentBuilder(String fireId) {
    reset();
    this.fireId = FireManager.sanitizeFireId(fireId);
  }

  public FireEnchantmentBuilder(String fireId, Rarity rarity) {
    reset();
    this.fireId = FireManager.sanitizeFireId(fireId);
    this.rarity = rarity;
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
        return new FireTypedAspectEnchantment(fireId, rarity, isTreasure, isCurse, isTradeable, isDiscoverable);
      }
      throw new IllegalStateException("Attempted to build a Fire Enchantment with a non-valid rarity");
    }
    throw new IllegalStateException("Attempted to build a Fire Enchantment with a non-valid id");
  }

  public FireTypedArrowEnchantment buildFlame() {
    if (FireManager.isValidFireId(fireId)) {
      if (rarity != null) {
        return new FireTypedArrowEnchantment(fireId, rarity, isTreasure, isCurse, isTradeable, isDiscoverable);
      }
      throw new IllegalStateException("Attempted to build a Fire Enchantment with a non-valid rarity");
    }
    throw new IllegalStateException("Attempted to build a Fire Enchantment with a non-valid id");
  }
}
