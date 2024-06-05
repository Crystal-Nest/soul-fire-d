package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.config.ModConfig;
import net.fabricmc.fabric.api.loot.v2.FabricLootTableBuilder;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
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
  private static final ResourceLocation BASTION_CHEST_IDENTIFIER = new ResourceLocation("minecraft", "chests/bastion_other");

  private LootTableEventsHandler() {}

  /**
   * Handles modifying Vanilla loot table to include Soul Flame enchantment.
   *
   * @param resourceManager
   * @param lootDataManager
   * @param resourceLocation
   * @param builder
   * @param lootTableSource
   */
  public static void handle(ResourceManager resourceManager, LootDataManager lootDataManager, ResourceLocation resourceLocation, FabricLootTableBuilder builder, LootTableSource lootTableSource) {
    if (ModConfig.getEnableSoulFlame() && resourceLocation.equals(BASTION_CHEST_IDENTIFIER)) {
      builder.pool(
        LootPool.lootPool()
          .setRolls(ConstantValue.exactly(1))
          .conditionally(LootItemRandomChanceCondition.randomChance(0.05F).build())
          .with(LootItem.lootTableItem(Items.BOOK).build())
          .apply(new EnchantRandomlyFunction.Builder().withEnchantment(FireManager.getFlame(FireManager.SOUL_FIRE_TYPE)))
          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
          .build()
      );
    }
  }
}
