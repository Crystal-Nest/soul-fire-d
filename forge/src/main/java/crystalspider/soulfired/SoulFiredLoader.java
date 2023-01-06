package crystalspider.soulfired;

import crystalspider.soulfired.config.SoulFiredConfig;
import crystalspider.soulfired.loot.ChestLootModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Soul fire'd mod loader.
 */
@Mod(SoulFiredLoader.MODID)
public class SoulFiredLoader {
  /**
   * ID of this mod.
   */
  public static final String MODID = "soulfired";

  /**
   * Network channel protocol version.
   */
  public static final String PROTOCOL_VERSION = "1.16-3.0";
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

  /**
   * {@link GlobalLootModifierSerializer} {@link DeferredRegister deferred register}.
   */
  public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MODID);

  /**
   * {@link RegistryObject} for {@link ChestLootModifier} {@link ChestLootModifier.Serializer Serializer}.
   */
  public static final RegistryObject<ChestLootModifier.Serializer> CHEST_LOOT_MODIFIER = LOOT_MODIFIERS.register("chest_loot_modifier", ChestLootModifier.Serializer::new);

  /**
   * Registers {@link SoulFiredConfig} and {@link #LOOT_MODIFIERS}.
   */
  public SoulFiredLoader() {
    ModLoadingContext.get().registerConfig(Type.COMMON, SoulFiredConfig.SPEC);
    LOOT_MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }
}
