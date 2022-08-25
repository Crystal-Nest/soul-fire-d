package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.FireTyped;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;

@Mixin(Zombie.class)
public abstract class ZombieMixin implements FireTyped {
  @Inject(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setSecondsOnFire(I)V", shift = At.Shift.AFTER))
  private void onDoHurtTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
    if (FireManager.isFireId(getFireId())) {
      ((FireTyped) entity).setFireId(getFireId());
    }
  }
}
