package crystalspider.soulfired.handlers;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.config.SoulFiredConfig;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback.LootTableSetter;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

/**
 * {@link LootTableEvents} handler.
 */
public class LootTableEventsHandler {
  /**
   * {@link Identifier} of bastion chests.
   */
  private static final Identifier BASTION_CHEST_IDENTIFIER = new Identifier("minecraft", "chests/bastion_other");

  /**
   * Handles modifing Vanilla loot table to include Soul Flame enchantment.
   * 
   * @param resourceManager
   * @param lootManager
   * @param identifier
   * @param builder
   * @param lootTableSource
   */
  public static void handle(ResourceManager resourceManager, LootManager lootManager, Identifier identifier, FabricLootSupplierBuilder builder, LootTableSetter lootTableSource) {
    if (SoulFiredConfig.getEnableSoulFlame() && identifier.equals(BASTION_CHEST_IDENTIFIER)) {
      builder.pool(
        LootPool.builder()
          .rolls(ConstantLootTableRange.create(1))
          .conditionally(RandomChanceLootCondition.builder(0.05F))
          .with(ItemEntry.builder(Items.BOOK))
          .apply(new EnchantRandomlyLootFunction.Builder().add(FireManager.getFlame(FireManager.SOUL_FIRE_TYPE)))
          .apply(SetCountLootFunction.builder(ConstantLootTableRange.create(1)))
      );
    }
  }
}
