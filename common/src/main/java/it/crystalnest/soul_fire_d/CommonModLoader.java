package it.crystalnest.soul_fire_d;

import it.crystalnest.cobweb.api.pack.fixed.StaticDataPack;
import it.crystalnest.soul_fire_d.fire.FireRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.repository.Pack;
import org.jetbrains.annotations.ApiStatus;

/**
 * Common mod loader.
 */
@ApiStatus.Internal
public final class CommonModLoader {
  private CommonModLoader() {}

  /**
   * Initialize common operations across loaders.
   */
  public static void init() {
    FireRegistry.register();
    new StaticDataPack(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "enchantments"), Pack.Position.TOP).register();
  }
}
