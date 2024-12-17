package it.crystalnest.soul_fire_d.handler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.platform.Services;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Resource reload listener for syncing ddfires.
 */
public class FireResourceReloadListener extends SimpleJsonResourceReloadListener<FireResourceReloadListener.DDFires> {
  /**
   * Current ddfires to unregister (previous registered ddfires).
   */
  protected static final ArrayList<ResourceLocation> ddfiresUnregister = new ArrayList<>();

  /**
   * Current registered ddfires.
   */
  protected static final ArrayList<ResourceLocation> ddfiresRegister = new ArrayList<>();

  protected FireResourceReloadListener() {
    super(DDFires.CODEC, FileToIdConverter.json("fires"));
  }

  /**
   * Handles datapack sync event.
   *
   * @param player {@link ServerPlayer} to which the data is being sent.
   */
  protected static void handle(@Nullable ServerPlayer player) {
    for (ResourceLocation fireType : ddfiresUnregister) {
      Services.NETWORK.sendToClient(player, fireType);
    }
    for (ResourceLocation fireType : ddfiresRegister) {
      Services.NETWORK.sendToClient(player, FireManager.getFire(fireType));
    }
  }

  /**
   * Unregisters all DDFires.
   */
  private static void unregisterFires() {
    for (ResourceLocation fireType : ddfiresRegister) {
      if (FireManager.unregisterFire(fireType) != null) {
        ddfiresUnregister.add(fireType);
      }
    }
    ddfiresRegister.clear();
  }

  /**
   * Registers a DDFire.
   *
   * @param fireType fire type.
   * @param fire fire.
   */
  private static void registerFire(ResourceLocation fireType, Fire fire) {
    if (FireManager.registerFire(fire) != null) {
      ddfiresRegister.add(fireType);
    } else {
      Constants.LOGGER.error("Unable to register ddfire [{}].", fireType);
    }
  }

  @Override
  protected void apply(@NotNull Map<ResourceLocation, DDFires> resourceLocationDDFiresMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
    unregisterFires();
    for (final Map.Entry<ResourceLocation, DDFires> entry : resourceLocationDDFiresMap.entrySet()) {
      DDFires fires = entry.getValue();
      if (Services.PLATFORM.isModLoaded(fires.mod)) {
        for (DDFire fire : fires.fires) {
          ResourceLocation fireType = ResourceLocation.fromNamespaceAndPath(fires.mod, fire.fire);
          Fire.Builder builder = FireManager.fireBuilder(fireType)
            .setDamage(fire.damage.orElse(Fire.Builder.DEFAULT_DAMAGE))
            .setInvertHealAndHarm(fire.invertHealAndHarm.orElse(Fire.Builder.DEFAULT_INVERT_HEAL_AND_HARM))
            .removeComponent(Fire.Component.CAMPFIRE_ITEM)
            .removeComponent(Fire.Component.LANTERN_BLOCK)
            .removeComponent(Fire.Component.LANTERN_ITEM)
            .removeComponent(Fire.Component.TORCH_BLOCK)
            .removeComponent(Fire.Component.TORCH_ITEM)
            .removeComponent(Fire.Component.WALL_TORCH_BLOCK)
            .removeComponent(Fire.Component.FLAME_PARTICLE);
          removeOrSet(builder, fire.source, Fire.Component.SOURCE_BLOCK);
          removeOrSet(builder, fire.campfire, Fire.Component.CAMPFIRE_BLOCK);
          registerFire(fireType, builder.build());
        }
      } else {
        Constants.LOGGER.warn("Registering of ddfires for [{}] is canceled: {} is not loaded.", fires.mod, fires.mod);
      }
    }
  }

  /**
   * Either removes the specified component or sets its value to the provided reference.
   *
   * @param builder {@link Fire.Builder}.
   * @param reference component optional value.
   * @param component {@link Fire.Component} to set.
   */
  private void removeOrSet(Fire.Builder builder, Optional<ResourceLocation> reference, Fire.Component<?, ?> component) {
    if (reference.isPresent()) {
      if ((reference.get().getNamespace().equalsIgnoreCase(ResourceLocation.DEFAULT_NAMESPACE) || reference.get().getNamespace().equalsIgnoreCase(Constants.MOD_ID)) && reference.get().getPath().equalsIgnoreCase("remove")) {
        builder.removeComponent(component);
      } else {
        builder.setComponent(component, reference.get());
      }
    }
  }

  /**
   * Representation of a Data Driven Fire.
   *
   * @param fire fire id.
   * @param damage {@link Fire#invertHealAndHarm}.
   * @param invertHealAndHarm {@link Fire#invertHealAndHarm}.
   * @param source {@link Fire.Component#SOURCE_BLOCK}.
   * @param campfire {@link Fire.Component#CAMPFIRE_BLOCK}.
   */
  protected record DDFire(String fire, Optional<Float> damage, Optional<Boolean> invertHealAndHarm, Optional<ResourceLocation> source, Optional<ResourceLocation> campfire) {
    /**
     * Codec.
     */
    private static final Codec<DDFire> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.STRING.fieldOf("fire").forGetter(ddFire -> ddFire.fire),
      Codec.FLOAT.optionalFieldOf("damage").forGetter(ddFire -> ddFire.damage),
      Codec.BOOL.optionalFieldOf("invertHealAndHarm").forGetter(ddFire -> ddFire.invertHealAndHarm),
      ResourceLocation.CODEC.optionalFieldOf("source").forGetter(ddFire -> ddFire.source),
      ResourceLocation.CODEC.optionalFieldOf("campfire").forGetter(ddFire -> ddFire.campfire)
    ).apply(instance, DDFire::new));
  }

  /**
   * Representation of a list of Data Driven Fires.
   *
   * @param mod mod id.
   * @param fires list of {@link DDFire}s.
   */
  protected record DDFires(String mod, List<DDFire> fires) {
    /**
     * Codec.
     */
    private static final Codec<DDFires> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.STRING.fieldOf("mod").forGetter(ddFires -> ddFires.mod),
      DDFire.CODEC.listOf().fieldOf("fires").forGetter(ddFires -> ddFires.fires)
    ).apply(instance, DDFires::new));
  }
}
