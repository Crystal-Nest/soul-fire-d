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

import java.util.function.Function;
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
  FireTypedFireAspectEnchantment(
    ResourceLocation fireType,
    Rarity rarity,
    Supplier<Boolean> isTreasure,
    Supplier<Boolean> isCurse,
    Supplier<Boolean> isTradeable,
    Supplier<Boolean> isDiscoverable,
    Supplier<Boolean> enabled,
    Function<Enchantment, Boolean> compatibility,
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
    return (
      enabled.get() &&
      super.checkCompatibility(enchantment) &&
      !(enchantment instanceof FireAspectEnchantment) &&
      compatibility.apply(enchantment) &&
      (!Services.PLATFORM.isModLoaded("ensorcellation") || Ensorcellation.checkFireAspectCompatibility(enchantment))
    );
  }

  @Override
  public boolean canEnchant(@NotNull ItemStack itemStack) {
    return enabled.get() && super.canEnchant(itemStack);
  }

  @Override
  @SuppressWarnings("resource")
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

  /**
   * Returns whether the given {@link Entity} was last hit by a projectile.
   *
   * @param target
   * @return whether the given {@link Entity} was last hit by a projectile.
   */
  private boolean wasLastHitByProjectile(Entity target) {
    if (target instanceof LivingEntity) {
      DamageSource lastDamageSource = ((LivingEntity) target).getLastDamageSource();
      return lastDamageSource != null && lastDamageSource.is(DamageTypeTags.IS_PROJECTILE);
    }
    return false;
  }
}
