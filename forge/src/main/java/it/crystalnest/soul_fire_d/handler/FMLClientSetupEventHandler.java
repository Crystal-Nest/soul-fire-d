package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireComponent;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.block.CustomCampfireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomFireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomTorchBlock;
import it.crystalnest.soul_fire_d.api.block.CustomWallTorchBlock;
import it.crystalnest.soul_fire_d.api.client.FireClientManager;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Handles the registry events.
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FMLClientSetupEventHandler {
  private FMLClientSetupEventHandler() {}

  /**
   * Handles the {@link FMLClientSetupEvent} event.
   * <p>
   * Registers all {@link Fire Fires} to the client.
   *
   * @param event
   */
  @SubscribeEvent
  @SuppressWarnings("deprecation")
  public static void handle(FMLClientSetupEvent event) {
    FireClientManager.registerFires(FireManager.getFires());
    FireManager.getComponentList(FireComponent.CAMPFIRE_BLOCK).stream().filter(campfire -> campfire instanceof CustomCampfireBlock).forEach(campfire -> ItemBlockRenderTypes.setRenderLayer(campfire, RenderType.cutout()));
    FireManager.getComponentList(FireComponent.SOURCE_BLOCK).stream().filter(source -> source instanceof CustomFireBlock).forEach(source -> ItemBlockRenderTypes.setRenderLayer(source, RenderType.cutout()));
    FireManager.getComponentList(FireComponent.TORCH_BLOCK).stream().filter(torch -> torch instanceof CustomTorchBlock).forEach(torch -> ItemBlockRenderTypes.setRenderLayer(torch, RenderType.cutout()));
    FireManager.getComponentList(FireComponent.WALL_TORCH_BLOCK).stream().filter(torch -> torch instanceof CustomWallTorchBlock).forEach(torch -> ItemBlockRenderTypes.setRenderLayer(torch, RenderType.cutout()));
  }

  @SubscribeEvent
  public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
    FireManager.getComponentList(FireComponent.FLAME_PARTICLE).forEach(flame -> event.registerSpriteSet((SimpleParticleType) flame, FlameParticle.Provider::new));
  }

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(FireManager.CUSTOM_CAMPFIRE_ENTITY_TYPE.get(), CampfireRenderer::new);
  }
}
