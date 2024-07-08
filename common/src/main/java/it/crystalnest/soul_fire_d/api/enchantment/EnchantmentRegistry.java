package it.crystalnest.soul_fire_d.api.enchantment;

import com.mojang.serialization.MapCodec;
import it.crystalnest.cobweb.api.registry.CobwebRegister;
import it.crystalnest.cobweb.api.registry.CobwebRegistry;
import it.crystalnest.soul_fire_d.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

/**
 * Enchantment registry.
 */
public final class EnchantmentRegistry {
  /**
   * {@link CobwebRegister} for {@link EnchantmentEntityEffect}s.
   */
  private static final CobwebRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_ENCHANTMENT_EFFECTS = CobwebRegistry.of(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Constants.MOD_ID);

  static {
    ENTITY_ENCHANTMENT_EFFECTS.register("ignite", () -> Ignite.CODEC);
  }

  private EnchantmentRegistry() {}

  /**
   * Called outside to load the class and register.
   */
  public static void register() {}
}
