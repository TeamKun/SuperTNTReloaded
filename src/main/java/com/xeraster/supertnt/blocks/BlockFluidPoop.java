package com.xeraster.supertnt.blocks;

import com.xeraster.supertnt.SuperTNTMod;
import com.xeraster.supertnt.fluids.ModMaterials;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockFluidPoop extends BlockFluidClassic {

    public BlockFluidPoop() {
        super(SuperTNTMod.LIQUID_POOP, ModMaterials.POOPD);
        this.setRegistryName("liquid_poop_block");
        this.setUnlocalizedName("liquid_poop_block");
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
}
