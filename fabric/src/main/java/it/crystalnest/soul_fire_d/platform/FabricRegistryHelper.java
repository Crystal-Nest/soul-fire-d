package it.crystalnest.soul_fire_d.platform;

import it.crystalnest.cobweb.api.registry.RegisterProvider;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import it.crystalnest.soul_fire_d.api.type.FireTypedEnchantment;
import it.crystalnest.soul_fire_d.platform.services.RegistryHelper;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;

import java.util.Arrays;
import java.util.function.Supplier;

public class FabricRegistryHelper implements RegistryHelper {
  @Override
  public <T extends Enchantment & FireTypedEnchantment> void registerEnchantment(ResourceLocation key, Supplier<T> supplier) {
    new RegisterProvider(key.getNamespace()).of(BuiltInRegistries.ENCHANTMENT).apply(key.getPath(), supplier.get());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Block & FireTyped> Supplier<T> registerBlock(ResourceLocation key, Supplier<T> supplier) {
    T block = (T) new RegisterProvider(key.getNamespace()).of(BuiltInRegistries.BLOCK).apply(key.getPath(), supplier.get());
    return () -> block;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Supplier<BlockEntityType<CampfireBlockEntity>> registerCampfireBlockEntity(ResourceLocation key, Supplier<? extends Block>... campfires) {
    BlockEntityType<CampfireBlockEntity> blockEntityType = (BlockEntityType<CampfireBlockEntity>) new RegisterProvider(key.getNamespace()).of(BuiltInRegistries.BLOCK_ENTITY_TYPE).apply(key.getPath(), BlockEntityType.Builder.of(CampfireBlockEntity::new, Arrays.stream(campfires).map(Supplier::get).toList().toArray(new Block[]{})).build(null));
    return () -> blockEntityType;
  }

  @Override
  public Supplier<BlockItem> registerBlockItem(ResourceLocation key, Supplier<? extends Block> block) {
    BlockItem item = (BlockItem) new RegisterProvider(key.getNamespace()).of(BuiltInRegistries.ITEM).apply(key.getPath(), new BlockItem(block.get(), new Item.Properties()));
    return () -> item;
  }

  @Override
  public <T extends TorchBlock & FireTyped, W extends WallTorchBlock & FireTyped> Supplier<StandingAndWallBlockItem> registerStandingAndWallBlock(ResourceLocation key, Supplier<T> torch, Supplier<W> wallTorch) {
    StandingAndWallBlockItem item = (StandingAndWallBlockItem) new RegisterProvider(key.getNamespace()).of(BuiltInRegistries.ITEM).apply(key.getPath(), new StandingAndWallBlockItem(torch.get(), wallTorch.get(), new Item.Properties(), Direction.DOWN));
    return () -> item;
  }

  @Override
  public Supplier<SimpleParticleType> registerParticle(ResourceLocation key) {
    SimpleParticleType particle = (SimpleParticleType) new RegisterProvider(key.getNamespace()).of(BuiltInRegistries.PARTICLE_TYPE).apply(key.getPath(), FabricParticleTypes.simple());
    return () -> particle;
  }
}
