package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Injects into {@link BowItem} to alter Fire behavior for consistency.
 */
@Mixin(BowItem.class)
public class BowItemMixin {
  /**
   * Redirects the call to {@link World#addFreshEntity(Entity)} inside the method {@link BowItem#releaseUsing(ItemStack, World, LivingEntity, int)}.
   * <p>
   * Handles setting the arrow on the correct kind of fire if the bow has a custom fire enchantment.
   * 
   * @param caller {@link Level} invoking (owning) the redirected method.
   * @param abstractArrow parameter of the redirected method: the arrow being add to the world.
   * @param bow bow being released.
   * @param world world inside which the arrow should be generated (is the same instance as {@code caller}).
   * @param user {@link LivingEntity} holding the {@code bow}.
   * @param remainingUseTicks time left before pulling the {@code bow} to the max.
   * @return the result of calling the redirected method.
   */
  @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addFreshEntity(Lnet/minecraft/entity/Entity;)Z"))
  private boolean redirectAddFreshEntity(World caller, Entity abstractArrow, ItemStack bow, World world, LivingEntity user, int remainingUseTicks) {
    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) <= 0) {
      for (Enchantment enchantment : FireManager.getFlames()) {
        if (EnchantmentHelper.getItemEnchantmentLevel(enchantment, bow) > 0) {
          abstractArrow.setSecondsOnFire(100);
          ((FireTypeChanger) abstractArrow).setFireId(((FireTyped) enchantment).getFireId());
          break;
        }
      }
    }
    return caller.addFreshEntity(abstractArrow);
  }
}
