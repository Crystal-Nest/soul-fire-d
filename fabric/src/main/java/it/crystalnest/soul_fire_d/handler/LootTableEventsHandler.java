package it.crystalnest.soul_fire_d.handler;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
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
   * @param provider holder reference provider.
   */
  public static void handle(ResourceKey<LootTable> key, LootTable.Builder builder, LootTableSource source, HolderLookup.Provider provider) {
    if (key.location().equals(BASTION_CHEST_IDENTIFIER)) {
      buildPool(builder, provider, "soul_fire_aspect");
      buildPool(builder, provider, "soul_flame");
    }
  }

  /**
   * Conditionally build a loot pool to add a chance of finding the specified Soul Fire enchantment.
   *
   * @param builder builder of the loot table being loaded.
   * @param provider holder reference provider.
   * @param name enchantment name.
   */
  private static void buildPool(LootTable.Builder builder, HolderLookup.Provider provider, String name) {
    provider.lookupOrThrow(Registries.ENCHANTMENT).get(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.withDefaultNamespace(name))).ifPresent(enchantment -> builder.pool(
      LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1))
        .conditionally(LootItemRandomChanceCondition.randomChance(0.05F).build())
        .with(LootItem.lootTableItem(Items.BOOK).build())
        .apply(new EnchantRandomlyFunction.Builder().withEnchantment(enchantment))
        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
        .build()
    ));
  }
}
