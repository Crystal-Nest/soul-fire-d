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
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Injects into {@link Entity} to alter Fire behavior for consistency.
 */
@Mixin(Entity.class)
public abstract class EntityMixin implements FireTypeChanger {
  /**
   * Shadowed {@link Entity#level}.
   */
  @Shadow
  public World level;
  /**
   * Shadowed {@link Entity#entityData}.
   */
  @Shadow
  protected EntityDataManager entityData;

  /**
   * {@link DataParameter} to synchronize the Fire Type across client and server.
   */
  private static final DataParameter<String> DATA_FIRE_TYPE = EntityDataManager.defineId(Entity.class, DataSerializers.STRING);

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
  @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;hurt(Lnet/minecraft/util/DamageSource;F)Z"))
  private boolean redirectHurt(Entity caller, DamageSource damageSource, float damage) {
    return FireManager.damageOnFire(caller, ((FireTyped) caller).getFireType());
  }
  
  /**
   * Redirects the call to {@link Entity#setSecondsOnFire(int)} inside the method {@link Entity#lavaHurt()}.
   * <p>
   * Sets the base fire id.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method. It's the same as {@code this} entity.
   * @param seconds seconds to set the entity on fire for.
   */
  @Redirect(method = "lavaHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setSecondsOnFire(I)V"))
  private void redirectSetSecondsOnFire(Entity caller, int seconds) {
    FireManager.setOnFire(caller, seconds, FireManager.DEFAULT_FIRE_TYPE);
  }

  /**
   * Injects at the end of the constructor.
   * <p>
   * Defines the {@link #DATA_FIRE_TYPE Fire Id data} to synchronize across client and server.
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
   * Resets the FireId when this entity stops burning or catches fire from a new fire source.
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
   * Injects in the method {@link Entity#saveWithoutId(CompoundNBT)} before the invocation of {@link Entity#addAdditionalSaveData(CompoundNBT)}.
   * <p>
   * If valid, saves the current FireId in the given {@link CompoundNBT}.
   * 
   * @param tag
   * @param cir {@link CallbackInfoReturnable}.
   */
  @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundNBT;)V"))
  private void onSaveWithoutId(CompoundNBT tag, CallbackInfoReturnable<CompoundNBT> cir) {
    if (FireManager.isRegisteredType(getFireType())) {
      tag.putString("FireType", getFireType().toString());
    }
  }

  /**
   * Injects in the method {@link Entity#load(CompoundNBT)} before the invocation of {@link Entity#readAdditionalSaveData(CompoundNBT)}.
   * <p>
   * Loads the FireId from the given {@link CompoundNBT}.
   * 
   * @param tag
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundNBT;)V"))
  private void onLoad(CompoundNBT tag, CallbackInfo ci) {
    setFireType(FireManager.ensure(ResourceLocation.tryParse(tag.getString("FireType"))));
  }
}
