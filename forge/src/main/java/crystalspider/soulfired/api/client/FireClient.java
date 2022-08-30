package crystalspider.soulfired.api.client;

import crystalspider.soulfired.api.FireManager;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;

/**
 * Fire client side only.
 */
public class FireClient {
  /**
   * Mod ID.
   */
  private final String modId;
  /**
   * Fire unique ID.
   */
  private final String id;
  /**
   * Fire {@link Material} for the sprite0.
   * <p>
   * Used only in rendering the Fire of an entity.
   */
  private final RenderMaterial material0;
  /**
   * Fire {@link Material} for the sprite1.
   * <p>
   * Used both for rendering the Fire of an entity and the player overlay.
   */
  private final RenderMaterial material1;

  FireClient(String modId, String id) {
    this.modId = modId;
    this.id = id;
    if (modId.equals(FireManager.SOUL_FIRE_ID)) {
      this.material0 = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, new ResourceLocation(modId, "block/" + id + "_fire_0"));
      this.material1 = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, new ResourceLocation(modId, "block/" + id + "_fire_1"));
    } else {
      this.material0 = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, new ResourceLocation("block/" + id + "_fire_0"));
      this.material1 = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, new ResourceLocation("block/" + id + "_fire_1"));
    }
  }

  /**
   * Returns this {@link #modId}.
   * 
   * @return this {@link #modId}.
   */
  public String getModId() {
    return modId;
  }

  /**
   * Returns this {@link #id}.
   * 
   * @return this {@link #id}.
   */
  public String getId() {
    return id;
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
    return "FireClient [material0=" + material0 + ", material1=" + material1 + "]";
  }
}
