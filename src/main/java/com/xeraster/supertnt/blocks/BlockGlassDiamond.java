package com.xeraster.supertnt.blocks;

import java.util.Random;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGlassDiamond extends BlockGlass {

    public BlockGlassDiamond(Material materialIn, boolean ignoreSimilarity) {
        super(materialIn, ignoreSimilarity);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(10.0F);
        this.setResistance(100.0F);
        this.setHarvestLevel("pickaxe", 0);
        this.setUnlocalizedName("strongglass");
        this.setRegistryName("strongglass");
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    protected boolean canSilkHarvest() {
        return true;
    }
}
