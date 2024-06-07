package it.crystalnest.soul_fire_d.platform.services;

import it.crystalnest.cobweb.platform.model.Environment;
import it.crystalnest.cobweb.platform.model.Platform;

/**
 * Platform specific helper.
 */
public interface PlatformHelper {
  /**
   * Gets the name of the current platform
   *
   * @return The name of the current platform.
   */
  Platform getPlatformName();

  /**
   * Checks if a mod with the given id is loaded.
   *
   * @param modId The mod to check if it is loaded.
   * @return True if the mod is loaded, false otherwise.
   */
  boolean isModLoaded(String modId);

  /**
   * Check if the game is currently in a development environment.
   *
   * @return True if in a development environment, false otherwise.
   */
  boolean isDevEnv();

  /**
   * Gets the name of the environment type as a string.
   *
   * @return The name of the environment type.
   */
  default Environment getEnvironment() {
    return isDevEnv() ? Environment.DEVELOPMENT : Environment.PRODUCTION;
  }
}
