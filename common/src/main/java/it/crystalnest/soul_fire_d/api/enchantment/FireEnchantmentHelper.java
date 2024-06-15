package it.crystalnest.soul_fire_d.api.enchantment;

import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import it.crystalnest.soul_fire_d.mixin.EnchantmentHelperMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

/**
 * Helper for Fire related enchantments (Fire Aspect and Flame).
 */
public final class FireEnchantmentHelper {
  private FireEnchantmentHelper() {}

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
    return getAnyFireEnchantment(entity, FireManager.getComponentList(Fire.Component.FIRE_ASPECT_ENCHANTMENT), FireEnchantmentHelper::getBaseFireAspect, EnchantmentHelper::getEnchantmentLevel);
  }

  /**
   * Returns the {@link FireEnchantment data} of whatever Fire Aspect enchantment applied.
   *
   * @param stack {@link ItemStack} to check if it has the enchantment.
   * @return the {@link FireEnchantment data} of whatever Fire Aspect enchantment applied.
   */
  public static FireEnchantment getWhichFireAspect(ItemStack stack) {
    return getAnyFireEnchantment(stack, FireManager.getComponentList(Fire.Component.FIRE_ASPECT_ENCHANTMENT), FireEnchantmentHelper::getBaseFireAspect, EnchantmentHelper::getItemEnchantmentLevel);
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
    return getAnyFireEnchantment(entity, FireManager.getComponentList(Fire.Component.FLAME_ENCHANTMENT), FireEnchantmentHelper::getBaseFlame, EnchantmentHelper::getEnchantmentLevel);
  }

  /**
   * Returns the {@link FireEnchantment data} of whatever Flame enchantment applied.
   *
   * @param stack {@link ItemStack} to check if it has the enchantment.
   * @return the {@link FireEnchantment data} of whatever Flame enchantment applied.
   */
  public static FireEnchantment getWhichFlame(ItemStack stack) {
    return getAnyFireEnchantment(stack, FireManager.getComponentList(Fire.Component.FLAME_ENCHANTMENT), FireEnchantmentHelper::getBaseFlame, EnchantmentHelper::getItemEnchantmentLevel);
  }

  /**
   * Returns the {@link FireEnchantment data} of whatever Fire enchantment in the enchantment list is applied.
   *
   * @param <T> type of what is enchanted.
   * @param enchanted what is enchanted.
   * @param enchantments enchantment list.
   * @param getBaseFireEnchantment getter for the default fire enchantment.
   * @param getLevel getter for the enchantment level.
   * @return the {@link FireEnchantment data} of whatever Fire enchantment in the enchantments list is applied.
   */
  private static <T> FireEnchantment getAnyFireEnchantment(T enchanted, List<? extends Enchantment> enchantments, ToIntFunction<T> getBaseFireEnchantment, ToIntBiFunction<Enchantment, T> getLevel) {
    int fireEnchantmentLevel = getBaseFireEnchantment.applyAsInt(enchanted);
    ResourceLocation fireType = FireManager.DEFAULT_FIRE_TYPE;
    if (fireEnchantmentLevel <= 0) {
      for (Enchantment enchantment : enchantments) {
        int enchantmentLevel = getLevel.applyAsInt(enchantment, enchanted);
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
   * Logic copied from {@link EnchantmentHelper#getEnchantmentLevel(Enchantment, LivingEntity)}.<br />
   * Necessary to avoid recursion when {@link EnchantmentHelperMixin} is applied and above methods are called.
   *
   * @param enchantment enchantment.
   * @param entity entity with some enchanted equipment.
   * @return same as {@link EnchantmentHelper#getEnchantmentLevel(Enchantment, LivingEntity)}.
   */
  private static int getFireEquipmentLevel(Enchantment enchantment, LivingEntity entity) {
    int i = 0;
    for (ItemStack stack : enchantment.getSlotItems(entity).values()) {
      int j = EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
      if (j > i) {
        i = j;
      }
    }
    return i;
  }

  /**
   * Logic copied from {@link EnchantmentHelper#getItemEnchantmentLevel(Enchantment, ItemStack)}.<br />
   * Necessary to avoid recursion when {@link EnchantmentHelperMixin} is applied and above methods are called.
   *
   * @param enchantment enchantment.
   * @param stack enchanted item.
   * @return same as {@link EnchantmentHelper#getItemEnchantmentLevel(Enchantment, ItemStack)}.
   */
  private static int getFireLevel(Enchantment enchantment, ItemStack stack) {
    if (!stack.isEmpty()) {
      ResourceLocation idToFind = EnchantmentHelper.getEnchantmentId(enchantment);
      ListTag tags = stack.getEnchantmentTags();
      for (int i = 0; i < tags.size(); ++i) {
        CompoundTag tag = tags.getCompound(i);
        ResourceLocation enchantmentId = EnchantmentHelper.getEnchantmentId(tag);
        if (enchantmentId != null && enchantmentId.equals(idToFind)) {
          return EnchantmentHelper.getEnchantmentLevel(tag);
        }
      }
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
    private final ResourceLocation fireType;

    /**
     * Whether there's a fire enchantment applied at all.
     */
    private final boolean applied;

    /**
     * @param level enchantment level.
     * @param fireType fire type.
     */
    FireEnchantment(int level, ResourceLocation fireType) {
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
    public ResourceLocation getFireType() {
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
