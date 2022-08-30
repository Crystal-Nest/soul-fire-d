package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

/**
 * Injects into {@link AbstractArrowEntity} to alter Fire behavior for consistency.
 */
@Mixin(AbstractArrowEntity.class)
public abstract class AbstractArrowEntityMixin extends Entity implements FireTypeChanger {
  /**
   * Useless constructor required by the super class to make the compiler happy.
   * 
   * @param entityType
   * @param world
   */
  public AbstractArrowEntityMixin(EntityType<?> entityType, World world) {
    super(entityType, world);
  }

  /**
   * Redirects the call to {@link Entity#setSecondsOnFire(int)} inside the method {@link AbstractArrowEntity#onHitEntity(EntityRayTraceResult)}.
   * <p>
   * Sets the correct FireId for the Entity.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method.
   * @param seconds seconds the entity should be set on fire for.
   */
  @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setSecondsOnFire(I)V"))
  private void redirectSetSecondsOnFire(Entity caller, int seconds) {
    caller.setSecondsOnFire(seconds);
    ((FireTypeChanger) caller).setFireId(getFireId());
  }

  /**
   * Injects at the end of the method {@link AbstractArrowEntity#setEnchantmentEffectsFromEntity(LivingEntity, float)}.
   * <p>
   * Sets this arrow on fire for 100 seconds with the correct FireId if it was shot by a mob with an item enchanted with a custom fire enchantment.
   * 
   * @param entity {@link LivingEntity}, a mob, shooting the arrow.
   * @param damage arrow damage modifier.
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "setEnchantmentEffectsFromEntity", at = @At(value = "TAIL"))
  private void onSetEnchantmentEffectsFromEntity(LivingEntity entity, float damageModifier, CallbackInfo ci) {
    for (Enchantment enchantment : FireManager.getFlames()) {
      if (EnchantmentHelper.getEnchantmentLevel(enchantment, entity) > 0) {
        setSecondsOnFire(100);
        setFireId(((FireTyped) enchantment).getFireId());
        break;
      }
    }
  }
}
