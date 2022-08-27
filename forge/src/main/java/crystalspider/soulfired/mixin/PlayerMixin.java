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

@Mixin(Player.class)
public abstract class PlayerMixin implements FireTyped {
  @Inject(method = "getHurtSound", at = @At(value = "HEAD"))
  private void onGetHurtSound(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
    if (FireManager.isFireDamageSource(damageSource) && cir.isCancellable()) {
      cir.setReturnValue(FireManager.getHurtSound(getFireId()));
    }
  }
}
