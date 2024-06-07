package it.crystalnest.soul_fire_d.mixin;

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
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

  @Inject(method = "getState", at = @At(value = "RETURN"), cancellable = true)
  private static void test(BlockGetter level, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
    FireManager.getFires()
      .stream().filter(fire -> fire.getCampfire().isPresent() && FireManager.getSourceBlock(fire.getFireType()) instanceof CustomFireBlock customFireBlock && customFireBlock.canSurvive(level.getBlockState(pos.below())))
      .map(fire -> (CustomFireBlock) FireManager.getSourceBlock(fire.getFireType()))
      .findFirst()
      .ifPresent(fireBlock -> cir.setReturnValue(fireBlock.defaultBlockState()));
  }

  /**
   * Redirects the call to {@link Entity#hurt(DamageSource, float)} inside the method {@link BaseFireBlock#entityInside(BlockState, Level, BlockPos, Entity)}.<br />
   * Hurts the entity with the correct fire damage and {@link DamageSource}.
   *
   * @param instance {@link Entity} invoking (owning) the redirected method.
   * @param damageSource original {@link DamageSource} (normal fire).
   * @param damage original damage (normal fire).
   * @return the result of calling the redirected method.
   */
  @Redirect(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
  private boolean redirectHurt(Entity instance, DamageSource damageSource, float damage) {
    return FireManager.damageInFire(instance, getFireType());
  }
}
