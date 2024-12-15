package it.crystalnest.soul_fire_d.api.block.entity;

import it.crystalnest.soul_fire_d.api.FireManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Custom campfire block entity that allows both automatic use of {@link CampfireBlockEntity} for custom registered campfires and an easier way to create other, more specific, custom campfire block entities.
 */
public class CustomCampfireBlockEntity extends CampfireBlockEntity {
  /**
   * @param pos campfire position.
   * @param state campfire block state.
   */
  public CustomCampfireBlockEntity(BlockPos pos, BlockState state) {
    super(pos, state);
  }

  /**
   * Logic copied from {@link CampfireBlockEntity#cookTick(ServerLevel, BlockPos, BlockState, CampfireBlockEntity, RecipeManager.CachedCheck)}.<br>
   * Allows for custom recipe types.
   *
   * @param level server level.
   * @param pos campfire block position.
   * @param state campfire block state.
   * @param campfire campfire block entity.
   * @param check recipe cached check.
   * @param <T> recipe type.
   */
  public static <T extends Recipe<SingleRecipeInput>> void cookTickGeneric(ServerLevel level, BlockPos pos, BlockState state, CampfireBlockEntity campfire, RecipeManager.CachedCheck<SingleRecipeInput, T> check) {
    boolean flag = false;
    for (int i = 0; i < campfire.getItems().size(); ++i) {
      ItemStack ingredient = campfire.getItems().get(i);
      if (!ingredient.isEmpty()) {
        flag = true;
        if (++campfire.cookingProgress[i] >= campfire.cookingTime[i]) {
          SingleRecipeInput recipe = new SingleRecipeInput(ingredient);
          ItemStack result = check.getRecipeFor(recipe, level).map(holder -> holder.value().assemble(recipe, level.registryAccess())).orElse(ingredient);
          if (result.isItemEnabled(level.enabledFeatures())) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), result);
            campfire.getItems().set(i, ItemStack.EMPTY);
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
          }
        }
      }
    }
    if (flag) {
      setChanged(level, pos, state);
    }
  }

  @NotNull
  @Override
  public BlockEntityType<?> getType() {
    return FireManager.CUSTOM_CAMPFIRE_ENTITY_TYPE.get();
  }
}
