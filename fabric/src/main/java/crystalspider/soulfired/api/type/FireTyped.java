package crystalspider.soulfired.api.type;

/**
 * Type sensitive to the fire type it has (burns or burn).
 */
public interface FireTyped {
  /**
   * Returns this {@code fireId}.
   * <p>
   * May or may not process the actual fireId before returning.
   * 
   * @return this {@code fireId}.
   */
  public String getFireId();
}
