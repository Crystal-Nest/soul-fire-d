package crystalspider.soulfired.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.mojang.blaze3d.matrix.MatrixStack;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;

/**
 * Injects into {@link EntityRendererManager} to alter Fire behavior for consistency.
 */
@Mixin(EntityRendererManager.class)
public abstract class EntityRendererManagerMixin {
  /**
   * Modifies the assignment value returned by the first call to {@link RenderMaterial#sprite()} in the method {@link EntityRendererManager#renderFlame(MatrixStack, IRenderTypeBuffer, Entity)}.
   * <p>
   * Assigns the correct sprite for the fire type the entity is burning from.
   * 
   * @param value original sprite returned by the modified method.
   * @param matrixStack
   * @param buffer
   * @param entity {@link Entity} that's burning.
   * @return {@link TextureAtlasSprite} to assign.
   */
  @ModifyVariable(method = "renderFlame", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/model/RenderMaterial;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0), ordinal = 0)
  private TextureAtlasSprite onRenderFlameAtSprite0(TextureAtlasSprite value, MatrixStack matrixStack, IRenderTypeBuffer buffer, Entity entity) {
    String fireId = ((FireTyped) entity).getFireId();
    if (FireManager.isFireId(fireId)) {
      return FireClientManager.getSprite0(fireId);
    }
    return value;
  }

  /**
   * Modifies the assignment value returned by the second call to {@link RenderMaterial#sprite()} in the method {@link EntityRendererManager#renderFlame(MatrixStack, IRenderTypeBuffer, Entity)}.
   * <p>
   * Assigns the correct sprite for the fire type the entity is burning from.
   * 
   * @param value original sprite returned by the modified method.
   * @param matrixStack
   * @param buffer
   * @param entity {@link Entity} that's burning.
   * @return {@link TextureAtlasSprite} to assign.
   */
  @ModifyVariable(method = "renderFlame", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/model/RenderMaterial;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1), ordinal = 1)
  private TextureAtlasSprite onRenderFlameAtSprite1(TextureAtlasSprite value, MatrixStack matrixStack, IRenderTypeBuffer buffer, Entity entity) {
    String fireId = ((FireTyped) entity).getFireId();
    if (FireManager.isFireId(fireId)) {
      return FireClientManager.getSprite1(fireId);
    }
    return value;
  }
}
