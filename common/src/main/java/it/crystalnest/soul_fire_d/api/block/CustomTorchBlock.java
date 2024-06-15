package it.crystalnest.soul_fire_d.api.block;

import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Custom torch block.
 */
public class CustomTorchBlock extends TorchBlock implements FireTyped {
  /**
   * Fire type.
   */
  private final ResourceLocation fireType;

  /**
   * Particle type.
   */
  private final Supplier<SimpleParticleType> type;

  /**
   * @param fireType fire type.
   * @param type particle type.
   */
  public CustomTorchBlock(ResourceLocation fireType, Supplier<SimpleParticleType> type) {
    this(fireType, type, BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY));
  }

  /**
   * @param fireType fire type.
   * @param type particle type.
   * @param properties block properties.
   */
  public CustomTorchBlock(ResourceLocation fireType, Supplier<SimpleParticleType> type, Properties properties) {
    // noinspection DataFlowIssue
    super(null, properties.lightLevel(state -> FireManager.getProperty(fireType, Fire::getLight)));
    this.fireType = fireType;
    this.type = type;
  }

  @Override
  public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
    // noinspection ConstantValue
    if (this.flameParticle == null) {
      this.flameParticle = type.get();
    }
    super.animateTick(state, level, pos, random);
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }
}
