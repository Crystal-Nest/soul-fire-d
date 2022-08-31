package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

/**
 * Injects into {@link Player} to alter Fire behavior for consistency.
 */
@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements FireTyped {
  /**
   * Useless constructor required by the super class to make the compiler happy.
   * 
   * @param entityType
   * @param world
   */
  private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
    super(entityType, world);
  }

  /**
   * Injects at the start of the method {@link Player#getHurtSound(DamageSource)}.
   * <p>
   * If {@code damageSource} is a custom Fire DamageSource, forces the target method to return the correct hurt sound.
   * 
   * @param damageSource {@link DamageSource} causing the damage.
   * @param cir {@link CallbackInfoReturnable}.
   */
  @Inject(method = "getHurtSound", at = @At(value = "HEAD"))
  private void onGetHurtSound(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
    if (FireManager.isFireDamageSource(damageSource) && cir.isCancellable()) {
      cir.setReturnValue(FireManager.getHurtSound(getFireId()));
    }
  }

  /**
   * Modifies the assignment value returned by {@link EnchantmentHelper#getFireAspect(LivingEntity)} in the method {@link Player#attack()}.
   * <p>
   * Returns the proper Fire Aspect level taking into account all possible Fire Aspect enchantments.
   * 
   * @param level level the basic Fire Aspect enchantment is at.
   */
  @ModifyVariable(method = "attack", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/world/entity/LivingEntity;)I"))
  private int onAttack(int level) {
    int fireAspectLevel = level;
    if (fireAspectLevel <= 0) {
      for (Enchantment fireAspect : FireManager.getFireAspects()) {
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(fireAspect, this);
        if (enchantmentLevel > 0) {
          fireAspectLevel = enchantmentLevel;
          break;
        }
      }
    }
    return fireAspectLevel;
  }
}
