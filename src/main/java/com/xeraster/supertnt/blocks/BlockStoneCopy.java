package com.xeraster.supertnt.blocks;

import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockStoneCopy extends BlockStone {

    public BlockStoneCopy(String name, Material mat, CreativeTabs tab, float hardness, float resistance, String tool, int harvest) {
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setCreativeTab(tab);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setHarvestLevel(tool, harvest);
    }
}
