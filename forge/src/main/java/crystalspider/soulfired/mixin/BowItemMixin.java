package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.enchantment.FireTypedArrowEnchantment;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;


@Mixin(BowItem.class)
public class BowItemMixin {
  @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
  private boolean redirectAddFreshEntity(Level caller, Entity abstractArrow, ItemStack bow, Level level, LivingEntity holder, int drawTime) {
    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) <= 0) {
      for (FireTypedArrowEnchantment enchantment : FireManager.getFlameEnchants()) {
        if (EnchantmentHelper.getItemEnchantmentLevel(enchantment, bow) > 0) {
          abstractArrow.setSecondsOnFire(100);
          ((FireTypeChanger) abstractArrow).setFireId(enchantment.getFireId());
          break;
        }
      }
    }
    return caller.addFreshEntity(abstractArrow);
  }
}
