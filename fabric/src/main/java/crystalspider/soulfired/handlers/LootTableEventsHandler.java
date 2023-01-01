package crystalspider.soulfired.handlers;

import crystalspider.soulfired.api.FireManager;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.item.Items;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
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
  public static void handle(ResourceManager resourceManager, LootManager lootManager, Identifier identifier, LootTable.Builder builder, LootTableSource lootTableSource) {
    if (identifier.equals(BASTION_CHEST_IDENTIFIER)) {
      builder.pool(
        LootPool.builder()
        .rolls(ConstantLootNumberProvider.create(1))
        .conditionally(RandomChanceLootCondition.builder(1F))
        .with(ItemEntry.builder(Items.BOOK))
        .apply(new EnchantRandomlyLootFunction.Builder().add(FireManager.getFlame(FireManager.SOUL_FIRE_ID)))
        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1)))
      );
    }
  }
}
