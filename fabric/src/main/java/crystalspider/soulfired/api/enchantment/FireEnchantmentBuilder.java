package crystalspider.soulfired.api.enchantment;

import java.util.function.Function;
import java.util.function.Supplier;

import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.util.Identifier;

/**
 * Builder for {@link FireTyped} {@link Enchantment}.
 */
@SuppressWarnings("unchecked")
public abstract class FireEnchantmentBuilder<T extends Enchantment & FireTyped> {
  /**
   * {@link Identifier} to uniquely identify the associated Fire.
   */
  protected final Identifier fireType;
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
   * Whether the enchantment is treasure only.
   * <p>
   * Defaults to {@code false}
   */
  protected boolean isTreasure = false;
  /**
   * Whether the enchantment is a curse.
   * <p>
   * Defaults to {@code false}
   */
  protected boolean isCurse = false;
  /**
   * Whether the enchantment can appear in the enchanted book trade offers of librarian villagers.
   * <p>
   * Defaults to {@code true}
   */
  protected boolean isTradeable = true;
  /**
   * Whether the enchantment will appear in the enchanting table or loots with random enchant function.
   * <p>
   * Note that {@link #isTreasure} takes precedence.
   * <p>
   * Defaults to {@code true}
   */
  protected boolean isDiscoverable = true;

  /**
   * @param fireType {@link #fireType}.
   */
  protected FireEnchantmentBuilder(Identifier fireType) {
    this.fireType = fireType;
  }

  /**
   * Sets the enchantment {@link #rarity}.
   * 
   * @param <B>
   * @param rarity
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setRarity(Rarity rarity) {
    this.rarity = rarity;
    return (B) this;
  }

  /**
   * Sets {@link #isTreasure}.
   * 
   * @param <B>
   * @param isTreasure
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsTreasure(boolean isTreasure) {
    this.isTreasure = isTreasure;
    return (B) this;
  }

  /**
   * Sets {@link #isCurse}.
   * 
   * @param <B>
   * @param isCurse
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsCurse(boolean isCurse) {
    this.isCurse = isCurse;
    return (B) this;
  }

  /**
   * Sets {@link #isTradeable}.
   * 
   * @param <B>
   * @param isTradeable
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsTradeable(boolean isTradeable) {
    this.isTradeable = isTradeable;
    return (B) this;
  }

  /**
   * Sets {@link #isDiscoverable}.
   * 
   * @param <B>
   * @param isDiscoverable
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setIsDiscoverable(boolean isDiscoverable) {
    this.isDiscoverable = isDiscoverable;
    return (B) this;
  }

  /**
   * Sets the {@link #compatibility} {@link Function}.
   * 
   * @param <B>
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
   * @param <B>
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
   * @param fireType
   * @return {@link T} instance.
   */
  public abstract T build();
}
