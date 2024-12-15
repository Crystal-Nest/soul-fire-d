package it.crystalnest.soul_fire_d.mixin.client;

import it.crystalnest.soul_fire_d.api.type.FireTypeChanger;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Injects into {@link EntityRenderState} to alter Fire behavior for consistency.
 */
@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements FireTypeChanger {
  /**
   * Fire type.
   */
  @Unique
  private ResourceLocation fireType;

  @Override
  public ResourceLocation getFireType() {
    return fireType;
  }

  @Override
  public void setFireType(ResourceLocation fireType) {
    this.fireType = fireType;
  }
}
