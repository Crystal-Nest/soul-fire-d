package author.modid;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mod Title mod loader.
 */
public class ModIdLoader implements ModInitializer {
  /**
   * Logger.
   */
	public static final Logger LOGGER = LoggerFactory.getLogger(ModIdLoader.MODID);

  /**
   * ID of this mod.
   */
  public static final String MODID = "modid";

  @Override
	public void onInitialize() {
    // Register handlers and configs.
  }
}