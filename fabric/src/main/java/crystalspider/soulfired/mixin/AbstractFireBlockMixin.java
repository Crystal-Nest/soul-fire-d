package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;


/**
 * Injects into {@link AbstractFireBlock} to alter Fire behavior for consistency.
 */
@Mixin(AbstractFireBlock.class)
public abstract class AbstractFireBlockMixin extends Block implements FireTypeChanger {
  /**
   * Fire Id.
   */
  private String fireId;

  /**
   * Useless constructor required by the super class to make the compiler happy.
   * 
   * @param settings
   */
  private AbstractFireBlockMixin(Settings settings) {
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
  private boolean redirectHurt(Entity caller, DamageSource damageSource, float damage) {
    return FireManager.damageInFire(caller, getFireId(), damageSource, damage);
  }
}
