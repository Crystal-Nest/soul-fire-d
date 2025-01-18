package it.crystalnest.soul_fire_d.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.client.FireClientManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

/**
 * Injects into {@link ScreenEffectRenderer} to alter Fire behavior for consistency.
 */
@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
  /**
   * Modifies the assignment value returned by {@link Material#sprite()} in the method {@link ScreenEffectRenderer#renderFire(PoseStack, MultiBufferSource)}.<br>
   * Assigns the correct sprite for the Fire Type the player is burning from.
   *
   * @param originalMaterial material of the original sprite returned by the modified method.
   * @param original the operation that gets the original sprite returned by the modified method.
   * @return {@link TextureAtlasSprite} to assign.
   */
  @WrapOperation(method = "renderFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
  private static TextureAtlasSprite onRenderFire(Material originalMaterial, Operation<TextureAtlasSprite> original) {
    ResourceLocation fireType = ((FireTyped) Objects.requireNonNull(Minecraft.getInstance().player)).getFireType();
    if (FireManager.isRegisteredType(fireType)) {
      return FireClientManager.getSprite1(fireType);
    }
    return original.call(originalMaterial);
  }
}
