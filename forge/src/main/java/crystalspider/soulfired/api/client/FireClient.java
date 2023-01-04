package crystalspider.soulfired.api.client;

import crystalspider.soulfired.api.FireManager;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;

/**
 * Fire, client side only.
 */
public class FireClient {
  /**
   * {@link ResourceLocation} to uniquely identify this Fire.
   */
  private final ResourceLocation fireType;

  /**
   * Fire {@link Material} for the sprite 0.
   * <p>
   * Used only in rendering the Fire of an entity.
   */
  private final RenderMaterial material0;
  /**
   * Fire {@link Material} for the sprite 1.
   * <p>
   * Used both for rendering the Fire of an entity and the player overlay.
   */
  private final RenderMaterial material1;

  FireClient(ResourceLocation fireType) {
    this.fireType = fireType;
    String modId = fireType.getNamespace(), fireId = fireType.getPath();
    String joiner = FireManager.DEFAULT_FIRE_TYPE.equals(fireType) ? "" : "_";
    this.material0 = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, new ResourceLocation(modId, "block/" + fireId + joiner + "fire_0"));
    this.material1 = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, new ResourceLocation(modId, "block/" + fireId + joiner + "fire_1"));
  }

  /**
   * Returns this {@link #fireType}.
   * 
   * @return this {@link #fireType}.
   */
  public ResourceLocation getFireType() {
    return fireType;
  }

  /**
   * Returns this material 0.
   * 
   * @return this material 0.
   */
  public RenderMaterial getMaterial0() {
    return material0;
  }

  /**
   * Returns this material 1.
   * 
   * @return this material 1.
   */
  public RenderMaterial getMaterial1() {
    return material1;
  }

  /**
   * Returns this sprite 0.
   * 
   * @return this sprite 0.
   */
  public TextureAtlasSprite getSprite0() {
    return material0.sprite();
  }

  /**
   * Returns this sprite 1.
   * 
   * @return this sprite 1.
   */
  public TextureAtlasSprite getSprite1() {
    return material1.sprite();
  }

  @Override
  public String toString() {
    return "FireClient [fireType=" + fireType + ", material0=" + material0 + ", material1=" + material1 + "]";
  }
}
