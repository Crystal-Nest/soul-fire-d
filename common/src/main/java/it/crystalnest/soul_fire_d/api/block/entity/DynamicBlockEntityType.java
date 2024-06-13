package it.crystalnest.soul_fire_d.api.block.entity;

import com.mojang.datafixers.types.Type;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.block.CustomCampfireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class DynamicBlockEntityType<T extends BlockEntity> extends BlockEntityType<T> {
  public DynamicBlockEntityType(BlockEntitySupplier<? extends T> supplier, Set<Block> validBlocks, @SuppressWarnings("DataFlowIssue") Type<?> type) {
    super(supplier, validBlocks, type);
  }

  @Override
  public boolean isValid(BlockState state) {
    return FireManager.getCampfires().stream().filter(campfire -> campfire instanceof CustomCampfireBlock).toList().contains(state.getBlock());
  }
}
