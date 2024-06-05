package it.crystalnest.soul_fire_d.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.crystalnest.soul_fire_d.Constants;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireBuilder;
import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

public abstract class FireResourceReloadListener extends SimpleJsonResourceReloadListener {
  /**
   * Current ddfires to unregister (previous registered ddfires).
   */
  protected static final ArrayList<ResourceLocation> ddfiresUnregister = new ArrayList<>();

  /**
   * Current registered ddfires.
   */
  protected static final ArrayList<ResourceLocation> ddfiresRegister = new ArrayList<>();

  /**
   * JSON field name for a Fire's source block.
   */
  private static final String SOURCE_FIELD_NAME = "source";

  /**
   * JSON field name for a Fire's campfire block.
   */
  private static final String CAMPFIRE_FIELD_NAME = "campfire";

  protected FireResourceReloadListener() {
    super(new Gson(), "fires");
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
   * Returns the given {@link JsonElement} as a {@link JsonObject}.
   *
   * @param identifier identifier of the json file.
   * @param element
   * @return the given {@link JsonElement} as a {@link JsonObject}.
   * @throws IllegalStateException if the element is not a {@link JsonObject}.
   */
  private static JsonObject getJsonObject(String identifier, JsonElement element) throws IllegalStateException {
    try {
      return element.getAsJsonObject();
    } catch (IllegalStateException e) {
      Constants.LOGGER.error(Constants.MOD_ID + " encountered a non-blocking DDFire error!\nError parsing ddfire [{}]: not a JSON object.", identifier);
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
  private static <T> T parse(String identifier, String field, JsonObject data, Function<JsonElement, T> parser) throws NullPointerException, UnsupportedOperationException, IllegalStateException, NumberFormatException {
    try {
      return parser.apply(data.get(field));
    } catch (NullPointerException | UnsupportedOperationException | IllegalStateException | NumberFormatException e) {
      Constants.LOGGER.error(Constants.MOD_ID + " encountered a non-blocking DDFire error!\nError parsing required field \"{}\" for ddfire [{}]: missing or malformed field.", field, identifier);
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
  private static <T> T parse(String identifier, String field, JsonObject data, Function<JsonElement, T> parser, T fallback) throws UnsupportedOperationException, IllegalStateException, NumberFormatException {
    try {
      return parser.apply(data.get(field));
    } catch (NullPointerException e) {
      return fallback;
    } catch (UnsupportedOperationException | IllegalStateException | NumberFormatException e) {
      Constants.LOGGER.error(Constants.MOD_ID + " encountered a non-blocking DDFire error!\nError parsing optional field \"{}\" for ddfire [{}]: malformed field.", field, identifier);
      throw e;
    }
  }

  /**
   * Unregisters all DDFires.
   */
  private static void unregisterFires() {
    for (ResourceLocation fireType : ddfiresRegister) {
      if (FireManager.unregisterFire(fireType)) {
        ddfiresUnregister.add(fireType);
      }
    }
    ddfiresRegister.clear();
  }

  /**
   * Registers a DDFire.
   *
   * @param fireType
   * @param fire
   */
  private static void registerFire(ResourceLocation fireType, Fire fire) {
    if (FireManager.registerFire(fire)) {
      ddfiresRegister.add(fireType);
    } else {
      Constants.LOGGER.error("Unable to register ddfire [{}].", fireType);
    }
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> fires, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
    unregisterFires();
    for (Map.Entry<ResourceLocation, JsonElement> fire : fires.entrySet()) {
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
          if (jsonFire.get(SOURCE_FIELD_NAME) != null && jsonFire.get(SOURCE_FIELD_NAME).isJsonNull()) {
            fireBuilder.removeSource();
          } else {
            String source = parse(jsonIdentifier, SOURCE_FIELD_NAME, jsonFire, JsonElement::getAsString, null);
            if (source != null && ResourceLocation.isValidResourceLocation(source)) {
              fireBuilder.setSource(new ResourceLocation(source));
            }
          }
          if (jsonFire.get(CAMPFIRE_FIELD_NAME) != null && jsonFire.get(CAMPFIRE_FIELD_NAME).isJsonNull()) {
            fireBuilder.removeCampfire();
          } else {
            String campfire = parse(jsonIdentifier, CAMPFIRE_FIELD_NAME, jsonFire, JsonElement::getAsString, null);
            if (campfire != null && ResourceLocation.isValidResourceLocation(campfire)) {
              fireBuilder.setCampfire(new ResourceLocation(campfire));
            }
          }
          fireBuilder.removeFireAspect();
          fireBuilder.removeFlame();
          registerFire(fireType, fireBuilder.build());
        }
      } catch (NullPointerException | UnsupportedOperationException | IllegalStateException | NumberFormatException e) {
        Constants.LOGGER.error("Registering of ddfire [{}] is canceled.", jsonIdentifier);
      }
    }
  }
}
