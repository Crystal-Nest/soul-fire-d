package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper;
import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper.FireEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Injects into {@link BowItem} to alter Fire behavior for consistency.
 */
@Mixin(BowItem.class)
public class BowItemMixin {
  /**
   * Redirects the call to {@link PersistentProjectileEntity#setOnFireFor(int)} inside the method {@link BowItem#onStoppedUsing(ItemStack, World, LivingEntity, int)}.
   * <p>
   * Handles setting the arrow on the correct kind of fire if the bow has a custom fire enchantment.
   * 
   * @param caller {@link PersistentProjectileEntity} invoking (owning) the redirected method.
   * @param seconds parameter of the redirected method: number of seconds to set the arrow on fire for.
   * @param bow bow being released.
   * @param world world inside which the arrow should be generated.
   * @param user {@link LivingEntity} holding the {@code bow}.
   * @param remainingUseTicks time left before pulling the {@code bow} to the max.
   */
  @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setOnFireFor(I)V"))
  private void redirectSpawnEntity(PersistentProjectileEntity caller, int seconds, ItemStack bow, World world, LivingEntity user, int remainingUseTicks) {
    FireEnchantment fireEnchantment = FireEnchantmentHelper.getWhichFlame(bow);
    if (fireEnchantment.isApplied()) {
      FireManager.setOnFire(caller, seconds, fireEnchantment.getFireId());
    }
  }
}
