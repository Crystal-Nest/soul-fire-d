package crystalspider.soulfired.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue; 

/**
 * Soul Fire'd Configuration.
 */
public class SoulFiredConfig {
  /**
   * {@link ForgeConfigSpec} {@link ForgeConfigSpec.Builder Builder}.
   */
  private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
  /**
   * Common Configuration as read from the configuration file.
   */
  public static final CommonConfig COMMON = new CommonConfig(BUILDER);
  /**
   * {@link ForgeConfigSpec}.
   */
  public static final ForgeConfigSpec SPEC = BUILDER.build();

  /**
   * Returns the value of {@link CommonConfig#enableSoulFireAspect}.
   *
   * @return {@link CommonConfig#enableSoulFireAspect} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getEnableSoulFireAspect() {
    return COMMON.enableSoulFireAspect.get();
  }

  /**
   * Returns the value of {@link CommonConfig#enableSoulFlame}.
   *
   * @return {@link CommonConfig#enableSoulFlame} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getEnableSoulFlame() {
    return COMMON.enableSoulFlame.get();
  }

  /**
   * Common Configuration for Torch hit!.
   */
  public static class CommonConfig {
    /**
     * Whether to enable Soul Fire Aspect enchantment.
     */
    private final BooleanValue enableSoulFireAspect;
    /**
     * Whether to enable Soul Flame enchantment.
     */
    private final BooleanValue enableSoulFlame;

    /**
     * Defines the configuration options, their default values and their comments.
     *
     * @param builder
     */
    public CommonConfig(ForgeConfigSpec.Builder builder) {
      enableSoulFireAspect = builder.comment("Whether to enable Soul Fire Aspect enchantment.").define("enable soul fire aspect", true);
      enableSoulFlame = builder.comment("Whether to enable Soul Flame enchantment.").define("enable soul flame", true);
    }
  }
}
