package it.crystalnest.soul_fire_d.api.block.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;

public class CustomCampfireRenderer extends CampfireRenderer {
  public CustomCampfireRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
  }
}
