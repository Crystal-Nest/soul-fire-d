package crystalspider.soulfired.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import crystalspider.soulfired.SoulFiredLoader;
import crystalspider.soulfired.api.Fire;
import crystalspider.soulfired.api.FireBuilder;
import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.network.SoulFiredNetwork;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

/**
 * Resource (datapack) reload listener.
 */
public class FireResourceReloadListener implements SimpleResourceReloadListener<HashMap<Identifier, Fire>> {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger();

  /**
   * Current ddfires to unregister (previous registered ddfires).
   */
  private static ArrayList<Identifier> unregister_ddfires = new ArrayList<>();
  /**
   * Current registered ddfires.
   */
  private static ArrayList<Identifier> register_ddfires = new ArrayList<>();

  /**
   * Handles datapack sync event.
   * 
   * @param player {@link ServerPlayerEntity} to which the data is being sent.
   * @param joined {@code true} if the player is joining the server, {@code false} if the server finished a successful resource reload.
   */
  public static void handle(ServerPlayerEntity player, boolean joined) {
    for (Identifier fireType : unregister_ddfires) {
      SoulFiredNetwork.sendToClient(player, fireType);
    }
    for (Identifier fireType : register_ddfires) {
      SoulFiredNetwork.sendToClient(player, FireManager.getFire(fireType));
    }
  }

  @Override
  public Identifier getFabricId() {
    return new Identifier(SoulFiredLoader.MODID, "ddfires");
  }

  @Override
  public CompletableFuture<HashMap<Identifier, Fire>> load(ResourceManager manager, Profiler profiler, Executor executor) {
    return CompletableFuture.supplyAsync(() -> {
      HashMap<Identifier, Fire> data = new HashMap<>();
      for (Entry<Identifier, Resource> entry : manager.findResources("fires", path -> path.getNamespace().equals(SoulFiredLoader.MODID) && path.getPath().endsWith(".json"))) {
        String jsonIdentifier = entry.getKey().getPath();
        try {
          JsonObject fire = JsonParser.parseReader(new Gson().newJsonReader(entry.getValue().getReader())).getAsJsonObject();
          try {
            JsonObject jsonData = getJsonObject(jsonIdentifier, fire);
            String mod = parse(jsonIdentifier, "mod", jsonData, JsonElement::getAsString);
            JsonArray jsonFires = parse(jsonIdentifier, "fires", jsonData, JsonElement::getAsJsonArray);
            for (JsonElement element : jsonFires) {
              JsonObject jsonFire = getJsonObject(jsonIdentifier, element);
              Identifier fireType = new Identifier(mod, parse(jsonIdentifier, "fire", jsonFire, JsonElement::getAsString));
              FireBuilder fireBuilder = FireManager.fireBuilder(fireType)
                .setDamage(parse(jsonIdentifier, "damage", jsonFire, JsonElement::getAsFloat, FireBuilder.DEFAULT_DAMAGE))
                .setInvertHealAndHarm(parse(jsonIdentifier, "invertHealAndHarm", jsonFire, JsonElement::getAsBoolean, FireBuilder.DEFAULT_INVERT_HEAL_AND_HARM));
              if (jsonFire.get("source") != null && jsonFire.get("source").isJsonNull()) {
                fireBuilder.removeSource();
              } else {
                String source = parse(jsonIdentifier, "source", jsonFire, JsonElement::getAsString, null);
                if (source != null && Identifier.isValid(source)) {
                  fireBuilder.setSource(new Identifier(source));
                }
              }
              if (jsonFire.get("campfire") != null && jsonFire.get("campfire").isJsonNull()) {
                fireBuilder.removeCampfire();
              } else {
                String campfire = parse(jsonIdentifier, "campfire", jsonFire, JsonElement::getAsString, null);
                if (campfire != null && Identifier.isValid(campfire)) {
                  fireBuilder.setCampfire(new Identifier(campfire));
                }
              }
              fireBuilder.removeFireAspect();
              fireBuilder.removeFlame();
              data.put(fireType, fireBuilder.build());
            }
          } catch (NullPointerException | UnsupportedOperationException | IllegalStateException | NumberFormatException e) {
            LOGGER.error("Registering of ddfire [" + jsonIdentifier + "] is canceled.");
          }
        } catch (IOException | IllegalStateException e) {
          LOGGER.error("Error while retrieving ddfire [" + jsonIdentifier + "]: ", e);
        }
      }
      return data;
    }, executor);
  }

  @Override
  public CompletableFuture<Void> apply(HashMap<Identifier, Fire> data, ResourceManager manager, Profiler profiler, Executor executor) {
    return CompletableFuture.runAsync(() -> {
      unregisterFires();
      for (Entry<Identifier, Fire> entry : data.entrySet()) {
        registerFire(entry.getKey(), entry.getValue());
      }
    }, executor);
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
      LOGGER.error(SoulFiredLoader.MODID + " encountered a non-blocking DDFire error!\nError parsing ddfire [" + identifier + "]: not a JSON object.");
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
      LOGGER.error(SoulFiredLoader.MODID + " encountered a non-blocking DDFire error!\nError parsing required field \"" + field + "\" for ddfire [" + identifier + "]: missing or malformed field.");
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
      LOGGER.error(SoulFiredLoader.MODID + " encountered a non-blocking DDFire error!\nError parsing optional field \"" + field + "\" for ddfire [" + identifier + "]: malformed field.");
      throw e;
    }
  }

  /**
   * Unregisters all DDFires.
   */
  @SuppressWarnings("deprecation")
  private void unregisterFires() {
    for (Identifier fireType : register_ddfires) {
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
  private void registerFire(Identifier fireType, Fire fire) {
    if (FireManager.registerFire(fire)) {
      register_ddfires.add(fireType);
    } else {
      LOGGER.error("Unable to register ddfire [" + fireType + "].");
    }
  }
}
