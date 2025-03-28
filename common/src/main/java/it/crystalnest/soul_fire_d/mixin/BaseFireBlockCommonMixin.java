package it.crystalnest.soul_fire_d.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.block.CustomFireBlock;
import it.crystalnest.soul_fire_d.api.type.FireTypeChanger;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
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
public abstract class BaseFireBlockCommonMixin implements FireTypeChanger {
  /**
   * Fire Type.
   */
  @Unique
  private ResourceLocation fireType;

  /**
   * Modifies the return value of {@link BaseFireBlock#getState(BlockGetter, BlockPos)}.<br>
   * Returns the most appropriate fire {@link BlockState}.
   *
   * @param original original block state.
   * @param level level.
   * @param pos position.
   */
  @ModifyReturnValue(method = "getState", at = @At(value = "RETURN"))
  private static BlockState modifyGetState(BlockState original, BlockGetter level, BlockPos pos) {
    return FireManager.getComponentList(Fire.Component.SOURCE_BLOCK).stream().filter(source -> canSurvive(source, level.getBlockState(pos.below()))).findFirst().map(Block::defaultBlockState).orElse(original);
  }

  /**
   * Checks whether the given {@link Block} can burn on the given base.
   *
   * @param source fire source block.
   * @param base block base.
   * @return whether the source can burn on the base.
   */
  @Unique
  private static boolean canSurvive(Block source, BlockState base) {
    return source instanceof CustomFireBlock customFireBlock && customFireBlock.canSurvive(base);
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }

  @Override
  public void setFireType(ResourceLocation fireType) {
    this.fireType = fireType;
  }
}
