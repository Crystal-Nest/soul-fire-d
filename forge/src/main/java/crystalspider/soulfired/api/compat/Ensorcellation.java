package crystalspider.soulfired.api.compat;

// import cofh.ensorcellation.enchantment.FrostAspectEnchantment;
// import cofh.ensorcellation.enchantment.override.FireAspectEnchantmentImp;
import crystalspider.soulfired.api.enchantment.FireTypedFireAspectEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Static class for references to the optionally loaded Ensorcellation mod.
 */
public final class Ensorcellation {
  private Ensorcellation() {}

  /**
   * TODO: Ensorcellation for 1.19.4 is not available yet.
   * Returns whether the given {@link Enchantment} is is an Ensorcellation enchantment incompatible with any {@link FireTypedFireAspectEnchantment}.
   * 
   * @param enchantment
   * @return whether the given {@link Enchantment} is an Ensorcellation enchantment incompatible with any {@link FireTypedFireAspectEnchantment}.
   */
  public static boolean checkFireAspectCompatibility(Enchantment enchantment) {
    // return !(enchantment instanceof FireAspectEnchantmentImp || enchantment instanceof FrostAspectEnchantment);
    return true;
  }
}
