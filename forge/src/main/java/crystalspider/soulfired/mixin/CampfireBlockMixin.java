package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import crystalspider.soulfired.overrides.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends BaseEntityBlock implements SimpleWaterloggedBlock {
  @Shadow
  private boolean spawnParticles;
  @Shadow
  private int fireDamage;

  @Shadow
  public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

  private CampfireBlockMixin(Properties properties) {
    super(properties);
  }

  @Override
  @Overwrite
  @SuppressWarnings("deprecation")
  public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
    if (!entity.fireImmune() && state.getValue(CampfireBlock.LIT) && entity instanceof LivingEntity) {
      // Normal campfires have spawnParticles to true while soul campfires have spawnParticles to false.
      if (!spawnParticles) {
        entity.hurt(DamageSource.IN_SOUL_FIRE, (float) this.fireDamage); // TODO: add dmg mult
      } else if (!EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
        entity.hurt(DamageSource.IN_FIRE, (float) this.fireDamage);
      }
    }
    super.entityInside(state, world, pos, entity);
 }
}
