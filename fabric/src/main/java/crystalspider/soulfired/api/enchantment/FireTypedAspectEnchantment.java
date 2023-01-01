package crystalspider.soulfired.api.enchantment;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

/**
 * Fire Aspect Enchantment sensitive to the fire type.
 */
public class FireTypedAspectEnchantment extends FireAspectEnchantment implements FireTyped {
  /**
   * Mod Id.
   */
  private final String modId;
  /**
   * Fire Id.
   */
  private final String fireId;

  /**
   * Whether the enchantment is treasure only.
   */
  private final boolean isTreasure;
  /**
   * Whether the enchantment is a curse.
   */
  private final boolean isCurse;
  /**
   * Whether the enchantment can appear in the enchanted book trade offers of librarian villagers.
   */
  private final boolean isTradeable;
  /**
   * Whether the enchantment will appear in the enchanting table or loots with random enchant function.
   * <p>
   * Note that {@link #isTreasure} takes precedence.
   */
  private final boolean isDiscoverable;

  /**
   * @param modId
   * @param fireId
   * @param rarity
   * @param isTreasure
   * @param isCurse
   * @param isTradeable
   * @param isDiscoverable
   */
  public FireTypedAspectEnchantment(String modId, String fireId, Rarity rarity, boolean isTreasure, boolean isCurse, boolean isTradeable, boolean isDiscoverable) {
    super(rarity, EquipmentSlot.MAINHAND);
    this.modId = modId;
    this.fireId = FireManager.sanitizeFireId(fireId);
    this.isTreasure = isTreasure;
    this.isCurse = isCurse;
    this.isTradeable = isTradeable;
    this.isDiscoverable = isDiscoverable;
  }

  /**
   * @param modId
   * @param fireId
   * @param rarity
   */
  public FireTypedAspectEnchantment(String modId, String fireId, Rarity rarity) {
    this(modId, fireId, rarity, true, false, true, true);
  }

  @Override
  public final void onTargetDamaged(LivingEntity attacker, Entity target, int level) {
    if (!attacker.world.isClient) {
      target.setOnFireFor(level * 4);
    }
    ((FireTypeChanger) target).setFireId(FireManager.ensureFireId(fireId));
  }

  @Override
  public final boolean canAccept(Enchantment enchantment) {
    return super.canAccept(enchantment) && !(enchantment instanceof FireAspectEnchantment) && !FireManager.getFireAspects().contains(enchantment);
  }

  @Override
  public final boolean isTreasure() {
    return isTreasure;
  }

  @Override
  public final boolean isCursed() {
    return isCurse;
  }

  @Override
  public final boolean isAvailableForEnchantedBookOffer() {
    return isTradeable;
  }

  @Override
  public final boolean isAvailableForRandomSelection() {
    return isDiscoverable;
  }

  @Override
  public final String getFireId() {
    return fireId;
  }

  /**
   * Returns this {@link #modId}.
   * 
   * @return this {@link #modId}.
   */
  public final String getModId() {
    return modId;
  }
}
