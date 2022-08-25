package crystalspider.soulfired.api;

/**
 * Type sensitive to the fire type it has (burns or burn).
 */
public interface FireTyped {
  /**
   * Sets this {@code fireId}.
   * <p>
   * May or may not process the argument before setting.
   * 
   * @param fireId
   */
  public void setFireId(String fireId);

  /**
   * Returns this {@code fireId}.
   * <p>
   * May or may not process the actual fireId before returning.
   * 
   * @return this {@code fireId}.
   */
  public String getFireId();
}
