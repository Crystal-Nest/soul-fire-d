package it.crystalnest.soul_fire_d.api.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

public class CustomFireBlock extends BaseFireBlock implements FireTyped {
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
    super(properties.lightLevel(state -> FireManager.getLight(fireType)), FireManager.getDamage(fireType));
    this.fireType = fireType;
    this.base = base;
  }

  public boolean canSurvive(BlockState state) {
    return state.is(base);
  }

  @NotNull
  @Override
  @SuppressWarnings("deprecation")
  public BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState state2, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos pos2) {
    return this.canSurvive(state, level, pos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
    return canSurvive(level.getBlockState(pos.below()));
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
}
