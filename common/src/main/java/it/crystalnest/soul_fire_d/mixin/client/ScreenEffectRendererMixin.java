package it.crystalnest.soul_fire_d.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

/**
 * Injects into {@link ScreenEffectRenderer} to alter Fire behavior for consistency.
 */
@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
  /**
   * Wraps the call to {@link Material#sprite()} in the method {@link ScreenEffectRenderer#renderFire(Minecraft, PoseStack)}.<br />
   * Assigns the correct sprite for the Fire Type the player is burning from.
   *
   * @param originalMaterial material of the original sprite returned by the modified method.
   * @param original the {@link Operation} that gets the original sprite returned by the modified method.
   * @param minecraft Minecraft client.
   * @param poseStack matrices.
   * @return {@link TextureAtlasSprite} to assign.
   */
  @WrapOperation(method = "renderFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
  private static TextureAtlasSprite onRenderFire(Material originalMaterial, Operation<TextureAtlasSprite> original, Minecraft minecraft, PoseStack poseStack) {
    ResourceLocation fireType = minecraft.player != null ? ((FireTyped) minecraft.player).getFireType() : null;
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite1(fireType);
    }
    return original.call(originalMaterial);
  }
}
