package it.crystalnest.soul_fire_d.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.crystalnest.soul_fire_d.config.ModConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

/**
 * Chests loot modifier.
 */
public final class ChestLootModifier extends LootModifier {
  /**
   * {@link Supplier} for this {@link LootModifier} {@link Codec}.
   */
  public static final Supplier<MapCodec<ChestLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(instance -> codecStart(instance).and(
    Addition.CODEC.listOf().fieldOf("additions").forGetter(modifier -> modifier.additions)
  ).apply(instance, ChestLootModifier::new)));

  /**
   * Additional items to add to the chest loot.
   */
  private final List<Addition> additions;

  /**
   * @param conditionsIn loot item conditions.
   * @param additions item additions.
   */
  private ChestLootModifier(LootItemCondition[] conditionsIn, List<Addition> additions) {
    super(conditionsIn);
    this.additions = additions;
  }

  @Override
  @Nonnull
  protected ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
    for (Addition addition : additions) {
      if (ModConfig.getEnableSoulFlame() && context.getRandom().nextFloat() <= addition.chance) {
        generatedLoot.add(addition.getEnchantedBook());
      }
    }
    return generatedLoot;
  }

  @NotNull
  @Override
  public MapCodec<? extends IGlobalLootModifier> codec() {
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
      Enchantment.CODEC.fieldOf("enchantment").forGetter(addition -> addition.enchantment),
      Codec.FLOAT.fieldOf("chance").forGetter(addition -> addition.chance),
      Codec.INT.fieldOf("level").forGetter(addition -> addition.level)
    ).apply(instance, Addition::new));

    /**
     * {@link Enchantment} to add to the loot.
     */
    private final Holder<Enchantment> enchantment;

    /**
     * Chance for this {@link #enchantment} to add to the loot.
     */
    private final Float chance;

    /**
     * Level of this {@link #enchantment} to add to the loot.
     */
    private final Integer level;

    /**
     * @param item item.
     * @param chance chance to add the item.
     * @param quantity item quantity.
     */
    private Addition(Holder<Enchantment> item, Float chance, Integer quantity) {
      this.enchantment = item;
      this.chance = chance;
      this.level = quantity;
    }

    /**
     * Returns an enchanted book with this {@link #enchantment} applied.
     *
     * @return an enchanted book with this {@link #enchantment} applied.
     */
    private ItemStack getEnchantedBook() {
      return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, Math.min(enchantment.value().getMaxLevel(), level)));
    }
  }
}
