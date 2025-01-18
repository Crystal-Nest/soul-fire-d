package it.crystalnest.soul_fire_d.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.block.CustomFireBlock;
import it.crystalnest.soul_fire_d.api.type.FireTypeChanger;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Injects into {@link BaseFireBlock} to alter Fire behavior for consistency.
 */
@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin implements FireTypeChanger {
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
   * Conditionally modifies the return value of {@link BaseFireBlock#getState(BlockGetter, BlockPos)}.<br>
   * Returns the most appropriate fire {@link BlockState}.
   *
   * @param original the original block state.
   * @param level level.
   * @param pos position.
   */
  @ModifyReturnValue(method = "getState", at = @At(value = "RETURN"))
  private static BlockState onGetState(BlockState original, BlockGetter level, BlockPos pos) {
    return FireManager.getComponentList(Fire.Component.SOURCE_BLOCK).stream()
      .filter(source -> source instanceof CustomFireBlock customFireBlock && customFireBlock.canSurvive(level.getBlockState(pos.below())))
      .findFirst().map(Block::defaultBlockState).orElse(original);
  }

  /**
   * Redirects the call to {@link Entity#hurt(DamageSource, float)} inside the method {@link BaseFireBlock#entityInside(BlockState, Level, BlockPos, Entity)}.<br>
   * Hurts the entity with the correct fire damage and {@link DamageSource}.
   *
   * @param instance {@link Entity} invoking (owning) the redirected method.
   * @param damageSource original {@link DamageSource} (normal fire).
   * @param damage original damage (normal fire).
   */
  @WrapOperation(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)V"))
  private void redirectHurt(Entity instance, DamageSource damageSource, float damage, Operation<Void> original) {
    FireManager.affect(instance, getFireType(), Fire::getInFire, original::call, true);
  }
}
