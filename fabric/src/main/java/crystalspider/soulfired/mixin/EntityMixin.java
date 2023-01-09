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
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * Injects into {@link Entity} to alter Fire behavior for consistency.
 */
@Mixin(Entity.class)
public abstract class EntityMixin implements FireTypeChanger {
  /**
   * Shadowed {@link Entity#world}.
   */
  @Shadow
  public World world;
  /**
   * Shadowed {@link Entity#dataTracker}.
   */
  @Shadow
  protected DataTracker dataTracker;

  /**
   * {@link TrackedData} to synchronize the Fire Type across client and server.
   */
  private static final TrackedData<String> DATA_FIRE_TYPE = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.STRING);

  /**
   * Shadowed {@link Entity#getFireTicks()}.
   * 
   * @return the remaining ticks the entity is set to burn for.
   */
  @Shadow
  public abstract int getFireTicks();
  /**
   * Shadowed {@link Entity#isInLava()}.
   * 
   * @return whether this entity is in lava.
   */
  @Shadow
  public abstract boolean isInLava();
  /**
   * Shadowed {@link Entity#isFireImmune()}.
   * 
   * @return whether this entity is immune to fire damage.
   */
  @Shadow
  public abstract boolean isFireImmune();

  @Override
  public void setFireType(Identifier fireType) {
    if (!this.isFireImmune()) {
      dataTracker.set(DATA_FIRE_TYPE, FireManager.ensure(fireType).toString());
    }
  }

  @Override
  public Identifier getFireType() {
    return Identifier.tryParse(dataTracker.get(DATA_FIRE_TYPE));
  }

  /**
   * Redirects the call to {@link Entity#damage(DamageSource, float)} inside the method {@link Entity#baseTick()}.
   * <p>
   * Hurts the entity with the correct fire damage and {@link DamageSource}.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method. It's the same as {@code this} entity.
   * @param damageSource original {@link DamageSource} (normal fire).
   * @param damage original damage (normal fire).
   * @return the result of calling the redirected method.
   */
  @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
  private boolean redirectDamage(Entity caller, DamageSource damageSource, float damage) {
    return FireManager.damageOnFire(caller, ((FireTyped) caller).getFireType());
  }

  /**
   * Redirects the call to {@link Entity#setOnFireFor(int)} inside the method {@link Entity#setOnFireFromLava()}.
   * <p>
   * Sets the base Fire Type.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method. It's the same as {@code this} entity.
   * @param seconds seconds to set the entity on fire for.
   */
  @Redirect(method = "setOnFireFromLava", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V"))
  private void redirectSetOnFireFor(Entity caller, int seconds) {
    FireManager.setOnFire(caller, seconds, FireManager.DEFAULT_FIRE_TYPE);
  }

  /**
   * Redirects the call to {@link Entity#initDataTracker()} inside the constructor.
   * <p>
   * Defines the {@link #DATA_FIRE_TYPE Fire Type data} to synchronize across client and server.
   * 
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "<init>", at = @At("TAIL"))
  private void redirectInitDataTracker(CallbackInfo ci) {
    dataTracker.startTracking(DATA_FIRE_TYPE, FireManager.DEFAULT_FIRE_TYPE.toString());
  }

  /**
   * Injects at the start of the method {@link Entity#setFireTicks(int)}.
   * <p>
   * Resets the Fire Type when this entity stops burning or catches fire from a new fire source.
   * 
   * @param ticks ticks this entity should burn for.
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "setFireTicks", at = @At(value = "HEAD"))
  private void onSetFireTicks(int ticks, CallbackInfo ci) {
    if (!world.isClient && ticks >= getFireTicks()) {
      setFireType(FireManager.DEFAULT_FIRE_TYPE);
    }
  }

  /**
   * Injects in the method {@link Entity#writeNbt(NbtCompound)} before the invocation of {@link Entity#writeCustomDataToNbt(NbtCompound)}.
   * <p>
   * If valid, saves the current Fire Type in the given {@link NbtCompound}.
   * 
   * @param tag
   * @param cir {@link CallbackInfoReturnable}.
   */
  @Inject(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
  private void onWriteNbt(NbtCompound tag, CallbackInfoReturnable<NbtCompound> cir) {
    FireManager.writeNbt(tag, getFireType());
  }

  /**
   * Injects in the method {@link Entity#readNbt(NbtCompound)} before the invocation of {@link Entity#readCustomDataFromNbt(NbtCompound)}.
   * <p>
   * Loads the Fire Type from the given {@link NbtCompound}.
   * 
   * @param tag
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
  private void onReadNbt(NbtCompound tag, CallbackInfo ci) {
    setFireType(FireManager.readNbt(tag));
  }
}
