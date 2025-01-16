package it.crystalnest.soul_fire_d.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.client.FireClientManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Injects into {@link EntityRenderDispatcher} to alter Fire behavior for consistency.
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
  /**
   * Modifies the assignment value returned by the first call to {@link Material#sprite()} in the method {@link EntityRenderDispatcher#renderFlame(PoseStack, MultiBufferSource, Entity)}.<br />
   * Assigns the correct sprite for the fire type the entity is burning from.
   *
   * @param originalMaterial material of the original sprite returned by the modified method.
   * @param original the operation that gets the original sprite returned by the modified method.
   * @param poseStack matrices.
   * @param multiBufferSource buffer.
   * @param entity {@link Entity} that's burning.
   * @return {@link TextureAtlasSprite} to assign.
   */
  @WrapOperation(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0))
  private TextureAtlasSprite onRenderFlameAtSprite0(Material originalMaterial, Operation<TextureAtlasSprite> original, PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity) {
    ResourceLocation fireType = ((FireTyped) entity).getFireType();
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite0(fireType);
    }
    return original.call(originalMaterial);
  }

  /**
   * Modifies the assignment value returned by the second call to {@link Material#sprite()} in the method {@link EntityRenderDispatcher#renderFlame(PoseStack, MultiBufferSource, Entity)}.<br />
   * Assigns the correct sprite for the fire type the entity is burning from.
   *
   * @param originalMaterial material of the original sprite returned by the modified method.
   * @param original the operation that gets the original sprite returned by the modified method.
   * @param poseStack matrices.
   * @param multiBufferSource buffer.
   * @param entity {@link Entity} that's burning.
   * @return {@link TextureAtlasSprite} to assign.
   */
  @WrapOperation(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1))
  private TextureAtlasSprite onRenderFlameAtSprite1(Material originalMaterial, Operation<TextureAtlasSprite> original, PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity) {
    ResourceLocation fireType = ((FireTyped) entity).getFireType();
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite1(fireType);
    }
    return original.call(originalMaterial);
  }
}
