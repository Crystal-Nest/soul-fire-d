package it.crystalnest.soul_fire_d.handler;

import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.config.ModConfig;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
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
   * @param resourceManager the server resource manager.
   * @param lootManager the loot manager.
   * @param id the loot table ID.
   * @param builder a builder of the loot table being loaded.
   * @param source the source of the loot table.
   */
  public static void handle(ResourceManager resourceManager, LootTables lootManager, ResourceLocation id, LootTable.Builder builder, LootTableSource source) {
    if (ModConfig.getEnableSoulFlame() && id.equals(BASTION_CHEST_IDENTIFIER)) {
      builder.pool(
        LootPool.lootPool()
          .setRolls(ConstantValue.exactly(1))
          .conditionally(LootItemRandomChanceCondition.randomChance(1F).build())
          .with(LootItem.lootTableItem(Items.BOOK).build())
          .apply(new EnchantRandomlyFunction.Builder().withEnchantment(FireManager.getComponent(FireManager.SOUL_FIRE_TYPE, Fire.Component.FLAME_ENCHANTMENT)))
          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
          .build()
      );
    }
  }
}
