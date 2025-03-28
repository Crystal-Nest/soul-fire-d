package it.crystalnest.soul_fire_d.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTypeChanger;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Injects into {@link BaseFireBlock} to alter Fire behavior for consistency.
 */
@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin implements FireTypeChanger {
  /**
   * Wraps the call to {@link Entity#hurt(DamageSource, float)} inside the method {@link BaseFireBlock#entityInside(BlockState, Level, BlockPos, Entity, InsideBlockEffectApplier)}.<br>
   * Hurts the entity with the correct fire damage and {@link DamageSource}.
   *
   * @param instance {@link Entity} invoking (owning) the redirected method.
   * @param damageSource original {@link DamageSource} (normal fire).
   * @param damage original damage (normal fire).
   */
  @WrapOperation(method = "method_67679", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)V"))
  private void wrapHurt(Entity instance, DamageSource damageSource, float damage, Operation<Void> original) {
    FireManager.affect(instance, getFireType(), Fire::getInFire, (TriFunction<Entity, DamageSource, Float, Void>) original::call);
  }
}
