package it.crystalnest.soul_fire_d.platform.services;

import it.crystalnest.cobweb.platform.model.Environment;
import it.crystalnest.cobweb.platform.model.Platform;
import it.crystalnest.soul_fire_d.api.type.FireTypedEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

/**
 * Platform specific helper.
 */
public interface PlatformHelper {
  /**
   * Builds and registers a {@link FireTypedEnchantment} from the provided builder.
   *
   * @param key enchantment key.
   * @param builder enchantment builder.
   * @param <T> enchantment type.
   */
  <T extends Enchantment & FireTypedEnchantment> void registerEnchantment(ResourceLocation key, Supplier<T> builder);

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
