package crystalspider.soulfired.api.enchantment;

import java.util.function.Function;
import java.util.function.Supplier;

import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;

/**
 * Builder for {@link FireTyped} {@link Enchantment}.
 */
@SuppressWarnings("unchecked")
public abstract class FireEnchantmentBuilder<T extends Enchantment & FireTyped> {
  /**
   * {@link ResourceLocation} to uniquely identify the associated Fire.
   */
  protected final ResourceLocation fireType;
  /**
   * {@link Supplier} to check whether the enchantment is enabled in survival.
   * <p>
   * Defaults to {@code () -> true}.
   */
  protected Supplier<Boolean> enabled = () -> true;
  /**
   * Addtional compatibility {@link Function} to call and check for when checking compatibility with other enchantments.
   * <p>
   * Defaults to {@code (enchantment) -> true}.
   */
  protected Function<Enchantment, Boolean> compatibility = (enchantment) -> true;
  /**
   * {@link Rarity} for the enchantment.
   * <p>
   * Defaults to {@link Rarity#VERY_RARE}.
   */
  protected Rarity rarity = Rarity.VERY_RARE;
  /**
   * {@link Supplier} for whether the enchantment is treasure only.
   * <p>
   * Defaults to {@code () -> false}
   */
  protected Supplier<Boolean> isTreasure = () -> false;
  /**
   * {@link Supplier} for whether the enchantment is a curse.
   * <p>
   * Defaults to {@code () -> false}
   */
  protected Supplier<Boolean> isCurse = () -> false;
  /**
   * {@link Supplier} for whether the enchantment can appear in the enchanted book trade offers of librarian villagers.
   * <p>
   * Defaults to {@code () -> true}
   */
  protected Supplier<Boolean> isTradeable = () -> true;
  /**
   * {@link Supplier} for whether the enchantment will appear in the enchanting table or loots with random enchant function.
   * <p>
   * Note that {@link #isTreasure} takes precedence.
   * <p>
   * Defaults to {@code () -> true}
   */
  protected Supplier<Boolean> isDiscoverable = () -> true;

  /**
   * @param fireType {@link #fireType}.
   */
  protected FireEnchantmentBuilder(ResourceLocation fireType) {
    this.fireType = fireType;
  }

  /**
   * Sets the enchantment {@link #rarity}.
   * 
   * @param <B> builder type.
   * @param rarity
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setRarity(Rarity rarity) {
    this.rarity = rarity;
    return (B) this;
  }

  /**
   * Sets {@link #isTreasure} {@link Supplier}.
   * 
   * @param <B> builder type.
   * @param isTreasure
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsTreasure(Supplier<Boolean> isTreasure) {
    this.isTreasure = isTreasure;
    return (B) this;
  }

  /**
   * Sets {@link #isTreasure}.
   * 
   * @param <B> builder type.
   * @param isTreasure
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsTreasure(boolean isTreasure) {
    return setIsTreasure(() -> isTreasure);
  }

  /**
   * Sets {@link #isCurse} {@link Supplier}.
   * 
   * @param <B> builder type.
   * @param isCurse
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsCurse(Supplier<Boolean> isCurse) {
    this.isCurse = isCurse;
    return (B) this;
  }

  /**
   * Sets {@link #isCurse}.
   * 
   * @param <B> builder type.
   * @param isCurse
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsCurse(boolean isCurse) {
    return setIsCurse(() -> isCurse);
  }

  /**
   * Sets {@link #isTradeable} {@link Supplier}.
   * 
   * @param <B> builder type.
   * @param isTradeable
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsTradeable(Supplier<Boolean> isTradeable) {
    this.isTradeable = isTradeable;
    return (B) this;
  }

  /**
   * Sets {@link #isTradeable}.
   * 
   * @param <B> builder type.
   * @param isTradeable
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsTradeable(boolean isTradeable) {
    return setIsTradeable(() -> isTradeable);
  }

  /**
   * Sets {@link #isDiscoverable} {@link Supplier}.
   * 
   * @param <B> builder type.
   * @param isDiscoverable
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsDiscoverable(Supplier<Boolean> isDiscoverable) {
    this.isDiscoverable = isDiscoverable;
    return (B) this;
  }

  /**
   * Sets {@link #isDiscoverable}.
   * 
   * @param <B> builder type.
   * @param isDiscoverable
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsDiscoverable(boolean isDiscoverable) {
    return setIsDiscoverable(() -> isDiscoverable);
  }

  /**
   * Sets the {@link #compatibility} {@link Function}.
   * 
   * @param <B> builder type.
   * @param compatibility
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setCompatibility(Function<Enchantment, Boolean> compatibility) {
    this.compatibility = compatibility;
    return (B) this;
  }

  /**
   * Sets the {@link #enabled} {@link Supplier}.
   * 
   * @param <B> builder type.
   * @param enabled
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setEnabled(Supplier<Boolean> enabled) {
    this.enabled = enabled;
    return (B) this;
  }

  /**
   * Builds a {@link T} instance.
   * 
   * @return {@link T} instance.
   */
  public abstract T build();
}
