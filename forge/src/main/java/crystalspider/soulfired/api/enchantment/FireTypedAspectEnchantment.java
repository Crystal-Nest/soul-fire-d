package crystalspider.soulfired.api.enchantment;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.FireTypeChanger;
import crystalspider.soulfired.api.FireTyped;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.FireAspectEnchantment;

public class FireTypedAspectEnchantment extends FireAspectEnchantment implements FireTyped {
  private final String fireId;

  private final boolean isTreasure;
  private final boolean isCurse;
  private final boolean isTradeable;
  private final boolean isDiscoverable;

  public FireTypedAspectEnchantment(String fireId, Rarity rarity, boolean isTreasure, boolean isCurse, boolean isTradeable, boolean isDiscoverable) {
    super(rarity, EquipmentSlot.MAINHAND);
    this.fireId = FireManager.sanitizeFireId(fireId);
    this.isTreasure = isTreasure;    
    this.isCurse = isCurse;
    this.isTradeable = isTradeable;
    this.isDiscoverable = isDiscoverable;
  }

  public FireTypedAspectEnchantment(String fireId, Rarity rarity) {
    this(fireId, rarity, true, false, true, true);
  }

  @Override
  public final void doPostAttack(LivingEntity attacker, Entity target, int level) {
    if (!attacker.level.isClientSide) {
      target.setSecondsOnFire(level * 4);
    }
    ((FireTypeChanger) target).setFireId(FireManager.ensureFireId(fireId));
  }

  @Override
  public final boolean checkCompatibility(Enchantment enchantment) {
    return super.checkCompatibility(enchantment) && !(enchantment instanceof FireAspectEnchantment);
  }

  @Override
  public final boolean isTreasureOnly() {
    return isTreasure;
  }

  @Override
  public final boolean isCurse() {
    return isCurse;
  }

  @Override
  public final boolean isTradeable() {
    return isTradeable;
  }

  @Override
  public final boolean isDiscoverable() {
    return isDiscoverable;
  }

  @Override
  public final String getFireId() {
    return fireId;
  }
}
