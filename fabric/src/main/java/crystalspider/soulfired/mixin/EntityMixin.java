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
   * {@link TrackedData} to synchronize the Fire Id across client and server.
   */
  private static final TrackedData<String> DATA_FIRE_ID = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.STRING);

  /**
   * Shadowed {@link Entity#initDataTracker()}.
   */
  @Shadow
  protected abstract void initDataTracker();
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

  @Override
  public void setFireId(String fireId) {
    dataTracker.set(DATA_FIRE_ID, FireManager.ensureFireId(fireId));
  }

  @Override
  public String getFireId() {
    return dataTracker.get(DATA_FIRE_ID);
  }

  /**
   * Redirects the call to {@link Entity#initDataTracker()} inside the constructor.
   * <p>
   * Defines the {@link #DATA_FIRE_ID Fire Id data} to synchronize across client and server.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method. It's the same as {@code this} entity.
   */
  @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;initDataTracker()V"))
  private void redirectInitDataTracker(Entity caller) {
    dataTracker.startTracking(DATA_FIRE_ID, FireManager.BASE_FIRE_ID);
    initDataTracker();
  }

  /**
   * Redirects the call to {@link Entity#damage(DamageSource, float)} inside the method {@link Entity#baseTick()}.
   * <p>
   * Hurts the entity with the correct fire damage and {@link DamageSource}.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method. It's the same as {@code this} entity.
   * @param damageSource original {@link DamageSource} (normale fire).
   * @param damage original damage (normal fire).
   * @return the result of calling the redirected method.
   */
  @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
  private boolean redirectDamage(Entity caller, DamageSource damageSource, float damage) {
    return FireManager.damageOnFire(caller, ((FireTyped) caller).getFireId(), damageSource, damage);
  }

  /**
   * Redirects the call to {@link Entity#setOnFireFor(int)} inside the method {@link Entity#setOnFireFromLava()}.
   * <p>
   * Sets the base fire id.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method. It's the same as {@code this} entity.
   * @param seconds seconds to set the entity on fire for.
   */
  @Redirect(method = "setOnFireFromLava", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V"))
  private void redirectSetOnFireFor(Entity caller, int seconds) {
    FireManager.setOnFire(caller, seconds, FireManager.BASE_FIRE_ID);
  }

  /**
   * Injects at the start of the method {@link Entity#setFireTicks(int)}.
   * <p>
   * Resets the FireId when this entity stops burning or catches fire from a new fire source.
   * 
   * @param ticks ticks this entity should burn for.
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "setFireTicks", at = @At(value = "HEAD"))
  private void onSetFireTicks(int ticks, CallbackInfo ci) {
    if (!world.isClient && ticks >= getFireTicks()) {
      setFireId(FireManager.BASE_FIRE_ID);
    }
  }

  /**
   * Injects in the method {@link Entity#writeNbt(NbtCompound)} before the invocation of {@link Entity#writeCustomDataToNbt(NbtCompound)}.
   * <p>
   * If valid, saves the current FireId in the given {@link NbtCompound}.
   * 
   * @param tag
   * @param cir {@link CallbackInfoReturnable}.
   */
  @Inject(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
  private void onWriteNbt(NbtCompound tag, CallbackInfoReturnable<NbtCompound> cir) {
    if (FireManager.isFireId(getFireId())) {
      tag.putString("FireId", getFireId());
    }
  }

  /**
   * Injects in the method {@link Entity#readNbt(NbtCompound)} before the invocation of {@link Entity#readCustomDataFromNbt(NbtCompound)}.
   * <p>
   * Loads the FireId from the given {@link NbtCompound}.
   * 
   * @param tag
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
  private void onReadNbt(NbtCompound tag, CallbackInfo ci) {
    setFireId(FireManager.ensureFireId(tag.getString("FireId")));
  }
}
