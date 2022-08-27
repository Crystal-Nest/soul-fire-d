package crystalspider.soulfired.api;

/**
 * Type sensitive to the fire type it has (burns or burn).
 * <p>
 * The fire type can change over time.
 */
public interface FireTypeChanger extends FireTyped {
  /**
   * Sets this {@code fireId}.
   * <p>
   * May or may not process the argument before setting.
   * 
   * @param fireId
   */
  public void setFireId(String fireId);
}
