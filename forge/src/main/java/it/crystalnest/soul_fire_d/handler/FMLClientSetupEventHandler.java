package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.block.CustomCampfireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomFireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomTorchBlock;
import it.crystalnest.soul_fire_d.api.block.CustomWallTorchBlock;
import it.crystalnest.soul_fire_d.api.block.render.CustomCampfireRenderer;
import it.crystalnest.soul_fire_d.api.client.FireClientManager;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

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
    FireManager.getCampfires().stream().filter(campfire -> campfire instanceof CustomCampfireBlock).forEach(campfire -> ItemBlockRenderTypes.setRenderLayer(campfire, RenderType.cutout()));
    FireManager.getSources().stream().filter(source -> source instanceof CustomFireBlock).forEach(source -> ItemBlockRenderTypes.setRenderLayer(source, RenderType.cutout()));

    FireManager.getFires().stream()
      .filter(fire -> fire.getSource().isPresent() && ForgeRegistries.BLOCKS.getValue(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_torch")) instanceof CustomTorchBlock)
      .forEach(fire -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_torch")), RenderType.cutout()));
    FireManager.getFires().stream()
      .filter(fire -> fire.getSource().isPresent() && ForgeRegistries.BLOCKS.getValue(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_wall_torch")) instanceof CustomWallTorchBlock)
      .forEach(fire -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_wall_torch")), RenderType.cutout()));
  }

  @SubscribeEvent
  public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
    FireManager.getFires().stream()
      .filter(fire -> ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_flame")) != null)
      .forEach(fire -> event.registerSpriteSet((SimpleParticleType) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_flame")), FlameParticle.Provider::new));
  }

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(FireManager.CUSTOM_CAMPFIRE_ENTITY_TYPE.get(), CustomCampfireRenderer::new);
  }
}
