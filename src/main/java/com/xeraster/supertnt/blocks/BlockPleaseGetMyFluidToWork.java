package com.xeraster.supertnt.blocks;

import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

public class BlockPleaseGetMyFluidToWork extends Block {

    public BlockPleaseGetMyFluidToWork(Material material, MapColor mapColor, String blockName) {
        super(material, mapColor);
        setBlockName(this, blockName);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public BlockPleaseGetMyFluidToWork(Material materialIn, String blockName) {
        this(materialIn, materialIn.getMaterialMapColor(), blockName);
    }

    public static void setBlockName(Block block, String blockName) {
        block.setRegistryName("supertnt", blockName);
        ResourceLocation registryName = (ResourceLocation) Objects.requireNonNull(block.getRegistryName());

        block.setUnlocalizedName(registryName.toString());
    }
}
