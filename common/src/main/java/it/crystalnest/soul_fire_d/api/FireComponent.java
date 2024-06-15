package it.crystalnest.soul_fire_d.api;

import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFireAspectEnchantment;
import it.crystalnest.soul_fire_d.api.enchantment.FireTypedFlameEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public final class FireComponent<R, T extends R> {
  public static final FireComponent<Block, Block> SOURCE_BLOCK = new FireComponent<>(Registries.BLOCK, "_fire");

  public static final FireComponent<Block, Block> CAMPFIRE_BLOCK = new FireComponent<>(Registries.BLOCK, "_campfire");

  public static final FireComponent<Item, BlockItem> CAMPFIRE_ITEM = new FireComponent<>(Registries.ITEM, "_campfire");

  public static final FireComponent<Block, Block> LANTERN_BLOCK = new FireComponent<>(Registries.BLOCK, "_lantern");

  public static final FireComponent<Item, BlockItem> LANTERN_ITEM = new FireComponent<>(Registries.ITEM, "_lantern");

  public static final FireComponent<Block, Block> TORCH_BLOCK = new FireComponent<>(Registries.BLOCK, "_torch");

  public static final FireComponent<Item, StandingAndWallBlockItem> TORCH_ITEM = new FireComponent<>(Registries.ITEM, "_torch");

  public static final FireComponent<Block, Block> WALL_TORCH_BLOCK = new FireComponent<>(Registries.BLOCK, "_wall_torch");

  public static final FireComponent<ParticleType<?>, ParticleType<?>> FLAME_PARTICLE = new FireComponent<>(Registries.PARTICLE_TYPE, "_flame");

  public static final FireComponent<Enchantment, FireTypedFireAspectEnchantment> FIRE_ASPECT_ENCHANTMENT = new FireComponent<>(Registries.ENCHANTMENT, "_fire_aspect");

  public static final FireComponent<Enchantment, FireTypedFlameEnchantment> FLAME_ENCHANTMENT = new FireComponent<>(Registries.ENCHANTMENT, "_flame");

  private final ResourceKey<? extends Registry<R>> key;

  private final String suffix;

  private FireComponent(ResourceKey<? extends Registry<R>> key, String suffix) {
    this.key = key;
    this.suffix = suffix;
  }

  @NotNull
  @SuppressWarnings("unchecked")
  Registry<R> getRegistry() {
    return (Registry<R>) BuiltInRegistries.REGISTRY.get(key.location());
  }

  @Nullable
  @SuppressWarnings("unchecked")
  T getValue(ResourceLocation id) {
    return (T) getRegistry().get(id);
  }

  @Nullable
  T getValue(Fire fire) {
    return getValue(fire.getComponent(this));
  }

  @SuppressWarnings("unchecked")
  Optional<T> getOptionalValue(ResourceLocation id) {
    return (Optional<T>) getRegistry().getOptional(id);
  }

  Optional<T> getOptionalValue(Fire fire) {
    return getOptionalValue(fire.getComponent(this));
  }

  Map.Entry<FireComponent<R, T>, ResourceLocation> getEntry(String modId, String fireId) {
    return Map.entry(this, new ResourceLocation(modId, fireId + suffix));
  }
}
