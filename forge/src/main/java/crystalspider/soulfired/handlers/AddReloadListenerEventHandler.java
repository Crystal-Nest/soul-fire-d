package crystalspider.soulfired.handlers;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import crystalspider.soulfired.SoulFiredLoader;
import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireBuilder;
import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import crystalspider.soulfired.api.enchantment.FireEnchantmentBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Handles datapack reload events.
 */
@EventBusSubscriber(bus = Bus.FORGE)
public final class AddReloadListenerEventHandler {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger();

  /**
   * Handles the {@link AddReloadListenerEvent}.
   * 
   * @param event
   */
  @SubscribeEvent
  public static void handle(AddReloadListenerEvent event) {
    event.addListener(new FireResourceReloadListener());
  }

  /**
   * Reloads the DDFires datapack.
   */
  private static class FireResourceReloadListener extends SimpleJsonResourceReloadListener {
    /**
     * Current registered ddfires.
     */
    private static volatile ConcurrentHashMap<ResourceLocation, Fire> ddfires = new ConcurrentHashMap<>();

    private FireResourceReloadListener() {
      super(new Gson(), "fires");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> fires, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      // Unregister previous ddfires.
      for (ResourceLocation fireType : ddfires.keySet()) {
        FireManager.unregisterFire(fireType);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new SafeRunnable() { @Override public void run() { FireClientManager.unregisterFire(fireType); } });
      }
      ddfires.clear();
      // Register new ddfires.
      for (Entry<ResourceLocation, JsonElement> fire : fires.entrySet()) {
        String jsonIdentifier = fire.getKey().getPath();
        try {
          JsonObject jsonFire = fire.getValue().getAsJsonObject();
          ResourceLocation fireType = new ResourceLocation(parse(jsonIdentifier, "mod", jsonFire, JsonElement::getAsString), fire.getKey().getPath());
          FireBuilder fireBuilder = FireManager.fireBuilder(fireType)
            .setDamage(parse(jsonIdentifier, "damage", jsonFire, JsonElement::getAsFloat, FireBuilder.DEFAULT_DAMAGE))
            .setInvertHealAndHarm(parse(jsonIdentifier, "invertHealAndHarm", jsonFire, JsonElement::getAsBoolean, FireBuilder.DEFAULT_INVERT_HEAL_AND_HARM));
          if (jsonFire.get("source") != null && jsonFire.get("source").isJsonNull()) {
            fireBuilder.removeSource();
          } else {
            String source = parse(jsonIdentifier, "source", jsonFire, JsonElement::getAsString, null);
            if (source != null && ResourceLocation.isValidResourceLocation(source)) {
              fireBuilder.setSource(new ResourceLocation(source));
            }
          }
          if (jsonFire.get("campfire") != null && jsonFire.get("campfire").isJsonNull()) {
            fireBuilder.removeCampfire();
          } else {
            String campfire = parse(jsonIdentifier, "campfire", jsonFire, JsonElement::getAsString, null);
            if (campfire != null && ResourceLocation.isValidResourceLocation(campfire)) {
              fireBuilder.setCampfire(new ResourceLocation(campfire));
            }
          }
          if (jsonFire.get("fireAspect") != null && jsonFire.get("fireAspect").isJsonNull()) {
            fireBuilder.removeFireAspect();
          } else {
            JsonObject config = parse(jsonIdentifier, "fireAspect", jsonFire, JsonElement::getAsJsonObject, null);
            if (config != null) {
              fireBuilder.setFireAspectConfig(builder -> builder
                .setRarity(parseRarity(parse(jsonIdentifier, "rarity", config, JsonElement::getAsString, "very_rare")))
                .setIsTreasure(parse(jsonIdentifier, "isTreasure", config, JsonElement::getAsBoolean, FireEnchantmentBuilder.DEFAULT_IS_TREASURE))
                .setIsCurse(parse(jsonIdentifier, "isCurse", config, JsonElement::getAsBoolean, FireEnchantmentBuilder.DEFAULT_IS_CURSE))
                .setIsTradeable(parse(jsonIdentifier, "isTradeable", config, JsonElement::getAsBoolean, FireEnchantmentBuilder.DEFAULT_IS_TRADEABLE))
                .setIsDiscoverable(parse(jsonIdentifier, "isDiscoverable", config, JsonElement::getAsBoolean, FireEnchantmentBuilder.DEFAULT_IS_DISCOVERABLE))
              );
            }
          }
          if (jsonFire.get("flame") != null && jsonFire.get("flame").isJsonNull()) {
            fireBuilder.removeFlame();
          } else {
            JsonObject config = parse(jsonIdentifier, "flame", jsonFire, JsonElement::getAsJsonObject, null);
            if (config != null) {
              fireBuilder.setFlameConfig(builder -> builder
                .setRarity(parseRarity(parse(jsonIdentifier, "rarity", config, JsonElement::getAsString, "very_rare")))
                .setIsTreasure(parse(jsonIdentifier, "isTreasure", config, JsonElement::getAsBoolean, FireEnchantmentBuilder.DEFAULT_IS_TREASURE))
                .setIsCurse(parse(jsonIdentifier, "isCurse", config, JsonElement::getAsBoolean, FireEnchantmentBuilder.DEFAULT_IS_CURSE))
                .setIsTradeable(parse(jsonIdentifier, "isTradeable", config, JsonElement::getAsBoolean, FireEnchantmentBuilder.DEFAULT_IS_TRADEABLE))
                .setIsDiscoverable(parse(jsonIdentifier, "isDiscoverable", config, JsonElement::getAsBoolean, FireEnchantmentBuilder.DEFAULT_IS_DISCOVERABLE))
              );
            }
          }
          Fire fireBuilt = fireBuilder.build();
          if (FireManager.registerFire(fireBuilt)) {
            ddfires.put(fireType, fireBuilt);
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new SafeRunnable() { @Override public void run() { FireClientManager.registerFire(FireManager.getFire(fireType)); } });
          } else {
            LOGGER.warn("Unable to register ddfire [" + fireType + "].");
          }
        } catch (NullPointerException | UnsupportedOperationException | IllegalStateException | NumberFormatException e) {
          LOGGER.error("Registering of ddfire [" + jsonIdentifier + "] is canceled.");
        }
      }
    }

    /**
     * Parses the given {@link JsonObject data} to retrieve the specified {@code field} using the provided {@code parser}.
     * 
     * @param <T>
     * @param fire identifier of the fire being built.
     * @param field field to parse.
     * @param data {@link JsonObject} with data to parse.
     * @param parser function to use to retrive parse a json field.
     * @return value of the field.
     * @throws NullPointerException if there's no such field.
     * @throws UnsupportedOperationException if this element is not a JsonPrimitive or JsonArray.
     * @throws IllegalStateException if this element is of the type JsonArray but contains more than a single element.
     * @throws NumberFormatException if the value contained is not a valid number and the expected type ({@code T}) was a number.
     */
    private <T> T parse(String fire, String field, JsonObject data, Function<JsonElement, T> parser) throws NullPointerException, UnsupportedOperationException, IllegalStateException, NumberFormatException {
      try {
        return parser.apply(data.get(field));
      } catch (NullPointerException | UnsupportedOperationException | IllegalStateException | NumberFormatException e) {
        LOGGER.error(SoulFiredLoader.MODID + " encountered a non-blocking DDFire error!\nError parsing field \"" + field + "\" for ddfire [" + fire + "]: missing or malformed field.");
        throw e;
      }
    }

    /**
     * Parses the given {@link JsonObject data} to retrieve the specified {@code field} using the provided {@code parser}.
     * 
     * @param <T>
     * @param fire identifier of the fire being built.
     * @param field field to parse.
     * @param data {@link JsonObject} with data to parse.
     * @param parser function to use to retrive parse a json field.
     * @param fallback default value if no field named {@code field} exists.
     * @return value of the field or default.
     * @throws UnsupportedOperationException if this element is not a JsonPrimitive or JsonArray.
     * @throws IllegalStateException if this element is of the type JsonArray but contains more than a single element.
     * @throws NumberFormatException if the value contained is not a valid number and the expected type ({@code T}) was a number.
     */
    private <T> T parse(String fire, String field, JsonObject data, Function<JsonElement, T> parser, T fallback) throws UnsupportedOperationException, IllegalStateException, NumberFormatException {
      try {
        return parser.apply(data.get(field));
      } catch (NullPointerException e) {
        return fallback;
      } catch (UnsupportedOperationException | IllegalStateException | NumberFormatException e) {
        LOGGER.error(SoulFiredLoader.MODID + " encountered a non-blocking DDFire error!\nError parsing field \"" + field + "\" for ddfire [" + fire + "]: malformed field.");
        throw e;
      }
    }

    /**
     * Parses the given string to retrieve the proper {@link Rarity} value.
     * 
     * @param rarity string to parse.
     * @return {@link Rarity}.
     */
    private Rarity parseRarity(String rarity) {
      switch (rarity.toLowerCase()) {
        case "common":
          return Rarity.COMMON;
        case "uncommon":
          return Rarity.UNCOMMON;
        case "rare":
          return Rarity.RARE;
        case "very_rare":
          return Rarity.VERY_RARE;
        default:
          return FireEnchantmentBuilder.DEFAULT_RARITY;
      }
    }
  }
} 

