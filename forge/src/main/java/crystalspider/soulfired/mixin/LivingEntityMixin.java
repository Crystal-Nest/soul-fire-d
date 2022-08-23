package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
  // TODO: 1316 - if (damageSource == DamageSource.(IN|ON)_SOUL_FIRE) return DamageSource.(IN|ON)_FIRE;
  // TODO: 1317 - if (damageSource == DamageSource.(IN|ON)_SOUL_FIRE) return DamageSource.(IN|ON)_FIRE;
}
