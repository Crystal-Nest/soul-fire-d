package it.crystalnest.soul_fire_d.api.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

public class CustomFireBlock extends BaseFireBlock implements FireTyped {
  public static final int MAX_AGE = FireBlock.MAX_AGE;

  public static final IntegerProperty AGE = FireBlock.AGE;

  public static final MapCodec<CustomFireBlock> CODEC = RecordCodecBuilder.mapCodec(
    instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("fire_type").forGetter(block -> block.fireType),
        TagKey.codec(Registries.BLOCK).fieldOf("base").forGetter(block -> block.base),
        propertiesCodec()
      )
      .apply(instance, CustomFireBlock::new)
  );

  private final ResourceLocation fireType;

  private final TagKey<Block> base;

  public CustomFireBlock(ResourceLocation fireType, TagKey<Block> base, MapColor color) {
    this(fireType, base, BlockBehaviour.Properties.of().mapColor(color).replaceable().noCollission().instabreak().sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY));
  }

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
   * Refer to {@link FireBlock#tick(BlockState, ServerLevel, BlockPos, RandomSource)}.
   */
  @Override
  @SuppressWarnings("deprecation")
  public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource rand) {
    super.tick(state, level, pos, rand);
    scheduleTick(level, pos);
    if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
      int age = state.getValue(AGE);
      if (
        !state.canSurvive(level, pos) ||
        FireManager.getProperty(getFireType(), Fire::canRainDouse) && !level.getBlockState(pos.below()).is(level.dimensionType().infiniburn()) && level.isRaining() && level.isRainingAt(pos) && rand.nextFloat() < 0.2 + (float) age * 0.03
      ) {
        level.removeBlock(pos, false);
      } else if (age < MAX_AGE && rand.nextInt(2) == 0) {
        level.setBlock(pos, state.setValue(AGE, age + 1), UPDATE_INVISIBLE);
      }
    }
  }

  @Override
  public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState initial, boolean updateFlags) {
    super.onPlace(state, level, pos, initial, updateFlags);
    scheduleTick(level, pos);
  }

  @NotNull
  @Override
  public MapCodec<? extends BaseFireBlock> codec() {
    return CODEC;
  }

  @Override
  protected boolean canBurn(@NotNull BlockState state) {
    return true;
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }

  public boolean canSurvive(BlockState state) {
    return state.is(base);
  }

  protected BlockState getStateWithAge(LevelAccessor level, BlockPos pos, int age) {
    BlockState state = getState(level, pos);
    return state.hasProperty(AGE) ? state.setValue(AGE, age) : state;
  }

  protected void scheduleTick(Level level, BlockPos pos) {
    level.scheduleTick(pos, this, 30 + level.random.nextInt(10));
  }
}
