package it.crystalnest.soul_fire_d.mixin.client;

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
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Injects into {@link EntityRenderDispatcher} to alter Fire behavior for consistency.
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
  /**
   * Modifies the assignment value returned by the first call to {@link Material#sprite()} in the method {@link EntityRenderDispatcher#renderFlame(PoseStack, MultiBufferSource, Entity, Quaternionf)}.
   * <p>
   * Assigns the correct sprite for the fire type the entity is burning from.
   *
   * @param value original sprite returned by the modified method.
   * @param poseStack
   * @param multiBufferSource
   * @param entity {@link Entity} that's burning.
   * @return {@link TextureAtlasSprite} to assign.
   */
  @SuppressWarnings("InvalidInjectorMethodSignature")
  @ModifyVariable(method = "renderFlame", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0), ordinal = 0)
  private TextureAtlasSprite onRenderFlameAtSprite0(TextureAtlasSprite value, PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity, Quaternionf matrix) {
    ResourceLocation fireType = ((FireTyped) entity).getFireType();
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite0(fireType);
    }
    return value;
  }

  /**
   * Modifies the assignment value returned by the second call to {@link Material#sprite()} in the method {@link EntityRenderDispatcher#renderFlame(PoseStack, MultiBufferSource, Entity, Quaternionf)}.
   * <p>
   * Assigns the correct sprite for the fire type the entity is burning from.
   *
   * @param value original sprite returned by the modified method.
   * @param poseStack
   * @param multiBufferSource
   * @param entity {@link Entity} that's burning.
   * @return {@link TextureAtlasSprite} to assign.
   */
  @ModifyVariable(method = "renderFlame", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1), ordinal = 1)
  private TextureAtlasSprite onRenderFlameAtSprite1(TextureAtlasSprite value, PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity, Quaternionf matrix) {
    ResourceLocation fireType = ((FireTyped) entity).getFireType();
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite1(fireType);
    }
    return value;
  }
}
