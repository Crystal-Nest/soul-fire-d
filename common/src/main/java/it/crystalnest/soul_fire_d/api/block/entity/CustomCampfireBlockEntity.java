package it.crystalnest.soul_fire_d.api.block.entity;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CustomCampfireBlockEntity extends CampfireBlockEntity implements FireTyped {
  private final ResourceLocation fireType;

  public CustomCampfireBlockEntity(ResourceLocation fireType, BlockPos pos, BlockState state) {
    super(pos, state);
    this.fireType = fireType;
  }

  @NotNull
  @Override
  public BlockEntityType<?> getType() {
    return BuiltInRegistries.BLOCK_ENTITY_TYPE.get(FireManager.getFire(getFireType()).getCampfire().orElse(FireManager.DEFAULT_FIRE.getCampfire().orElseThrow()));
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }
}
