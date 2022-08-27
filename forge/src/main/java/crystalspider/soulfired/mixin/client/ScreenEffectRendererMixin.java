package crystalspider.soulfired.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.PoseStack;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.event.ForgeEventFactory;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
  @Redirect(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;renderFireOverlay(Lnet/minecraft/world/entity/player/Player;Lcom/mojang/blaze3d/vertex/PoseStack;)Z"))
  private static boolean redirectRenderFireOverlay(Player player, PoseStack poseStack) {
    String fireId = ((FireTyped) player).getFireId();
    if (FireManager.isFireId(fireId)) {
      return ForgeEventFactory.renderBlockOverlay(player, poseStack, OverlayType.FIRE, FireManager.getSourceBlock(fireId), player.blockPosition());
    }
    return ForgeEventFactory.renderFireOverlay(player, poseStack);
  }

  @ModifyVariable(method = "renderFire", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
  private static TextureAtlasSprite onRenderFire(TextureAtlasSprite value, Minecraft minecraft, PoseStack poseStack) {
    String fireId = ((FireTyped) minecraft.player).getFireId();
    if (FireManager.isFireId(fireId)) {
      return FireManager.getMaterial1(fireId).sprite();
    }
    return value;
  }
}
