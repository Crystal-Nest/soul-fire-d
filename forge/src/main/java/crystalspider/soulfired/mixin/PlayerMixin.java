package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crystalspider.soulfired.api.FireManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public abstract class PlayerMixin {
  @Inject(method = "getHurtSound", at = @At(value = "HEAD"))
  private void onGetHurtSound(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
    if (FireManager.isFireDamageSource(damageSource)) {
      cir.setReturnValue(SoundEvents.PLAYER_HURT_ON_FIRE);
    }
  }
}
