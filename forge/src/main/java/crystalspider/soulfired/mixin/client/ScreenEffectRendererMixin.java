package crystalspider.soulfired.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.PoseStack;

import crystalspider.soulfired.imixin.SoulFiredEntity;
import crystalspider.soulfired.overrides.ModelBakery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.event.ForgeEventFactory;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
  @Redirect(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z"))
  private static boolean redirectIsOnFire(LocalPlayer caller, Minecraft minecraft, PoseStack poseStack) {
    return ((SoulFiredEntity) caller).isOnAnyFire();
  }

  @Redirect(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;renderFireOverlay(Lnet/minecraft/world/entity/player/Player;Lcom/mojang/blaze3d/vertex/PoseStack;)Z"))
  private static boolean redirectRenderFireOverlay(Player player, PoseStack poseStack) {
    if (((SoulFiredEntity) player).isOnSoulFire()) {
      return ForgeEventFactory.renderBlockOverlay(player, poseStack, OverlayType.FIRE, Blocks.SOUL_FIRE.defaultBlockState(), player.blockPosition());
    }
    return ForgeEventFactory.renderFireOverlay(player, poseStack);
  }

  @Redirect(method = "renderFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
  private static TextureAtlasSprite onRenderFire$sprite(Material caller, Minecraft minecraft, PoseStack poseStack) {
    if (((SoulFiredEntity) minecraft.player).isOnSoulFire()) {
      return ModelBakery.SOUL_FIRE_1.sprite();
    }
    return caller.sprite();
  }
}
