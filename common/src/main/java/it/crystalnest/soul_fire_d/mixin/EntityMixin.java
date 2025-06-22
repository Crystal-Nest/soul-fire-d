package it.crystalnest.soul_fire_d.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.crystalnest.soul_fire_d.QuadriFunction;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTypeSynched;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects into {@link Entity} to alter Fire behavior for consistency.
 */
@Mixin(Entity.class)
public abstract class EntityMixin implements FireTypeSynched {
  /**
   * {@link EntityDataAccessor} to synchronize the Fire Type across client and server.
   */
  @Unique
  private static final EntityDataAccessor<String> DATA_FIRE_TYPE = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.STRING);

  /**
   * Shadowed {@link Entity#entityData}.
   */
  @Final
  @Shadow
  protected SynchedEntityData entityData;

  /**
   * Shadowed {@link Entity#level}.
   */
  @Shadow
  private Level level;

  /**
   * Shadowed {@link Entity#getRemainingFireTicks()}.
   *
   * @return the remaining ticks the entity is set to burn for.
   */
  @Shadow
  public abstract int getRemainingFireTicks();

  /**
   * Shadowed {@link Entity#fireImmune()}.
   *
   * @return whether this entity is immune to fire damage.
   */
  @Shadow
  public abstract boolean fireImmune();

  @Override
  public ResourceLocation getFireType() {
    return ResourceLocation.tryParse(entityData.get(DATA_FIRE_TYPE));
  }

  @Override
  public void setFireType(ResourceLocation fireType) {
    if (!this.fireImmune()) {
      entityData.set(DATA_FIRE_TYPE, FireManager.ensure(fireType).toString());
    }
  }

  @Override
  public EntityDataAccessor<String> fireTypeAccessor() {
    return DATA_FIRE_TYPE;
  }

  /**
   * Wraps the call to {@link Entity#hurtServer(ServerLevel, DamageSource, float)} inside the method {@link Entity#baseTick()}.<br>
   * Hurts the entity with the correct fire damage and {@link DamageSource}.
   *
   * @param instance owner of the redirected method.
   * @param damageSource original {@link DamageSource} (normal fire).
   * @param damage original damage (normal fire).
   * @param original original {@link Operation} being wrapped.
   * @return the result of calling the redirected method.
   */
  @WrapOperation(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
  private boolean wrapHurtServer(Entity instance, ServerLevel level, DamageSource damageSource, float damage, Operation<Boolean> original) {
    return FireManager.affect(instance, ((FireTyped) instance).getFireType(), Fire::getOnFire, (QuadriFunction<Entity, ServerLevel, DamageSource, Float, Boolean>) original::call);
  }

  /**
   * Wraps the call to {@link Entity#igniteForSeconds(float)} inside the method {@link Entity#lavaIgnite()}.<br>
   * Sets the base Fire Type.
   *
   * @param instance owner of the redirected method.
   * @param seconds seconds to set the entity on fire for.
   * @param original original {@link Operation} being wrapped.
   */
  @WrapOperation(method = "lavaIgnite", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;igniteForSeconds(F)V"))
  private void wrapIgniteForSeconds(Entity instance, float seconds, Operation<Void> original) {
    FireManager.setOnFire(instance, seconds, FireManager.DEFAULT_FIRE_TYPE, original::call);
  }

  /**
   * Injects at the start of the method {@link Entity#setRemainingFireTicks(int)}.<br>
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
   * Injects in the method {@link Entity#saveWithoutId(ValueOutput)} before the invocation of {@link Entity#addAdditionalSaveData(ValueOutput)}.<br>
   * If valid, saves the current Fire Type in the given {@link ValueOutput}.
   *
   * @param output {@link ValueOutput}.
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueOutput;)V"))
  private void onSaveWithoutId(ValueOutput output, CallbackInfo ci) {
    FireManager.writeTag(output, getFireType());
  }

  /**
   * Injects in the method {@link Entity#load(ValueInput)} before the invocation of {@link Entity#readAdditionalSaveData(ValueInput)}.<br>
   * Loads the Fire Type from the given {@link ValueInput}.
   *
   * @param input {@link ValueInput}.
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueInput;)V"))
  private void onLoad(ValueInput input, CallbackInfo ci) {
    setFireType(FireManager.readTag(input));
  }
}
