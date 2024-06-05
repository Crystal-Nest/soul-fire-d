package it.crystalnest.soul_fire_d.loot;

import com.mojang.serialization.Codec;
import it.crystalnest.soul_fire_d.Constants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class LootRegistry {
  private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Constants.MOD_ID);

  static {
    LOOT_MODIFIERS.register("chest_loot_modifier", ChestLootModifier.CODEC);
  }

  private LootRegistry() {}

  public static void register(IEventBus bus) {
    LOOT_MODIFIERS.register(bus);
  }
}
