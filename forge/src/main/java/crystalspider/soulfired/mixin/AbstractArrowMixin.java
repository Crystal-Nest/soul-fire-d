package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Injects into {@link AbstractArrow} to alter Fire behavior for consistency.
 */
@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile implements FireTypeChanger {
  /**
   * Useless constructor required by the super class to make the compiler happy.
   * 
   * @param entityType
   * @param world
   */
  private AbstractArrowMixin(EntityType<? extends Projectile> entityType, Level world) {
    super(entityType, world);
  }

  /**
   * Redirects the call to {@link Entity#setSecondsOnFire(int)} inside the method {@link AbstractArrow#onHitEntity(EntityHitResult)}.
   * <p>
   * Sets the correct FireId for the Entity.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method.
   * @param seconds seconds the entity should be set on fire for.
   */
  @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setSecondsOnFire(I)V"))
  private void redirectSetSecondsOnFire(Entity caller, int seconds) {
    caller.setSecondsOnFire(seconds);
    ((FireTypeChanger) caller).setFireId(getFireId());
  }

  /**
   * Injects at the end of the method {@link AbstractArrow#setEnchantmentEffectsFromEntity(LivingEntity, float)}.
   * <p>
   * Sets this arrow on fire for 100 seconds with the correct FireId if it was shot by a mob with an item enchanted with a custom fire enchantment.
   * 
   * @param entity {@link LivingEntity}, a mob, shooting the arrow.
   * @param damage arrow base damage.
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "setEnchantmentEffectsFromEntity", at = @At(value = "TAIL"))
  private void onSetEnchantmentEffectsFromEntity(LivingEntity entity, float damage, CallbackInfo ci) {
    for (Enchantment enchantment : FireManager.getFlames()) {
      if (EnchantmentHelper.getEnchantmentLevel(enchantment, entity) > 0) {
        setSecondsOnFire(100);
        setFireId(((FireTyped) enchantment).getFireId());
        break;
      }
    }
  }
}
