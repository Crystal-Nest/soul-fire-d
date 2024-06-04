package it.crystalnest.soul_fire_d.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.client.FireClientManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Injects into {@link ScreenEffectRenderer} to alter Fire behavior for consistency.
 */
@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
  /**
   * Modifies the assignment value returned by {@link Material#sprite()} in the method {@link ScreenEffectRenderer#renderFire(Minecraft, PoseStack)}.
   * <p>
   * Assigns the correct sprite for the Fire Type the player is burning from.
   *
   * @param value original sprite returned by the modified method.
   * @param minecraft Minecraft client.
   * @param poseStack
   * @return {@link TextureAtlasSprite} to assign.
   */
  @SuppressWarnings("InvalidInjectorMethodSignature")
  @ModifyVariable(method = "renderFire", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
  private static TextureAtlasSprite onRenderFire(TextureAtlasSprite value, Minecraft minecraft, PoseStack poseStack) {
    ResourceLocation fireType = minecraft.player != null ? ((FireTyped) minecraft.player).getFireType() : null;
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite1(fireType);
    }
    return value;
  }
}
