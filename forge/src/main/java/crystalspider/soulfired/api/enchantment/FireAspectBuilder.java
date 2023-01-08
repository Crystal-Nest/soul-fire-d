package crystalspider.soulfired.api.enchantment;

import crystalspider.soulfired.api.FireManager;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.util.ResourceLocation;

public final class FireAspectBuilder extends FireEnchantmentBuilder<FireTypedFireAspectEnchantment> {
  public FireAspectBuilder() {
    compatibility = (enchantment) -> !(enchantment instanceof FireAspectEnchantment) && !FireManager.getFireAspects().contains(enchantment);
  }

  @Override
  protected FireTypedFireAspectEnchantment make(ResourceLocation fireType) {
    return new FireTypedFireAspectEnchantment(fireType, rarity, isTreasure, isCurse, isTradeable, isDiscoverable, enabled, compatibility);
  }
}
