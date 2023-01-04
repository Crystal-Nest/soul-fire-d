package crystalspider.soulfired.api.type;

import net.minecraft.util.ResourceLocation;

/**
 * Type sensitive to the fire type it has (burns or burn).
 * <p>
 * The fire type can change over time.
 */
public interface FireTypeChanger extends FireTyped {
  /**
   * Sets this {@code fireType}.
   * <p>
   * May or may not process the argument before setting.
   * 
   * @param fireType
   */
  public void setFireType(ResourceLocation fireType);
}
