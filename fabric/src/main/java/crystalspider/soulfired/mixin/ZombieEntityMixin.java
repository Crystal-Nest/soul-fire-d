package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieEntity;

/**
 * Injects into {@link ZombieEntity} to alter Fire behavior for consistency.
 */
@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin implements FireTyped {
  /**
   * Injects into the method {@link Entity#setOnFireFor(int)} inside the method {@link ZombieEntity#tryAttack(Entity)}.
   * <p>
   * Sets the correct FireId to the {@link Entity} being set on fire.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method.
   * @param seconds amount of seconds the entity should be set on fire for.
   */
  @Redirect(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V"))
  private void redirectSetOnFireFor(Entity caller, int seconds) {
    FireManager.setOnFire(caller, seconds, FireManager.ensureFireId(getFireId()));
  }
}
