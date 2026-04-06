package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.soul_fire_d.fire.FireRegistry;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.Supplier;

/**
 * {@link LootTableEvents} handler.
 */
public final class LootTableEventsHandler {
  /**
   * {@link Identifier} of piglin bartering loot table.
   */
  private static final Identifier PIGLIN_BARTER_IDENTIFIER = Identifier.withDefaultNamespace("gameplay/piglin_bartering");

  /**
   * {@link Identifier} of ancient city chests loot table.
   */
  private static final Identifier ANCIENT_CITY_CHEST_IDENTIFIER = Identifier.withDefaultNamespace("chests/ancient_city");

  /**
   * {@link Identifier} of bastion treasure chests loot table.
   */
  private static final Identifier BASTION_TREASURE_CHEST_IDENTIFIER = Identifier.withDefaultNamespace("chests/bastion_treasure");

  /**
   * {@link Identifier} of bastion bridge chests loot table.
   */
  private static final Identifier BASTION_BRIDGE_CHEST_IDENTIFIER = Identifier.withDefaultNamespace("chests/bastion_bridge");

  /**
   * {@link Identifier} of bastion hoglin stable chests loot table.
   */
  private static final Identifier BASTION_HOGLIN_STABLE_CHEST_IDENTIFIER = Identifier.withDefaultNamespace("chests/bastion_hoglin_stable");

  /**
   * {@link Identifier} of bastion chests loot table.
   */
  private static final Identifier BASTION_OTHER_CHEST_IDENTIFIER = Identifier.withDefaultNamespace("chests/bastion_other");

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
    if (key.identifier().equals(PIGLIN_BARTER_IDENTIFIER)) {
      builder.modifyPools(pool -> pool.add(LootItem.lootTableItem(FireRegistry.SOUL_FIRE_CHARGE.get()).setWeight(20)));
      builder.modifyPools(pool -> addEnchantment(provider, pool, "soul_fire_aspect", 5));
      builder.modifyPools(pool -> addEnchantment(provider, pool, "soul_flame", 5));
    } else if (key.identifier().equals(BASTION_BRIDGE_CHEST_IDENTIFIER) || key.identifier().equals(BASTION_HOGLIN_STABLE_CHEST_IDENTIFIER) || key.identifier().equals(BASTION_OTHER_CHEST_IDENTIFIER)) {
      buildPool(provider, builder, 20, 20, 5);
    } else if (key.identifier().equals(ANCIENT_CITY_CHEST_IDENTIFIER) || key.identifier().equals(BASTION_TREASURE_CHEST_IDENTIFIER)) {
      buildPool(provider, builder, 10, 30, 10);
    }
  }

  /**
   * Builds a new pool with a single roll that can yield nothing, 1 to 3 soul fire charges, a book with soul fire aspect, or a book with soul flame.
   *
   * @param provider registry provider.
   * @param builder loot table builder.
   * @param emptyWeight weight for the empty entry.
   * @param fireChargeWeight weight for the soul fire charge.
   * @param enchantmentsWeight weight for each of the enchantments.
   */
  private static void buildPool(HolderLookup.Provider provider, LootTable.Builder builder, int emptyWeight, int fireChargeWeight, int enchantmentsWeight) {
    LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(EmptyLootItem.emptyItem().setWeight(emptyWeight));
    addItem(pool, FireRegistry.SOUL_FIRE_CHARGE, fireChargeWeight, UniformGenerator.between(1, 3));
    addEnchantment(provider, pool, "soul_fire_aspect", enchantmentsWeight);
    addEnchantment(provider, pool, "soul_flame", enchantmentsWeight);
    builder.pool(pool.build());
  }

  /**
   * Adds the specified item to the given pool with the given weight and count.
   *
   * @param pool loot pool builder to which the item will be added.
   * @param item item to add.
   * @param weight weight for the item.
   * @param count item count.
   */
  private static void addItem(LootPool.Builder pool, Supplier<? extends Item> item, int weight, NumberProvider count) {
    pool.add(LootItem.lootTableItem(item.get()).setWeight(weight).apply(SetItemCountFunction.setCount(count)));
  }

  /**
   * Adds the specified enchantment book, if present in the game, to the given loot table with the given weight.
   *
   * @param provider registry provider.
   * @param pool loot pool builder to which the enchantment book will be added.
   * @param name enchantment ID.
   * @param weight weight for the enchanted book.
   */
  private static void addEnchantment(HolderLookup.Provider provider, LootPool.Builder pool, String name, int weight) {
    provider.lookupOrThrow(Registries.ENCHANTMENT).get(ResourceKey.create(Registries.ENCHANTMENT, Identifier.withDefaultNamespace(name))).ifPresent(enchantment -> pool.add(
      LootItem.lootTableItem(Items.BOOK).setWeight(weight).apply(new EnchantRandomlyFunction.Builder().withEnchantment(enchantment))
    ));
  }
}
