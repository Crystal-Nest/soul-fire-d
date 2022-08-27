package crystalspider.soulfired.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.mojang.blaze3d.vertex.PoseStack;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.Entity;

/**
 * 
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
  /**
   * 
   * 
   * @param value
   * @param poseStack
   * @param multiBufferSource
   * @param entity
   * @return
   */
  @ModifyVariable(method = "renderFlame", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0), ordinal = 0)
  private TextureAtlasSprite onRenderFlameAtSprite0(TextureAtlasSprite value, PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity) {
    String fireId = ((FireTyped) entity).getFireId();
    if (FireManager.isFireId(fireId)) {
      return FireManager.getMaterial0(fireId).sprite();
    }
    return value;
  }

  /**
   * 
   * 
   * @param value
   * @param poseStack
   * @param multiBufferSource
   * @param entity
   * @return
   */
  @ModifyVariable(method = "renderFlame", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1), ordinal = 1)
  private TextureAtlasSprite onRenderFlameAtSprite1(TextureAtlasSprite value, PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity) {
    String fireId = ((FireTyped) entity).getFireId();
    if (FireManager.isFireId(fireId)) {
      return FireManager.getMaterial1(fireId).sprite();
    }
    return value;
  }
}
