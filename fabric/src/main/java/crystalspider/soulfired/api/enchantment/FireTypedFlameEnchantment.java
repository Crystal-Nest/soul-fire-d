package crystalspider.soulfired.api.enchantment;

import java.util.function.Function;
import java.util.function.Supplier;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.FlameEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * Flame Enchantment sensitive to the Fire Type.
 */
public final class FireTypedFlameEnchantment extends FlameEnchantment implements FireTyped {
  /**
   * {@link Identifier} to uniquely identify the associated Fire.
   */
  private final Identifier fireType;

  /**
   * {@link Supplier} to check whether this enchantment is enabled in survival.
   */
  private final Supplier<Boolean> enabled;
  /**
   * Addtional compatibility {@link Function} to call and check for when checking compatibility with other enchantments.
   */
  private final Function<Enchantment, Boolean> compatibility;

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
   * @param fireType {@link #fireType}.
   * @param rarity {@link Rarity}.
   * @param isTreasure {@link #isTreasure}.
   * @param isCurse {@link #isCurse}.
   * @param isTradeable {@link #isTradeable}.
   * @param isDiscoverable {@link #isDiscoverable}.
   * @param enabled {@link #enabled}.
   * @param compatibility {@link #compatibility}.
   */
  FireTypedFlameEnchantment(Identifier fireType, Rarity rarity, boolean isTreasure, boolean isCurse, boolean isTradeable, boolean isDiscoverable, Supplier<Boolean> enabled, Function<Enchantment, Boolean> compatibility) {
    super(rarity, EquipmentSlot.MAINHAND);
    this.fireType = FireManager.sanitize(fireType);
    this.isTreasure = isTreasure;
    this.isCurse = isCurse;
    this.isTradeable = isTradeable;
    this.isDiscoverable = isDiscoverable;
    this.enabled = enabled;
    this.compatibility = compatibility;
  }

  @Override
  public boolean isAcceptableItem(ItemStack itemStack) {
    return enabled.get() && super.isAcceptableItem(itemStack);
  }

  @Override
  public boolean canAccept(Enchantment enchantment) {
    return enabled.get() && super.canAccept(enchantment) && !(enchantment instanceof FlameEnchantment) && !FireManager.getFlames().contains(enchantment) && compatibility.apply(enchantment);
  }
  
  @Override
  public boolean isTreasure() {
    return isTreasure;
  }

  @Override
  public boolean isCursed() {
    return isCurse;
  }

  @Override
  public boolean isAvailableForEnchantedBookOffer() {
    return isTradeable && enabled.get();
  }

  @Override
  public boolean isAvailableForRandomSelection() {
    return isDiscoverable && enabled.get();
  }

  @Override
  public Identifier getFireType() {
    return fireType;
  }
}