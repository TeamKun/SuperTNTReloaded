//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.mcreator.waterworld;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;

public class WaterworldModElements {
    public final List<WaterworldModElements.ModElement> elements = new ArrayList();
    public final List<Supplier<Block>> blocks = new ArrayList();
    public final List<Supplier<Item>> items = new ArrayList();
    public final List<Supplier<Biome>> biomes = new ArrayList();
    public final List<Supplier<EntityType<?>>> entities = new ArrayList();
    public final List<Supplier<Enchantment>> enchantments = new ArrayList();
    public static Map<ResourceLocation, SoundEvent> sounds = new HashMap();
    private int messageID = 0;

    public WaterworldModElements() {
        try {
            ModFileScanData modFileInfo = ModList.get().getModFileById("waterworld").getFile().getScanResult();
            Set<AnnotationData> annotations = modFileInfo.getAnnotations();
            Iterator var3 = annotations.iterator();

            while(var3.hasNext()) {
                AnnotationData annotationData = (AnnotationData)var3.next();
                if (annotationData.getAnnotationType().getClassName().equals(WaterworldModElements.ModElement.Tag.class.getName())) {
                    Class<?> clazz = Class.forName(annotationData.getClassType().getClassName());
                    if (clazz.getSuperclass() == WaterworldModElements.ModElement.class) {
                        this.elements.add((WaterworldModElements.ModElement)clazz.getConstructor(this.getClass()).newInstance(this));
                    }
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        Collections.sort(this.elements);
        this.elements.forEach(WaterworldModElements.ModElement::initElements);
    }

    public void registerSounds(Register<SoundEvent> event) {
        Iterator var2 = sounds.entrySet().iterator();

        while(var2.hasNext()) {
            Entry<ResourceLocation, SoundEvent> sound = (Entry)var2.next();
            event.getRegistry().register(((SoundEvent)sound.getValue()).setRegistryName((ResourceLocation)sound.getKey()));
        }

    }

    public <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, PacketBuffer> encoder, Function<PacketBuffer, T> decoder, BiConsumer<T, Supplier<Context>> messageConsumer) {
        WaterworldMod.PACKET_HANDLER.registerMessage(this.messageID, messageType, encoder, decoder, messageConsumer);
        ++this.messageID;
    }

    public List<WaterworldModElements.ModElement> getElements() {
        return this.elements;
    }

    public List<Supplier<Block>> getBlocks() {
        return this.blocks;
    }

    public List<Supplier<Item>> getItems() {
        return this.items;
    }

    public List<Supplier<Biome>> getBiomes() {
        return this.biomes;
    }

    public List<Supplier<EntityType<?>>> getEntities() {
        return this.entities;
    }

    public List<Supplier<Enchantment>> getEnchantments() {
        return this.enchantments;
    }

    public static class ModElement implements Comparable<WaterworldModElements.ModElement> {
        protected final WaterworldModElements elements;
        protected final int sortid;

        public ModElement(WaterworldModElements elements, int sortid) {
            this.elements = elements;
            this.sortid = sortid;
        }

        public void initElements() {
        }

        public void init(FMLCommonSetupEvent event) {
        }

        public void serverLoad(FMLServerStartingEvent event) {
        }

        @OnlyIn(Dist.CLIENT)
        public void clientLoad(FMLClientSetupEvent event) {
        }

        public int compareTo(WaterworldModElements.ModElement other) {
            return this.sortid - other.sortid;
        }

        @Retention(RetentionPolicy.RUNTIME)
        public @interface Tag {
        }
    }
}
