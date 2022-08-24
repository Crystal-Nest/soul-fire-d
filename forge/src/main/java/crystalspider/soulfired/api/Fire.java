package crystalspider.soulfired.api;

import net.minecraft.client.resources.model.Material;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.block.state.BlockState;

public class Fire {
  private final String id;

  private final float damage;

  private final Material material0;
  private final Material material1;

  private final DamageSource inFire;
  private final DamageSource onFire;

  private final BlockState blockState;

  Fire(String id, float damage, Material material0, Material material1, DamageSource inFire, DamageSource onFire, BlockState blockState) {
    this.id = id;
    this.damage = damage;
    this.material0 = material0;
    this.material1 = material1;
    this.inFire = inFire;
    this.onFire = onFire;
    this.blockState = blockState;
  }

  public String getId() {
    return id;
  }

  public float getDamage() {
    return damage;
  }

  public Material getMaterial0() {
    return material0;
  }

  public Material getMaterial1() {
    return material1;
  }

  public DamageSource getInFire() {
    return inFire;
  }

  public DamageSource getOnFire() {
    return onFire;
  }

  public BlockState getBlockState() {
    return blockState;
  }

  @Override
  public String toString() {
    return "Fire [id=" + id + ", damage=" + damage + ", material0=" + material0 + ", material1=" + material1 + ", inFire=" + inFire + ", onFire=" + onFire + ", blockState=" + blockState + "]";
  }
}
