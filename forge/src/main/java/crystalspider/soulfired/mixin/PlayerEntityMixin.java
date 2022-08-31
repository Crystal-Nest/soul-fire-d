package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * Injects into {@link PlayerEntity} to alter Fire behavior for consistency.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements FireTyped {
  /**
   * Useless constructor required by the super class to make the compiler happy.
   * 
   * @param entityType
   * @param world
   */
  private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
    super(entityType, world);
  }

  /**
   * Injects at the start of the method {@link PlayerEntity#getHurtSound(DamageSource)}.
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
   * Modifies the assignment value returned by {@link EnchantmentHelper#getFireAspect(LivingEntity)} in the method {@link PlayerEntity#attack(Entity)}.
   * <p>
   * Returns the proper Fire Aspect level taking into account all possible Fire Aspect enchantments.
   * 
   * @param level level the basic Fire Aspect enchantment is at.
   */
  @ModifyVariable(method = "attack", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I"))
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
