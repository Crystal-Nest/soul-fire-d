package it.crystalnest.soul_fire_d.api.enchantment;

import it.crystalnest.soul_fire_d.api.type.FireTyped;
import it.crystalnest.soul_fire_d.api.type.FireTypedEnchantment;
import it.crystalnest.soul_fire_d.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Builder for {@link FireTyped} {@link Enchantment}.
 */
@SuppressWarnings("unchecked")
public abstract class FireEnchantmentBuilder<T extends Enchantment & FireTypedEnchantment> {
  /**
   * Default rarity for fire enchantments.
   */
  public static final Rarity DEFAULT_RARITY = Rarity.VERY_RARE;

  /**
   * Default flag {@code isTreasure} for fire enchantments.
   */
  public static final boolean DEFAULT_IS_TREASURE = false;

  /**
   * Default flag {@code isCurse} for fire enchantments.
   */
  public static final boolean DEFAULT_IS_CURSE = false;

  /**
   * Default flag {@code isTradeable} for fire enchantments.
   */
  public static final boolean DEFAULT_IS_TRADEABLE = true;

  /**
   * Default flag {@code isDiscoverable} for fire enchantments.
   */
  public static final boolean DEFAULT_IS_DISCOVERABLE = true;

  /**
   * {@link ResourceLocation} to uniquely identify the associated Fire.
   */
  protected final ResourceLocation fireType;

  /**
   * Enchantment kind identifier.
   * Will be used as suffix when registering the enchantment.
   */
  private final String kind;

  /**
   * {@link BooleanSupplier} to check whether the enchantment is enabled in survival.
   * <p>
   * Defaults to {@code () -> true}.
   */
  protected BooleanSupplier enabled = () -> true;

  /**
   * Additional compatibility {@link Predicate} to call and check for when checking compatibility with other enchantments.
   * <p>
   * Defaults to {@code (enchantment) -> true}.
   */
  protected Predicate<Enchantment> compatibility = enchantment -> true;

  /**
   * {@link TriFunction} to tweak the flame duration.
   */
  protected TriFunction<Entity, Entity, Integer, Integer> duration = (attacker, target, seconds) -> seconds;

  /**
   * {@link Rarity} for the enchantment.
   * <p>
   * Defaults to {@link Rarity#VERY_RARE}.
   */
  protected Rarity rarity = DEFAULT_RARITY;

  /**
   * {@link Supplier} for whether the enchantment cannot appear in the enchanting table.<br />
   * If set to {@code true} along with {@link #isDiscoverable}, the enchantment won't appear in the enchanting table, but can still be found in loots.<br />
   * Defaults to {@code () -> false}
   */
  protected BooleanSupplier isTreasure = () -> DEFAULT_IS_TREASURE;

  /**
   * {@link Supplier} for whether the enchantment is a curse.<br />
   * Defaults to {@code () -> false}
   */
  protected BooleanSupplier isCurse = () -> DEFAULT_IS_CURSE;

  /**
   * {@link Supplier} for whether the enchantment can appear in the enchanted book trade offers of librarian villagers.<br />
   * Defaults to {@code () -> true}
   */
  protected BooleanSupplier isTradeable = () -> DEFAULT_IS_TRADEABLE;

  /**
   * {@link Supplier} for whether the enchantment will appear in the enchanting table or loots with random enchant function.<br />
   * Note that {@link #isTreasure} takes precedence.<br />
   * Defaults to {@code () -> true}
   */
  protected BooleanSupplier isDiscoverable = () -> DEFAULT_IS_DISCOVERABLE;

  /**
   * @param fireType {@link #fireType}.
   * @param kind {@link #kind}.
   */
  protected FireEnchantmentBuilder(ResourceLocation fireType, String kind) {
    this.fireType = fireType;
    this.kind = kind;
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
  public final <B extends FireEnchantmentBuilder<T>> B setIsTreasure(BooleanSupplier isTreasure) {
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
  public final <B extends FireEnchantmentBuilder<T>> B setIsCurse(BooleanSupplier isCurse) {
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
  public final <B extends FireEnchantmentBuilder<T>> B setIsTradeable(BooleanSupplier isTradeable) {
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
  public final <B extends FireEnchantmentBuilder<T>> B setIsDiscoverable(BooleanSupplier isDiscoverable) {
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
  public final <B extends FireEnchantmentBuilder<T>> B setCompatibility(Predicate<Enchantment> compatibility) {
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
  public final <B extends FireEnchantmentBuilder<T>> B setEnabled(BooleanSupplier enabled) {
    this.enabled = enabled;
    return (B) this;
  }

  /**
   * Sets the {@link #duration} {@link TriFunction}.
   *
   * @param <B> builder type.
   * @param duration
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setDuration(TriFunction<Entity, Entity, Integer, Integer> duration) {
    this.duration = duration;
    return (B) this;
  }

  /**
   * Sets the {@link #duration}.
   *
   * @param <B> builder type.
   * @param duration
   * @return this Builder to either set other properties or {@link #build}.
   */
  public final <B extends FireEnchantmentBuilder<T>> B setDuration(int duration) {
    this.duration = (attacker, target, base) -> duration;
    return (B) this;
  }

  /**
   * Builds and registers the enchantment instance.
   */
  public ResourceLocation register() {
    ResourceLocation key = new ResourceLocation(fireType.getNamespace(), fireType.getPath() + "_" + kind);
    Services.REGISTRY.registerEnchantment(key, this::build);
    return key;
  }

  /**
   * Builds a {@link T} instance.
   *
   * @return {@link T} instance.
   */
  protected abstract T build();
}
