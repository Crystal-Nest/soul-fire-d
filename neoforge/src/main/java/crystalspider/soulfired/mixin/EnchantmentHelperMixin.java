package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

/**
 * Injects into {@link EnchantmentHelper} to alter the levels returned by the enchantment level getters for Fire Aspect and Flame to include any Fire.
 */
@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
  /**
   * Injects at the start of the method {@link EnchantmentHelper#getEnchantmentLevel(Enchantment, LivingEntity)}.
   * <p>
   * Returns the level of any Fire Aspect or Flame.
   * 
   * @param enchantment
   * @param entity
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
   * Injects at the start of the method {@link EnchantmentHelper#getTagEnchantmentLevel(Enchantment, ItemStack)}.
   * <p>
   * Returns the level of any Fire Aspect or Flame.
   * 
   * @param enchantment
   * @param stack
   * @param cir {@link CallbackInfoReturnable}.
   */
  @Inject(method = "getTagEnchantmentLevel", at = @At(value = "HEAD"), cancellable = true, remap = false)
  private static void onGetTagEnchantmentLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
    if (enchantment == Enchantments.FIRE_ASPECT) {
      cir.setReturnValue(FireEnchantmentHelper.getAnyFireAspect(stack));
    }
    if (enchantment == Enchantments.FLAMING_ARROWS) {
      cir.setReturnValue(FireEnchantmentHelper.getAnyFlame(stack));
    }
  }
}
