package crystalspider.soulfired.api.enchantment;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.ArrowFireEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

public class FireTypedArrowEnchantment extends ArrowFireEnchantment implements FireTyped {
  private final String fireId;

  private final boolean isTreasure;
  private final boolean isCurse;
  private final boolean isTradeable;
  private final boolean isDiscoverable;

  public FireTypedArrowEnchantment(String fireId, Rarity rarity, boolean isTreasure, boolean isCurse, boolean isTradeable, boolean isDiscoverable) {
    super(rarity, EquipmentSlot.MAINHAND);
    this.fireId = FireManager.sanitizeFireId(fireId);
    this.isTreasure = isTreasure;    
    this.isCurse = isCurse;
    this.isTradeable = isTradeable;
    this.isDiscoverable = isDiscoverable;
  }

  public FireTypedArrowEnchantment(String fireId, Rarity rarity) {
    this(fireId, rarity, true, false, true, true);
  }

  @Override
  public final boolean checkCompatibility(Enchantment enchantment) {
    return super.checkCompatibility(enchantment) && !(enchantment instanceof ArrowFireEnchantment);
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
}