package it.crystalnest.soul_fire_d.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTypeChanger;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Injects into {@link AbstractArrow} to alter Fire behavior for consistency.
 */
@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin implements FireTypeChanger {
  /**
   * Wraps the call to {@link Entity#igniteForSeconds(float)} inside the method {@link AbstractArrow#onHitEntity(EntityHitResult)}.<br>
   * Sets the correct Fire Type for the Entity.
   *
   * @param instance {@link Entity} invoking (owning) the redirected method.
   * @param seconds seconds the entity should be set on fire for.
   * @param original the original call that is being wrapped.
   */
  @WrapOperation(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;igniteForSeconds(F)V"))
  private void redirectSetSecondsOnFire(Entity instance, float seconds, Operation<Void> original) {
    FireManager.setOnFire(instance, seconds, getFireType(), original::call);
  }
}
