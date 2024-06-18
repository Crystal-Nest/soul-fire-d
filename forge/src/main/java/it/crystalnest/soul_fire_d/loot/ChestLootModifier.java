package it.crystalnest.soul_fire_d.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.crystalnest.soul_fire_d.config.ModConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Chests loot modifier.
 */
public final class ChestLootModifier extends LootModifier {
  /**
   * Additional items to add to the chest loot.
   */
  private final HashMap<ItemStack, Float> additions;

  /**
   * @param conditionsIn loot item conditions.
   * @param additions item additions.
   */
  private ChestLootModifier(LootItemCondition[] conditionsIn, HashMap<ItemStack, Float> additions) {
    super(conditionsIn);
    this.additions = additions;
  }

  @Override
  @Nonnull
  protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
    if (generatedLoot == null) {
      generatedLoot = new ArrayList<>();
    }
    for (Map.Entry<ItemStack, Float> addition : additions.entrySet()) {
      if (ModConfig.getEnableSoulFlame() && context.getRandom().nextFloat() <= addition.getValue()) {
        generatedLoot.add(addition.getKey());
      }
    }
    return generatedLoot;
  }

  /**
   * {@link ChestLootModifier} Serializer.
   */
  public static class Serializer extends GlobalLootModifierSerializer<ChestLootModifier> {
    @Override
    public ChestLootModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn) {
      HashMap<ItemStack, Float> additions = new HashMap<>();
      for (JsonElement jsonElement : GsonHelper.getAsJsonArray(json, "additions")) {
        JsonObject entry = jsonElement.getAsJsonObject();
        Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(GsonHelper.getAsString(entry, "enchantment")));
        if (enchantment != null) {
          ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
          int level = GsonHelper.getAsInt(entry, "level"), maxLevel = enchantment.getMaxLevel();
          EnchantedBookItem.addEnchantment(book, new EnchantmentInstance(enchantment, Math.min(level, maxLevel)));
          additions.put(book, GsonHelper.getAsFloat(entry, "chance"));
        }
      }
      return new ChestLootModifier(conditionsIn, additions);
    }

    @Override
    public JsonObject write(ChestLootModifier instance) {
      return makeConditions(instance.conditions);
    }
  }
}
