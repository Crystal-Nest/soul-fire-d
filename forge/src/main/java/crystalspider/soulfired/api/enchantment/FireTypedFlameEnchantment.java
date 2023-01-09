package crystalspider.soulfired.api.enchantment;

import java.util.function.Function;
import java.util.function.Supplier;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ArrowFireEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Flame Enchantment sensitive to the Fire Type.
 */
public final class FireTypedFlameEnchantment extends ArrowFireEnchantment implements FireTyped {
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
  FireTypedFlameEnchantment(ResourceLocation fireType, Rarity rarity, boolean isTreasure, boolean isCurse, boolean isTradeable, boolean isDiscoverable, Supplier<Boolean> enabled, Function<Enchantment, Boolean> compatibility) {
    super(rarity, EquipmentSlot.MAINHAND);
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
    return enabled.get() && super.checkCompatibility(enchantment) && !(enchantment instanceof ArrowFireEnchantment) && !FireManager.getFlames().contains(enchantment) && compatibility.apply(enchantment);
  }
  
  @Override
  public boolean isTreasureOnly() {
    return isTreasure;
  }

  @Override
  public boolean isCurse() {
    return isCurse;
  }

  @Override
  public boolean isTradeable() {
    return isTradeable && enabled.get();
  }

  @Override
  public boolean isDiscoverable() {
    return isDiscoverable && enabled.get();
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }
}