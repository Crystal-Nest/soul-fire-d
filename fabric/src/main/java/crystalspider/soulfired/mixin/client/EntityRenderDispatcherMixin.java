package crystalspider.soulfired.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;


/**
 * Injects into {@link EntityRenderDispatcher} to alter Fire behavior for consistency.
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
  /**
   * Modifies the assignment value returned by the first call to {@link SpriteIdentifier#getSprite()} in the method {@link EntityRenderDispatcher#renderFire(MatrixStack, VertexConsumerProvider, Entity)}.
   * <p>
   * Assigns the correct sprite for the Fire Type the entity is burning from.
   * 
   * @param value original sprite returned by the modified method.
   * @param poseStack
   * @param multiBufferSource
   * @param entity {@link Entity} that's burning.
   * @return {@link Sprite} to assign.
   */
	@ModifyVariable(method = "renderFire", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/util/SpriteIdentifier;getSprite()Lnet/minecraft/client/texture/Sprite;", ordinal = 0), ordinal = 0)
  private Sprite onRenderFireAtSprite0(Sprite value, MatrixStack matrices, VertexConsumerProvider vertexConsumer, Entity entity) {
    Identifier fireType = ((FireTyped) entity).getFireType();
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite0(fireType);
    }
    return value;
  }

  /**
   * Modifies the assignment value returned by the second call to {@link SpriteIdentifier#getSprite()} in the method {@link EntityRenderDispatcher#renderFire(MatrixStack, VertexConsumerProvider, Entity)}.
   * <p>
   * Assigns the correct sprite for the Fire Type the entity is burning from.
   * 
   * @param value original sprite returned by the modified method.
   * @param poseStack
   * @param multiBufferSource
   * @param entity {@link Entity} that's burning.
   * @return {@link Sprite} to assign.
   */
	@ModifyVariable(method = "renderFire", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/util/SpriteIdentifier;getSprite()Lnet/minecraft/client/texture/Sprite;", ordinal = 1), ordinal = 1)
  private Sprite onRenderFireAtSprite1(Sprite value, MatrixStack matrices, VertexConsumerProvider vertexConsumer, Entity entity) {
    Identifier fireType = ((FireTyped) entity).getFireType();
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite1(fireType);
    }
    return value;
  }
}
