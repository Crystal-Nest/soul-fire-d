package it.crystalnest.soul_fire_d.mixin;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTypeSynched;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SyncedDataHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Injects into {@link SynchedEntityData.Builder} to alter Fire behavior for consistency.
 */
@Mixin(SynchedEntityData.Builder.class)
public abstract class SynchedEntityDataBuilderMixin {
  @Final
  @Shadow
  private SyncedDataHolder entity;

  /**
   * Shadowed {@link SynchedEntityData.Builder#define(EntityDataAccessor, Object)}.
   *
   * @param accessor {@link EntityDataAccessor}.
   * @param value value.
   * @return builder.
   * @param <T> data type.
   */
  @Shadow
  public abstract <T> SynchedEntityData.Builder define(EntityDataAccessor<T> accessor, T value);

  /**
   * Injects at the end of the constructor.<br />
   * Defines the Fire Type data accessor to synchronize across client and server.
   *
   * @param ci {@link CallbackInfoReturnable}.
   */
  @Inject(method = "<init>", at = @At("TAIL"))
  private void onInit(SyncedDataHolder pEntity, CallbackInfo ci) {
    define(((FireTypeSynched) entity).fireTypeAccessor(), FireManager.DEFAULT_FIRE_TYPE.toString());
  }
}
