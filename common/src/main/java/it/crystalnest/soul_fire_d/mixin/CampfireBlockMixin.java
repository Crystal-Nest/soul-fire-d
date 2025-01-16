package it.crystalnest.soul_fire_d.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTypeChanger;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Injects into {@link CampfireBlock} to alter Fire behavior for consistency.
 */
@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin implements FireTypeChanger {
  /**
   * Fire Type.
   */
  @Unique
  private ResourceLocation fireType;

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }

  @Override
  public void setFireType(ResourceLocation fireType) {
    this.fireType = fireType;
  }

  /**
   * Wraps the call to {@link Entity#hurt(DamageSource, float)} inside the method {@link CampfireBlock#entityInside(BlockState, Level, BlockPos, Entity)}.<br>
   * Hurts the entity with the correct fire damage and {@link DamageSource}.
   *
   * @param instance owner of the redirected method.
   * @param damageSource original {@link DamageSource} (normal fire).
   * @param damage original damage (normal fire).
   * @param original original {@link Operation} being wrapped.
   * @return the result of calling the redirected method.
   */
  @WrapOperation(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
  private boolean redirectHurt(Entity instance, DamageSource damageSource, float damage, Operation<Boolean> original) {
    return FireManager.damageInFire(instance, getFireType(), original::call);
  }
}
