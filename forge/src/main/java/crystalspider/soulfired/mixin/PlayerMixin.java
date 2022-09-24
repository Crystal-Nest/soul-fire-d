package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

/**
 * Injects into {@link Player} to alter Fire behavior for consistency.
 */
@Mixin(Player.class)
public abstract class PlayerMixin implements FireTyped {
  /**
   * Injects at the start of the method {@link Player#getHurtSound(DamageSource)}.
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
