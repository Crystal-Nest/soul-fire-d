package it.crystalnest.soul_fire_d.api.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.block.entity.CustomCampfireBlockEntity;
import it.crystalnest.soul_fire_d.api.block.entity.DynamicBlockEntityType;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom campfire block.
 */
public class CustomCampfireBlock extends CampfireBlock implements FireTyped {
  /**
   * Codec.
   */
  public static final MapCodec<CampfireBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    ResourceLocation.CODEC.fieldOf("fire_type").forGetter(block -> ((FireTyped) block).getFireType()),
    Codec.BOOL.fieldOf("spawn_particles").forGetter(block -> block.spawnParticles),
    propertiesCodec()
  ).apply(instance, CustomCampfireBlock::new));

  /**
   * Fire type.
   */
  private final ResourceLocation fireType;

  /**
   * @param fireType fire type.
   * @param spawnParticles whether to spawn crackling particles.
   * @param properties block properties.
   */
  public CustomCampfireBlock(ResourceLocation fireType, boolean spawnParticles, Properties properties) {
    this(fireType, spawnParticles, true, properties);
  }

  /**
   * @param fireType fire type.
   * @param spawnParticles whether to spawn crackling particles.
   * @param addDefaultProperties whether to add default block properties.
   * @param properties block properties.
   */
  public CustomCampfireBlock(ResourceLocation fireType, boolean spawnParticles, boolean addDefaultProperties, Properties properties) {
    super(spawnParticles, Math.round(FireManager.getProperty(fireType, Fire::getDamage)), (addDefaultProperties ? addDefaultProperties(properties) : properties).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? FireManager.light(fireType) : 0));
    this.fireType = fireType;
  }

  /**
   * Adds the default properties.
   *
   * @param properties initial properties.
   * @return combination of initial and default properties.
   */
  private static Properties addDefaultProperties(Properties properties) {
    return properties.mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).noOcclusion().ignitedByLava();
  }

  /**
   * Returns the {@link DynamicBlockEntityType} for the custom campfire block entity.<br>
   * Override to change it with a more specific one if you registered a different {@link DynamicBlockEntityType} for a subclass of {@link CustomCampfireBlockEntity}.
   *
   * @return {@link DynamicBlockEntityType}.
   */
  protected DynamicBlockEntityType<CustomCampfireBlockEntity> getBlockEntityType() {
    return FireManager.CUSTOM_CAMPFIRE_ENTITY_TYPE.get();
  }

  /**
   * Returns the {@link CampfireBlockEntity#particleTick(Level, BlockPos, BlockState, CampfireBlockEntity)} override for the custom campfire block entity.<br>
   * Override to change it with a more specific one if you subclass {@link CustomCampfireBlockEntity}.
   *
   * @return {@link CampfireBlockEntity#particleTick(Level, BlockPos, BlockState, CampfireBlockEntity)} custom override.
   */
  protected BlockEntityTicker<CampfireBlockEntity> particleTick() {
    return CustomCampfireBlockEntity::particleTick;
  }

  /**
   * Return the {@link CampfireBlockEntity#cookTick(ServerLevel, BlockPos, BlockState, CampfireBlockEntity, RecipeManager.CachedCheck)} override for the custom campfire block entity.<br>
   * Override to change it with a more specific one if you subclass {@link CustomCampfireBlockEntity}.
   * The input {@link Level} for the returned {@link BlockEntityTicker} can be safely cast into a {@link ServerLevel} as this method will only be called with a previous check.
   *
   * @param cachedCheck {@link RecipeManager.CachedCheck} for fast retrieval of recipes.
   * @return {@link CampfireBlockEntity#cookTick(ServerLevel, BlockPos, BlockState, CampfireBlockEntity, RecipeManager.CachedCheck)} custom override.
   */
  protected <T extends Recipe<SingleRecipeInput>> BlockEntityTicker<CampfireBlockEntity> cookTick(RecipeManager.CachedCheck<SingleRecipeInput, T> cachedCheck) {
    return (level, pos, state, blockEntity) -> CustomCampfireBlockEntity.cookTickGeneric((ServerLevel) level, pos, state, blockEntity, cachedCheck);
  }

  /**
   * Returns the {@link CampfireBlockEntity#cooldownTick(Level, BlockPos, BlockState, CampfireBlockEntity)} override for the custom campfire block entity.<br>
   * Override to change it with a more specific one if you subclass {@link CustomCampfireBlockEntity}.
   *
   * @return {@link CampfireBlockEntity#cooldownTick(Level, BlockPos, BlockState, CampfireBlockEntity)} custom override.
   */
  protected BlockEntityTicker<CampfireBlockEntity> cooldownTick() {
    return CustomCampfireBlockEntity::cooldownTick;
  }

  /**
   * Returns the {@link RecipeType#CAMPFIRE_COOKING}.<br>
   * Override to change it with a different recipe type that better suits your needs.
   *
   * @return cooking recipe type.
   */
  protected RecipeType<? extends Recipe<SingleRecipeInput>> recipeType() {
    return RecipeType.CAMPFIRE_COOKING;
  }

  @NotNull
  @Override
  public MapCodec<CampfireBlock> codec() {
    return CODEC;
  }

  /**
   * Return a new block entity for this block.<br>
   * Override this to change the block entity created when this block is placed down.
   *
   * @param pos block position.
   * @param state block state.
   * @return new block entity.
   */
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new CustomCampfireBlockEntity(pos, state);
  }

  /**
   * Handles custom block entity.<br>
   * To change the block entity used, override the other specific methods in {@link CustomCampfireBlock}.
   *
   * @param level level.
   * @param state block state.
   * @param blockEntityType block entity type.
   * @param <T> block entity.
   * @return {@link BlockEntityTicker}.
   */
  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
    BlockEntityType<CustomCampfireBlockEntity> customBlockEntityType = getBlockEntityType();
    if (level.isClientSide) {
      return state.getValue(LIT) ? createTickerHelper(blockEntityType, customBlockEntityType, particleTick()) : null;
    } else {
      return state.getValue(LIT) ? createTickerHelper(blockEntityType, customBlockEntityType, cookTick(RecipeManager.createCheck(recipeType()))) : createTickerHelper(blockEntityType, customBlockEntityType, cooldownTick());
    }
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }
}
