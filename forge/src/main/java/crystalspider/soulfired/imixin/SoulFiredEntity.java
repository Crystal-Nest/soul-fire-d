package crystalspider.soulfired.imixin;

public interface SoulFiredEntity {
  public boolean getWasOnSoulFire();

  public void setWasOnSoulFire(boolean wasOnSoulFire);

  public void setSharedFlagOnSoulFire(boolean flag);

  public void setSecondsOnSoulFire(int seconds);

  public void setRemainingSoulFireTicks(int ticks);

  public int getRemainingSoulFireTicks();

  public void clearSoulFire();

  public boolean soulFireImmune();

  public boolean isOnSoulFire();

  public boolean displaySoulFireAnimation();
}
