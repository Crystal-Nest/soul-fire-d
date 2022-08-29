package crystalspider.soulfired.api.enchantment;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.FlameEnchantment;
import net.minecraft.entity.EquipmentSlot;


/**
 * Flame Enchantment sensitive to the fire type.
 */
public class FireTypedArrowEnchantment extends FlameEnchantment implements FireTyped {
  /**
   * Mod Id.
   */
  private final String modId;
  /**
   * Fire Id.
   */
  private final String fireId;

  private final boolean isTreasure;
  private final boolean isCurse;
  private final boolean isTradeable;
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
  }

  /**
   * @param modId
   * @param fireId
   * @param rarity
   */
  public FireTypedArrowEnchantment(String modId, String fireId, Rarity rarity) {
    this(modId, fireId, rarity, true, false, true, true);
  }

  @Override
  public final boolean canAccept(Enchantment enchantment) {
    return super.canAccept(enchantment) && !(enchantment instanceof FlameEnchantment) && !FireManager.getFlames().contains(enchantment);
  }
  
  @Override
  public final boolean isTreasure() {
    return isTreasure;
  }

  @Override
  public final boolean isCursed() {
    return isCurse;
  }

  @Override
  public final boolean isAvailableForEnchantedBookOffer() {
    return isTradeable;
  }

  @Override
  public final boolean isAvailableForRandomSelection() {
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