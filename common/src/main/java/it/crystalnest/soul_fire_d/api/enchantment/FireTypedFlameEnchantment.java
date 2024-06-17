package it.crystalnest.soul_fire_d.api.enchantment;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTypedEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ArrowFireEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Flame Enchantment sensitive to the Fire Type.
 */
public final class FireTypedFlameEnchantment extends ArrowFireEnchantment implements FireTypedEnchantment {
  /**
   * {@link ResourceLocation} to uniquely identify the associated Fire.
   */
  private final ResourceLocation fireType;

  /**
   * {@link Supplier} to check whether this enchantment is enabled in survival.
   */
  private final BooleanSupplier enabled;

  /**
   * Additional compatibility {@link Predicate} to call and check for when checking compatibility with other enchantments.
   */
  private final Predicate<Enchantment> compatibility;

  /**
   * {@link TriFunction} to tweak the flame duration.
   */
  private final TriFunction<Entity, Entity, Integer, Integer> duration;

  /**
   * Whether the enchantment cannot appear in the enchanting table.<br />
   * If enabled along with {@link #isDiscoverable}, the enchantment won't appear in the enchanting table, but can still be found in loots.
   */
  private final BooleanSupplier isTreasure;

  /**
   * Whether the enchantment is a curse.
   */
  private final BooleanSupplier isCurse;

  /**
   * Whether the enchantment can appear in the enchanted book trade offers of librarian villagers.
   */
  private final BooleanSupplier isTradeable;

  /**
   * Whether the enchantment will appear in the enchanting table or loots with random enchant function.<br />
   * Note that {@link #isTreasure} takes precedence.
   */
  private final BooleanSupplier isDiscoverable;

  /**
   * @param fireType {@link #fireType}.
   * @param rarity {@link Rarity}.
   * @param isTreasure {@link #isTreasure}.
   * @param isCurse {@link #isCurse}.
   * @param isTradeable {@link #isTradeable}.
   * @param isDiscoverable {@link #isDiscoverable}.
   * @param enabled {@link #enabled}.
   * @param compatibility {@link #compatibility}.
   * @param duration {@link #duration}.
   */
  FireTypedFlameEnchantment(
    ResourceLocation fireType,
    Rarity rarity,
    BooleanSupplier isTreasure,
    BooleanSupplier isCurse,
    BooleanSupplier isTradeable,
    BooleanSupplier isDiscoverable,
    BooleanSupplier enabled,
    Predicate<Enchantment> compatibility,
    TriFunction<Entity, Entity, Integer, Integer> duration
  ) {
    super(rarity, EquipmentSlot.MAINHAND);
    this.fireType = FireManager.sanitize(fireType);
    this.isTreasure = isTreasure;
    this.isCurse = isCurse;
    this.isTradeable = isTradeable;
    this.isDiscoverable = isDiscoverable;
    this.enabled = enabled;
    this.compatibility = compatibility;
    this.duration = duration;
  }

  @Override
  public boolean checkCompatibility(@NotNull Enchantment enchantment) {
    return enabled.getAsBoolean() && super.checkCompatibility(enchantment) && !(enchantment instanceof ArrowFireEnchantment) && compatibility.test(enchantment);
  }

  @Override
  public boolean canEnchant(@NotNull ItemStack itemStack) {
    return enabled.getAsBoolean() && super.canEnchant(itemStack);
  }

  @Override
  public boolean isTreasureOnly() {
    return isTreasure.getAsBoolean();
  }

  @Override
  public boolean isCurse() {
    return isCurse.getAsBoolean();
  }

  @Override
  public boolean isTradeable() {
    return isTradeable.getAsBoolean() && enabled.getAsBoolean();
  }

  @Override
  public boolean isDiscoverable() {
    return isDiscoverable.getAsBoolean() && enabled.getAsBoolean();
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }

  @Override
  public int duration(Entity attacker, Entity target, Integer duration) {
    return this.duration.apply(attacker, target, duration);
  }
}
