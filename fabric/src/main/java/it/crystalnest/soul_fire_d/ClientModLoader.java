package it.crystalnest.soul_fire_d;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.block.CustomCampfireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomFireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomTorchBlock;
import it.crystalnest.soul_fire_d.api.block.CustomWallTorchBlock;
import it.crystalnest.soul_fire_d.api.block.render.CustomCampfireRenderer;
import it.crystalnest.soul_fire_d.api.client.FireClientManager;
import it.crystalnest.soul_fire_d.network.handler.FabricFirePacketHandler;
import it.crystalnest.soul_fire_d.network.packet.RegisterFirePacket;
import it.crystalnest.soul_fire_d.network.packet.UnregisterFirePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import org.jetbrains.annotations.ApiStatus;

/**
 * Soul fire'd mod client loader.
 */
@ApiStatus.Internal
public final class ClientModLoader implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    FireClientManager.registerFires(FireManager.getFires());
    FireManager.getFires().stream()
      .filter(fire -> fire.getCampfire().isPresent() && FireManager.getCampfireBlock(fire.getFireType()) instanceof CustomCampfireBlock)
      .forEach(fire -> {
        BlockEntityRenderers.register((BlockEntityType<CampfireBlockEntity>) BuiltInRegistries.BLOCK_ENTITY_TYPE.get(fire.getCampfire().orElse(FireManager.DEFAULT_FIRE.getCampfire().orElseThrow())), CustomCampfireRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(FireManager.getCampfireBlock(fire.getFireType()), RenderType.cutout());
      });
    FireManager.getFires().stream()
      .filter(fire -> fire.getSource().isPresent() && FireManager.getSourceBlock(fire.getFireType()) instanceof CustomFireBlock)
      .forEach(fire -> BlockRenderLayerMap.INSTANCE.putBlock(FireManager.getSourceBlock(fire.getFireType()), RenderType.cutout()));
    FireManager.getFires().stream()
      .filter(fire -> fire.getSource().isPresent() && BuiltInRegistries.BLOCK.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_torch")) instanceof CustomTorchBlock)
      .forEach(fire -> BlockRenderLayerMap.INSTANCE.putBlock(BuiltInRegistries.BLOCK.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_torch")), RenderType.cutout()));
    FireManager.getFires().stream()
      .filter(fire -> fire.getSource().isPresent() && BuiltInRegistries.BLOCK.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_wall_torch")) instanceof CustomWallTorchBlock)
      .forEach(fire -> BlockRenderLayerMap.INSTANCE.putBlock(BuiltInRegistries.BLOCK.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_wall_torch")), RenderType.cutout()));
    FireManager.getFires().stream()
      .filter(fire -> BuiltInRegistries.PARTICLE_TYPE.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_flame")) != null)
      .forEach(fire -> ParticleFactoryRegistry.getInstance().register((SimpleParticleType) BuiltInRegistries.PARTICLE_TYPE.get(new ResourceLocation(fire.getFireType().getNamespace(), fire.getFireType().getPath() + "_flame")), FlameParticle.Provider::new));
    ClientPlayNetworking.registerGlobalReceiver(RegisterFirePacket.ID, FabricFirePacketHandler::handleRegister);
    ClientPlayNetworking.registerGlobalReceiver(UnregisterFirePacket.ID, FabricFirePacketHandler::handleUnregister);
  }
}
