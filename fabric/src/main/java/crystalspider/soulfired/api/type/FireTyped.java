package crystalspider.soulfired.api.type;

import net.minecraft.util.Identifier;

/**
 * Type sensitive to the fire type it has (burns or burn).
 */
public interface FireTyped {
  /**
   * Returns this {@code fireType}.
   * <p>
   * May or may not process the actual fireType before returning.
   * 
   * @return this {@code fireType}.
   */
  public Identifier getFireType();
}
