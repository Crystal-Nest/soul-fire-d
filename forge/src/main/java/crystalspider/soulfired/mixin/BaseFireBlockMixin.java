package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import crystalspider.soulfired.imixin.SoulFiredEntity;
import crystalspider.soulfired.overrides.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin extends Block {
  @Shadow
  private float fireDamage;

  private BaseFireBlockMixin(Properties properties) {
    super(properties);
  }

  /**
   * @author Crystal Spider
   * @reason original method needs to be called only when this Block is not a Soul Fire Block.
   * 
   * @param state
   * @param world
   * @param pos
   * @param entity
   */
  @Override
  @Overwrite
  @SuppressWarnings("deprecation")
  public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
    if (this == Blocks.SOUL_FIRE) {
      SoulFiredEntity soulFiredEntity = (SoulFiredEntity) entity;
      if (!soulFiredEntity.soulFireImmune()) {
        soulFiredEntity.setRemainingSoulFireTicks(soulFiredEntity.getRemainingSoulFireTicks() + 1);
        if (soulFiredEntity.getRemainingSoulFireTicks() == 0) {
          soulFiredEntity.setSecondsOnSoulFire(8);
        }
        // TODO: add dmg mult (here or in SoulFireBlock constructor: https://mixin-wiki.readthedocs.io/mixin-basics/#injecting-into-constructors)
        entity.hurt(DamageSource.IN_SOUL_FIRE, this.fireDamage);
      }
      super.entityInside(state, world, pos, entity);
    } else {
      // TODO: invoke original method
      if (!entity.fireImmune()) {
        entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
        if (entity.getRemainingFireTicks() == 0) {
          entity.setSecondsOnFire(8);
        }
        entity.hurt(DamageSource.IN_FIRE, this.fireDamage);
      }
      super.entityInside(state, world, pos, entity);
    }
  }
}
