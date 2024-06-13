package it.crystalnest.soul_fire_d.api.block;

import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LanternBlock;

public class CustomLanternBlock extends LanternBlock implements FireTyped {
  private final ResourceLocation fireType;

  public CustomLanternBlock(ResourceLocation fireType, Properties properties) {
    super(properties.lightLevel(state -> FireManager.getProperty(fireType, Fire::getLight)));
    this.fireType = fireType;
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }
}
