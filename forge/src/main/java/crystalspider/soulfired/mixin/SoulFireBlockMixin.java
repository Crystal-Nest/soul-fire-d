package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crystalspider.soulfired.imixin.SoulFiredEntity;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

@Mixin(SoulFireBlock.class)
public abstract class SoulFireBlockMixin {
  @Inject(method = "<init>", at = @At("TAIL"))
  private void onInit(Properties properties, CallbackInfo ci) {
    ((SoulFiredEntity) this).setFireId("soul");
  }
}
