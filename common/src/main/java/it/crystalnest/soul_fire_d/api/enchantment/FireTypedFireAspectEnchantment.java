package it.crystalnest.soul_fire_d.api.enchantment;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.compat.Ensorcellation;
import it.crystalnest.soul_fire_d.api.type.FireTypeChanger;
import it.crystalnest.soul_fire_d.api.type.FireTypedEnchantment;
import it.crystalnest.soul_fire_d.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.FireAspectEnchantment;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Fire Aspect Enchantment sensitive to the Fire Type.
 */
public final class FireTypedFireAspectEnchantment extends FireAspectEnchantment implements FireTypedEnchantment {
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
   * Whether the enchantment is treasure only.
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
   * Whether the enchantment will appear in the enchanting table or loots with random enchant function.
   * <p>
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
  FireTypedFireAspectEnchantment(
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

  /**
   * Returns whether the given {@link Entity} was last hit by a projectile.
   *
   * @param target
   * @return whether the given {@link Entity} was last hit by a projectile.
   */
  private static boolean wasLastHitByProjectile(Entity target) {
    if (target instanceof LivingEntity livingEntity) {
      DamageSource lastDamageSource = livingEntity.getLastDamageSource();
      return lastDamageSource != null && lastDamageSource.is(DamageTypeTags.IS_PROJECTILE);
    }
    return false;
  }

  @Override
  public boolean checkCompatibility(@NotNull Enchantment enchantment) {
    return (
      enabled.getAsBoolean() &&
      super.checkCompatibility(enchantment) &&
      !(enchantment instanceof FireAspectEnchantment) &&
      compatibility.test(enchantment) &&
      (!Services.PLATFORM.isModLoaded("ensorcellation") || Ensorcellation.checkFireAspectCompatibility(enchantment))
    );
  }

  @Override
  public boolean canEnchant(@NotNull ItemStack itemStack) {
    return enabled.getAsBoolean() && super.canEnchant(itemStack);
  }

  @Override
  public void doPostAttack(@NotNull LivingEntity attacker, @NotNull Entity target, int level) {
    if (!wasLastHitByProjectile(target)) {
      if (!attacker.level().isClientSide) {
        target.setSecondsOnFire(this.duration(attacker, target, level * 4));
      }
      ((FireTypeChanger) target).setFireType(FireManager.ensure(fireType));
    }
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
