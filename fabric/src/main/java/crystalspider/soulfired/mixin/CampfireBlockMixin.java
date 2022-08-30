package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Injects into {@link CampfireBlock} to alter Fire behavior for consistency.
 */
@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends BlockWithEntity implements Waterloggable, FireTypeChanger {
  /**
   * Fire Id.
   */
  private String fireId;

  /**
   * Useless constructor required by the super class to make the compiler happy.
   * 
   * @param settings
   */
  private CampfireBlockMixin(Settings settings) {
    super(settings);
  }

  @Override
  public void setFireId(String id) {
    if (FireManager.isValidFireId(id)) {
      fireId = id;
    }
  }

  @Override
  public String getFireId() {
    return fireId;
  }

  /**
   * Redirects the call to {@link Entity#damage(DamageSource, float)} inside the method {@link CampfireBlock#onEntityCollision(BlockState, World, BlockPos, Entity)}.
   * <p>
   * Hurts the entity with the correct fire damage and {@link DamageSource}.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method.
   * @param damageSource original {@link DamageSource} (normale fire).
   * @param damage original damage (normal fire).
   * @return the result of calling the redirected method.
   */
  @Redirect(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
  private boolean redirectDamage(Entity caller, DamageSource damageSource, float damage) {
    if (this == Blocks.SOUL_CAMPFIRE && !FireManager.isFireId(getFireId())) {
      setFireId(FireManager.SOUL_FIRE_ID);
    }
    return FireManager.damageInFire(caller, getFireId(), damageSource, damage);
  }
}
