package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.FireTyped;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;

@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin extends Block implements FireTyped {
  private String fireId;

  private BaseFireBlockMixin(Properties properties) {
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
    return FireManager.hurtEntity(caller, getFireId(), damageSource, damage);
  }
}
