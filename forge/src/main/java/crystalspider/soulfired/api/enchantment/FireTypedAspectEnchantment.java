package crystalspider.soulfired.api.enchantment;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;

/**
 * Fire Aspect Enchantment sensitive to the Fire Type.
 */
public class FireTypedAspectEnchantment extends FireAspectEnchantment implements FireTyped {
  /**
   * {@link ResourceLocation} to uniquely identify the associated Fire.
   */
  private final ResourceLocation fireType;

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
   * @param fireType
   * @param rarity
   * @param isTreasure
   * @param isCurse
   * @param isTradeable
   * @param isDiscoverable
   */
  public FireTypedAspectEnchantment(ResourceLocation fireType, Rarity rarity, boolean isTreasure, boolean isCurse, boolean isTradeable, boolean isDiscoverable) {
    super(rarity, EquipmentSlotType.MAINHAND);
    this.fireType = FireManager.sanitize(fireType);
    this.isTreasure = isTreasure;
    this.isCurse = isCurse;
    this.isTradeable = isTradeable;
    this.isDiscoverable = isDiscoverable;
    setRegistryName(fireType.getNamespace(), fireType.getPath() + "_fire_aspect");
  }

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
    this(new ResourceLocation(modId, fireId), rarity, isTreasure, isCurse, isTradeable, isDiscoverable);
  }

  /**
   * @param fireType
   * @param rarity
   */
  public FireTypedAspectEnchantment(ResourceLocation fireType, Rarity rarity) {
    this(fireType, rarity, false, false, true, true);
  }

  /**
   * @param modId
   * @param fireId
   * @param rarity
   */
  public FireTypedAspectEnchantment(String modId, String fireId, Rarity rarity) {
    this(new ResourceLocation(modId, fireId), rarity, false, false, true, true);
  }

  @Override
  public final void doPostAttack(LivingEntity attacker, Entity target, int level) {
    if (!attacker.level.isClientSide) {
      target.setSecondsOnFire(level * 4);
    }
    ((FireTypeChanger) target).setFireType(FireManager.ensure(fireType));
  }

  @Override
  public final boolean checkCompatibility(Enchantment enchantment) {
    return super.checkCompatibility(enchantment) && !(enchantment instanceof FireAspectEnchantment) && !FireManager.getFireAspects().contains(enchantment);
  }

  @Override
  public final boolean isTreasureOnly() {
    return isTreasure;
  }

  @Override
  public final boolean isCurse() {
    return isCurse;
  }

  @Override
  public final boolean isTradeable() {
    return isTradeable;
  }

  @Override
  public final boolean isDiscoverable() {
    return isDiscoverable;
  }

  @Override
  public final ResourceLocation getFireType() {
    return fireType;
  }
}
