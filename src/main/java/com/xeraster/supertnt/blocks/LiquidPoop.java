package com.xeraster.supertnt.blocks;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class LiquidPoop extends Fluid {

    public LiquidPoop(String fluidName, ResourceLocation still, ResourceLocation flowing) {
        super(fluidName, flowing, flowing);
    }
}
