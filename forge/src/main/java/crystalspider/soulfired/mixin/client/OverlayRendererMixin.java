package crystalspider.soulfired.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.matrix.MatrixStack;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OverlayRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Injects into {@link OverlayRenderer} to alter Fire behavior for consistency.
 */
@Mixin(OverlayRenderer.class)
public class OverlayRendererMixin {
  /**
   * Redirects the call to {@link ForgeEventFactory#renderFireOverlay(PlayerEntity, MatrixStack)} inside the method {@link OverlayRenderer#renderScreenEffect(Minecraft, MatrixStack)}.
   * <p>
   * If the player is on fire, posts the correct event for the screen overlay kind.
   * 
   * @param player
   * @param matrixStack
   * @return the result of the posted event.
   */
  @Redirect(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;renderFireOverlay(Lnet/minecraft/entity/player/PlayerEntity;Lcom/mojang/blaze3d/matrix/MatrixStack;)Z"))
  private static boolean redirectRenderFireOverlay(PlayerEntity player, MatrixStack matrixStack) {
    ResourceLocation fireType = ((FireTyped) player).getFireType();
    if (FireManager.isRegisteredType(fireType)) {
      return ForgeEventFactory.renderBlockOverlay(player, matrixStack, OverlayType.FIRE, FireManager.getSourceBlock(fireType).defaultBlockState(), player.blockPosition());
    }
    return ForgeEventFactory.renderFireOverlay(player, matrixStack);
  }

  /**
   * Modifies the assignment value returned by {@link RenderMaterial#sprite()} in the method {@link OverlayRenderer#renderFire(Minecraft, MatrixStack)}.
   * <p>
   * Assigns the correct sprite for the Fire Type the player is burning from.
   * 
   * @param value original sprite returned by the modified method.
   * @param minecraft Minecraft client.
   * @param matrixStack
   * @return {@link TextureAtlasSprite} to assign.
   */
  @SuppressWarnings("null")
  @ModifyVariable(method = "renderFire", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/model/RenderMaterial;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
  private static TextureAtlasSprite onRenderFire(TextureAtlasSprite value, Minecraft minecraft, MatrixStack matrixStack) {
    ResourceLocation fireType = ((FireTyped) minecraft.player).getFireType();
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite1(fireType);
    }
    return value;
  }
}
