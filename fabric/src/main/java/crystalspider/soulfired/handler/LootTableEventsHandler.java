package crystalspider.soulfired.handler;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.config.ModConfig;
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
public final class LootTableEventsHandler {
  /**
   * {@link Identifier} of bastion chests.
   */
  private static final Identifier BASTION_CHEST_IDENTIFIER = new Identifier("minecraft", "chests/bastion_other");

  private LootTableEventsHandler() {}

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
    if (ModConfig.getEnableSoulFlame() && identifier.equals(BASTION_CHEST_IDENTIFIER)) {
      builder.pool(
        LootPool.builder()
        .rolls(ConstantLootNumberProvider.create(1))
        .conditionally(RandomChanceLootCondition.builder(0.05F))
        .with(ItemEntry.builder(Items.BOOK))
        .apply(new EnchantRandomlyLootFunction.Builder().add(FireManager.getFlame(FireManager.SOUL_FIRE_TYPE)))
        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1)))
      );
    }
  }
}
