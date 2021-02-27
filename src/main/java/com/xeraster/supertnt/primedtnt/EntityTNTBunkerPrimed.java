package com.xeraster.supertnt.primedtnt;

import com.xeraster.supertnt.SuperTNTMod;
import com.xeraster.supertnt.explosions.DiamondExplosion;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityTNTBunkerPrimed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTBunkerPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityTNTBunkerPrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTBunkerPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
        this(worldIn);
        this.setPosition(x, y, z);
        float f = (float) (Math.random() * 6.283185307179586D);

        this.motionX = (double) (-((float) Math.sin((double) f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double) (-((float) Math.cos((double) f)) * 0.02F);
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }

    public DiamondExplosion createExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isSmoking) {
        return this.newExplosion(entityIn, x, y, z, strength, false, isSmoking);
    }

    public DiamondExplosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
        DiamondExplosion explosion = new DiamondExplosion(this.world, entityIn, x, y, z, strength, isFlaming, isSmoking);

        if (ForgeEventFactory.onExplosionStart(this.world, explosion)) {
            return explosion;
        } else {
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            return explosion;
        }
    }

    protected void entityInit() {
        this.dataManager.register(EntityTNTBunkerPrimed.FUSE, Integer.valueOf(80));
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (!this.hasNoGravity()) {
            this.motionY -= 0.03999999910593033D;
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        --this.fuse;
        if (this.fuse <= 0) {
            this.setDead();
            if (!this.world.isRemote) {
                this.explode();
            }
        } else {
            this.handleWaterMovement();
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }

    }

    private void explode() {
        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        BlockPos blockPos = new BlockPos(this.posX, this.posY, this.posZ);

        this.world.getBlockState(blockPos);
        this.world.setBlockState(new BlockPos(this.posX, this.posY - 1.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY - 1.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY - 1.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY - 1.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY - 1.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY - 1.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY - 1.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY - 1.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY - 1.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY - 1.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY - 1.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY - 1.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY - 1.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY - 1.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY - 1.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY - 1.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY - 1.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY - 1.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY - 1.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY - 1.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY - 1.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY - 1.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY - 1.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY - 1.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY - 1.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY - 1.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY - 1.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY - 1.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY - 1.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY - 1.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY - 1.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY - 1.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY - 1.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY - 1.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY - 1.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY, this.posZ + 2.0D), Blocks.CRAFTING_TABLE.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY, this.posZ + 1.0D), Blocks.CHEST.getDefaultState());
        TileEntityChest tileentitychest1 = (TileEntityChest) this.world.getTileEntity(new BlockPos(this.posX + 3.0D, this.posY, this.posZ + 1.0D));
        ItemStack isk = new ItemStack(Items.APPLE, 2);
        ItemStack door = new ItemStack(Items.OAK_DOOR, 1);
        ItemStack woodcrap = new ItemStack(Items.STICK, 4);
        ItemStack woodcrap2 = new ItemStack(Item.getItemFromBlock(Blocks.OBSIDIAN), 4);
        ItemStack iron = new ItemStack(Items.COAL, 3);
        ItemStack torch = new ItemStack(Item.getItemFromBlock(Blocks.TORCH), 4);
        ItemStack bone = new ItemStack(Items.BONE, 1);
        ItemStack lever = new ItemStack(Item.getItemFromBlock(Blocks.LEVER), 3);
        ItemStack redStone = new ItemStack(Items.REDSTONE, 20);

        tileentitychest1.setInventorySlotContents(0, door);
        tileentitychest1.setInventorySlotContents(1, isk);
        tileentitychest1.setInventorySlotContents(2, woodcrap);
        tileentitychest1.setInventorySlotContents(3, woodcrap2);
        tileentitychest1.setInventorySlotContents(4, iron);
        tileentitychest1.setInventorySlotContents(5, torch);
        tileentitychest1.setInventorySlotContents(6, bone);
        tileentitychest1.setInventorySlotContents(7, lever);
        tileentitychest1.setInventorySlotContents(8, redStone);
        this.world.setBlockState(new BlockPos(this.posX, this.posY, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 1.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 1.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY + 1.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 1.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 1.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY + 1.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 1.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 2.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 2.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY + 2.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 2.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 2.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY + 2.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 2.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 1.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 1.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 1.0D, this.posZ - 1.0D), SuperTNTMod.DIAMOND_GLASS.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 1.0D, this.posZ - 2.0D), SuperTNTMod.DIAMOND_GLASS.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 1.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 1.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 1.0D, this.posZ), Blocks.TORCH.getStateFromMeta(2));
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 1.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 2.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 2.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 2.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 2.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 2.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 2.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 2.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 1.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 1.0D, this.posZ + 1.0D), SuperTNTMod.DIAMOND_GLASS.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 1.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 1.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 1.0D, this.posZ - 2.0D), Blocks.TORCH.getStateFromMeta(2).withRotation(Rotation.CLOCKWISE_180));
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 1.0D, this.posZ + 2.0D), SuperTNTMod.DIAMOND_GLASS.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 1.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 1.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 2.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 2.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 2.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 2.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 2.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 2.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 2.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 1.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 1.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY + 1.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 1.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 1.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY, this.posZ - 3.0D), Blocks.IRON_DOOR.getStateFromMeta(1));
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY + 1.0D, this.posZ - 3.0D), Blocks.IRON_DOOR.getStateFromMeta(10));
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY - 1.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY, this.posZ - 2.0D), Blocks.STONE_PRESSURE_PLATE.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 1.0D, this.posZ - 4.0D), Blocks.STONE_BUTTON.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 1.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 2.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 2.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY + 2.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 2.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 2.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY + 2.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 2.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 3.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 3.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY + 3.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 3.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 3.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY + 3.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 3.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 3.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 3.0D, this.posZ), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 3.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 3.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY + 3.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 3.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 3.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY + 3.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 3.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 3.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 3.0D, this.posZ + 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 3.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 3.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY + 3.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 3.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 3.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY + 3.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 3.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 3.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 3.0D, this.posZ - 1.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 3.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 3.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY + 3.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 3.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 3.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY + 3.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 3.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 3.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 3.0D, this.posZ - 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 3.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 3.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY + 3.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 3.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 3.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY + 3.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 3.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 3.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 3.0D, this.posZ + 2.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 3.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 3.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY + 3.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 3.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 3.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY + 3.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 3.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 3.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 3.0D, this.posZ + 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 3.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 3.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY + 3.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY + 3.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 3.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 2.0D, this.posY + 3.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 3.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 4.0D, this.posY + 3.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 4.0D, this.posY + 3.0D, this.posZ - 3.0D), Blocks.OBSIDIAN.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY, this.posZ - 2.0D), Blocks.BED.getStateFromMeta(3));
        this.world.setBlockState(new BlockPos(this.posX - 2.0D, this.posY, this.posZ - 2.0D), Blocks.BED.getStateFromMeta(11));
        this.world.setBlockState(new BlockPos(this.posX - 3.0D, this.posY, this.posZ + 2.0D), Blocks.FURNACE.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 3.0D, this.posY + 1.0D, this.posZ - 4.0D), Blocks.TORCH.getStateFromMeta(2).withRotation(Rotation.CLOCKWISE_90));
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 1.0D, this.posZ - 4.0D), Blocks.TORCH.getStateFromMeta(2).withRotation(Rotation.CLOCKWISE_90));
    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("Fuse", (short) this.getFuse());
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.setFuse(compound.getShort("Fuse"));
    }

    @Nullable
    public EntityLivingBase getTntPlacedBy() {
        return this.tntPlacedBy;
    }

    public float getEyeHeight() {
        return 0.0F;
    }

    public void setFuse(int fuseIn) {
        this.dataManager.set(EntityTNTBunkerPrimed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTBunkerPrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTBunkerPrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
