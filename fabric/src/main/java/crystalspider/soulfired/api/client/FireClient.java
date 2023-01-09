package crystalspider.soulfired.api.client;

import crystalspider.soulfired.api.FireManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

/**
 * Fire, client side only.
 */
public final class FireClient {
  /**
   * {@link Identifier} to uniquely identify this Fire.
   */
  private final Identifier fireType;

  /**
   * Fire {@link Material} for the sprite 0.
   * <p>
   * Used only in rendering the Fire of an entity.
   */
  private final SpriteIdentifier spriteIdentifier0;
  /**
   * Fire {@link Material} for the sprite 1.
   * <p>
   * Used both for rendering the Fire of an entity and the player overlay.
   */
  private final SpriteIdentifier spriteIdentifier1;

  FireClient(Identifier fireType) {
    this.fireType = fireType;
    String modId = fireType.getNamespace(), fireId = fireType.getPath();
    String joiner = FireManager.DEFAULT_FIRE_TYPE.equals(fireType) ? "" : "_";
    this.spriteIdentifier0 = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(modId, "block/" + fireId + joiner + "fire_0"));
    this.spriteIdentifier1 = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(modId, "block/" + fireId + joiner + "fire_1"));
  }

  /**
   * Returns this {@link #fireType}.
   * 
   * @return this {@link #fireType}.
   */
  public Identifier getFireType() {
    return fireType;
  }

  /**
   * Returns this {@link #spriteIdentifier0}.
   * 
   * @return this {@link #spriteIdentifier0}.
   */
  public SpriteIdentifier getSpriteIdentifier0() {
    return spriteIdentifier0;
  }

  /**
   * Returns this {@link #spriteIdentifier1}.
   * 
   * @return this {@link #spriteIdentifier1}.
   */
  public SpriteIdentifier getSpriteIdentifier1() {
    return spriteIdentifier1;
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
    return "FireClient [fireType=" + fireType + ", spriteIdentifier0=" + spriteIdentifier0 + ", spriteIdentifier1=" + spriteIdentifier1 + "]";
  }
}
