package crystalspider.soulfired.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

/**
 * Soul Fire'd Configuration.
 */
public final class SoulFiredConfig {
  /**
   * {@link ModConfigSpec} {@link ModConfigSpec.Builder Builder}.
   */
  private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
  /**
   * Common Configuration as read from the configuration file.
   */
  public static final CommonConfig COMMON = new CommonConfig(BUILDER);
  /**
   * {@link ModConfigSpec}.
   */
  public static final ModConfigSpec SPEC = BUILDER.build();

  /**
   * Returns the value of {@link CommonConfig#enableSoulFireAspect}.
   *
   * @return {@link CommonConfig#enableSoulFireAspect} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getEnableSoulFireAspect() {
    return COMMON.enableSoulFireAspect.get();
  }

  /**
   * Returns the value of {@link CommonConfig#enableSoulFireAspectDiscovery}.
   *
   * @return {@link CommonConfig#enableSoulFireAspectDiscovery} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getEnableSoulFireAspectDiscovery() {
    return COMMON.enableSoulFireAspectDiscovery.get();
  }

  /**
   * Returns the value of {@link CommonConfig#enableSoulFireAspectTrades}.
   *
   * @return {@link CommonConfig#enableSoulFireAspectTrades} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getEnableSoulFireAspectTrades() {
    return COMMON.enableSoulFireAspectTrades.get();
  }

  /**
   * Returns the value of {@link CommonConfig#enableSoulFireAspectTreasure}.
   *
   * @return {@link CommonConfig#enableSoulFireAspectTreasure} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getEnableSoulFireAspectTreasure() {
    return COMMON.enableSoulFireAspectTreasure.get();
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
   * Returns the value of {@link CommonConfig#enableSoulFlameDiscovery}.
   *
   * @return {@link CommonConfig#enableSoulFlameDiscovery} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getEnableSoulFlameDiscovery() {
    return COMMON.enableSoulFlameDiscovery.get();
  }

  /**
   * Returns the value of {@link CommonConfig#enableSoulFlameTrades}.
   *
   * @return {@link CommonConfig#enableSoulFlameTrades} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getEnableSoulFlameTrades() {
    return COMMON.enableSoulFlameTrades.get();
  }

  /**
   * Returns the value of {@link CommonConfig#enableSoulFlameTreasure}.
   *
   * @return {@link CommonConfig#enableSoulFlameTreasure} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getEnableSoulFlameTreasure() {
    return COMMON.enableSoulFlameTreasure.get();
  }

  /**
   * Common Configuration for Torch hit!.
   */
  public static final class CommonConfig {
    /**
     * Whether to enable Soul Fire Aspect enchantment.
     */
    private final BooleanValue enableSoulFireAspect;
    /**
     * Whether Soul Fire Aspect can appear in the enchanting table and loots.
     */
    private final BooleanValue enableSoulFireAspectDiscovery;
    /**
     * Whether Soul Fire Aspect can be traded with villagers.
     */
    private final BooleanValue enableSoulFireAspectTrades;
    /**
     * Whether Soul Fire Aspect is a treasure enchantment (like Vanilla Mending).
     */
    private final BooleanValue enableSoulFireAspectTreasure;

    /**
     * Whether to enable Soul Flame enchantment.
     */
    private final BooleanValue enableSoulFlame;
    /**
     * Whether Soul Flame can appear in the enchanting table and loots.
     */
    private final BooleanValue enableSoulFlameDiscovery;
    /**
     * Whether Soul Flame can be traded with villagers.
     */
    private final BooleanValue enableSoulFlameTrades;
    /**
     * Whether Soul Flame is a treasure enchantment (like Vanilla Mending).
     */
    private final BooleanValue enableSoulFlameTreasure;

    /**
     * Defines the configuration options, their default values and their comments.
     *
     * @param builder
     */
    public CommonConfig(ModConfigSpec.Builder builder) {
      enableSoulFireAspect = builder.comment("Whether to enable Soul Fire Aspect enchantment.", "Has higher priority than all other Soul Fire Aspect configs.").define("enable soul fire aspect", true);
      enableSoulFireAspectDiscovery = builder.comment("Whether Soul Fire Aspect can appear in the enchanting table and loots.").define("enable soul fire aspect discovery", true);
      enableSoulFireAspectTrades = builder.comment("Whether Soul Fire Aspect can be traded with villagers.").define("enable soul fire aspect trades", true);
      enableSoulFireAspectTreasure = builder.comment("Whether Soul Fire Aspect is a treasure enchantment (like Vanilla Mending).").define("enable soul fire aspect treasure", false);
      enableSoulFlame = builder.comment("Whether to enable Soul Flame enchantment.").define("enable soul flame", true);
      enableSoulFlameDiscovery = builder.comment("Whether Soul Flame can appear in the enchanting table and loots.").define("enable soul flame discovery", true);
      enableSoulFlameTrades = builder.comment("Whether Soul Flame can be traded with villagers.").define("enable soul flame trades", true);
      enableSoulFlameTreasure = builder.comment("Whether Soul Flame is a treasure enchantment (like Vanilla Mending).").define("enable soul flame treasure", false);
    }
  }
}
