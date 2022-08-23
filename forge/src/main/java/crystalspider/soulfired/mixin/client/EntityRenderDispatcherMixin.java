package crystalspider.soulfired.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.PoseStack;

import crystalspider.soulfired.imixin.SoulFiredEntity;
import crystalspider.soulfired.overrides.ModelBakery;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.Entity;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
  @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;displayFireAnimation()Z"))
  private boolean redirectDisplayFireAnimation(Entity caller, Entity entity, double d0, double d1, double d2, float f0, float f1, PoseStack poseStack, MultiBufferSource multiBufferSource, int i0) {
    return ((SoulFiredEntity) caller).displayAnyFireAnimation();
  }

  @Redirect(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
  private TextureAtlasSprite onRenderFlame$sprite0(Material caller, PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity) {
    if (((SoulFiredEntity) entity).isOnSoulFire()) {
      return ModelBakery.SOUL_FIRE_0.sprite();
    }
    return caller.sprite();
  }

  @Redirect(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
  private TextureAtlasSprite onRenderFlame$sprite1(Material caller, PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity) {
    if (((SoulFiredEntity) entity).isOnSoulFire()) {
      return ModelBakery.SOUL_FIRE_1.sprite();
    }
    return caller.sprite();
  }
}
