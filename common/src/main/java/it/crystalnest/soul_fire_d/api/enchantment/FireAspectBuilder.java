package it.crystalnest.soul_fire_d.api.enchantment;

import net.minecraft.resources.ResourceLocation;

/**
 * Builder for {@link FireTypedFireAspectEnchantment}.
 */
public final class FireAspectBuilder extends FireEnchantmentBuilder<FireTypedFireAspectEnchantment> {
  /**
   * @param fireType {@link #fireType}.
   */
  public FireAspectBuilder(ResourceLocation fireType) {
    super(fireType, "fire_aspect");
  }

  @Override
  protected FireTypedFireAspectEnchantment build() {
    return new FireTypedFireAspectEnchantment(fireType, rarity, isTreasure, isCurse, isTradeable, isDiscoverable, enabled, compatibility, duration);
  }
}
