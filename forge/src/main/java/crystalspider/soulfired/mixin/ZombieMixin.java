package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;

/**
 * Injects into {@link Zombie} to alter Fire behavior for consistency.
 */
@Mixin(Zombie.class)
public abstract class ZombieMixin implements FireTyped {
  /**
   * Injects into the method {@link Entity#setSecondsOnFire(int)} inside the method {@link Zombie#doHurtTarget(Entity)}.
   * <p>
   * Sets the correct FireId to the {@link Entity} being set on fire.
   * 
   * @param caller {@link Entity} invoking (owning) the redirected method.
   * @param seconds amount of seconds the entity should be set on fire for.
   */
  @Redirect(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setSecondsOnFire(I)V"))
  private void onDoHurtTarget(Entity entity, int seconds) {
    FireManager.setOnFire(entity, seconds, getFireId());
  }
}
