package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper;
import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper.FireEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Injects into {@link BowItem} to alter Fire behavior for consistency.
 */
@Mixin(BowItem.class)
public class BowItemMixin {
  /**
   * Redirects the call to {@link AbstractArrowEntity#setSecondsOnFire(int)} inside the method {@link BowItem#releaseUsing(ItemStack, World, LivingEntity, int)}.
   * <p>
   * Handles setting the arrow on the correct kind of fire if the bow has a custom fire enchantment.
   * 
   * @param caller {@link AbstractArrowEntity} invoking (owning) the redirected method.
   * @param seconds parameter of the redirected method: number of seconds to set the arrow on fire for.
   * @param bow bow being released.
   * @param world world inside which the arrow should be generated.
   * @param user {@link LivingEntity} holding the {@code bow}.
   * @param remainingUseTicks time left before pulling the {@code bow} to the max.
   */
  @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/AbstractArrowEntity;setSecondsOnFire(I)V"))
  private void redirectSetSecondsOnFire(AbstractArrowEntity caller, int seconds, ItemStack bow, World world, LivingEntity user, int remainingUseTicks) {
    FireEnchantment fireEnchantment = FireEnchantmentHelper.getWhichFlame(bow);
    if (fireEnchantment.isApplied()) {
      FireManager.setOnFire(caller, seconds, fireEnchantment.getFireId());
    }
  }
}
