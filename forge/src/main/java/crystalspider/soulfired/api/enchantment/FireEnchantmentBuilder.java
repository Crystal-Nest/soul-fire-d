package crystalspider.soulfired.api.enchantment;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("unchecked")
public abstract class FireEnchantmentBuilder<T extends Enchantment> {
  protected Supplier<Boolean> enabled;
  protected Rarity rarity;
  protected boolean isTreasure;
  protected boolean isCurse;
  protected boolean isTradeable;
  protected boolean isDiscoverable;
  protected Function<Enchantment, Boolean> compatibility;

  protected FireEnchantmentBuilder() {
    reset();
  }

  public final <B extends FireEnchantmentBuilder<T>> B setRarity(Rarity rarity) {
    this.rarity = rarity;
    return (B) this;
  }

  public final <B extends FireEnchantmentBuilder<T>> B setIsTreasure(boolean isTreasure) {
    this.isTreasure = isTreasure;
    return (B) this;
  }

  public final <B extends FireEnchantmentBuilder<T>> B setIsCurse(boolean isCurse) {
    this.isCurse = isCurse;
    return (B) this;
  }

  public final <B extends FireEnchantmentBuilder<T>> B setIsTradeable(boolean isTradeable) {
    this.isTradeable = isTradeable;
    return (B) this;
  }

  public final <B extends FireEnchantmentBuilder<T>> B setIsDiscoverable(boolean isDiscoverable) {
    this.isDiscoverable = isDiscoverable;
    return (B) this;
  }

  public final <B extends FireEnchantmentBuilder<T>> B setCompatibility(Function<Enchantment, Boolean> compatibility) {
    this.compatibility = compatibility;
    return (B) this;
  }
  public final <B extends FireEnchantmentBuilder<T>> B setCompatibility(boolean compatibility) {
    this.compatibility = (enchantment) -> compatibility;
    return (B) this;
  }

  public final <B extends FireEnchantmentBuilder<T>> B setEnabled(Supplier<Boolean> enabled) {
    this.enabled = enabled;
    return (B) this;
  }

  public final <B extends FireEnchantmentBuilder<T>> B reset() {
    enabled = () -> true;
    rarity = Rarity.VERY_RARE;
    isTreasure = false;
    isCurse = false;
    isTradeable = true;
    isDiscoverable = true;
    compatibility = null;
    return (B) this;
  }

  public final T build(ResourceLocation fireType) throws IllegalStateException {
    if (rarity != null || compatibility != null) {
      return make(fireType);
    }
    throw new IllegalStateException("Tried to build a Fire Typed enchantment before setting rarity and/or compatibility.");
  }

  protected abstract T make(ResourceLocation fireType);
}
