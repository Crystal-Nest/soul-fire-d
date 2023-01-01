package crystalspider.soulfired.loot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Chests loot modifier.
 */
public class ChestLootModifier extends LootModifier {
  /**
   * Additional items to add to the chest loot.
   */
  private final HashMap<ItemStack, Float> additions;

  protected ChestLootModifier(ILootCondition[] conditionsIn, HashMap<ItemStack, Float> additions) {
    super(conditionsIn);
    this.additions = additions;
  }

  @Override
  @Nonnull
  protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
    if (generatedLoot == null) {
      generatedLoot = new ArrayList<>();
    }
    for (Entry<ItemStack, Float> entry : additions.entrySet()) {
      if (context.getRandom().nextFloat() <= entry.getValue()) {
        generatedLoot.add(entry.getKey());
      }
    }
    return generatedLoot;
  }

  /**
   * {@link ChestLootModifier} Serializer.
   */
  public static class Serializer extends GlobalLootModifierSerializer<ChestLootModifier> {
    @Override
    public ChestLootModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
      HashMap<ItemStack, Float> additions = new HashMap<>();
      for (JsonElement jsonElement : JSONUtils.getAsJsonArray(json, "additions")) {
        JsonObject entry = jsonElement.getAsJsonObject();
        Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(JSONUtils.getAsString(entry, "enchantment")));
        if (enchantment != null) {
          ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
          int level = JSONUtils.getAsInt(entry, "level"), maxLevel = enchantment.getMaxLevel();
          EnchantedBookItem.addEnchantment(book, new EnchantmentData(enchantment, level > maxLevel ? maxLevel : level));
          additions.put(book, JSONUtils.getAsFloat(entry, "chance"));
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
