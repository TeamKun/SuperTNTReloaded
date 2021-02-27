package com.xeraster.supertnt.primedtnt;

import com.xeraster.supertnt.explosions.OceanExplosion;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityTNTTinyIslandPrimed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTTinyIslandPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityTNTTinyIslandPrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTTinyIslandPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
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

    public OceanExplosion createExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isSmoking) {
        return this.newExplosion(entityIn, x, y, z, strength, false, isSmoking);
    }

    public OceanExplosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
        OceanExplosion explosion = new OceanExplosion(this.world, entityIn, x, y, z, strength, isFlaming, isSmoking);

        if (ForgeEventFactory.onExplosionStart(this.world, explosion)) {
            return explosion;
        } else {
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            return explosion;
        }
    }

    protected void entityInit() {
        this.dataManager.register(EntityTNTTinyIslandPrimed.FUSE, Integer.valueOf(80));
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
        byte islandHeight = 20;
        Block[] typesOfOres = new Block[] { Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.DIAMOND_ORE, Blocks.REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.EMERALD_ORE};

        int x;
        int y;
        int z;
        Random rn3;
        int grassOrNot;

        for (x = -4; x < 4; ++x) {
            for (y = -7; y < 1; ++y) {
                for (z = -3; z < 4; ++z) {
                    rn3 = new Random();
                    grassOrNot = rn3.nextInt(8);
                    if (grassOrNot == 7) {
                        Random rn2 = new Random();
                        int oreType = rn2.nextInt(6);

                        this.world.setBlockState(new BlockPos(this.posX + (double) x, this.posY + (double) y + (double) islandHeight, this.posZ + (double) z), typesOfOres[oreType].getDefaultState());
                    } else {
                        this.world.setBlockState(new BlockPos(this.posX + (double) x, this.posY + (double) y + (double) islandHeight, this.posZ + (double) z), Blocks.STONE.getDefaultState());
                    }
                }
            }
        }

        for (x = -4; x < 4; ++x) {
            for (y = 1; y < 6; ++y) {
                for (z = -3; z < 4; ++z) {
                    if (y == 4) {
                        if (z == -2 && x > -3 && x < 1) {
                            this.world.setBlockState(new BlockPos(this.posX + (double) x, this.posY + (double) y + (double) islandHeight, this.posZ + (double) z), Blocks.FLOWING_WATER.getDefaultState());
                        } else if (z == -1 && x > -3 && x < 1) {
                            this.world.setBlockState(new BlockPos(this.posX + (double) x, this.posY + (double) y + (double) islandHeight, this.posZ + (double) z), Blocks.FLOWING_WATER.getDefaultState());
                        } else {
                            this.world.setBlockState(new BlockPos(this.posX + (double) x, this.posY + (double) y + (double) islandHeight, this.posZ + (double) z), Blocks.GRASS.getDefaultState());
                        }
                    } else if (y == 5) {
                        rn3 = new Random();
                        grassOrNot = rn3.nextInt(6);
                        if (grassOrNot == 4 && this.world.getBlockState(new BlockPos(this.posX + (double) x, this.posY + (double) y + (double) islandHeight - 1.0D, this.posZ + (double) z)).getBlock() != Blocks.WATER) {
                            this.world.setBlockState(new BlockPos(this.posX + (double) x, this.posY + (double) y + (double) islandHeight, this.posZ + (double) z), Blocks.TALLGRASS.getStateFromMeta(1));
                        }
                    } else {
                        this.world.setBlockState(new BlockPos(this.posX + (double) x, this.posY + (double) y + (double) islandHeight, this.posZ + (double) z), Blocks.DIRT.getDefaultState());
                    }
                }
            }
        }

        this.createTree((int) this.posX + 1, (int) this.posY + 5 + islandHeight, (int) this.posZ + 2);
    }

    public void createTree(int xPos, int yPos, int zPos) {
        BlockPos blockPos = new BlockPos(this.posX, this.posY, this.posZ);

        this.world.getBlockState(blockPos);
        this.world.setBlockState(new BlockPos(xPos, yPos, zPos), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos, yPos + 1, zPos), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos, yPos + 2, zPos), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos, yPos + 3, zPos), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos, yPos + 4, zPos), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos, yPos + 4, zPos + 1), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos, yPos + 4, zPos - 1), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos + 1, yPos + 4, zPos), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos - 1, yPos + 4, zPos), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos - 1, yPos + 4, zPos + 1), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos - 1, yPos + 4, zPos - 1), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos + 1, yPos + 4, zPos - 1), Blocks.LEAVES.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos + 1, yPos + 4, zPos + 1), Blocks.LEAVES.getDefaultState());

        int y;
        int z;

        for (y = -2; y < 3; ++y) {
            for (z = -2; z < 3; ++z) {
                this.world.setBlockState(new BlockPos(xPos + z, yPos + 5, zPos + y), Blocks.LEAVES.getDefaultState());
            }
        }

        for (y = -2; y < 3; ++y) {
            for (z = -2; z < 3; ++z) {
                this.world.setBlockState(new BlockPos(xPos + z, yPos + 6, zPos + y), Blocks.LEAVES.getDefaultState());
            }
        }

        for (y = -1; y < 2; ++y) {
            for (z = -1; z < 2; ++z) {
                this.world.setBlockState(new BlockPos(xPos + z, yPos + 7, zPos + y), Blocks.LEAVES.getDefaultState());
            }
        }

        this.world.setBlockState(new BlockPos(xPos, yPos + 5, zPos), Blocks.LOG.getDefaultState());
        this.world.setBlockState(new BlockPos(xPos, yPos + 6, zPos), Blocks.LOG.getDefaultState());
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
        this.dataManager.set(EntityTNTTinyIslandPrimed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTTinyIslandPrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTTinyIslandPrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
