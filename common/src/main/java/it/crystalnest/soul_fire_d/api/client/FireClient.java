package it.crystalnest.soul_fire_d.api.client;

import it.crystalnest.soul_fire_d.api.FireManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

/**
 * Fire, client side only.
 */
public final class FireClient {
  /**
   * {@link ResourceLocation} to uniquely identify this Fire.
   */
  private final ResourceLocation fireType;

  /**
   * Fire {@link Material} for the sprite 0.<br />
   * Used only in rendering the Fire of an entity.
   */
  private final Material material0;

  /**
   * Fire {@link Material} for the sprite 1.<br />
   * Used both for rendering the Fire of an entity and the player overlay.
   */
  private final Material material1;

  /**
   * @param fireType {@link #fireType}.
   */
  FireClient(ResourceLocation fireType) {
    this.fireType = fireType;
    String modId = fireType.getNamespace();
    String fireId = fireType.getPath();
    String joiner = FireManager.DEFAULT_FIRE_TYPE.equals(fireType) ? "" : "_";
    this.material0 = new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(modId, "block/" + fireId + joiner + "fire_0"));
    this.material1 = new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(modId, "block/" + fireId + joiner + "fire_1"));
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
   * Returns this {@link #material0}.
   *
   * @return this {@link #material0}.
   */
  public Material getMaterial0() {
    return material0;
  }

  /**
   * Returns this {@link #material1}.
   *
   * @return this {@link #material1}.
   */
  public Material getMaterial1() {
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
