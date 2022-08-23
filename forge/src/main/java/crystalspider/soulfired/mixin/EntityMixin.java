package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import crystalspider.soulfired.imixin.SoulFiredEntity;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.extensions.IForgeEntity;

@Mixin(Entity.class)
public abstract class EntityMixin extends CapabilityProvider<Entity> implements Nameable, EntityAccess, CommandSource, IForgeEntity, SoulFiredEntity {
  @Shadow
  public Level level;

  private final int FLAG_ONSOULFIRE = -1; // TODO: value
  private boolean wasOnSoulFire;
  private boolean hasVisualSoulFire;
  private int remainingSoulFireTicks = -getSoulFireImmuneTicks();

  EntityMixin() {
    super(Entity.class);
  }

  @Shadow
  protected abstract boolean getSharedFlag(int p_20292_);

  @Shadow
  protected abstract void setSharedFlag(int flag, boolean byteOp);

  @Shadow
  public abstract boolean isSpectator();

  @Shadow
  public abstract boolean isOnFire();

  @Shadow
  public abstract EntityType<?> getType();

  @Override
  public boolean getWasOnSoulFire() {
    return wasOnSoulFire;
  }

  @Override
  public void setWasOnSoulFire(boolean wasOnSoulFire) {
    this.wasOnSoulFire = wasOnSoulFire;
  }

  @Override
  public void setSharedFlagOnSoulFire(boolean flag) {
    setSharedFlag(FLAG_ONSOULFIRE, flag || hasVisualSoulFire);
  }

  @Override
  public void setSecondsOnSoulFire(int seconds) {
    int ticks = seconds * 20;
    if (isLivingEntity(this)) {
      ticks = ProtectionEnchantment.getFireAfterDampener(castToLivingEntity(this), ticks);
    }
    if (remainingSoulFireTicks < ticks) {
      setRemainingSoulFireTicks(ticks);
    }
  }

  @Override
  public void setRemainingSoulFireTicks(int ticks) {
    remainingSoulFireTicks = ticks;
  }

  @Override
  public int getRemainingSoulFireTicks() {
    return remainingSoulFireTicks;
  }

  @Override
  public void clearSoulFire() {
    setRemainingSoulFireTicks(0);
  }

  @Override
  public boolean soulFireImmune() {
    return getType().fireImmune(); // TODO: use directly fireImmune() or add new EntityType property
  }

  @Override
  public boolean isOnSoulFire() {
    boolean flag = level != null && level.isClientSide;
    return !soulFireImmune() && (remainingSoulFireTicks > 0 || flag && getSharedFlag(FLAG_ONSOULFIRE));
  }

  @Override
  public boolean isOnAnyFire() {
    return isOnFire() || isOnSoulFire();
  }

  @Override
  public boolean displaySoulFireAnimation() {
    return isOnSoulFire() && !isSpectator();
  }

  @Override
  public boolean displayAnyFireAnimation() {
    return !isSpectator() && (isOnSoulFire() || isOnFire());
  }

  protected int getSoulFireImmuneTicks() {
    return 1;
  }

  private boolean isLivingEntity(CapabilityProvider<Entity> entity) {
    return entity instanceof LivingEntity;
  }

  private LivingEntity castToLivingEntity(CapabilityProvider<Entity> entity) {
    return (LivingEntity) entity;
  }
}
