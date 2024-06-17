package it.crystalnest.soul_fire_d.api.block;

import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

/**
 * Custom lantern block.
 */
public class CustomLanternBlock extends LanternBlock implements FireTyped {
  /**
   * Fire type.
   */
  private final ResourceLocation fireType;

  /**
   * @param fireType fire type.
   */
  public CustomLanternBlock(ResourceLocation fireType) {
    this(fireType, Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).noOcclusion());
  }

  /**
   * @param fireType fire type.
   * @param properties block properties.
   */
  public CustomLanternBlock(ResourceLocation fireType, Properties properties) {
    super(properties.lightLevel(state -> FireManager.getProperty(fireType, Fire::getLight)));
    this.fireType = fireType;
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }
}
