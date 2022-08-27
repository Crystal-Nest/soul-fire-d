package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.commands.CommandSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.extensions.IForgeEntity;

@Mixin(Entity.class)
public abstract class EntityMixin extends CapabilityProvider<Entity> implements Nameable, EntityAccess, CommandSource, IForgeEntity, FireTypeChanger {
  @Shadow
  public Level level;
  @Shadow
  protected SynchedEntityData entityData;

  private static final EntityDataAccessor<String> DATA_FIRE_ID = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.STRING);

  EntityMixin() {
    super(Entity.class);
  }

  @Shadow
  protected abstract void defineSynchedData();
  @Shadow
  public abstract int getRemainingFireTicks();
  @Shadow
  public abstract boolean isInLava();

  @Override
  public void setFireId(String fireId) {
    entityData.set(DATA_FIRE_ID, fireId != null ? fireId.trim() : "");
  }

  @Override
  public String getFireId() {
    return entityData.get(DATA_FIRE_ID);
  }

  @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
  private boolean redirectHurt(Entity caller, DamageSource damageSource, float damage) {
    return FireManager.hurtEntityOnFire(caller, ((FireTyped) caller).getFireId(), damageSource, damage);
  }

  @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;defineSynchedData()V"))
  private void redirectDefineSynchedData(Entity caller) {
    entityData.define(DATA_FIRE_ID, "");
    defineSynchedData();
  }

  @Inject(method = "setRemainingFireTicks", at = @At(value = "HEAD"))
  private void onSetRemainingFireTicks(int ticks, CallbackInfo ci) {
    if (!level.isClientSide && (ticks <= 0 || ticks >= getRemainingFireTicks())) {
      setFireId(FireManager.DEFAULT_FIRE_ID);
    }
  }

  @Inject(method = "saveWithoutId", at = @At(value = "TAIL"))
  private void onSaveWithoutId(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
    if (FireManager.isFireId(getFireId())) {
      tag.putString("FireId", getFireId());
    }
  }

  @Inject(method = "load", at = @At(value = "TAIL"))
  private void onLoad(CompoundTag tag, CallbackInfo ci) {
    setFireId(FireManager.ensureFireId(tag.getString("FireId")));
  }
}
