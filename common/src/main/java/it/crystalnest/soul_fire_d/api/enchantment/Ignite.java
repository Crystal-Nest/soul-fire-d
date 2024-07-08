package it.crystalnest.soul_fire_d.api.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.crystalnest.soul_fire_d.api.FireManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Fire typed Ignite entity enchantment effect.
 *
 * @param duration fire duration.
 * @param fireType fire type.
 */
public record Ignite(LevelBasedValue duration, ResourceLocation fireType) implements EnchantmentEntityEffect {
  /**
   * Ignite codec.
   */
  public static final MapCodec<Ignite> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    LevelBasedValue.CODEC.fieldOf("duration").forGetter(Ignite::duration),
    ResourceLocation.CODEC.fieldOf("fire_type").forGetter(Ignite::fireType)
  ).apply(instance, Ignite::new));

  @Override
  public void apply(@NotNull ServerLevel serverLevel, int enchantmentLevel, @NotNull EnchantedItemInUse item, @NotNull Entity entity, @NotNull Vec3 origin) {
    FireManager.setOnFire(entity, duration.calculate(enchantmentLevel), fireType);
  }

  @NotNull
  @Override
  public MapCodec<Ignite> codec() {
    return CODEC;
  }
}
