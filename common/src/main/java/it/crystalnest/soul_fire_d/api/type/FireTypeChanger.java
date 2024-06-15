package it.crystalnest.soul_fire_d.api.type;

import net.minecraft.resources.ResourceLocation;

/**
 * Type sensitive to the fire type it has (burns or burn).<br />
 * The fire type can change over time.
 */
public interface FireTypeChanger extends FireTyped {
  /**
   * Sets this {@code fireType}.<br />
   * May or may not process the argument before setting.
   *
   * @param fireType fire type.
   */
  void setFireType(ResourceLocation fireType);
}
