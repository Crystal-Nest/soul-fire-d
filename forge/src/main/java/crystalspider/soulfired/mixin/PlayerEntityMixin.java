package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;

/**
 * Injects into {@link PlayerEntity} to alter Fire behavior for consistency.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements FireTyped {
  /**
   * Injects at the start of the method {@link PlayerEntity#getHurtSound(DamageSource)}.
   * <p>
   * If {@code damageSource} is a custom Fire DamageSource, forces the target method to return the correct hurt sound.
   * 
   * @param damageSource {@link DamageSource} causing the damage.
   * @param cir {@link CallbackInfoReturnable}.
   */
  @Inject(method = "getHurtSound", at = @At(value = "HEAD"), cancellable = true)
  private void onGetHurtSound(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
    if (FireManager.isFireDamageSource(damageSource)) {
      cir.setReturnValue(FireManager.getHurtSound(getFireId()));
    }
  }
}
