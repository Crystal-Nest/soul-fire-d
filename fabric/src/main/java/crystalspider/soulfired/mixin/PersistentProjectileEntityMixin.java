package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper;
import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper.FireEnchantment;
import crystalspider.soulfired.api.enchantment.FireTypedFlameEnchantment;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

/**
 * Injects into {@link PersistentProjectileEntity} to alter Fire behavior for consistency.
 */
@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity implements FireTypeChanger {
  public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
    super(entityType, world);
  }

  /**
   * Redirects the call to {@link Entity#setOnFireFor(int)} inside the method {@link PersistentProjectileEntity#onEntityHit(EntityHitResult)}.
   * <p>
   * Sets the correct Fire Type for the Entity.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method.
   * @param seconds seconds the entity should be set on fire for.
   */
  @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V"))
  private void redirectSetOnFireFor(Entity caller, int seconds) {
    FireTypedFlameEnchantment flame = FireManager.getFlame(getFireType());
    FireManager.setOnFire(caller, flame != null ? flame.duration(this.getOwner(), caller, seconds) : seconds, getFireType());
  }

  /**
   * Redirects the call to {@link PersistentProjectileEntity#setOnFireFor(int)} inside the method {@link PersistentProjectileEntity#applyEnchantmentEffects(LivingEntity, float)}.
   * <p>
   * Handles setting this arrow on the correct kind of fire, if any.
   * 
   * @param caller {@link AbstractArrow} invoking (owning) the redirected method. It's the same as {@code this}.
   * @param seconds seconds the arrow should be set on fire for.
   * @param entity {@link LivingEntity}, a mob, shooting the arrow.
   */
  @Redirect(method = "applyEnchantmentEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setOnFireFor(I)V"))
  private void redirectSetOnFireFor(PersistentProjectileEntity caller, int seconds, LivingEntity entity) {
    FireEnchantment fireEnchantment = FireEnchantmentHelper.getWhichFlame(entity);
    if (fireEnchantment.isApplied()) {
      FireManager.setOnFire(caller, seconds, fireEnchantment.getFireType());
    }
  }
}
