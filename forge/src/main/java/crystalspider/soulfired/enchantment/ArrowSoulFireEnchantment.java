package crystalspider.soulfired.enchantment;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.enchantment.FireTypedArrowEnchantment;

public class ArrowSoulFireEnchantment extends FireTypedArrowEnchantment {
  public ArrowSoulFireEnchantment() {
    super(FireManager.SOUL_FIRE_ID, Rarity.VERY_RARE);
  }
}
