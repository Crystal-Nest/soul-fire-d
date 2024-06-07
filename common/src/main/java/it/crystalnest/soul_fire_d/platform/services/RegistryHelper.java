package it.crystalnest.soul_fire_d.platform.services;

import it.crystalnest.soul_fire_d.api.type.FireTyped;
import it.crystalnest.soul_fire_d.api.type.FireTypedEnchantment;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;

import java.util.function.Supplier;

public interface RegistryHelper {
  /**
   * Builds and registers a {@link FireTypedEnchantment} from the provided builder.
   *
   * @param key enchantment key.
   * @param supplier enchantment supplier.
   * @param <T> enchantment type.
   */
  <T extends Enchantment & FireTypedEnchantment> void registerEnchantment(ResourceLocation key, Supplier<T> supplier);

  <T extends Block & FireTyped> Supplier<T> registerBlock(ResourceLocation key, Supplier<T> supplier);

  Supplier<BlockEntityType<CampfireBlockEntity>> registerCampfireBlockEntity(ResourceLocation key, Supplier<? extends Block>... campfires);

  Supplier<BlockItem> registerBlockItem(ResourceLocation key, Supplier<? extends Block> block);

  <T extends TorchBlock & FireTyped, W extends WallTorchBlock & FireTyped> Supplier<StandingAndWallBlockItem> registerStandingAndWallBlock(ResourceLocation key, Supplier<T> torch, Supplier<W> wallTorch);

  Supplier<SimpleParticleType> registerParticle(ResourceLocation key);
}
