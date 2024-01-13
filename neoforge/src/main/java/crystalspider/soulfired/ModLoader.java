package crystalspider.soulfired;

import com.mojang.serialization.Codec;
import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.config.ModConfig;
import crystalspider.soulfired.loot.ChestLootModifier;
import crystalspider.soulfired.network.SoulFiredNetwork;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.network.NetworkRegistry.ChannelBuilder;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Soul fire'd mod loader.
 */
@Mod(ModLoader.MOD_ID)
public final class ModLoader {
  /**
   * ID of this mod.
   */
  public static final String MOD_ID = "soulfired";

  /**
   * Network channel protocol version.
   */
  public static final String PROTOCOL_VERSION = "1.20.4-3.2";
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  public static final SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(MOD_ID, "main")).networkProtocolVersion(() -> PROTOCOL_VERSION).serverAcceptedVersions(PROTOCOL_VERSION::equals).clientAcceptedVersions(PROTOCOL_VERSION::equals).simpleChannel();

  /**
   * {@link Codec<? extends IGlobalLootModifier>} {@link DeferredRegister deferred register}.
   */
  public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MOD_ID);

  /**
   * {@link DeferredHolder} for {@link Codec} of {@link ChestLootModifier}.
   */
  public static final DeferredHolder<Codec<? extends IGlobalLootModifier>, Codec<ChestLootModifier>> CHEST_LOOT_MODIFIER = LOOT_MODIFIERS.register("chest_loot_modifier", ChestLootModifier.CODEC);

  /**
   * Registers {@link ModConfig}, {@link #LOOT_MODIFIERS}, Soul Fire and {@link SoulFiredNetwork}.
   */
  public ModLoader(IEventBus bus) {
    ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.SPEC);
    LOOT_MODIFIERS.register(bus);
    FireManager.registerFire(
      FireManager.fireBuilder(FireManager.SOUL_FIRE_TYPE)
        .setDamage(2)
        .setFireAspectConfig(builder -> builder
          .setEnabled(ModConfig::getEnableSoulFireAspect)
          .setIsDiscoverable(ModConfig::getEnableSoulFireAspectDiscovery)
          .setIsTradeable(ModConfig::getEnableSoulFireAspectTrades)
          .setIsTreasure(ModConfig::getEnableSoulFireAspectTreasure)
        )
        .setFlameConfig(builder -> builder
          .setEnabled(ModConfig::getEnableSoulFlame)
          .setIsDiscoverable(ModConfig::getEnableSoulFlameDiscovery)
          .setIsTradeable(ModConfig::getEnableSoulFlameTrades)
          .setIsTreasure(ModConfig::getEnableSoulFlameTreasure)
        )
        .build()
    );
    SoulFiredNetwork.register();
  }
}
