package crystalspider.soulfired.api.enchantment;

import net.minecraft.util.ResourceLocation;

/**
 * Builder for {@link FireTypedFireAspectEnchantment}.
 */
public final class FireAspectBuilder extends FireEnchantmentBuilder<FireTypedFireAspectEnchantment> {
  /**
   * @param fireType {@link FireEnchantmentBuilder#fireType fireType}.
   */
  public FireAspectBuilder(ResourceLocation fireType) {
    super(fireType, "fire_aspect");
  }

  @Override
  protected final FireTypedFireAspectEnchantment build() {
    return new FireTypedFireAspectEnchantment(fireType, rarity, isTreasure, isCurse, isTradeable, isDiscoverable, enabled, compatibility, duration);
  }
}
