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
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

/**
 * Injects into {@link AbstractArrowEntity} to alter Fire behavior for consistency.
 */
@Mixin(AbstractArrowEntity.class)
public abstract class AbstractArrowEntityMixin extends ProjectileEntity implements FireTypeChanger {
  private AbstractArrowEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
    super(entityType, world);
  }

  /**
   * Redirects the call to {@link Entity#setSecondsOnFire(int)} inside the method {@link AbstractArrowEntity#onHitEntity(EntityRayTraceResult)}.
   * <p>
   * Sets the correct Fire Type for the Entity.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method.
   * @param seconds seconds the entity should be set on fire for.
   */
  @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setSecondsOnFire(I)V"))
  private void redirectSetSecondsOnFire(Entity caller, int seconds) {
    FireTypedFlameEnchantment flame = FireManager.getFlame(getFireType());
    FireManager.setOnFire(caller, flame != null ? flame.duration(this.getOwner(), caller, seconds) : seconds, getFireType());
  }

  /**
   * Redirects the call to {@link AbstractArrowEntity#setSecondsOnFire(int)} inside the method {@link AbstractArrowEntity#setEnchantmentEffectsFromEntity(LivingEntity, float)}.
   * <p>
   * Handles setting this arrow on the correct kind of fire, if any.
   * 
   * @param caller {@link AbstractArrowEntity} invoking (owning) the redirected method. It's the same as {@code this}.
   * @param seconds seconds the arrow should be set on fire for.
   * @param entity {@link LivingEntity}, a mob, shooting the arrow.
   */
  @Redirect(method = "setEnchantmentEffectsFromEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/AbstractArrowEntity;setSecondsOnFire(I)V"))
  private void redirectSetSecondsOnFire(AbstractArrowEntity caller, int seconds, LivingEntity entity) {
    FireEnchantment fireEnchantment = FireEnchantmentHelper.getWhichFlame(entity);
    if (fireEnchantment.isApplied()) {
      FireManager.setOnFire(caller, seconds, fireEnchantment.getFireType());
    }
  }
}
