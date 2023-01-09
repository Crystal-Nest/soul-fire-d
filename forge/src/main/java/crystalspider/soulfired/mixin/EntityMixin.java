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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * Injects into {@link Entity} to alter Fire behavior for consistency.
 */
@Mixin(Entity.class)
public abstract class EntityMixin implements FireTypeChanger {
  /**
   * Shadowed {@link Entity#level}.
   */
  @Shadow
  public Level level;
  /**
   * Shadowed {@link Entity#entityData}.
   */
  @Shadow
  protected SynchedEntityData entityData;

  /**
   * {@link EntityDataAccessor} to synchronize the Fire Type across client and server.
   */
  private static final EntityDataAccessor<String> DATA_FIRE_TYPE = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.STRING);

  /**
   * Shadowed {@link Entity#getRemainingFireTicks()}.
   * 
   * @return the remaining ticks the entity is set to burn for.
   */
  @Shadow
  public abstract int getRemainingFireTicks();
  /**
   * Shadowed {@link Entity#isInLava()}.
   * 
   * @return whether this entity is in lava.
   */
  @Shadow
  public abstract boolean isInLava();
  /**
   * Shadowed {@link Entity#fireImmune()}.
   * 
   * @return whether this entity is immune to fire damage.
   */
  @Shadow
  public abstract boolean fireImmune();

  @Override
  public void setFireType(ResourceLocation fireType) {
    if (!this.fireImmune()) {
      entityData.set(DATA_FIRE_TYPE, FireManager.ensure(fireType).toString());
    }
  }

  @Override
  public ResourceLocation getFireType() {
    return ResourceLocation.tryParse(entityData.get(DATA_FIRE_TYPE));
  }

  /**
   * Redirects the call to {@link Entity#hurt(DamageSource, float)} inside the method {@link Entity#baseTick()}.
   * <p>
   * Hurts the entity with the correct fire damage and {@link DamageSource}.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method. It's the same as {@code this} entity.
   * @param damageSource original {@link DamageSource} (normal fire).
   * @param damage original damage (normal fire).
   * @return the result of calling the redirected method.
   */
  @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
  private boolean redirectHurt(Entity caller, DamageSource damageSource, float damage) {
    return FireManager.damageOnFire(caller, ((FireTyped) caller).getFireType());
  }
  
  /**
   * Redirects the call to {@link Entity#setSecondsOnFire(int)} inside the method {@link Entity#lavaHurt()}.
   * <p>
   * Sets the base Fire Type.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method. It's the same as {@code this} entity.
   * @param seconds seconds to set the entity on fire for.
   */
  @Redirect(method = "lavaHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setSecondsOnFire(I)V"))
  private void redirectSetSecondsOnFire(Entity caller, int seconds) {
    FireManager.setOnFire(caller, seconds, FireManager.DEFAULT_FIRE_TYPE);
  }

  /**
   * Injects at the end of the constructor.
   * <p>
   * Defines the {@link #DATA_FIRE_TYPE Fire Type data} to synchronize across client and server.
   * 
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "<init>", at = @At("TAIL"))
  private void redirectDefineSynchedData(CallbackInfo ci) {
    entityData.define(DATA_FIRE_TYPE, FireManager.DEFAULT_FIRE_TYPE.toString());
  }

  /**
   * Injects at the start of the method {@link Entity#setRemainingFireTicks(int)}.
   * <p>
   * Resets the Fire Type when this entity stops burning or catches fire from a new fire source.
   * 
   * @param ticks ticks this entity should burn for.
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "setRemainingFireTicks", at = @At(value = "HEAD"))
  private void onSetRemainingFireTicks(int ticks, CallbackInfo ci) {
    if (!level.isClientSide && ticks >= getRemainingFireTicks()) {
      setFireType(FireManager.DEFAULT_FIRE_TYPE);
    }
  }

  /**
   * Injects in the method {@link Entity#saveWithoutId(CompoundTag)} before the invocation of {@link Entity#addAdditionalSaveData(CompoundTag)}.
   * <p>
   * If valid, saves the current Fire Type in the given {@link CompoundTag}.
   * 
   * @param tag
   * @param cir {@link CallbackInfoReturnable}.
   */
  @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
  private void onSaveWithoutId(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
    FireManager.writeNbt(tag, getFireType());
  }

  /**
   * Injects in the method {@link Entity#load(CompoundTag)} before the invocation of {@link Entity#readAdditionalSaveData(CompoundTag)}.
   * <p>
   * Loads the Fire Type from the given {@link CompoundNBT}.
   * 
   * @param tag
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
  private void onLoad(CompoundTag tag, CallbackInfo ci) {
    setFireType(FireManager.readNbt(tag));
  }
}
