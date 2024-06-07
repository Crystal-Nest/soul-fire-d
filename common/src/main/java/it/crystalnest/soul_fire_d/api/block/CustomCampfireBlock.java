package it.crystalnest.soul_fire_d.api.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.block.entity.CustomCampfireBlockEntity;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomCampfireBlock extends CampfireBlock implements FireTyped {
  public static final MapCodec<CampfireBlock> CODEC = RecordCodecBuilder.mapCodec(
    instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("fire_type").forGetter(block -> ((FireTyped) block).getFireType()),
        Codec.BOOL.fieldOf("spawn_particles").forGetter(block -> block.spawnParticles),
        propertiesCodec()
      )
      .apply(instance, CustomCampfireBlock::new)
  );

  private final ResourceLocation fireType;

  public CustomCampfireBlock(ResourceLocation fireType, boolean spawnParticles) {
    this(
      fireType,
      spawnParticles,
      BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).noOcclusion().ignitedByLava()
    );
  }

  public CustomCampfireBlock(ResourceLocation fireType, boolean spawnParticles, Properties properties) {
    super(spawnParticles, Math.round(FireManager.getDamage(fireType)), properties.lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? FireManager.getLight(fireType) : 0));
    this.fireType = fireType;
  }

  @NotNull
  @Override
  public MapCodec<CampfireBlock> codec() {
    return CODEC;
  }

  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new CustomCampfireBlockEntity(getFireType(), pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
    BlockEntityType<CampfireBlockEntity> customBlockEntityType = (BlockEntityType<CampfireBlockEntity>) BuiltInRegistries.BLOCK_ENTITY_TYPE.get(FireManager.getFire(getFireType()).getCampfire().orElse(FireManager.DEFAULT_FIRE.getCampfire().orElseThrow()));
    if (level.isClientSide) {
      return state.getValue(LIT) ? createTickerHelper(blockEntityType, customBlockEntityType, CampfireBlockEntity::particleTick) : null;
    } else {
      return state.getValue(LIT) ? createTickerHelper(blockEntityType, customBlockEntityType, CampfireBlockEntity::cookTick) : createTickerHelper(blockEntityType, customBlockEntityType, CampfireBlockEntity::cooldownTick);
    }
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }
}
