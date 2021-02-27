package com.xeraster.supertnt.blocks;

import com.xeraster.supertnt.SuperTNTMod;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class BlockGayPlant extends BlockBush implements IShearable {

    protected static final AxisAlignedBB POOP_PLANT_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public BlockGayPlant(String name) {
        super(Material.VINE);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setSoundType(SoundType.PLANT);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BlockGayPlant.POOP_PLANT_AABB;
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.WOOD;
    }

    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == Blocks.SAND || state.getBlock() == SuperTNTMod.POOP_BLOCK || state.getBlock() == Blocks.HARDENED_CLAY || state.getBlock() == Blocks.STAINED_HARDENED_CLAY || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.WOOL;
    }

    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    public int quantityDropped(Random random) {
        return random.nextInt(3);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.COOKIE;
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, new ItemStack(Items.COOKIE, 1, 0));
        } else {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }

    }

    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return true;
    }

    public List onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Arrays.asList(new ItemStack[] { new ItemStack(Items.COOKIE)});
    }
}
