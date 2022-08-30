package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * Injects into {@link BowItem} to alter Fire behavior for consistency.
 */
@Mixin(BowItem.class)
public class BowItemMixin {
  /**
   * Redirects the call to {@link Level#addFreshEntity(Entity)} inside the method {@link BowItem#releaseUsing(ItemStack, Level, LivingEntity, int)}.
   * <p>
   * Handles setting the arrow on the correct kind of fire if the bow has a custom fire enchantment.
   * 
   * @param caller {@link Level} invoking (owning) the redirected method.
   * @param abstractArrow parameter of the redirected method: the arrow being add to the world.
   * @param bow bow being released.
   * @param level world inside which the arrow should be generated (is the same instance as {@code caller}).
   * @param user {@link LivingEntity} holding the {@code bow}.
   * @param remainingUseTicks time left before pulling the {@code bow} to the max.
   * @return the result of calling the redirected method.
   */
  @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
  private boolean redirectAddFreshEntity(Level caller, Entity abstractArrow, ItemStack bow, Level level, LivingEntity user, int remainingUseTicks) {
    if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) <= 0) {
      for (Enchantment enchantment : FireManager.getFlames()) {
        if (EnchantmentHelper.getTagEnchantmentLevel(enchantment, bow) > 0) {
          abstractArrow.setSecondsOnFire(100);
          ((FireTypeChanger) abstractArrow).setFireId(((FireTyped) enchantment).getFireId());
          break;
        }
      }
    }
    return caller.addFreshEntity(abstractArrow);
  }
}
