package com.xeraster.supertnt.blocks;

import com.xeraster.supertnt.primedtnt.EntityTNTAirbornePrimed;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNTAirborne extends Block {

    public static final PropertyBool EXPLODE = PropertyBool.create("explode");

    public BlockTNTAirborne(String name, Material mat, CreativeTabs tab, float hardness, float resistance, String tool, int harvest) {
        super(mat);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTNTAirborne.EXPLODE, Boolean.valueOf(false)));
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setCreativeTab(tab);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(BlockTNTAirborne.EXPLODE, Boolean.valueOf(true)));
            worldIn.setBlockToAir(pos);
        }

    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(BlockTNTAirborne.EXPLODE, Boolean.valueOf(true)));
            worldIn.setBlockToAir(pos);
        }

    }

    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            EntityTNTAirbornePrimed entitytntprimed = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());

            entitytntprimed.setFuse((short) (worldIn.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8));
            worldIn.spawnEntity(entitytntprimed);
        }

    }

    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        this.explode(worldIn, pos, state, (EntityLivingBase) null);
    }

    public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
        if (!worldIn.isRemote && ((Boolean) state.getValue(BlockTNTAirborne.EXPLODE)).booleanValue()) {
            EntityTNTAirbornePrimed entitytntprimed = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed);
            EntityTNTAirbornePrimed entitytntprimed2 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed2);
            EntityTNTAirbornePrimed entitytntprimed3 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed3);
            EntityTNTAirbornePrimed entitytntprimed4 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed4);
            EntityTNTAirbornePrimed entitytntprimed5 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed5);
            EntityTNTAirbornePrimed entitytntprimed6 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed6);
            EntityTNTAirbornePrimed entitytntprimed7 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed7);
            EntityTNTAirbornePrimed entitytntprimed8 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed8);
            EntityTNTAirbornePrimed entitytntprimed20 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed20);
            EntityTNTAirbornePrimed entitytntprimed9 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed9);
            EntityTNTAirbornePrimed entitytntprimed10 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed10);
            EntityTNTAirbornePrimed entitytntprimed11 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed11);
            EntityTNTAirbornePrimed entitytntprimed12 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed12);
            EntityTNTAirbornePrimed entitytntprimed13 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed13);
            EntityTNTAirbornePrimed entitytntprimed14 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed14);
            EntityTNTAirbornePrimed entitytntprimed15 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed15);
            EntityTNTAirbornePrimed entitytntprimed16 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed16);
            EntityTNTAirbornePrimed entitytntprimed17 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed17);
            EntityTNTAirbornePrimed entitytntprimed18 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed18);
            EntityTNTAirbornePrimed entitytntprimed19 = new EntityTNTAirbornePrimed(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);

            worldIn.spawnEntity(entitytntprimed19);
            worldIn.playSound((EntityPlayer) null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = playerIn.getHeldItem(hand);

        if (!itemstack.isEmpty() && (itemstack.getItem() == Items.FLINT_AND_STEEL || itemstack.getItem() == Items.FIRE_CHARGE)) {
            this.explode(worldIn, pos, state.withProperty(BlockTNTAirborne.EXPLODE, Boolean.valueOf(true)), playerIn);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
                itemstack.damageItem(1, playerIn);
            } else if (!playerIn.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            return true;
        } else {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
            EntityArrow entityarrow = (EntityArrow) entityIn;

            if (entityarrow.isBurning()) {
                this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty(BlockTNTAirborne.EXPLODE, Boolean.valueOf(true)), entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase) entityarrow.shootingEntity : null);
                worldIn.setBlockToAir(pos);
            }
        }

    }

    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockTNTAirborne.EXPLODE, Boolean.valueOf((meta & 1) > 0));
    }

    public int getMetaFromState(IBlockState state) {
        return ((Boolean) state.getValue(BlockTNTAirborne.EXPLODE)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockTNTAirborne.EXPLODE});
    }
}
