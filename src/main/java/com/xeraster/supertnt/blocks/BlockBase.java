package com.xeraster.supertnt.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBase extends Block {

    public BlockBase(String name, Material mat, CreativeTabs tab, float hardness, float resistance, String tool, int harvest) {
        this(name, mat, tab, hardness, resistance);
        this.setHarvestLevel(tool, harvest);
    }

    public BlockBase(String name, Material mat, CreativeTabs tab, float hardness, float resistance, float light) {
        this(name, mat, tab, hardness, resistance);
        this.setLightLevel(light);
    }

    public BlockBase(String name, Material mat, CreativeTabs tab, float hardness, float resistance) {
        super(mat);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setCreativeTab(tab);
    }
}
