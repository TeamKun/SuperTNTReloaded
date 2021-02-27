package com.xeraster.supertnt.primedtnt;

import com.xeraster.supertnt.SuperTNTMod;
import com.xeraster.supertnt.explosions.CrapExplosion;
import com.xeraster.supertnt.fluids.ModMaterials;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
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

public class EntityTNTCrap2Primed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTCrap2Primed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityTNTCrap2Primed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTCrap2Primed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
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

    public CrapExplosion createExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isSmoking) {
        return this.newExplosion(entityIn, x, y, z, strength, false, isSmoking);
    }

    public CrapExplosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
        CrapExplosion explosion = new CrapExplosion(this.world, entityIn, x, y, z, strength, isFlaming, isSmoking);

        if (ForgeEventFactory.onExplosionStart(this.world, explosion)) {
            return explosion;
        } else {
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            return explosion;
        }
    }

    protected void entityInit() {
        this.dataManager.register(EntityTNTCrap2Primed.FUSE, Integer.valueOf(80));
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

        for (int x = -60; x < 60; ++x) {
            for (int y = -10; y < 20; ++y) {
                for (int z = -60; z < 60; ++z) {
                    BlockPos blockScanPos = new BlockPos(this.posX + (double) x, this.posY + (double) y, this.posZ + (double) z);

                    this.world.getBlockState(blockScanPos);
                    if (this.world.getBlockState(blockScanPos).getMaterial() != Material.AIR && this.world.getBlockState(blockScanPos).getBlock() != Blocks.BEDROCK) {
                        if (this.world.getBlockState(blockScanPos).getMaterial() == Material.LEAVES) {
                            this.world.setBlockState(blockScanPos, SuperTNTMod.POOP_LEAVES.getDefaultState());
                        } else if (this.world.getBlockState(blockScanPos).getBlock() != SuperTNTMod.POOP_PLANT && this.world.getBlockState(blockScanPos).getBlock() != SuperTNTMod.CRAP_GRASS && this.world.getBlockState(blockScanPos).getMaterial() != Material.GLASS && this.world.getBlockState(blockScanPos).getBlock() != Blocks.IRON_DOOR && this.world.getBlockState(blockScanPos).getBlock() != Blocks.OAK_DOOR && this.world.getBlockState(blockScanPos).getBlock() != Blocks.STONE_BUTTON && this.world.getBlockState(blockScanPos).getBlock() != Blocks.STONE_PRESSURE_PLATE) {
                            if (this.world.getBlockState(blockScanPos).getMaterial() == Material.WATER) {
                                this.world.setBlockState(blockScanPos, SuperTNTMod.BlockLiquidPoop.getDefaultState());
                            } else if (this.world.getBlockState(blockScanPos).getMaterial() != Material.LEAVES && this.world.getBlockState(blockScanPos).getMaterial() != Material.WATER && this.world.getBlockState(blockScanPos).getMaterial() != ModMaterials.POOPD) {
                                this.world.setBlockState(blockScanPos, SuperTNTMod.POOP_BLOCK.getDefaultState());
                                Random rn = new Random();
                                int randomNum = rn.nextInt(20);

                                if (randomNum >= 18 && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY(), blockScanPos.getZ())).getBlock() == SuperTNTMod.POOP_BLOCK && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY() + 1, blockScanPos.getZ())).getBlock() == Blocks.AIR) {
                                    this.world.setBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY() + 1, blockScanPos.getZ()), SuperTNTMod.POOP_PLANT.getDefaultState());
                                } else if (randomNum >= 16 && randomNum < 18 && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY(), blockScanPos.getZ())).getBlock() == SuperTNTMod.POOP_BLOCK && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY() + 1, blockScanPos.getZ())).getBlock() == Blocks.AIR) {
                                    this.world.setBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY() + 1, blockScanPos.getZ()), SuperTNTMod.CRAP_GRASS.getDefaultState());
                                }
                            }
                        }
                    }
                }
            }
        }

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
        this.dataManager.set(EntityTNTCrap2Primed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTCrap2Primed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTCrap2Primed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
