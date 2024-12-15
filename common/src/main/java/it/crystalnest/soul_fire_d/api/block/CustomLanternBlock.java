package it.crystalnest.soul_fire_d.api.block;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

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
   * @param properties block properties.
   */
  public CustomLanternBlock(ResourceLocation fireType, Properties properties) {
    this(fireType, true, properties);
  }

  /**
   * @param fireType fire type.
   * @param addDefaultProperties whether to add default block properties.
   * @param properties block properties.
   */
  public CustomLanternBlock(ResourceLocation fireType, boolean addDefaultProperties, Properties properties) {
    super((addDefaultProperties ? addDefaultProperties(properties) : properties).lightLevel(state -> FireManager.light(fireType)));
    this.fireType = fireType;
  }

  /**
   * Adds the default properties.
   *
   * @param properties initial properties.
   * @return combination of initial and default properties.
   */
  private static Properties addDefaultProperties(Properties properties) {
    return properties.mapColor(MapColor.METAL).forceSolidOn().requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).noOcclusion().pushReaction(PushReaction.DESTROY);
  }

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }
}
