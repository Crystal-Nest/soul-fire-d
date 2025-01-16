package it.crystalnest.soul_fire_d.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.crystalnest.soul_fire_d.api.enchantment.FireEnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Injects into {@link EnchantmentHelper} to alter the levels returned by the enchantment level getters for Fire Aspect and Flame to include any Fire.
 */
@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
  /**
   * Injects at the start of the method {@link EnchantmentHelper#getEnchantmentLevel(Enchantment, LivingEntity)}.<br />
   * Returns the level of any Fire Aspect or Flame.
   *
   * @param enchantment enchantment to calculate the level of.
   * @param entity entity with the enchanted equipment.
   * @param original original {@link Operation} being wrapped.
   * @return enchantment level.
   */
  @WrapMethod(method = "getEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/entity/LivingEntity;)I")
  private static int onGetEnchantmentLevel(Enchantment enchantment, LivingEntity entity, Operation<Integer> original) {
    if (enchantment == Enchantments.FIRE_ASPECT) {
      return FireEnchantmentHelper.getAnyFireAspect(entity);
    }
    if (enchantment == Enchantments.FLAMING_ARROWS) {
      return FireEnchantmentHelper.getAnyFlame(entity);
    }

    return original.call(enchantment, entity);
  }

  /**
   * Injects at the start of the method {@link EnchantmentHelper#getItemEnchantmentLevel(Enchantment, ItemStack)}.<br />
   * Returns the level of any Fire Aspect or Flame.
   *
   * @param enchantment enchantment to calculate the level of.
   * @param stack enchanted {@link ItemStack}.
   * @param original original {@link Operation} being wrapped.
   * @return enchantment level.
   */
  @WrapMethod(method = "getItemEnchantmentLevel")
  private static int onGetItemEnchantmentLevel(Enchantment enchantment, ItemStack stack, Operation<Integer> original) {
    if (enchantment == Enchantments.FIRE_ASPECT) {
      return FireEnchantmentHelper.getAnyFireAspect(stack);
    }
    if (enchantment == Enchantments.FLAMING_ARROWS) {
      return FireEnchantmentHelper.getAnyFlame(stack);
    }

    return original.call(enchantment, stack);
  }
}
