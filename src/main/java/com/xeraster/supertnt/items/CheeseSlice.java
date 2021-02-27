package com.xeraster.supertnt.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

public class CheeseSlice extends ItemFood {

    public CheeseSlice(String idName) {
        super(1, true);
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.setRegistryName(idName);
        this.setUnlocalizedName(idName);
        this.setMaxStackSize(64);
    }
}
