package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.enchantment.FireTypedArrowEnchantment;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile implements FireTypeChanger {
  private AbstractArrowMixin(EntityType<? extends Projectile> entityType, Level world) {
    super(entityType, world);
  }

  @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setSecondsOnFire(I)V"))
  private void redirectSetSecondsOnFire(Entity caller, int seconds) {
    caller.setSecondsOnFire(5);
    ((FireTypeChanger) caller).setFireId(getFireId());
  }

  @Inject(method = "setEnchantmentEffectsFromEntity", at = @At(value = "TAIL"))
  private void onSetEnchantmentEffectsFromEntity(LivingEntity entity, float damage, CallbackInfo ci) {
    for (FireTypedArrowEnchantment enchantment : FireManager.getFlameEnchants()) {
      if (EnchantmentHelper.getEnchantmentLevel(enchantment, entity) > 0) {
        setSecondsOnFire(100);
        setFireId(enchantment.getFireId());
        break;
      }
    }
  }
}
