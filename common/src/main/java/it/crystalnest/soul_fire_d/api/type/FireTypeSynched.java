package it.crystalnest.soul_fire_d.api.type;

import net.minecraft.network.syncher.EntityDataAccessor;

/**
 * Type sensitive to the fire type it has (burns or burn).<br />
 * The fire type can change over time, but is synched with an entity data accessor.
 */
public interface FireTypeSynched extends FireTypeChanger {
  /**
   * Returns the fire type {@link EntityDataAccessor}.
   *
   * @return fire type {@link EntityDataAccessor}.
   */
  EntityDataAccessor<String> fireTypeAccessor();
}
