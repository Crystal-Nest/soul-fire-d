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

import java.util.function.Function;
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
  private final Supplier<Boolean> enabled;

  /**
   * Addtional compatibility {@link Function} to call and check for when checking compatibility with other enchantments.
   */
  private final Function<Enchantment, Boolean> compatibility;

  /**
   * {@link TriFunction} to tweak the flame duration.
   */
  private final TriFunction<Entity, Entity, Integer, Integer> duration;

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
   * @param duration {@link #duration}.
   */
  FireTypedFlameEnchantment(ResourceLocation fireType, Rarity rarity, Supplier<Boolean> isTreasure, Supplier<Boolean> isCurse, Supplier<Boolean> isTradeable, Supplier<Boolean> isDiscoverable, Supplier<Boolean> enabled, Function<Enchantment, Boolean> compatibility, TriFunction<Entity, Entity, Integer, Integer> duration) {
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
    return enabled.get() && super.checkCompatibility(enchantment) && !(enchantment instanceof ArrowFireEnchantment) && compatibility.apply(enchantment);
  }

  @Override
  public boolean canEnchant(@NotNull ItemStack itemStack) {
    return enabled.get() && super.canEnchant(itemStack);
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

  @Override
  public int duration(Entity attacker, Entity target, Integer duration) {
    return this.duration.apply(attacker, target, duration);
  }
}
