package crystalspider.soulfired;

import com.mojang.serialization.Codec;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.config.SoulFiredConfig;
import crystalspider.soulfired.loot.ChestLootModifier;
import crystalspider.soulfired.network.SoulFiredNetwork;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.network.NetworkRegistry.ChannelBuilder;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

/**
 * Soul fire'd mod loader.
 */
@Mod(SoulFiredLoader.MODID)
public final class SoulFiredLoader {
  /**
   * ID of this mod.
   */
  public static final String MODID = "soulfired";

  /**
   * Network channel protocol version.
   */
  public static final String PROTOCOL_VERSION = "1.20.2-3.2";
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  public static final SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(MODID, "main")).networkProtocolVersion(() -> PROTOCOL_VERSION).serverAcceptedVersions(PROTOCOL_VERSION::equals).clientAcceptedVersions(PROTOCOL_VERSION::equals).simpleChannel();

  /**
   * {@link Codec<? extends IGlobalLootModifier>} {@link DeferredRegister deferred register}.
   */
  public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

  /**
   * {@link RegistryObject} for {@link Codec} of {@link ChestLootModifier}.
   */
  public static final RegistryObject<Codec<ChestLootModifier>> CHEST_LOOT_MODIFIER = LOOT_MODIFIERS.register("chest_loot_modifier", ChestLootModifier.CODEC);

  /**
   * Registers {@link SoulFiredConfig}, {@link #LOOT_MODIFIERS}, Soul Fire and {@link SoulFiredNetwork}.
   */
  public SoulFiredLoader() {
    ModLoadingContext.get().registerConfig(Type.COMMON, SoulFiredConfig.SPEC);
    LOOT_MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    FireManager.registerFire(
      FireManager.fireBuilder(FireManager.SOUL_FIRE_TYPE)
        .setDamage(2)
        .setFireAspectConfig(builder -> builder
          .setEnabled(SoulFiredConfig::getEnableSoulFireAspect)
          .setIsDiscoverable(SoulFiredConfig::getEnableSoulFireAspectDiscovery)
          .setIsTradeable(SoulFiredConfig::getEnableSoulFireAspectTrades)
          .setIsTreasure(SoulFiredConfig::getEnableSoulFireAspectTreasure)
        )
        .setFlameConfig(builder -> builder
          .setEnabled(SoulFiredConfig::getEnableSoulFlame)
          .setIsDiscoverable(SoulFiredConfig::getEnableSoulFlameDiscovery)
          .setIsTradeable(SoulFiredConfig::getEnableSoulFlameTrades)
          .setIsTreasure(SoulFiredConfig::getEnableSoulFlameTreasure)
        )
      .build()
    );
    SoulFiredNetwork.register();
  }
}
