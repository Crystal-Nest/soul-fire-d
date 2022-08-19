package author.modid;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * ModId mod loader.
 */
@Mod(ModIdLoader.MODID)
public class ModIdLoader {
  /**
   * Logger.
   */
  public static final Logger LOGGER = LogUtils.getLogger();

  /**
   * Network channel protocol version.
   */
  public static final String PROTOCOL_VERSION = "1";
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ModIdLoader.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

  /**
   * ID of this mod.
   */
  public static final String MODID = "modid";

  public ModIdLoader() {
    // Register handlers and configs.
  }
}
