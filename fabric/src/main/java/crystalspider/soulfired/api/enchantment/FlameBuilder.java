package crystalspider.soulfired.api.enchantment;

import net.minecraft.util.Identifier;

/**
 * Builder for {@link FireTypedFlameEnchantment}.
 */
public final class FlameBuilder extends FireEnchantmentBuilder<FireTypedFlameEnchantment> {
  /**
   * @param fireType {@link FireEnchantmentBuilder#fireType fireType}.
   */
  public FlameBuilder(Identifier fireType) {
    super(fireType);
  }

  @Override
  public final FireTypedFlameEnchantment build() {
    return new FireTypedFlameEnchantment(fireType, rarity, isTreasure, isCurse, isTradeable, isDiscoverable, enabled, compatibility, duration);
  }
}
