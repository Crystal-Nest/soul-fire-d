package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.block.CustomCampfireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomFireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomLanternBlock;
import it.crystalnest.soul_fire_d.api.block.CustomTorchBlock;
import it.crystalnest.soul_fire_d.api.block.CustomWallTorchBlock;
import it.crystalnest.soul_fire_d.api.client.FireClientManager;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

/**
 * Handles the registry events.
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MOD_ID)
public final class FMLClientSetupEventHandler {
  private FMLClientSetupEventHandler() {}

  /**
   * Handles the {@link FMLClientSetupEvent} event.
   *
   * @param event {@link FMLClientSetupEvent}.
   */
  @SubscribeEvent
  @SuppressWarnings("deprecation")
  public static void handle(FMLClientSetupEvent event) {
    FireClientManager.registerFires(FireManager.getFires());
    FireManager.getComponentList(Fire.Component.CAMPFIRE_BLOCK).stream().filter(CustomCampfireBlock.class::isInstance).forEach(campfire -> ItemBlockRenderTypes.setRenderLayer(campfire, ChunkSectionLayer.CUTOUT));
    FireManager.getComponentList(Fire.Component.SOURCE_BLOCK).stream().filter(CustomFireBlock.class::isInstance).forEach(source -> ItemBlockRenderTypes.setRenderLayer(source, ChunkSectionLayer.CUTOUT));
    FireManager.getComponentList(Fire.Component.LANTERN_BLOCK).stream().filter(CustomLanternBlock.class::isInstance).forEach(lantern -> ItemBlockRenderTypes.setRenderLayer(lantern, ChunkSectionLayer.CUTOUT));
    FireManager.getComponentList(Fire.Component.TORCH_BLOCK).stream().filter(CustomTorchBlock.class::isInstance).forEach(torch -> ItemBlockRenderTypes.setRenderLayer(torch, ChunkSectionLayer.CUTOUT));
    FireManager.getComponentList(Fire.Component.WALL_TORCH_BLOCK).stream().filter(CustomWallTorchBlock.class::isInstance).forEach(torch -> ItemBlockRenderTypes.setRenderLayer(torch, ChunkSectionLayer.CUTOUT));
  }

  /**
   * Handles the {@link RegisterParticleProvidersEvent} event.
   *
   * @param event {@link RegisterParticleProvidersEvent}.
   */
  @SubscribeEvent
  public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
    FireManager.getComponentList(Fire.Component.FLAME_PARTICLE).forEach(flame -> event.registerSpriteSet(flame, FlameParticle.Provider::new));
  }

  /**
   * Handles the {@link EntityRenderersEvent.RegisterRenderers} event.
   *
   * @param event {@link EntityRenderersEvent.RegisterRenderers}.
   */
  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(FireManager.CUSTOM_CAMPFIRE_ENTITY_TYPE.get(), CampfireRenderer::new);
  }
}
