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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

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
  public static void handle(FMLClientSetupEvent event) {
    FireClientManager.registerFires(FireManager.getFires());
    FireManager.getFires().stream()
      .filter(fire -> fire.getCampfire().isPresent() && FireManager.getCampfireBlock(fire.getFireType()) instanceof CustomCampfireBlock)
      .forEach(fire -> ItemBlockRenderTypes.setRenderLayer(FireManager.getCampfireBlock(fire.getFireType()), RenderType.cutout()));
    FireManager.getFires().stream()
      .filter(fire -> fire.getSource().isPresent() && FireManager.getSourceBlock(fire.getFireType()) instanceof CustomFireBlock)
      .forEach(fire -> ItemBlockRenderTypes.setRenderLayer(FireManager.getSourceBlock(fire.getFireType()), RenderType.cutout()));
    FireManager.getFires().stream()
      .filter(fire -> fire.getSource().isPresent() && BuiltInRegistries.BLOCK.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_torch")) instanceof CustomTorchBlock)
      .forEach(fire -> ItemBlockRenderTypes.setRenderLayer(BuiltInRegistries.BLOCK.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_torch")), RenderType.cutout()));
    FireManager.getFires().stream()
      .filter(fire -> fire.getSource().isPresent() && BuiltInRegistries.BLOCK.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_wall_torch")) instanceof CustomWallTorchBlock)
      .forEach(fire -> ItemBlockRenderTypes.setRenderLayer(BuiltInRegistries.BLOCK.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_wall_torch")), RenderType.cutout()));
  }

  @SubscribeEvent
  public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
    FireManager.getFires().stream()
      .filter(fire -> BuiltInRegistries.PARTICLE_TYPE.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_flame")) != null)
      .forEach(fire -> event.registerSpriteSet((SimpleParticleType) BuiltInRegistries.PARTICLE_TYPE.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_flame")), FlameParticle.Provider::new));
  }

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    FireManager.getFires().stream()
      .filter(fire -> fire.getCampfire().isPresent() && FireManager.getCampfireBlock(fire.getFireType()) instanceof CustomCampfireBlock)
      .forEach(fire -> event.registerBlockEntityRenderer((BlockEntityType<CampfireBlockEntity>) BuiltInRegistries.BLOCK_ENTITY_TYPE.get(fire.getCampfire().orElse(FireManager.DEFAULT_FIRE.getCampfire().orElseThrow())), CustomCampfireRenderer::new));
  }
}
