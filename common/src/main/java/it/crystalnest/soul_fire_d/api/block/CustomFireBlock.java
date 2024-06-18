package it.crystalnest.soul_fire_d.api.block;

import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Custom fire block.
 */
public class CustomFireBlock extends BaseFireBlock implements FireTyped {
  /**
   * Fire max age property.
   */
  public static final int MAX_AGE = FireBlock.MAX_AGE;

  /**
   * Fire age property.
   */
  public static final IntegerProperty AGE = FireBlock.AGE;

  /**
   * Fire type.
   */
  private final ResourceLocation fireType;

  /**
   * Tag for blocks on which this fire can burn.
   */
  private final TagKey<Block> base;

  /**
   * @param fireType fire type.
   * @param base {@link CustomFireBlock#base}.
   * @param color light color.
   */
  public CustomFireBlock(ResourceLocation fireType, TagKey<Block> base, MaterialColor color) {
    this(fireType, base, Properties.of(Material.FIRE, color).noCollission().instabreak().sound(SoundType.WOOL));
  }

  /**
   * Use the {@link CustomFireBlock#CustomFireBlock(ResourceLocation, TagKey, MaterialColor) other constructor} if your fire should behave similarly to the Vanilla ones (suggested).
   *
   * @param fireType fire type.
   * @param base {@link CustomFireBlock#base}.
   * @param properties block properties.
   */
  public CustomFireBlock(ResourceLocation fireType, TagKey<Block> base, Properties properties) {
    super(properties.lightLevel(state -> FireManager.getProperty(fireType, Fire::getLight)), FireManager.getProperty(fireType, Fire::getDamage));
    registerDefaultState(stateDefinition.any().setValue(AGE, 0));
    this.fireType = fireType;
    this.base = base;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }

  @NotNull
  @Override
  @SuppressWarnings("deprecation")
  public BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState state2, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos pos2) {
    return canSurvive(state, level, pos) ? getStateWithAge(level, pos, state.getValue(AGE)) : Blocks.AIR.defaultBlockState();
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
    return canSurvive(level.getBlockState(pos.below()));
  }

  /**
   * Refer to {@link FireBlock#tick(BlockState, ServerLevel, BlockPos, Random)}.
   */
  @Override
  @SuppressWarnings("deprecation")
  public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random rand) {
    super.tick(state, level, pos, rand);
    scheduleTick(level, pos);
    if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
      int age = state.getValue(AGE);
      if (
        !state.canSurvive(level, pos) ||
        FireManager.getProperty(getFireType(), Fire::canRainDouse) && !level.getBlockState(pos.below()).is(level.dimensionType().infiniburn()) && level.isRaining() && level.isRainingAt(pos) && rand.nextFloat() < 0.2 + age * 0.03
      ) {
        level.removeBlock(pos, false);
      } else if (age < MAX_AGE && rand.nextInt(2) == 0) {
        level.setBlock(pos, state.setValue(AGE, age + 1), UPDATE_INVISIBLE);
      }
    }
  }

  @Override
  protected boolean canBurn(@NotNull BlockState state) {
    return true;
  }

  @Override
  public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState initial, boolean updateFlags) {
    super.onPlace(state, level, pos, initial, updateFlags);
    scheduleTick(level, pos);
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }

  /**
   * Simplified version of {@link CustomFireBlock#canSurvive(BlockState, LevelReader, BlockPos)}.
   *
   * @param state block state.
   * @return whether this fire can burn on the given block.
   */
  public boolean canSurvive(BlockState state) {
    return state.is(base);
  }

  /**
   * Copied from {@link FireBlock#getStateWithAge(LevelAccessor, BlockPos, int)}.
   *
   * @param level level.
   * @param pos position.
   * @param age {@link CustomFireBlock#AGE} value.
   * @return correct block state.
   */
  protected BlockState getStateWithAge(LevelAccessor level, BlockPos pos, int age) {
    BlockState state = getState(level, pos);
    return state.hasProperty(AGE) ? state.setValue(AGE, age) : state;
  }

  /**
   * Schedule the next fire tick.<br />
   * Based on {@link FireBlock#getFireTickDelay(Random)}.
   *
   * @param level level.
   * @param pos position.
   */
  protected void scheduleTick(Level level, BlockPos pos) {
    level.scheduleTick(pos, this, 30 + level.random.nextInt(10));
  }
}
