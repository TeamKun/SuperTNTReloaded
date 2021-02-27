package com.xeraster.supertnt.init;

import com.xeraster.supertnt.SoundHandler;
import com.xeraster.supertnt.items.CacaIngot;
import com.xeraster.supertnt.items.CheeseSlice;
import com.xeraster.supertnt.items.CustomAxe;
import com.xeraster.supertnt.items.CustomPickaxe;
import com.xeraster.supertnt.items.CustomShovel;
import com.xeraster.supertnt.items.CustomSword;
import com.xeraster.supertnt.items.NostalgiaItem;
import com.xeraster.supertnt.items.OtherCustomSword;
import com.xeraster.supertnt.items.StarWarsRecord;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder("supertnt")
public class Items {

    public static final StarWarsRecord STARWARSRECORD = new StarWarsRecord("starwarsrecord", SoundHandler.ACROSSTHESTARS2);
    public static final StarWarsRecord HALORECORD = new StarWarsRecord("halorecord", SoundHandler.NEVERFORGET);
    public static final StarWarsRecord CHEESERECORD = new StarWarsRecord("cheeserecord", SoundHandler.CHEESE);
    public static final CacaIngot CACAINGOT = new CacaIngot("cacaingot");
    public static final NostalgiaItem REDSOLOCUP = new NostalgiaItem("redsolocup");
    public static final NostalgiaItem SIMMRAM = new NostalgiaItem("64mbram");
    public static final NostalgiaItem ACTIONREPLAY = new NostalgiaItem("actionreplay");
    public static final NostalgiaItem FLOPPY = new NostalgiaItem("floppydisc");
    public static final NostalgiaItem NOKIA = new NostalgiaItem("nokia");
    public static final NostalgiaItem SNESCONTROLLER = new NostalgiaItem("snescontroller");
    public static final NostalgiaItem SONIC2 = new NostalgiaItem("sonic2");
    public static final CustomSword ENERGYSWORD = new CustomSword("energysword", ToolMaterial.DIAMOND, 4.0F);
    public static final CustomSword LIGHTSABER = new CustomSword("lightsaber", ToolMaterial.DIAMOND, 4.0F);
    public static final CustomSword PS2KEYBOARD = new CustomSword("ps2keyboard", ToolMaterial.WOOD, 1.0F);
    public static final CheeseSlice CHEESEPIECE = new CheeseSlice("cheesepiece");
    public static final CustomShovel CACASHOVEL = new CustomShovel(ToolMaterial.STONE, "cacashovel", 3.0F, 3.0F);
    public static final CustomPickaxe CACAPICKAXE = new CustomPickaxe(ToolMaterial.STONE, "cacapickaxe");
    public static final CustomAxe CACAAXE = new CustomAxe(ToolMaterial.STONE, "cacaaxe", 5.0F, 5.0F);
    public static final CustomShovel CHEESESHOVEL = new CustomShovel(ToolMaterial.STONE, "cheeseshovel", 3.0F, 3.0F);
    public static final CustomPickaxe CHEESEPICKAXE = new CustomPickaxe(ToolMaterial.STONE, "cheesepickaxe");
    public static final CustomAxe CHEESEAXE = new CustomAxe(ToolMaterial.STONE, "cheeseaxe", 5.0F, 5.0F);
    public static final OtherCustomSword CACASWORD = new OtherCustomSword("cacasword", ToolMaterial.DIAMOND, 4.0F);
    public static final OtherCustomSword CHEESESWORD = new OtherCustomSword("cheesesword", ToolMaterial.DIAMOND, 4.0F);

    @EventBusSubscriber(
        modid = "supertnt"
    )
    public static class RegistrationHandler {

        public static final Set ITEMS = new HashSet();

        @SubscribeEvent
        public static void registerItems(Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();
            Item[] items = new Item[] { Items.STARWARSRECORD, Items.HALORECORD, Items.CACAINGOT, Items.REDSOLOCUP, Items.ACTIONREPLAY, Items.SIMMRAM, Items.FLOPPY, Items.NOKIA, Items.SNESCONTROLLER, Items.SONIC2, Items.ENERGYSWORD, Items.LIGHTSABER, Items.PS2KEYBOARD, Items.CHEESEPIECE, Items.CHEESERECORD, Items.CACASHOVEL, Items.CACAPICKAXE, Items.CACAAXE, Items.CHEESESHOVEL, Items.CHEESEPICKAXE, Items.CHEESEAXE, Items.CACASWORD, Items.CHEESESWORD};

            registry.registerAll(items);
        }

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void onRegisterModels(ModelRegistryEvent event) {
            ModelLoader.setCustomModelResourceLocation(Items.STARWARSRECORD, 0, new ModelResourceLocation("supertnt:record.starwarsrecord", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.HALORECORD, 0, new ModelResourceLocation("supertnt:record.halorecord", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.CACAINGOT, 0, new ModelResourceLocation("supertnt:cacaingot", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.REDSOLOCUP, 0, new ModelResourceLocation("supertnt:redsolocup", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.ACTIONREPLAY, 0, new ModelResourceLocation("supertnt:actionreplay", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.SIMMRAM, 0, new ModelResourceLocation("supertnt:64mbram", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.FLOPPY, 0, new ModelResourceLocation("supertnt:floppydisc", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.NOKIA, 0, new ModelResourceLocation("supertnt:nokia", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.SNESCONTROLLER, 0, new ModelResourceLocation("supertnt:snescontroller", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.SONIC2, 0, new ModelResourceLocation("supertnt:sonic2", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.ENERGYSWORD, 0, new ModelResourceLocation("supertnt:energysword", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.LIGHTSABER, 0, new ModelResourceLocation("supertnt:lightsaber", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.PS2KEYBOARD, 0, new ModelResourceLocation("supertnt:ps2keyboard", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.CHEESEPIECE, 0, new ModelResourceLocation("supertnt:cheesepiece", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.CHEESERECORD, 0, new ModelResourceLocation("supertnt:record.cheeserecord", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.CACASHOVEL, 0, new ModelResourceLocation("supertnt:cacashovel", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.CACAPICKAXE, 0, new ModelResourceLocation("supertnt:cacapickaxe", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.CACAAXE, 0, new ModelResourceLocation("supertnt:cacaaxe", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.CHEESESHOVEL, 0, new ModelResourceLocation("supertnt:cheeseshovel", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.CHEESEPICKAXE, 0, new ModelResourceLocation("supertnt:cheesepickaxe", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.CHEESEAXE, 0, new ModelResourceLocation("supertnt:cheeseaxe", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.CACASWORD, 0, new ModelResourceLocation("supertnt:cacasword", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Items.CHEESESWORD, 0, new ModelResourceLocation("supertnt:cheesesword", "inventory"));
            System.out.println("record registry name is = " + Items.STARWARSRECORD.getRegistryName());
            System.out.println("record unlocalized name is = " + Items.STARWARSRECORD.getUnlocalizedName());
        }
    }
}
