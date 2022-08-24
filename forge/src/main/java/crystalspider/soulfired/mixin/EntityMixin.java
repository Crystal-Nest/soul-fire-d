package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.imixin.SoulFiredEntity;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.extensions.IForgeEntity;

@Mixin(Entity.class)
public abstract class EntityMixin extends CapabilityProvider<Entity> implements Nameable, EntityAccess, CommandSource, IForgeEntity, SoulFiredEntity {
  private String fireId;

  EntityMixin() {
    super(Entity.class);
  }

  @Override
  public void setFireId(String fireId) {
    this.fireId = fireId;
  }

  @Override
  public String getFireId() {
    return fireId;
  }

  @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
  private boolean redirectHurt(Entity caller, DamageSource damageSource, float damage) {
    // [caller] should always be [this]. May be worth changing annotation and avoid casts and [caller] accesses and use directly [this] instead.
    String fireId = ((SoulFiredEntity) caller).getFireId();
    if (FireManager.isFireId(fireId)) {
      return caller.hurt(FireManager.getInFireDamageSource(fireId), FireManager.getDamage(fireId));
    }
    ((SoulFiredEntity) caller).setFireId(null);
    return caller.hurt(damageSource, damage);
  }

  @Inject(method = "setRemainingFireTicks", at = @At(value = "HEAD"))
  private void onSetRemainingFireTicks(int ticks, CallbackInfo ci) {
    if (ticks <= 0) {
      setFireId(null);
    }
  }
}
