package com.xeraster.supertnt.init;

import com.xeraster.supertnt.blocks.BlockTestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Blocks {

    public static final Block testBlock = new BlockTestBlock("test block", Material.ROCK, CreativeTabs.BUILDING_BLOCKS, 5.0F, 15.0F, "pickaxe", 1);
}
