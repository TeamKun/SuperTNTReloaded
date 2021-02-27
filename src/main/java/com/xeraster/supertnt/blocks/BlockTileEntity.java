package com.xeraster.supertnt.blocks;

import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockTileEntity extends BlockPleaseGetMyFluidToWork {

    private final boolean preserveTileEntity;

    public BlockTileEntity(Material material, MapColor mapColor, String blockName, boolean preserveTileEntity) {
        super(material, mapColor, blockName);
        this.preserveTileEntity = preserveTileEntity;
    }

    public BlockTileEntity(Material materialIn, String blockName, boolean preserveTileEntity) {
        super(materialIn, blockName);
        this.preserveTileEntity = preserveTileEntity;
    }

    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    public abstract TileEntity createTileEntity(World world, IBlockState iblockstate);

    @Nullable
    protected TileEntity getTileEntity(IBlockAccess world, BlockPos pos) {
        return world.getTileEntity(pos);
    }

    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return this.preserveTileEntity && willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, te, stack);
        if (this.preserveTileEntity) {
            world.setBlockToAir(pos);
        }

    }
}
