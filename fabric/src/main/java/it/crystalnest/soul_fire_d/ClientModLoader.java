package it.crystalnest.soul_fire_d;

import it.crystalnest.soul_fire_d.api.FireComponent;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.block.CustomCampfireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomFireBlock;
import it.crystalnest.soul_fire_d.api.block.CustomTorchBlock;
import it.crystalnest.soul_fire_d.api.block.CustomWallTorchBlock;
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
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.ApiStatus;

/**
 * Soul fire'd mod client loader.
 */
@ApiStatus.Internal
public final class ClientModLoader implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    FireClientManager.registerFires(FireManager.getFires());
    BlockEntityRenderers.register(FireManager.CUSTOM_CAMPFIRE_ENTITY_TYPE.get(), CampfireRenderer::new);
    FireManager.getComponentList(FireComponent.CAMPFIRE_BLOCK).stream().filter(campfire -> campfire instanceof CustomCampfireBlock).forEach(campfire -> BlockRenderLayerMap.INSTANCE.putBlock(campfire, RenderType.cutout()));
    FireManager.getComponentList(FireComponent.SOURCE_BLOCK).stream().filter(source -> source instanceof CustomFireBlock).forEach(source -> BlockRenderLayerMap.INSTANCE.putBlock(source, RenderType.cutout()));
    FireManager.getComponentList(FireComponent.TORCH_BLOCK).stream().filter(torch -> torch instanceof CustomTorchBlock).forEach(torch -> BlockRenderLayerMap.INSTANCE.putBlock(torch, RenderType.cutout()));
    FireManager.getComponentList(FireComponent.WALL_TORCH_BLOCK).stream().filter(torch -> torch instanceof CustomWallTorchBlock).forEach(torch -> BlockRenderLayerMap.INSTANCE.putBlock(torch, RenderType.cutout()));
    FireManager.getComponentList(FireComponent.FLAME_PARTICLE).forEach(flame -> ParticleFactoryRegistry.getInstance().register((SimpleParticleType) flame, FlameParticle.Provider::new));
    ClientPlayNetworking.registerGlobalReceiver(RegisterFirePacket.ID, FabricFirePacketHandler::handleRegister);
    ClientPlayNetworking.registerGlobalReceiver(UnregisterFirePacket.ID, FabricFirePacketHandler::handleUnregister);
  }
}
