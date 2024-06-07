package it.crystalnest.soul_fire_d.platform;

import it.crystalnest.soul_fire_d.api.type.FireTyped;
import it.crystalnest.soul_fire_d.api.type.FireTypedEnchantment;
import it.crystalnest.soul_fire_d.platform.services.RegistryHelper;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
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
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.function.Supplier;

public class ForgeRegistryHelper implements RegistryHelper {
  @Override
  public <T extends Enchantment & FireTypedEnchantment> void registerEnchantment(ResourceLocation key, Supplier<T> supplier) {
    DeferredRegister<Enchantment> enchantments = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, key.getNamespace());
    enchantments.register(FMLJavaModLoadingContext.get().getModEventBus());
    enchantments.register(key.getPath(), supplier);
  }

  @Override
  public <T extends Block & FireTyped> Supplier<T> registerBlock(ResourceLocation key, Supplier<T> supplier) {
    DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, key.getNamespace());
    blocks.register(FMLJavaModLoadingContext.get().getModEventBus());
    return blocks.register(key.getPath(), supplier);
  }

  @Override
  @SafeVarargs
  public final Supplier<BlockEntityType<CampfireBlockEntity>> registerCampfireBlockEntity(ResourceLocation key, Supplier<? extends Block>... campfires) {
    DeferredRegister<BlockEntityType<?>> blockEntityTypes = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, key.getNamespace());
    blockEntityTypes.register(FMLJavaModLoadingContext.get().getModEventBus());
    return blockEntityTypes.register(key.getPath(), () -> BlockEntityType.Builder.of(CampfireBlockEntity::new, Arrays.stream(campfires).map(Supplier::get).toList().toArray(new Block[]{})).build(null));
  }

  @Override
  public Supplier<BlockItem> registerBlockItem(ResourceLocation key, Supplier<? extends Block> block) {
    DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, key.getNamespace());
    items.register(FMLJavaModLoadingContext.get().getModEventBus());
    return items.register(key.getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
  }

  @Override
  public <T extends TorchBlock & FireTyped, W extends WallTorchBlock & FireTyped> Supplier<StandingAndWallBlockItem> registerStandingAndWallBlock(ResourceLocation key, Supplier<T> torch, Supplier<W> wallTorch) {
    DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, key.getNamespace());
    items.register(FMLJavaModLoadingContext.get().getModEventBus());
    return items.register(key.getPath(), () -> new StandingAndWallBlockItem(torch.get(), wallTorch.get(), new Item.Properties(), Direction.DOWN));
  }

  @Override
  public Supplier<SimpleParticleType> registerParticle(ResourceLocation key) {
    DeferredRegister<ParticleType<?>> particles = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, key.getNamespace());
    particles.register(FMLJavaModLoadingContext.get().getModEventBus());
    return particles.register(key.getPath(), () -> new SimpleParticleType(false));
  }
}
