package com.xeraster.supertnt.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ModFluid extends Fluid {

    protected static int mapColor = -1;
    protected static float overlayAlpha = 0.2F;
    protected static SoundEvent emptySound = SoundEvents.ITEM_BUCKET_EMPTY;
    protected static SoundEvent fillSound = SoundEvents.ITEM_BUCKET_FILL;
    protected static Material material = Material.WATER;

    public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
        super(fluidName, still, flowing);
    }

    public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor) {
        this(fluidName, still, flowing);
        this.setColor(mapColor);
    }

    public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor, float overlayAlpha) {
        this(fluidName, still, flowing, mapColor);
        this.setAlpha(overlayAlpha);
    }

    public int getColor() {
        return ModFluid.mapColor;
    }

    public ModFluid setColor(int parColor) {
        ModFluid.mapColor = parColor;
        return this;
    }

    public float getAlpha() {
        return ModFluid.overlayAlpha;
    }

    public ModFluid setAlpha(float parOverlayAlpha) {
        ModFluid.overlayAlpha = parOverlayAlpha;
        return this;
    }

    public ModFluid setEmptySound(SoundEvent parSound) {
        ModFluid.emptySound = parSound;
        return this;
    }

    public SoundEvent getEmptySound() {
        return ModFluid.emptySound;
    }

    public ModFluid setFillSound(SoundEvent parSound) {
        ModFluid.fillSound = parSound;
        return this;
    }

    public SoundEvent getFillSound() {
        return ModFluid.fillSound;
    }

    public ModFluid setMaterial(Material parMaterial) {
        ModFluid.material = parMaterial;
        return this;
    }

    public Material getMaterial() {
        return ModFluid.material;
    }

    public boolean doesVaporize(FluidStack fluidStack) {
        return this.block == null ? false : this.block.getDefaultState().getMaterial() == this.getMaterial();
    }
}
