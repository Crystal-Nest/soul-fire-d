package it.crystalnest.soul_fire_d.api.enchantment;

import net.minecraft.resources.ResourceLocation;

/**
 * Builder for {@link FireTypedFlameEnchantment}.
 */
public final class FlameBuilder extends FireEnchantmentBuilder<FireTypedFlameEnchantment> {
  /**
   * @param fireType {@link FireEnchantmentBuilder#fireType fireType}.
   */
  public FlameBuilder(ResourceLocation fireType) {
    super(fireType, "flame");
  }

  @Override
  protected FireTypedFlameEnchantment build() {
    return new FireTypedFlameEnchantment(fireType, rarity, isTreasure, isCurse, isTradeable, isDiscoverable, enabled, compatibility, duration);
  }
}
