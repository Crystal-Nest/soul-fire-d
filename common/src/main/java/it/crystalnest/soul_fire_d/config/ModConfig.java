package it.crystalnest.soul_fire_d.config;

import it.crystalnest.cobweb.api.config.CommonConfig;
import it.crystalnest.soul_fire_d.Constants;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.ApiStatus;

/**
 * Mod common configuration.
 */
@ApiStatus.Internal
public final class ModConfig extends CommonConfig {
  /**
   * Mod common configuration.
   */
  public static final ModConfig CONFIG = register(Constants.MOD_ID, ModConfig::new);

  /**
   * Soul fire aspect enchantment name.
   */
  private static final String SOUL_FIRE_ASPECT = "Soul Fire Aspect";

  /**
   * Soul flame enchantment name.
   */
  private static final String SOUL_FLAME = "Soul Flame";

  /**
   * Whether to enable Soul Fire Aspect enchantment.
   */
  private ForgeConfigSpec.BooleanValue enableSoulFireAspect;

  /**
   * Whether Soul Fire Aspect can appear in the enchanting table and loots.
   */
  private ForgeConfigSpec.BooleanValue enableSoulFireAspectDiscovery;

  /**
   * Whether Soul Fire Aspect can be traded with villagers.
   */
  private ForgeConfigSpec.BooleanValue enableSoulFireAspectTrades;

  /**
   * Whether Soul Fire Aspect is a treasure enchantment (like Vanilla Mending).
   */
  private ForgeConfigSpec.BooleanValue enableSoulFireAspectTreasure;

  /**
   * Whether to enable Soul Flame enchantment.
   */
  private ForgeConfigSpec.BooleanValue enableSoulFlame;

  /**
   * Whether Soul Flame can appear in the enchanting table and loots.
   */
  private ForgeConfigSpec.BooleanValue enableSoulFlameDiscovery;

  /**
   * Whether Soul Flame can be traded with villagers.
   */
  private ForgeConfigSpec.BooleanValue enableSoulFlameTrades;

  /**
   * Whether Soul Flame is a treasure enchantment (like Vanilla Mending).
   */
  private ForgeConfigSpec.BooleanValue enableSoulFlameTreasure;

  /**
   * @param builder configuration builder.
   */
  private ModConfig(ForgeConfigSpec.Builder builder) {
    super(builder);
  }

  /**
   * Returns the value of {@link #enableSoulFireAspect} as read from the configuration file.
   *
   * @return the value of {@link #enableSoulFireAspect} as read from the configuration file.
   */
  public static Boolean getEnableSoulFireAspect() {
    return CONFIG.enableSoulFireAspect.get();
  }

  /**
   * Returns the value of {@link #enableSoulFireAspectDiscovery} as read from the configuration file.
   *
   * @return the value of {@link #enableSoulFireAspectDiscovery} as read from the configuration file.
   */
  public static Boolean getEnableSoulFireAspectDiscovery() {
    return CONFIG.enableSoulFireAspectDiscovery.get();
  }

  /**
   * Returns the value of {@link #enableSoulFireAspectTrades} as read from the configuration file.
   *
   * @return the value of {@link #enableSoulFireAspectTrades} as read from the configuration file.
   */
  public static Boolean getEnableSoulFireAspectTrades() {
    return CONFIG.enableSoulFireAspectTrades.get();
  }

  /**
   * Returns the value of {@link #enableSoulFireAspectTreasure} as read from the configuration file.
   *
   * @return the value of {@link #enableSoulFireAspectTreasure} as read from the configuration file.
   */
  public static Boolean getEnableSoulFireAspectTreasure() {
    return CONFIG.enableSoulFireAspectTreasure.get();
  }

  /**
   * Returns the value of {@link #enableSoulFlame} as read from the configuration file.
   *
   * @return the value of {@link #enableSoulFlame} as read from the configuration file.
   */
  public static Boolean getEnableSoulFlame() {
    return CONFIG.enableSoulFlame.get();
  }

  /**
   * Returns the value of {@link #enableSoulFlameDiscovery} as read from the configuration file.
   *
   * @return the value of {@link #enableSoulFlameDiscovery} as read from the configuration file.
   */
  public static Boolean getEnableSoulFlameDiscovery() {
    return CONFIG.enableSoulFlameDiscovery.get();
  }

  /**
   * Returns the value of {@link #enableSoulFlameTrades} as read from the configuration file.
   *
   * @return the value of {@link #enableSoulFlameTrades} as read from the configuration file.
   */
  public static Boolean getEnableSoulFlameTrades() {
    return CONFIG.enableSoulFlameTrades.get();
  }

  /**
   * Returns the value of {@link #enableSoulFlameTreasure} as read from the configuration file.
   *
   * @return the value of {@link #enableSoulFlameTreasure} as read from the configuration file.
   */
  public static Boolean getEnableSoulFlameTreasure() {
    return CONFIG.enableSoulFlameTreasure.get();
  }

  /**
   * Defines the general enable flag for the specified enchantment.
   *
   * @param builder configuration builder.
   * @param enchantment enchantment name.
   * @return defined configuration property.
   */
  private static ForgeConfigSpec.BooleanValue defineEnableEnchantment(ForgeConfigSpec.Builder builder, String enchantment) {
    return builder.comment(" Whether to enable " + enchantment + " enchantment.", " Takes precedence over all other " + enchantment + " configs.").define("enable " + enchantment.toLowerCase(), true);
  }

  /**
   * Defined the enable treasure flag for the specified enchantment.
   *
   * @param builder configuration builder.
   * @param enchantment enchantment name.
   * @return defined configuration property.
   */
  private static ForgeConfigSpec.BooleanValue defineEnableTreasure(ForgeConfigSpec.Builder builder, String enchantment) {
    return builder.comment(
      " Whether " + enchantment + " cannot appear in the enchanting table.",
      " If enabled along with [enable " + enchantment.toLowerCase() + " discovery], the enchantment won't appear in the enchanting table, but can still be found in loots."
    ).define("enable " + enchantment.toLowerCase() + " treasure", false);
  }

  /**
   * Defined the enable discovery flag for the specified enchantment.
   *
   * @param builder configuration builder.
   * @param enchantment enchantment name.
   * @return defined configuration property.
   */
  private static ForgeConfigSpec.BooleanValue defineEnableDiscovery(ForgeConfigSpec.Builder builder, String enchantment) {
    return builder.comment(" Whether " + enchantment + " can appear in the enchanting table and loots.", " [enable " + enchantment + " treasure] takes precedence.").define("enable " + enchantment + " discovery", true);
  }

  /**
   * Defined the enable trades flag for the specified enchantment.
   *
   * @param builder configuration builder.
   * @param enchantment enchantment name.
   * @return defined configuration property.
   */
  private static ForgeConfigSpec.BooleanValue defineEnableTrades(ForgeConfigSpec.Builder builder, String enchantment) {
    return builder.comment(" Whether " + enchantment + " can be found in villager trades.").define("enable " + enchantment + " trades", true);
  }

  @Override
  protected void define(ForgeConfigSpec.Builder builder) {
    enableSoulFireAspect = defineEnableEnchantment(builder, SOUL_FIRE_ASPECT);
    enableSoulFireAspectTreasure = defineEnableTreasure(builder, SOUL_FIRE_ASPECT);
    enableSoulFireAspectDiscovery = defineEnableDiscovery(builder, SOUL_FIRE_ASPECT);
    enableSoulFireAspectTrades = defineEnableTrades(builder, SOUL_FIRE_ASPECT);
    enableSoulFlame = defineEnableEnchantment(builder, SOUL_FLAME);
    enableSoulFlameTreasure = defineEnableTreasure(builder, SOUL_FLAME);
    enableSoulFlameDiscovery = defineEnableDiscovery(builder, SOUL_FLAME);
    enableSoulFlameTrades = defineEnableTrades(builder, SOUL_FLAME);
  }
}
