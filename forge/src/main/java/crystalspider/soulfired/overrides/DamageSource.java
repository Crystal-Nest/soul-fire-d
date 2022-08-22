package crystalspider.soulfired.overrides;

/**
 * Wrapper around {@link net.minecraft.world.damagesource.DamageSource DamageSource} to add new centralized damage sources.
 */
public class DamageSource extends net.minecraft.world.damagesource.DamageSource {
  /**
   * DamageSource for damage derived by being inside a soul fire source.
   */
  public static final net.minecraft.world.damagesource.DamageSource IN_SOUL_FIRE = (new net.minecraft.world.damagesource.DamageSource("inSoulFire")).bypassArmor().setIsFire();
  /**
   * DamageSource for damage derived by being on fire (soul fire).
   */
  public static final net.minecraft.world.damagesource.DamageSource ON_SOUL_FIRE = (new net.minecraft.world.damagesource.DamageSource("onSoulFire")).bypassArmor().setIsFire();

  /**
   * @param msgId message id.
   */
  public DamageSource(String msgId) {
    super(msgId);
  }
}
