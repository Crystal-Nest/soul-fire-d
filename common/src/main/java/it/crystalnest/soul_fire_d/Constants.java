package it.crystalnest.soul_fire_d;

import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common shared constants across all loaders.
 */
@ApiStatus.Internal
public final class Constants {
  /**
   * Mod id.
   */
  public static final String MOD_ID = "soul_fire_d";

  /**
   * Data Driven Fires id.
   */
  public static final String DDFIRES = "ddfires";

  /**
   * Mod logger.
   */
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  private Constants() {}
}
