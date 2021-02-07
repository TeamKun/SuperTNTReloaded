//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.mcreator.waterworld;

import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod("waterworld")
public class WaterworldMod {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation("waterworld", "waterworld"), () -> {
        return "1";
    }, "1"::equals, "1"::equals);
    public WaterworldModElements elements = new WaterworldModElements();

    public WaterworldMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void init(FMLCommonSetupEvent event) {
        this.elements.getElements().forEach((element) -> {
            element.init(event);
        });
    }

    @SubscribeEvent
    public void serverLoad(FMLServerStartingEvent event) {
        this.elements.getElements().forEach((element) -> {
            element.serverLoad(event);
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void clientLoad(FMLClientSetupEvent event) {
        this.elements.getElements().forEach((element) -> {
            element.clientLoad(event);
        });
    }

    @SubscribeEvent
    public void registerBlocks(Register<Block> event) {
        event.getRegistry().registerAll(this.elements.getBlocks().stream().map(Supplier::get).toArray(Block[]::new));
    }

    @SubscribeEvent
    public void registerItems(Register<Item> event) {
        event.getRegistry().registerAll(this.elements.getItems().stream().map(Supplier::get).toArray(Item[]::new));
    }

    @SubscribeEvent
    public void registerBiomes(Register<Biome> event) {
        event.getRegistry().registerAll(this.elements.getBiomes().stream().map(Supplier::get).toArray(Biome[]::new));
    }

    @SubscribeEvent
    public void registerEntities(Register<EntityType<?>> event) {
        event.getRegistry().registerAll(this.elements.getEntities().stream().map(Supplier::get).toArray(EntityType[]::new));
    }

    @SubscribeEvent
    public void registerEnchantments(Register<Enchantment> event) {
        event.getRegistry().registerAll(this.elements.getEnchantments().stream().map(Supplier::get).toArray(Enchantment[]::new));
    }

    @SubscribeEvent
    public void registerSounds(Register<SoundEvent> event) {
        this.elements.registerSounds(event);
    }
}
