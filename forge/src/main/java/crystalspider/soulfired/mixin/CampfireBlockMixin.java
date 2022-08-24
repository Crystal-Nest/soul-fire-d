package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.imixin.SoulFiredEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends BaseEntityBlock implements SimpleWaterloggedBlock, SoulFiredEntity {
  private String fireId;

  private CampfireBlockMixin(Properties properties) {
    super(properties);
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

  @Redirect(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
  private boolean redirectHurt(Entity caller, DamageSource damageSource, float damage) {
    // TODO: implement fireId
    if (FireManager.isFireId(fireId)) {
      ((SoulFiredEntity) caller).setFireId(fireId);
      return caller.hurt(FireManager.getInFireDamageSource(fireId), FireManager.getDamage(fireId));
    }
    ((SoulFiredEntity) caller).setFireId(null);
    return caller.hurt(damageSource, damage);
  }
}
