package crystalspider.soulfired.api.enchantment;

import crystalspider.soulfired.api.FireManager;
import net.minecraft.enchantment.FlameEnchantment;
import net.minecraft.util.ResourceLocation;

public final class FlameBuilder extends FireEnchantmentBuilder<FireTypedFlameEnchantment> {
  public FlameBuilder() {
    compatibility = (enchantment) -> !(enchantment instanceof FlameEnchantment) && !FireManager.getFlames().contains(enchantment);
  }

  @Override
  protected FireTypedFlameEnchantment make(ResourceLocation fireType) {
    return new FireTypedFlameEnchantment(fireType, rarity, isTreasure, isCurse, isTradeable, isDiscoverable, enabled, compatibility);
  }
}
