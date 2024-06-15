package it.crystalnest.soul_fire_d.mixin;

import it.crystalnest.soul_fire_d.api.enchantment.FireEnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
   * @param cir {@link CallbackInfoReturnable}.
   */
  @Inject(method = "getEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/entity/LivingEntity;)I", at = @At(value = "HEAD"), cancellable = true)
  private static void onGetEnchantmentLevel(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
    if (enchantment == Enchantments.FIRE_ASPECT) {
      cir.setReturnValue(FireEnchantmentHelper.getAnyFireAspect(entity));
    }
    if (enchantment == Enchantments.FLAMING_ARROWS) {
      cir.setReturnValue(FireEnchantmentHelper.getAnyFlame(entity));
    }
  }

  /**
   * Injects at the start of the method {@link EnchantmentHelper#getItemEnchantmentLevel(Enchantment, ItemStack)}.<br />
   * Returns the level of any Fire Aspect or Flame.
   *
   * @param enchantment enchantment to calculate the level of.
   * @param stack enchanted {@link ItemStack}.
   * @param cir {@link CallbackInfoReturnable}.
   */
  @Inject(method = "getItemEnchantmentLevel", at = @At(value = "HEAD"), cancellable = true)
  private static void onGetItemEnchantmentLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
    if (enchantment == Enchantments.FIRE_ASPECT) {
      cir.setReturnValue(FireEnchantmentHelper.getAnyFireAspect(stack));
    }
    if (enchantment == Enchantments.FLAMING_ARROWS) {
      cir.setReturnValue(FireEnchantmentHelper.getAnyFlame(stack));
    }
  }
}
