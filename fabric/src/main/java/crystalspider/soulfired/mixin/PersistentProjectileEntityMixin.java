package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper;
import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper.FireEnchantment;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;

/**
 * Injects into {@link PersistentProjectileEntity} to alter Fire behavior for consistency.
 */
@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin implements FireTypeChanger {
  /**
   * Redirects the call to {@link Entity#setOnFireFor(int)} inside the method {@link PersistentProjectileEntity#onEntityHit(EntityHitResult)}.
   * <p>
   * Sets the correct FireId for the Entity.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method.
   * @param seconds seconds the entity should be set on fire for.
   */
  @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V"))
  private void redirectSetOnFireFor(Entity caller, int seconds) {
    FireManager.setOnFire(caller, seconds, getFireId());
  }

  /**
   * Redirects the call to {@link PersistentProjectileEntity#setOnFireFor(int)} inside the method {@link PersistentProjectileEntity#applyEnchantmentEffects(LivingEntity, float)}.
   * <p>
   * Handles setting this arrow on the correct kind of fire, if any.
   * 
   * @param entity {@link LivingEntity}, a mob, shooting the arrow.
   * @param damage arrow damage modifier.
   * @param ci {@link CallbackInfo}.
   */
  @Redirect(method = "applyEnchantmentEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setOnFireFor(I)V"))
  private void redirectSetOnFireFor(PersistentProjectileEntity caller, int seconds, LivingEntity entity) {
    FireEnchantment fireEnchantment = FireEnchantmentHelper.getWhichFlame(entity);
    if (fireEnchantment.isApplied()) {
      FireManager.setOnFire(caller, seconds, fireEnchantment.getFireId());
    }
  }
}
