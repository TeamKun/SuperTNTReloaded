package com.xeraster.supertnt.blocks;

import com.xeraster.supertnt.init.Items;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockSolidPoop extends BlockBase {

    public BlockSolidPoop(String name, Material mat, CreativeTabs tab, float hardness, float resistance, String tool, int harvest) {
        super(name, mat, tab, hardness, resistance, tool, harvest);
        this.setSoundType(SoundType.GROUND);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.CACAINGOT;
    }
}
