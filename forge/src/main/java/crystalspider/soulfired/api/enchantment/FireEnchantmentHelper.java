package crystalspider.soulfired.api.enchantment;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import crystalspider.soulfired.mixin.EnchantmentHelperMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

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
    return getAnyFireEnchantment(entity, FireManager.getFireAspects(), FireEnchantmentHelper::getBaseFireAspect, EnchantmentHelper::getEnchantmentLevel);
  }

  /**
   * Returns the {@link FireEnchantment data} of whatever Fire Aspect enchantment applied.
   * 
   * @param stack {@link ItemStack} to check if it has the enchantment.
   * @return the {@link FireEnchantment data} of whatever Fire Aspect enchantment applied.
   */
  public static FireEnchantment getWhichFireAspect(ItemStack stack) {
    return getAnyFireEnchantment(stack, FireManager.getFireAspects(), FireEnchantmentHelper::getBaseFireAspect, EnchantmentHelper::getTagEnchantmentLevel);
  }

  /**
   * Returns the level of the base Flame enchantment.
   * 
   * @param entity {@link LivingEntity} that could have the enchantment in its slots.
   * @return the level of the base Flame enchantment.
   */
  public static int getBaseFlame(LivingEntity entity) {
    return getFireEquipmentLevel(Enchantments.FLAMING_ARROWS, entity);
  }

  /**
   * Returns the level of the base Flame enchantment.
   * 
   * @param stack {@link ItemStack} to check if it has the enchantment.
   * @return the level of the base Flame enchantment.
   */
  public static int getBaseFlame(ItemStack stack) {
    return getFireLevel(Enchantments.FLAMING_ARROWS, stack);
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
    return getAnyFireEnchantment(entity, FireManager.getFlames(), FireEnchantmentHelper::getBaseFlame, EnchantmentHelper::getEnchantmentLevel);
  }

  /**
   * Returns the {@link FireEnchantment data} of whatever Flame enchantment applied.
   * 
   * @param stack {@link ItemStack} to check if it has the enchantment.
   * @return the {@link FireEnchantment data} of whatever Flame enchantment applied.
   */
  public static FireEnchantment getWhichFlame(ItemStack stack) {
    return getAnyFireEnchantment(stack, FireManager.getFlames(), FireEnchantmentHelper::getBaseFlame, EnchantmentHelper::getTagEnchantmentLevel);
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
  private static <T> FireEnchantment getAnyFireEnchantment(T stack, List<Enchantment> enchantments, Function<T, Integer> getBaseFireEnchantment, BiFunction<Enchantment, T, Integer> getLevel) {
    int fireEnchantmentLevel = getBaseFireEnchantment.apply(stack);
    String fireId = FireManager.BASE_FIRE_ID;
    if (fireEnchantmentLevel <= 0) {
      for (Enchantment enchantment : enchantments) {
        int enchantmentLevel = getLevel.apply(enchantment, stack);
        if (enchantmentLevel > 0) {
          fireEnchantmentLevel = enchantmentLevel;
          fireId = ((FireTyped) enchantment).getFireId();
          break;
        }
      }
    }
    return new FireEnchantment(fireEnchantmentLevel, fireEnchantmentLevel > 0 ? fireId : null);
  }

  /**
   * Logic copied from {@link EnchantmentHelper#getEnchantmentLevel(Enchantment, LivingEntity)}.
   * <p>
   * Necessary to avoid recursion when {@link EnchantmentHelperMixin} is applied and above methods are called.
   * 
   * @param enchantment
   * @param entity
   * @return same as {@link EnchantmentHelper#getEnchantmentLevel(Enchantment, LivingEntity)}.
   */
  private static int getFireEquipmentLevel(Enchantment enchantment, LivingEntity entity) {
    Collection<ItemStack> iterable = enchantment.getSlotItems(entity).values();
    if (iterable != null) {
      int i = 0;
      for(ItemStack itemstack : iterable) {
        int j = EnchantmentHelper.getTagEnchantmentLevel(enchantment, itemstack);
        if (j > i) {
          i = j;
        }
     }
      return i;
    }
    return 0;
  }

  /**
   * Logic copied from {@link EnchantmentHelper#getTagEnchantmentLevel(Enchantment, ItemStack)}.
   * <p>
   * Necessary to avoid recursion when {@link EnchantmentHelperMixin} is applied and above methods are called.
   * 
   * @param enchantment
   * @param stack
   * @return same as {@link EnchantmentHelper#getTagEnchantmentLevel(Enchantment, ItemStack)}.
   */
  private static int getFireLevel(Enchantment enchantment, ItemStack stack) {
    if (!stack.isEmpty()) {
      ResourceLocation resourcelocation = EnchantmentHelper.getEnchantmentId(enchantment);
      ListTag listtag = stack.getEnchantmentTags();
      for(int i = 0; i < listtag.size(); ++i) {
        CompoundTag compoundtag = listtag.getCompound(i);
        ResourceLocation resourcelocation1 = EnchantmentHelper.getEnchantmentId(compoundtag);
        if (resourcelocation1 != null && resourcelocation1.equals(resourcelocation)) {
          return EnchantmentHelper.getEnchantmentLevel(compoundtag);
        }
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
     * Fire Id of the enchantment, invalid if none.
     */
    private final String fireId;
    /**
     * Whether there's a fire enchantment applied at all.
     */
    private final boolean applied;

    FireEnchantment(int level, String fireId) {
      this.level = level;
      this.fireId = fireId;
      this.applied = level > 0 && (FireManager.BASE_FIRE_ID.equals(fireId) || FireManager.isFireId(fireId));
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
     * Returns this {@link #fireId}.
     * 
     * @return this {@link #fireId}.
     */
    public String getFireId() {
      return fireId;
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
