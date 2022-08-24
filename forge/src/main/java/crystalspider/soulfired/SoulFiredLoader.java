package crystalspider.soulfired;



import crystalspider.soulfired.api.FireManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Soul fire'd mod loader.
 */
@Mod(SoulFiredLoader.MODID)
public class SoulFiredLoader {
  /**
   * Network channel protocol version.
   */
  public static final String PROTOCOL_VERSION = "1.18-1.0";
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(SoulFiredLoader.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

  /**
   * ID of this mod.
   */
  public static final String MODID = "soulfired";

  public SoulFiredLoader() {
    FireManager.registerFire(
      FireManager.getFireBuilder()
        .setId("soul")
        .setDamage(2)
        .setInFire("inSoulFire")
        .setOnFire("onSoulFire")
        .setSourceBlock(Blocks.SOUL_FIRE)
      .build()
    );
  }
}
