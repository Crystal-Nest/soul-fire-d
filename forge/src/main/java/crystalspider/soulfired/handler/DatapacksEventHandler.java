package crystalspider.soulfired.handler;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import crystalspider.soulfired.ModLoader;
import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireBuilder;
import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.network.SoulFiredNetwork;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;

/**
 * Handles datapack reload events.
 */
@EventBusSubscriber(modid = ModLoader.MOD_ID, bus = Bus.FORGE)
public final class DatapacksEventHandler {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger();

  /**
   * Current ddfires to unregister (previous registered ddfires).
   */
  private static ArrayList<ResourceLocation> unregister_ddfires = new ArrayList<>();
  /**
   * Current registered ddfires.
   */
  private static ArrayList<ResourceLocation> register_ddfires = new ArrayList<>();

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
   * Handles the {@link OnDatapackSyncEvent}.
   * 
   * @param event
   */
  @SubscribeEvent
  public static void handle(OnDatapackSyncEvent event) {
    for (ResourceLocation fireType : unregister_ddfires) {
      SoulFiredNetwork.sendToClient(event.getPlayer(), fireType);
    }
    for (ResourceLocation fireType : register_ddfires) {
      SoulFiredNetwork.sendToClient(event.getPlayer(), FireManager.getFire(fireType));
    }
  }

  /**
   * Reloads the DDFires datapack.
   */
  private static class FireResourceReloadListener extends JsonReloadListener {
    private FireResourceReloadListener() {
      super(new Gson(), "fires");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> fires, IResourceManager resourceManager, IProfiler profiler) {
      unregisterFires();
      for (Entry<ResourceLocation, JsonElement> fire : fires.entrySet()) {
        String jsonIdentifier = fire.getKey().getPath();
        try {
          JsonObject jsonData = getJsonObject(jsonIdentifier, fire.getValue());
          String mod = parse(jsonIdentifier, "mod", jsonData, JsonElement::getAsString);
          JsonArray jsonFires = parse(jsonIdentifier, "fires", jsonData, JsonElement::getAsJsonArray);
          for (JsonElement element : jsonFires) {
            JsonObject jsonFire = getJsonObject(jsonIdentifier, element);
            ResourceLocation fireType = new ResourceLocation(mod, parse(jsonIdentifier, "fire", jsonFire, JsonElement::getAsString));
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
            fireBuilder.removeFireAspect();
            fireBuilder.removeFlame();
            registerFire(fireType, fireBuilder.build());
          }
        } catch (NullPointerException | UnsupportedOperationException | IllegalStateException | NumberFormatException e) {
          LOGGER.error("Registering of ddfire [" + jsonIdentifier + "] is canceled.");
        }
      }
    }

    /**
     * Returns the given {@link JsonElement} as a {@link JsonObject}.
     * 
     * @param identifier identifier of the json file.
     * @param element
     * @return the given {@link JsonElement} as a {@link JsonObject}.
     * @throws IllegalStateException if the element is not a {@link JsonObject}.
     */
    private JsonObject getJsonObject(String identifier, JsonElement element) throws IllegalStateException {
      try {
        return element.getAsJsonObject();
      } catch (IllegalStateException e) {
        LOGGER.error(ModLoader.MOD_ID + " encountered a non-blocking DDFire error!\nError parsing ddfire [" + identifier + "]: not a JSON object.");
        throw e;
      }
    }

    /**
     * Parses the given {@link JsonObject data} to retrieve the specified {@code field} using the provided {@code parser}.
     * 
     * @param <T>
     * @param identifier identifier of the json file.
     * @param field field to parse.
     * @param data {@link JsonObject} with data to parse.
     * @param parser function to use to retrive parse a json field.
     * @return value of the field.
     * @throws NullPointerException if there's no such field.
     * @throws UnsupportedOperationException if this element is not a {@link JsonPrimitive} or {@link JsonArray}.
     * @throws IllegalStateException if this element is of the type {@link JsonArray} but contains more than a single element.
     * @throws NumberFormatException if the value contained is not a valid number and the expected type ({@code T}) was a number.
     */
    private <T> T parse(String identifier, String field, JsonObject data, Function<JsonElement, T> parser) throws NullPointerException, UnsupportedOperationException, IllegalStateException, NumberFormatException {
      try {
        return parser.apply(data.get(field));
      } catch (NullPointerException | UnsupportedOperationException | IllegalStateException | NumberFormatException e) {
        LOGGER.error(ModLoader.MOD_ID + " encountered a non-blocking DDFire error!\nError parsing required field \"" + field + "\" for ddfire [" + identifier + "]: missing or malformed field.");
        throw e;
      }
    }

    /**
     * Parses the given {@link JsonObject data} to retrieve the specified {@code field} using the provided {@code parser}.
     * 
     * @param <T>
     * @param identifier identifier of the json file.
     * @param field field to parse.
     * @param data {@link JsonObject} with data to parse.
     * @param parser function to use to retrive parse a json field.
     * @param fallback default value if no field named {@code field} exists.
     * @return value of the field or default.
     * @throws UnsupportedOperationException if this element is not a {@link JsonPrimitive} or {@link JsonArray}.
     * @throws IllegalStateException if this element is of the type {@link JsonArray} but contains more than a single element.
     * @throws NumberFormatException if the value contained is not a valid number and the expected type ({@code T}) was a number.
     */
    private <T> T parse(String identifier, String field, JsonObject data, Function<JsonElement, T> parser, T fallback) throws UnsupportedOperationException, IllegalStateException, NumberFormatException {
      try {
        return parser.apply(data.get(field));
      } catch (NullPointerException e) {
        return fallback;
      } catch (UnsupportedOperationException | IllegalStateException | NumberFormatException e) {
        LOGGER.error(ModLoader.MOD_ID + " encountered a non-blocking DDFire error!\nError parsing optional field \"" + field + "\" for ddfire [" + identifier + "]: malformed field.");
        throw e;
      }
    }

    /**
     * Unregisters all DDFires.
     */
    @SuppressWarnings("deprecation")
    private void unregisterFires() {
      for (ResourceLocation fireType : register_ddfires) {
        if (FireManager.unregisterFire(fireType)) {
          unregister_ddfires.add(fireType);
        }
      }
      register_ddfires.clear();
    }

    /**
     * Registers a DDFire.
     * 
     * @param fireType
     * @param fire
     */
    private void registerFire(ResourceLocation fireType, Fire fire) {
      if (FireManager.registerFire(fire)) {
        register_ddfires.add(fireType);
      } else {
        LOGGER.error("Unable to register ddfire [" + fireType + "].");
      }
    }
  }
} 
