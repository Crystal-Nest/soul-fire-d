package crystalspider.soulfired.api.enchantment;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.ArrowFireEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Flame Enchantment sensitive to the fire type.
 */
public class FireTypedArrowEnchantment extends ArrowFireEnchantment implements FireTyped {
  /**
   * Mod Id.
   */
  private final String modId;
  /**
   * Fire Id.
   */
  private final String fireId;

  /**
   * Whether the enchantment is treasure only.
   */
  private final boolean isTreasure;
  /**
   * Whether the enchantment is a curse.
   */
  private final boolean isCurse;
  /**
   * Whether the enchantment can appear in the enchanted book trade offers of librarian villagers.
   */
  private final boolean isTradeable;
  /**
   * Whether the enchantment will appear in the enchanting table or loots with random enchant function.
   * <p>
   * Note that {@link #isTreasure} takes precedence.
   */
  private final boolean isDiscoverable;

  /**
   * @param modId
   * @param fireId
   * @param rarity
   * @param isTreasure
   * @param isCurse
   * @param isTradeable
   * @param isDiscoverable
   */
  public FireTypedArrowEnchantment(String modId, String fireId, Rarity rarity, boolean isTreasure, boolean isCurse, boolean isTradeable, boolean isDiscoverable) {
    super(rarity, EquipmentSlot.MAINHAND);
    this.modId = modId;
    this.fireId = FireManager.sanitizeFireId(fireId);
    this.isTreasure = isTreasure;
    this.isCurse = isCurse;
    this.isTradeable = isTradeable;
    this.isDiscoverable = isDiscoverable;
    setRegistryName(this.modId, this.fireId + "_flame");
  }

  /**
   * @param modId
   * @param fireId
   * @param rarity
   */
  public FireTypedArrowEnchantment(String modId, String fireId, Rarity rarity) {
    this(modId, fireId, rarity, false, false, true, true);
  }

  @Override
  public final boolean checkCompatibility(Enchantment enchantment) {
    return super.checkCompatibility(enchantment) && !(enchantment instanceof ArrowFireEnchantment) && !FireManager.getFlames().contains(enchantment);
  }
  
  @Override
  public final boolean isTreasureOnly() {
    return isTreasure;
  }

  @Override
  public final boolean isCurse() {
    return isCurse;
  }

  @Override
  public final boolean isTradeable() {
    return isTradeable;
  }

  @Override
  public final boolean isDiscoverable() {
    return isDiscoverable;
  }

  @Override
  public final String getFireId() {
    return fireId;
  }

  /**
   * Returns this {@link #modId}.
   * 
   * @return this {@link #modId}.
   */
  public final String getModId() {
    return modId;
  }
}