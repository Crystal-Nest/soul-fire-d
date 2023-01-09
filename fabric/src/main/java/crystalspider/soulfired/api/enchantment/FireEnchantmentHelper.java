package crystalspider.soulfired.api.enchantment;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import crystalspider.soulfired.mixin.EnchantmentHelperMixin;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

/**
 * Helper for Fire related enchantments (Fire Aspect and Flame).
 */
public class FireEnchantmentHelper {
  /**
   * Returns the level of the base Fire Aspect enchantment.
   * 
   * @param entity {@link LivingEntity} that could have the enchantment in its slots.
   * @return the level of the base Fire Aspect enchantment.
   */
  public static int getBaseFireAspect(LivingEntity entity) {
    return getFireEquipmentLevel(Enchantments.FIRE_ASPECT, entity);
  }

  /**
   * Returns the level of the base Fire Aspect enchantment.
   * 
   * @param stack {@link ItemStack} to check if it has the enchantment.
   * @return the level of the base Fire Aspect enchantment.
   */
  public static int getBaseFireAspect(ItemStack stack) {
    return getFireLevel(Enchantments.FIRE_ASPECT, stack);
  }

  /**
   * Returns the level of whatever Fire Aspect enchantment applied, {@code 0} if none.
   * 
   * @param entity {@link LivingEntity} that could have the enchantment in its slots.
   * @return the level of any Fire Aspect enchantment.
   */
  public static int getAnyFireAspect(LivingEntity entity) {
    return getWhichFireAspect(entity).getLevel();
  }

  /**
   * Returns the level of whatever Fire Aspect enchantment applied, {@code 0} if none.
   * 
   * @param stack {@link ItemStack} to check if it has the enchantment.
   * @return the level of any Fire Aspect enchantment.
   */
  public static int getAnyFireAspect(ItemStack stack) {
    return getWhichFireAspect(stack).getLevel();
  }

  /**
   * Returns the {@link FireEnchantment data} of whatever Fire Aspect enchantment applied.
   * 
   * @param entity {@link LivingEntity} that could have the enchantment in its slots.
   * @return the {@link FireEnchantment data} of whatever Fire Aspect enchantment applied.
   */
  public static FireEnchantment getWhichFireAspect(LivingEntity entity) {
    return getAnyFireEnchantment(entity, FireManager.getFireAspects(), FireEnchantmentHelper::getBaseFireAspect, EnchantmentHelper::getEquipmentLevel);
  }

  /**
   * Returns the {@link FireEnchantment data} of whatever Fire Aspect enchantment applied.
   * 
   * @param stack {@link ItemStack} to check if it has the enchantment.
   * @return the {@link FireEnchantment data} of whatever Fire Aspect enchantment applied.
   */
  public static FireEnchantment getWhichFireAspect(ItemStack stack) {
    return getAnyFireEnchantment(stack, FireManager.getFireAspects(), FireEnchantmentHelper::getBaseFireAspect, EnchantmentHelper::getLevel);
  }

  /**
   * Returns the level of the base Flame enchantment.
   * 
   * @param entity {@link LivingEntity} that could have the enchantment in its slots.
   * @return the level of the base Flame enchantment.
   */
  public static int getBaseFlame(LivingEntity entity) {
    return getFireEquipmentLevel(Enchantments.FLAME, entity);
  }

  /**
   * Returns the level of the base Flame enchantment.
   * 
   * @param stack {@link ItemStack} to check if it has the enchantment.
   * @return the level of the base Flame enchantment.
   */
  public static int getBaseFlame(ItemStack stack) {
    return getFireLevel(Enchantments.FLAME, stack);
  }

  /**
   * Returns the level of whatever Flame enchantment applied, {@code 0} if none.
   * 
   * @param entity {@link LivingEntity} that could have the enchantment in its slots.
   * @return the level of any Flame enchantment.
   */
  public static int getAnyFlame(LivingEntity entity) {
    return getWhichFlame(entity).getLevel();
  }

  /**
   * Returns the level of whatever Flame enchantment applied, {@code 0} if none.
   * 
   * @param stack {@link ItemStack} to check if it has the enchantment.
   * @return the level of any Flame enchantment.
   */
  public static int getAnyFlame(ItemStack stack) {
    return getWhichFlame(stack).getLevel();
  }

  /**
   * Returns the {@link FireEnchantment data} of whatever Flame enchantment applied.
   * 
   * @param entity {@link LivingEntity} that could have the enchantment in its slots.
   * @return the {@link FireEnchantment data} of whatever Flame enchantment applied.
   */
  public static FireEnchantment getWhichFlame(LivingEntity entity) {
    return getAnyFireEnchantment(entity, FireManager.getFlames(), FireEnchantmentHelper::getBaseFlame, EnchantmentHelper::getEquipmentLevel);
  }

  /**
   * Returns the {@link FireEnchantment data} of whatever Flame enchantment applied.
   * 
   * @param stack {@link ItemStack} to check if it has the enchantment.
   * @return the {@link FireEnchantment data} of whatever Flame enchantment applied.
   */
  public static FireEnchantment getWhichFlame(ItemStack stack) {
    return getAnyFireEnchantment(stack, FireManager.getFlames(), FireEnchantmentHelper::getBaseFlame, EnchantmentHelper::getLevel);
  }

  /**
   * Returns the {@link FireEnchantment data} of whatever Fire enchantment in the enchantments list is applied.
   * 
   * @param <T>
   * @param stack
   * @param enchantments
   * @param getBaseFireEnchantment
   * @param getLevel
   * @return the {@link FireEnchantment data} of whatever Fire enchantment in the enchantments list is applied.
   */
  private static <T> FireEnchantment getAnyFireEnchantment(T stack, List<? extends Enchantment> enchantments, Function<T, Integer> getBaseFireEnchantment, BiFunction<Enchantment, T, Integer> getLevel) {
    int fireEnchantmentLevel = getBaseFireEnchantment.apply(stack);
    Identifier fireType = FireManager.DEFAULT_FIRE_TYPE;
    if (fireEnchantmentLevel <= 0) {
      for (Enchantment enchantment : enchantments) {
        int enchantmentLevel = getLevel.apply(enchantment, stack);
        if (enchantmentLevel > 0) {
          fireEnchantmentLevel = enchantmentLevel;
          fireType = ((FireTyped) enchantment).getFireType();
          break;
        }
      }
    }
    return new FireEnchantment(fireEnchantmentLevel, fireEnchantmentLevel > 0 ? fireType : null);
  }

  /**
   * Logic copied from {@link EnchantmentHelper#getEquipmentLevel(Enchantment, LivingEntity)}.
   * <p>
   * Necessary to avoid recursion when {@link EnchantmentHelperMixin} is applied and above methods are called.
   * 
   * @param enchantment
   * @param entity
   * @return same as {@link EnchantmentHelper#getEquipmentLevel(Enchantment, LivingEntity)}.
   */
  private static int getFireEquipmentLevel(Enchantment enchantment, LivingEntity entity) {
    Collection<ItemStack> iterable = enchantment.getEquipment(entity).values();
    if (iterable != null) {
      int i = 0;
      for (ItemStack itemStack : iterable) {
        int j = EnchantmentHelper.getLevel(enchantment, itemStack);
        if (j > i) {
          i = j;
        }
      }
      return i;
    }
    return 0;
  }

  /**
   * Logic copied from {@link EnchantmentHelper#getLevel(Enchantment, ItemStack)}.
   * <p>
   * Necessary to avoid recursion when {@link EnchantmentHelperMixin} is applied and above methods are called.
   * 
   * @param enchantment
   * @param stack
   * @return same as {@link EnchantmentHelper#getLevel(Enchantment, ItemStack)}.
   */
  private static int getFireLevel(Enchantment enchantment, ItemStack stack) {
    if (!stack.isEmpty()) {
      Identifier identifier = EnchantmentHelper.getEnchantmentId(enchantment);
      NbtList nbtList = stack.getEnchantments();
      for (int i = 0; i < nbtList.size(); ++i) {
        NbtCompound nbtCompound = nbtList.getCompound(i);
        Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
        if (identifier2 == null || !identifier2.equals(identifier)) {
          continue;
        }
        return EnchantmentHelper.getLevelFromNbt(nbtCompound);
      }
      return 0;
    }
    return 0;
  }

  /**
   * Data of a fire enchantment.
   */
  public static class FireEnchantment {
    /**
     * Level of the enchantment, {@code 0} if none.
     */
    private final int level;
    /**
     * Fire Type of the enchantment, invalid if none.
     */
    private final Identifier fireType;
    /**
     * Whether there's a fire enchantment applied at all.
     */
    private final boolean applied;

    FireEnchantment(int level, Identifier fireType) {
      this.level = level;
      this.fireType = fireType;
      this.applied = level > 0 && (FireManager.DEFAULT_FIRE_TYPE.equals(fireType) || FireManager.isRegisteredType(fireType));
    }

    /**
     * Returns this {@link #level}.
     * 
     * @return this {@link #level}.
     */
    public int getLevel() {
      return level;
    }

    /**
     * Returns this {@link #fireType}.
     * 
     * @return this {@link #fireType}.
     */
    public Identifier getFireType() {
      return fireType;
    }

    /**
     * Returns this {@link #applied}.
     * 
     * @return this {@link #applied}.
     */
    public boolean isApplied() {
      return applied;
    }
  }
}
