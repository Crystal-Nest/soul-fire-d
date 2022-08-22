package crystalspider.soulfired.overrides;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

// TODO: only in client
public class ModelBakery extends net.minecraft.client.resources.model.ModelBakery {
  @SuppressWarnings("deprecation")
  public static final Material SOUL_FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("block/soul_fire_0"));
  @SuppressWarnings("deprecation")
  public static final Material SOUL_FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("block/Soul_fire_1"));

  public ModelBakery(ResourceManager resourceManager, BlockColors blockColors, ProfilerFiller profileFiller, int mipLevel) {
    super(resourceManager, blockColors, profileFiller, mipLevel);
  }

  protected ModelBakery(ResourceManager p_119247_, BlockColors p_119248_, boolean vanillaBakery) {
    super(p_119247_, p_119248_, vanillaBakery);
  }
}
