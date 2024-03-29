package crystalspider.soulfired.loot;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import crystalspider.soulfired.config.ModConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Chests loot modifier.
 */
public final class ChestLootModifier extends LootModifier {
  /**
   * Additional items to add to the chest loot.
   */
  private final List<Addition> additions;

  /**
   * {@link Supplier} for this {@link LootModifier} {@link Codec}.
   */
  public static final Supplier<Codec<ChestLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(instance -> codecStart(instance).and(Addition.CODEC.listOf().fieldOf("additions").forGetter(modifier -> modifier.additions)).apply(instance, ChestLootModifier::new)));

  private ChestLootModifier(LootItemCondition[] conditionsIn, List<Addition> additions) {
    super(conditionsIn);
    this.additions = additions;
  }

  @Override
  @Nonnull
  protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
    if (generatedLoot == null) {
      generatedLoot = new ObjectArrayList<>();
    }
    for (Addition addition : additions) {
      if (ModConfig.getEnableSoulFlame() && context.getRandom().nextFloat() <= addition.chance) {
        generatedLoot.add(addition.getEnchantedBook());
      }
    }
    return generatedLoot;
  }

  @Override
  public Codec<? extends IGlobalLootModifier> codec() {
    return CODEC.get();
  }

  /**
   * A single enchantment addition to the loot.
   */
  private static final class Addition {
    /**
     * {@link Codec}.
     */
    public static final Codec<Addition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      ForgeRegistries.ENCHANTMENTS.getCodec().fieldOf("enchantment").forGetter(addition -> addition.enchantment),
      Codec.FLOAT.fieldOf("chance").forGetter(addition -> addition.chance),
      Codec.INT.fieldOf("level").forGetter(addition -> addition.level)
    ).apply(instance, Addition::new));

    /**
     * {@link Enchantment} to add to the loot.
     */
    private final Enchantment enchantment;
    /**
     * Chance for this {@link #enchantment} to add to the loot.
     */
    private final Float chance;
    /**
     * Level of this {@link #enchantment} to add to the loot.
     */
    private final Integer level;

    private Addition(Enchantment item, Float chance, Integer quantity) {
      this.enchantment = item;
      this.chance = chance;
      this.level = quantity;
    }

    private final ItemStack getEnchantedBook() {
      ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
      EnchantedBookItem.addEnchantment(book, new EnchantmentInstance(enchantment, level > enchantment.getMaxLevel() ? enchantment.getMaxLevel() : level));
      return book;
    }
  }
}
