package crystalspider.soulfired;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.handlers.RegistryEventHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Soul fire'd mod loader.
 */
@Mod(SoulFiredLoader.MODID)
public class SoulFiredLoader {
  /**
   * ID of this mod.
   */
  public static final String MODID = "soulfired";

  /**
   * Network channel protocol version.
   */
  public static final String PROTOCOL_VERSION = "1.18-1.0";
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

  public SoulFiredLoader() {
    // ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    FMLJavaModLoadingContext.get().getModEventBus().register(new RegistryEventHandler());
    FireManager.registerFire(
      FireManager.fireBuilder()
        .setId(FireManager.SOUL_FIRE_ID)
        .setDamage(2)
        .setSourceBlock(Blocks.SOUL_FIRE)
      .build()
    );
  }
}
