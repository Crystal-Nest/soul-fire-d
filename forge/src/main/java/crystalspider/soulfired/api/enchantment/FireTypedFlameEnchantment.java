package crystalspider.soulfired.api.enchantment;

import java.util.function.Function;
import java.util.function.Supplier;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.FlameEnchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Flame Enchantment sensitive to the Fire Type.
 */
public final class FireTypedFlameEnchantment extends FlameEnchantment implements FireTyped {
  /**
   * {@link ResourceLocation} to uniquely identify the associated Fire.
   */
  private final ResourceLocation fireType;

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
  private final Supplier<Boolean> isTreasure;
  /**
   * Whether the enchantment is a curse.
   */
  private final Supplier<Boolean> isCurse;
  /**
   * Whether the enchantment can appear in the enchanted book trade offers of librarian villagers.
   */
  private final Supplier<Boolean> isTradeable;
  /**
   * Whether the enchantment will appear in the enchanting table or loots with random enchant function.
   * <p>
   * Note that {@link #isTreasure} takes precedence.
   */
  private final Supplier<Boolean> isDiscoverable;

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
  FireTypedFlameEnchantment(ResourceLocation fireType, Rarity rarity, Supplier<Boolean> isTreasure, Supplier<Boolean> isCurse, Supplier<Boolean> isTradeable, Supplier<Boolean> isDiscoverable, Supplier<Boolean> enabled, Function<Enchantment, Boolean> compatibility) {
    super(rarity, EquipmentSlotType.MAINHAND);
    this.fireType = FireManager.sanitize(fireType);
    this.isTreasure = isTreasure;
    this.isCurse = isCurse;
    this.isTradeable = isTradeable;
    this.isDiscoverable = isDiscoverable;
    this.enabled = enabled;
    this.compatibility = compatibility;
    setRegistryName(fireType.getNamespace(), fireType.getPath() + "_flame");
  }

  @Override
  public boolean canEnchant(ItemStack itemStack) {
    return enabled.get() && super.canEnchant(itemStack);
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack itemStack) {
    return enabled.get() && super.canApplyAtEnchantingTable(itemStack);
  }

  @Override
  public boolean isAllowedOnBooks() {
    return enabled.get() && super.isAllowedOnBooks();
  }

  @Override
  public boolean checkCompatibility(Enchantment enchantment) {
    return enabled.get() && super.checkCompatibility(enchantment) && !(enchantment instanceof FlameEnchantment) && !FireManager.getFlames().contains(enchantment) && compatibility.apply(enchantment);
  }
  
  @Override
  public boolean isTreasureOnly() {
    return isTreasure.get();
  }

  @Override
  public boolean isCurse() {
    return isCurse.get();
  }

  @Override
  public boolean isTradeable() {
    return isTradeable.get() && enabled.get();
  }

  @Override
  public boolean isDiscoverable() {
    return isDiscoverable.get() && enabled.get();
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }
}