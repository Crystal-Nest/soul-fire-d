package it.crystalnest.soul_fire_d.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.client.FireClientManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Injects into {@link EntityRenderDispatcher} to alter Fire behavior for consistency.
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
  /**
   * Wraps the first call to {@link Material#sprite()} in the method {@link EntityRenderDispatcher#renderFlame(PoseStack, MultiBufferSource, EntityRenderState, Quaternionf)}.<br>
   * Assigns the correct sprite (0) for the fire type the entity is burning from.
   *
   * @param originalMaterial material of the original sprite returned by the modified method.
   * @param original the {@link Operation} that gets the original sprite returned by the modified method.
   * @param poseStack matrices.
   * @param bufferSource buffer source.
   * @param renderState {@link EntityRenderState} of the entity that's burning.
   * @param quaternion matrix.
   * @return {@link TextureAtlasSprite} to assign.
   */
  @WrapOperation(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0))
  private TextureAtlasSprite wrapSprite0(Material originalMaterial, Operation<TextureAtlasSprite> original, PoseStack poseStack, MultiBufferSource bufferSource, EntityRenderState renderState, Quaternionf quaternion) {
    ResourceLocation fireType = ((FireTyped) renderState).getFireType();
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite0(fireType);
    }
    return original.call(originalMaterial);
  }

  /**
   * Wraps the second call to {@link Material#sprite()} in the method {@link EntityRenderDispatcher#renderFlame(PoseStack, MultiBufferSource, EntityRenderState, Quaternionf)}.<br>
   * Assigns the correct sprite (1) for the fire type the entity is burning from.
   *
   * @param originalMaterial material of the original sprite returned by the modified method.
   * @param original the {@link Operation} that gets the original sprite returned by the modified method.
   * @param poseStack matrices.
   * @param bufferSource buffer source.
   * @param renderState {@link EntityRenderState} of the entity that's burning.
   * @param quaternion matrix.
   * @return {@link TextureAtlasSprite} to assign.
   */
  @WrapOperation(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1))
  private TextureAtlasSprite wrapSprite1(Material originalMaterial, Operation<TextureAtlasSprite> original, PoseStack poseStack, MultiBufferSource bufferSource, EntityRenderState renderState, Quaternionf quaternion) {
    ResourceLocation fireType = ((FireTyped) renderState).getFireType();
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite1(fireType);
    }
    return original.call(originalMaterial);
  }
}
