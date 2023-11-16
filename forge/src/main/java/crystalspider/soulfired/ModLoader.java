package crystalspider.soulfired;

import com.mojang.serialization.Codec;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.config.ModConfig;
import crystalspider.soulfired.loot.ChestLootModifier;
import crystalspider.soulfired.network.SoulFiredNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
  public static final String PROTOCOL_VERSION = "1.19.4-3.2";
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

  /**
   * {@link Codec<? extends IGlobalLootModifier>} {@link DeferredRegister deferred register}.
   */
  public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MOD_ID);

  /**
   * {@link RegistryObject} for {@link Codec} of {@link ChestLootModifier}.
   */
  public static final RegistryObject<Codec<ChestLootModifier>> CHEST_LOOT_MODIFIER = LOOT_MODIFIERS.register("chest_loot_modifier", ChestLootModifier.CODEC);

  /**
   * Registers {@link ModConfig}, {@link #LOOT_MODIFIERS}, Soul Fire and {@link SoulFiredNetwork}.
   */
  public ModLoader() {
    ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.SPEC);
    LOOT_MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
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
