package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.soul_fire_d.config.ModConfig;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

/**
 * {@link LootTableEvents} handler.
 */
public final class LootTableEventsHandler {
  /**
   * {@link ResourceLocation} of bastion chests.
   */
  private static final ResourceLocation BASTION_CHEST_IDENTIFIER = ResourceLocation.withDefaultNamespace("chests/bastion_other");

  private LootTableEventsHandler() {}

  /**
   * Handles modifying Vanilla loot table to include Soul Flame enchantment.
   *
   * @param key loot table key.
   * @param builder builder of the loot table being loaded.
   * @param source loot table source.
   */
  public static void handle(ResourceKey<LootTable> key, LootTable.Builder builder, LootTableSource source, HolderLookup.Provider provider) {
    if (ModConfig.getEnableSoulFlame() && key.location().equals(BASTION_CHEST_IDENTIFIER)) {
      builder.pool(
        LootPool.lootPool()
          .setRolls(ConstantValue.exactly(1))
          .conditionally(LootItemRandomChanceCondition.randomChance(0.05F).build())
          .with(LootItem.lootTableItem(Items.BOOK).build())
          .apply(new EnchantRandomlyFunction.Builder().withEnchantment(provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.withDefaultNamespace("soul_flame")))))
          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
          .build()
      );
    }
  }
}
