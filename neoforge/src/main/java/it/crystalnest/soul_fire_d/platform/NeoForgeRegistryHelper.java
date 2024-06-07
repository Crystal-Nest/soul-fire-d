package it.crystalnest.soul_fire_d.platform;

import it.crystalnest.soul_fire_d.ModLoader;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import it.crystalnest.soul_fire_d.api.type.FireTypedEnchantment;
import it.crystalnest.soul_fire_d.platform.services.RegistryHelper;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
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
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.function.Supplier;

public class NeoForgeRegistryHelper implements RegistryHelper {
  @Override
  public <T extends Enchantment & FireTypedEnchantment> void registerEnchantment(ResourceLocation key, Supplier<T> builder) {
    DeferredRegister<Enchantment> enchantments = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, key.getNamespace());
    enchantments.register(ModLoader.getBus());
    enchantments.register(key.getPath(), builder);
  }

  @Override
  public <T extends Block & FireTyped> Supplier<T> registerBlock(ResourceLocation key, Supplier<T> supplier) {
    DeferredRegister.Blocks blocks = DeferredRegister.createBlocks(key.getNamespace());
    blocks.register(ModLoader.getBus());
    return blocks.register(key.getPath(), supplier);
  }

  @Override
  @SafeVarargs
  public final Supplier<BlockEntityType<CampfireBlockEntity>> registerCampfireBlockEntity(ResourceLocation key, Supplier<? extends Block>... campfires) {
    DeferredRegister<BlockEntityType<?>> blockEntityTypes = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, key.getNamespace());
    blockEntityTypes.register(ModLoader.getBus());
    return blockEntityTypes.register(key.getPath(), () -> BlockEntityType.Builder.of(CampfireBlockEntity::new, Arrays.stream(campfires).map(Supplier::get).toList().toArray(new Block[] {})).build(null));
  }

  @Override
  public Supplier<BlockItem> registerBlockItem(ResourceLocation key, Supplier<? extends Block> block) {
    DeferredRegister.Items items = DeferredRegister.createItems(key.getNamespace());
    items.register(ModLoader.getBus());
    return items.registerSimpleBlockItem(key.getPath(), block);
  }

  @Override
  public <T extends TorchBlock & FireTyped, W extends WallTorchBlock & FireTyped> Supplier<StandingAndWallBlockItem> registerStandingAndWallBlock(ResourceLocation key, Supplier<T> torch, Supplier<W> wallTorch) {
    DeferredRegister.Items items = DeferredRegister.createItems(key.getNamespace());
    items.register(ModLoader.getBus());
    return items.register(key.getPath(), () -> new StandingAndWallBlockItem(torch.get(), wallTorch.get(), new Item.Properties(), Direction.DOWN));
  }

  @Override
  public Supplier<SimpleParticleType> registerParticle(ResourceLocation key) {
    DeferredRegister<ParticleType<?>> particles = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, key.getNamespace());
    particles.register(ModLoader.getBus());
    return particles.register(key.getPath(), () -> new SimpleParticleType(false));
  }
}
