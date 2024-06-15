package it.crystalnest.soul_fire_d.api.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.block.entity.CustomCampfireBlockEntity;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.core.BlockPos;
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
    super(spawnParticles, Math.round(FireManager.getProperty(fireType, Fire::getDamage)), properties.lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? FireManager.getProperty(fireType, Fire::getLight) : 0));
    this.fireType = fireType;
  }

  protected BlockEntityType<? extends CustomCampfireBlockEntity> getBlockEntityType() {
    return FireManager.CUSTOM_CAMPFIRE_ENTITY_TYPE.get();
  }

  protected BlockEntityTicker<? super CampfireBlockEntity> particleTick() {
    return CustomCampfireBlockEntity::particleTick;
  }

  protected BlockEntityTicker<? super CampfireBlockEntity> cookTick() {
    return CustomCampfireBlockEntity::cookTick;
  }

  protected BlockEntityTicker<? super CampfireBlockEntity> cooldownTick() {
    return CustomCampfireBlockEntity::cooldownTick;
  }

  @NotNull
  @Override
  public MapCodec<CampfireBlock> codec() {
    return CODEC;
  }

  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new CustomCampfireBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
    BlockEntityType<? extends CustomCampfireBlockEntity> customBlockEntityType = getBlockEntityType();
    if (level.isClientSide) {
      return state.getValue(LIT) ? createTickerHelper(blockEntityType, customBlockEntityType, particleTick()) : null;
    } else {
      return state.getValue(LIT) ? createTickerHelper(blockEntityType, customBlockEntityType, cookTick()) : createTickerHelper(blockEntityType, customBlockEntityType, cooldownTick());
    }
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }
}
