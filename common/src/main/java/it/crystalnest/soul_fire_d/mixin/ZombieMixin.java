package it.crystalnest.soul_fire_d.mixin;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Injects into {@link Zombie} to alter Fire behavior for consistency.
 */
@Mixin(Zombie.class)
public abstract class ZombieMixin implements FireTyped {
  /**
   * Injects into the method {@link Entity#igniteForSeconds(float)} inside the method {@link Zombie#doHurtTarget(Entity)}.<br />
   * Sets the correct Fire Type to the {@link Entity} being set on fire.
   *
   * @param instance owner of the redirected method.
   * @param seconds amount of seconds the entity should be set on fire for.
   */
  @Redirect(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;igniteForSeconds(F)V"))
  private void onDoHurtTarget(Entity instance, float seconds) {
    FireManager.setOnFire(instance, seconds, getFireType());
  }
}
