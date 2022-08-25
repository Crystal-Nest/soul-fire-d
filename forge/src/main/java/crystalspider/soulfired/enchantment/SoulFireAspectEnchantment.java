package crystalspider.soulfired.enchantment;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.enchantment.FireTypedAspectEnchantment;

public class SoulFireAspectEnchantment extends FireTypedAspectEnchantment {
  public SoulFireAspectEnchantment() {
    super(FireManager.SOUL_FIRE_ID, Rarity.VERY_RARE);
  }
}
