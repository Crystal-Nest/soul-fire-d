package crystalspider.soulfired.api.client;

import crystalspider.soulfired.api.FireManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

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
   * Fire {@link SpriteIdentifier} for the sprite0.
   * <p>
   * Used only in rendering the Fire of an entity.
   */
  private final SpriteIdentifier spriteIdentifier0;
  /**
   * Fire {@link SpriteIdentifier} for the sprite1.
   * <p>
   * Used both for rendering the Fire of an entity and the player overlay.
   */
  private final SpriteIdentifier spriteIdentifier1;

  FireClient(String modId, String id) {
    this.modId = modId;
    this.id = id;
    if (modId.equals(FireManager.SOUL_FIRE_ID)) {
      this.spriteIdentifier0 = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(modId, "block/" + id + "_fire_0"));
      this.spriteIdentifier1 = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(modId, "block/" + id + "_fire_1"));
    } else {
      this.spriteIdentifier0 = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("block/" + id + "_fire_0"));
      this.spriteIdentifier1 = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("block/" + id + "_fire_1"));
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
  public Sprite getSprite0() {
    return spriteIdentifier0.getSprite();
  }

  /**
   * Returns this sprite 1.
   * 
   * @return this sprite 1.
   */
  public Sprite getSprite1() {
    return spriteIdentifier1.getSprite();
  }

  @Override
  public String toString() {
    return "FireClient [spriteIdentifier0=" + spriteIdentifier0 + ", spriteIdentifier1=" + spriteIdentifier1 + "]";
  }
}
