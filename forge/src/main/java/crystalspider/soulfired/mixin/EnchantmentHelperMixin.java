package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/**
 * Injects into {@link EnchantmentHelper} to alter the levels returned by the enchantment level getters for Fire Aspect and Flame to include any Fire.
 */
@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
  /**
   * Injects at the start of the method {@link EnchantmentHelper#getEnchantmentLevel(Enchantment, LivingEntity)}.
   * <p>
   * Returns the level of any Fire Aspect or Flame.
   * 
   * @param enchantment
   * @param entity
   * @param cir {@link CallbackInfoReturnable}.
   */
  @Inject(method = "getEnchantmentLevel", at = @At(value = "HEAD"), cancellable = true)
  private static void onGetEnchantmentLevel(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
    if (enchantment == Enchantments.FIRE_ASPECT) {
      cir.setReturnValue(FireEnchantmentHelper.getAnyFireAspect(entity));
    }
    if (enchantment == Enchantments.FLAMING_ARROWS) {
      cir.setReturnValue(FireEnchantmentHelper.getAnyFlame(entity));
    }
  }

  /**
   * Injects at the start of the method {@link EnchantmentHelper#getItemEnchantmentLevel(Enchantment, ItemStack)}.
   * <p>
   * Returns the level of any Fire Aspect or Flame.
   * 
   * @param enchantment
   * @param stack
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
