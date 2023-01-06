package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Injects into {@link CampfireBlock} to alter Fire behavior for consistency.
 */
@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin implements FireTypeChanger {
  /**
   * Fire Type.
   */
  private ResourceLocation fireType;

  @Override
  public void setFireType(ResourceLocation fireType) {
    this.fireType = fireType;
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }

  /**
   * Redirects the call to {@link Entity#hurt(DamageSource, float)} inside the method {@link CampfireBlock#entityInside(BlockState, World, BlockPos, Entity)}.
   * <p>
   * Hurts the entity with the correct fire damage and {@link DamageSource}.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method.
   * @param damageSource original {@link DamageSource} (normale fire).
   * @param damage original damage (normal fire).
   * @return the result of calling the redirected method.
   */
  @Redirect(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;hurt(Lnet/minecraft/util/DamageSource;F)Z"))
  private boolean redirectHurt(Entity caller, DamageSource damageSource, float damage) {
    return FireManager.damageInFire(caller, getFireType());
  }
}
