package crystalspider.soulfired.api.enchantment;

import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.function.TriFunction;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import crystalspider.soulfired.api.type.FireTypedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * Fire Aspect Enchantment sensitive to the Fire Type.
 */
public final class FireTypedFireAspectEnchantment extends FireAspectEnchantment implements FireTypedEnchantment {
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
    Identifier fireType,
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
  public void onTargetDamaged(LivingEntity attacker, Entity target, int level) {
    if (!wasLastHitByProjectile(target)) {
      if (!attacker.world.isClient) {
        target.setOnFireFor(this.duration(attacker, target, level * 4));
      }
      ((FireTypeChanger) target).setFireType(FireManager.ensure(fireType));
    }
  }

  /**
   * Returns whether the given {@link Entity} was last hit by a projectile.
   * 
   * @param target
   * @return whether the given {@link Entity} was last hit by a projectile.
   */
  private boolean wasLastHitByProjectile(Entity target) {
    if (target instanceof LivingEntity) {
      DamageSource lastDamageSource = ((LivingEntity) target).getRecentDamageSource();
      return lastDamageSource != null && lastDamageSource.isProjectile();
    }
    return false;
  }

  @Override
  public boolean isAcceptableItem(ItemStack itemStack) {
    return enabled.get() && super.isAcceptableItem(itemStack);
  }

  @Override
  public boolean canAccept(Enchantment enchantment) {
    return enabled.get() && super.canAccept(enchantment) && !(enchantment instanceof FireAspectEnchantment) && !FireManager.getFireAspects().contains(enchantment) && compatibility.apply(enchantment);
  }

  @Override
  public boolean isTreasure() {
    return isTreasure.get();
  }

  @Override
  public boolean isCursed() {
    return isCurse.get();
  }

  @Override
  public boolean isAvailableForEnchantedBookOffer() {
    return isTradeable.get() && enabled.get();
  }

  @Override
  public boolean isAvailableForRandomSelection() {
    return isDiscoverable.get() && enabled.get();
  }

  @Override
  public Identifier getFireType() {
    return fireType;
  }

  @Override
  public int duration(Entity attacker, Entity target, Integer duration) {
    return this.duration.apply(attacker, target, duration);
  }
}
