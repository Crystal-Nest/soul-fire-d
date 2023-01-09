package crystalspider.soulfired.api;

import net.minecraft.entity.damage.DamageSource;

/**
 * Wrapper around {@link DamageSource}.
 */
public class FireDamageSource extends DamageSource {
  /**
   * @param name
   */
  public FireDamageSource(String name) {
    super(name);
  }

  @Override
  public FireDamageSource setBypassesArmor() {
    return (FireDamageSource) super.setBypassesArmor();
  }

  @Override
  public FireDamageSource setFire() {
    return (FireDamageSource) super.setFire();
  }
}
