package crystalspider.soulfired.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.block.SoulFireBlock;

/**
 * Injects into {@link SoulFireBlock} to alter Fire behavior for consistency.
 */
@Mixin(SoulFireBlock.class)
public abstract class SoulFireBlockMixin {
  /**
   * Injects at the end of the constructor.
   * <p>
   * Sets the correct FireId for this Fire Block.
   * 
   * @param properties
   * @param ci {@link CallbackInfo}.
   */
  @Inject(method = "<init>", at = @At("TAIL"))
  private void onInit(CallbackInfo ci) {
    ((FireTypeChanger) this).setFireId(FireManager.SOUL_FIRE_ID);
  }
}
