package crystalspider.soulfired;



import java.util.function.Supplier;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.enchantment.ArrowSoulFireEnchantment;
import crystalspider.soulfired.enchantment.SoulFireAspectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
  public static final String PROTOCOL_VERSION = "1.18-1.0";
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

  // TODO: tidy up
  public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID);

  public static final RegistryObject<Enchantment> SOUL_FIRE_ASPECT = register("soul_fire_aspect", SoulFireAspectEnchantment::new);
  public static final RegistryObject<Enchantment> SOUL_FLAMING_ARROWS = register("soul_flame", ArrowSoulFireEnchantment::new);

  private static final RegistryObject<Enchantment> register(String id, Supplier<? extends Enchantment> supplier) {
    return SoulFiredLoader.ENCHANTMENTS.register(id, supplier);
  }
  // tidy up

  public SoulFiredLoader() {
    ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    FireManager.registerFire(
      FireManager.getFireBuilder()
        .setId(FireManager.SOUL_FIRE_ID)
        .setDamage(2)
        .setSourceBlock(Blocks.SOUL_FIRE)
      .build()
    );
    // TODO: Projectile
    // TODO: AbstractArrow
    // Dynamically add enchantments for all registered Fires.
    /**
     * Registered objects should not be stored in fields when they are created and registered.
     * They are to be always newly created and registered whenever RegisterEvent is fired for that registry.
     * This is to allow dynamic loading and unloading of mods in a future version of Forge.
     * 
     * Also handles Registry Replacement (although so does ObjectHolder if your using that instead) + creating the object at the correct time in modern versions of Minecraft (seeing as Mojang now lock/unlock registries)
     */
  }
}
