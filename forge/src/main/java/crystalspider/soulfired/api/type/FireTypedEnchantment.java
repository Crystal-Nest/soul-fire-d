package crystalspider.soulfired.api.type;

import net.minecraft.world.entity.Entity;

/**
 * Fire typed enchantment.
 */
public interface FireTypedEnchantment extends FireTyped {
  /**
   * Returns the duration for the applied flame.
   * 
   * @param attacker {@link Entity} attacking with the enchantment.
   * @param target {@link Entity} being attacked.
   * @param duration default duration that would be applied.
   * @return the duration for the applied flame.
   */
  public int duration(Entity attacker, Entity target, Integer duration);
}
