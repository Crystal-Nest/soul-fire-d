package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieEntity;

/**
 * Injects into {@link ZombieEntity} to alter Fire behavior for consistency.
 */
@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin implements FireTyped {
  /**
   * Injects into the method {@link ZombieEntity#tryAttack(Entity)} after the invocation of {@link Entity#setOnFireFor(int)}.
   * <p>
   * Sets the correct FireId to the {@link Entity} being set on fire.
   * 
   * @param entity target {@link Entity}.
   * @param cir {@link CallbackInfoReturnable}.
   */
  @Inject(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V", shift = At.Shift.AFTER))
  private void onTryAttack(Entity entity, CallbackInfoReturnable<Boolean> cir) {
    if (FireManager.isFireId(getFireId())) {
      ((FireTypeChanger) entity).setFireId(getFireId());
    }
  }
}
