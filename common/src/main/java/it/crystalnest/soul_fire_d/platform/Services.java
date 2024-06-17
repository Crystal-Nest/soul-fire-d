package it.crystalnest.soul_fire_d.platform;

import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.platform.services.NetworkHelper;
import it.crystalnest.soul_fire_d.platform.services.PlatformHelper;
import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

/**
 * Service loaders are a built-in Java feature that allow us to locate implementations of an interface that vary from one environment to another.
 * In the context of MultiLoader we use this feature to access a mock API in the common code that is swapped out for the platform specific implementation at runtime.
 */
@ApiStatus.Internal
public final class Services {
  /**
   * Provides information about what platform the mod is running on.
   */
  public static final PlatformHelper PLATFORM = load(PlatformHelper.class);

  /**
   * Provides networking handling.
   */
  public static final NetworkHelper NETWORK = load(NetworkHelper.class);

  private Services() {}

  /**
   * Load a service for the current environment.
   * Service implementation must be defined manually by including a text file in META-INF/services named with the fully qualified class name of the service.
   * Inside the file you should write the fully qualified class name of the implementation to load for the platform.
   *
   * @param clazz Class to load.
   * @param <T> class type.
   * @return The loaded service.
   */
  private static <T> T load(Class<T> clazz) {
    final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    Constants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
    return loadedService;
  }
}
