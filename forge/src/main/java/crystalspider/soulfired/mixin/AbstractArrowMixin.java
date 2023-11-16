package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper;
import crystalspider.soulfired.api.enchantment.FireEnchantmentHelper.FireEnchantment;
import crystalspider.soulfired.api.enchantment.FireTypedFlameEnchantment;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Injects into {@link AbstractArrow} to alter Fire behavior for consistency.
 */
@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile implements FireTypeChanger {
  private AbstractArrowMixin(EntityType<? extends Projectile> entityType, Level world) {
    super(entityType, world);
  }

  /**
   * Redirects the call to {@link Entity#setSecondsOnFire(int)} inside the method {@link AbstractArrow#onHitEntity(EntityHitResult)}.
   * <p>
   * Sets the correct Fire Type for the Entity.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method.
   * @param seconds seconds the entity should be set on fire for.
   */
  @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setSecondsOnFire(I)V"))
  private void redirectSetSecondsOnFire(Entity caller, int seconds) {
    FireTypedFlameEnchantment flame = FireManager.getFlame(getFireType());
    FireManager.setOnFire(caller, flame != null ? flame.duration(this.getOwner(), caller, seconds) : seconds, getFireType());
  }

  /**
   * Redirects the call to {@link AbstractArrow#setSecondsOnFire(int)} inside the method {@link AbstractArrow#setEnchantmentEffectsFromEntity(LivingEntity, float)}.
   * <p>
   * Handles setting this arrow on the correct kind of fire, if any.
   * 
   * @param caller {@link AbstractArrow} invoking (owning) the redirected method. It's the same as {@code this}.
   * @param seconds seconds the arrow should be set on fire for.
   * @param entity {@link LivingEntity}, a mob, shooting the arrow.
   */
  @Redirect(method = "setEnchantmentEffectsFromEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setSecondsOnFire(I)V"))
  private void redirectSetSecondsOnFire(AbstractArrow caller, int seconds, LivingEntity entity) {
    FireEnchantment fireEnchantment = FireEnchantmentHelper.getWhichFlame(entity);
    if (fireEnchantment.isApplied()) {
      FireManager.setOnFire(caller, seconds, fireEnchantment.getFireType());
    }
  }
}
