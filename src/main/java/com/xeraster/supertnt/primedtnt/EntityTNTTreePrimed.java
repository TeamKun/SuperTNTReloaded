package com.xeraster.supertnt.primedtnt;

import com.xeraster.supertnt.explosions.MassiveExplosion;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityTNTTreePrimed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTTreePrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityTNTTreePrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTTreePrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
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

    public MassiveExplosion createExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isSmoking) {
        return this.newExplosion(entityIn, x, y, z, strength, false, isSmoking);
    }

    public MassiveExplosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
        MassiveExplosion explosion = new MassiveExplosion(this.world, entityIn, x, y, z, strength, isFlaming, isSmoking);

        if (ForgeEventFactory.onExplosionStart(this.world, explosion)) {
            return explosion;
        } else {
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            return explosion;
        }
    }

    protected void entityInit() {
        this.dataManager.register(EntityTNTTreePrimed.FUSE, Integer.valueOf(80));
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
        float f = 25.0F;

        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        BlockPos blockPos = new BlockPos(this.posX, this.posY, this.posZ);

        this.world.getBlockState(blockPos);
        this.world.setBlockState(new BlockPos(this.posX, this.posY, this.posZ), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 1.0D, this.posZ), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 2.0D, this.posZ), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 3.0D, this.posZ), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 4.0D, this.posZ), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 4.0D, this.posZ + 1.0D), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 4.0D, this.posZ - 1.0D), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 4.0D, this.posZ), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 4.0D, this.posZ), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 4.0D, this.posZ + 1.0D), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY + 4.0D, this.posZ - 1.0D), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 4.0D, this.posZ - 1.0D), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY + 4.0D, this.posZ + 1.0D), Blocks.LEAVES.getDefaultState());

        int y;
        int z;

        for (y = -2; y < 3; ++y) {
            for (z = -2; z < 3; ++z) {
                this.world.setBlockState(new BlockPos(this.posX + (double) z, this.posY + 5.0D, this.posZ + (double) y), Blocks.LEAVES.getDefaultState());
            }
        }

        for (y = -2; y < 3; ++y) {
            for (z = -2; z < 3; ++z) {
                this.world.setBlockState(new BlockPos(this.posX + (double) z, this.posY + 6.0D, this.posZ + (double) y), Blocks.LEAVES.getDefaultState());
            }
        }

        for (y = -1; y < 2; ++y) {
            for (z = -1; z < 2; ++z) {
                this.world.setBlockState(new BlockPos(this.posX + (double) z, this.posY + 7.0D, this.posZ + (double) y), Blocks.LEAVES.getDefaultState());
            }
        }

        this.world.setBlockState(new BlockPos(this.posX, this.posY + 5.0D, this.posZ), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(this.posX, this.posY + 6.0D, this.posZ), Blocks.LOG.getDefaultState());
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
        this.dataManager.set(EntityTNTTreePrimed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTTreePrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTTreePrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
