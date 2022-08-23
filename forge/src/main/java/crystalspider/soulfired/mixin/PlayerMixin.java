package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crystalspider.soulfired.overrides.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntityMixin {
  @Shadow
  private Abilities abilities;
  
  public void setRemainingSoulFireTicks(int ticks) {
    super.setRemainingSoulFireTicks(abilities.invulnerable ? Math.min(ticks, 1) : ticks);
  }

  protected int getSoulFireImmuneTicks() {
    return 20;
  }

  @Inject(method = "getHurtSound", at = @At(value = "HEAD"))
  private void test(net.minecraft.world.damagesource.DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
    if (damageSource == DamageSource.ON_SOUL_FIRE) {
      cir.setReturnValue(SoundEvents.PLAYER_HURT_ON_FIRE);
    }
  }
}
