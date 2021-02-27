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
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
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

public class EntityTNTHellPrimed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTHellPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityTNTHellPrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTHellPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
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
        this.dataManager.register(EntityTNTHellPrimed.FUSE, Integer.valueOf(80));
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
        EntityGhast entityGhast = new EntityGhast(this.world);

        entityGhast.setLocationAndAngles(this.posX, this.posY + 25.0D, this.posZ + 6.0D, 0.0F, 0.0F);
        this.world.spawnEntity(entityGhast);
        EntityGhast entityGhast2 = new EntityGhast(this.world);

        entityGhast2.setLocationAndAngles(this.posX + 19.0D, this.posY + 25.0D, this.posZ - 6.0D, 0.0F, 0.0F);
        this.world.spawnEntity(entityGhast2);

        int x;
        int y;
        int z;
        BlockPos blockScanPos;

        for (x = -40; x < 40; ++x) {
            for (y = -10; y < 20; ++y) {
                for (z = -40; z < 40; ++z) {
                    blockScanPos = new BlockPos(this.posX + (double) x, this.posY + (double) y, this.posZ + (double) z);
                    this.world.getBlockState(blockScanPos);
                    if (this.world.getBlockState(blockScanPos).getMaterial() != Material.AIR && this.world.getBlockState(blockScanPos).getBlock() != Blocks.BEDROCK) {
                        if (this.world.getBlockState(blockScanPos).getMaterial() == Material.LEAVES) {
                            this.world.setBlockState(blockScanPos, Blocks.AIR.getDefaultState());
                        } else if (this.world.getBlockState(blockScanPos).getBlock() != SuperTNTMod.POOP_PLANT && this.world.getBlockState(blockScanPos).getBlock() != SuperTNTMod.CRAP_GRASS && this.world.getBlockState(blockScanPos).getMaterial() != Material.GLASS) {
                            if (this.world.getBlockState(blockScanPos).getMaterial() != Material.WATER && this.world.getBlockState(blockScanPos).getMaterial() != ModMaterials.POOPD) {
                                if (this.world.getBlockState(blockScanPos).getMaterial() != Material.LAVA && this.world.getBlockState(blockScanPos).getMaterial() != Material.LEAVES && this.world.getBlockState(blockScanPos).getMaterial() != Material.WATER && this.world.getBlockState(blockScanPos).getMaterial() != ModMaterials.POOPD && this.world.getBlockState(blockScanPos).getBlock() != Blocks.FIRE && this.world.getBlockState(blockScanPos).getBlock() != Blocks.SOUL_SAND) {
                                    this.world.setBlockState(blockScanPos, Blocks.NETHERRACK.getDefaultState());
                                    Random rn = new Random();
                                    int randomNum = rn.nextInt(100);

                                    if (randomNum >= 99 && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY(), blockScanPos.getZ())).getBlock() == Blocks.NETHERRACK && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY() + 1, blockScanPos.getZ())).getBlock() == Blocks.AIR) {
                                        EntityPigZombie entitypigzombie = new EntityPigZombie(this.world);

                                        entitypigzombie.setLocationAndAngles((double) blockScanPos.getX(), (double) (blockScanPos.getY() + 1), (double) blockScanPos.getZ(), 0.0F, 0.0F);
                                        this.world.spawnEntity(entitypigzombie);
                                    } else if (randomNum >= 89 && randomNum < 90 && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY(), blockScanPos.getZ())).getBlock() == Blocks.NETHERRACK && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY() + 1, blockScanPos.getZ())).getBlock() == Blocks.AIR) {
                                        EntityMagmaCube entity = new EntityMagmaCube(this.world);

                                        entity.setLocationAndAngles((double) blockScanPos.getX(), (double) (blockScanPos.getY() + 1), (double) blockScanPos.getZ(), 0.0F, 0.0F);
                                        this.world.spawnEntity(entity);
                                    } else if (randomNum >= 84 && randomNum < 85 && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY(), blockScanPos.getZ())).getBlock() == Blocks.NETHERRACK && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY() + 1, blockScanPos.getZ())).getBlock() == Blocks.AIR) {
                                        this.world.setBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY() + 1, blockScanPos.getZ()), Blocks.SOUL_SAND.getDefaultState());
                                    } else if (randomNum >= 46 && randomNum < 50 && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY(), blockScanPos.getZ())).getBlock() == Blocks.NETHERRACK && this.world.getBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY() + 1, blockScanPos.getZ())).getBlock() == Blocks.AIR) {
                                        this.world.setBlockState(new BlockPos(blockScanPos.getX(), blockScanPos.getY() + 1, blockScanPos.getZ()), Blocks.FIRE.getDefaultState());
                                    }
                                }
                            } else {
                                this.world.setBlockState(blockScanPos, SuperTNTMod.WHATEVER_TEST.getDefaultState());
                            }
                        } else {
                            this.world.setBlockState(blockScanPos, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }

        for (x = -40; x < 40; ++x) {
            for (y = -10; y < 20; ++y) {
                for (z = -40; z < 40; ++z) {
                    blockScanPos = new BlockPos(this.posX + (double) x, this.posY + (double) y, this.posZ + (double) z);
                    this.world.getBlockState(blockScanPos);
                    if (this.world.getBlockState(blockScanPos).getMaterial() != Material.AIR && this.world.getBlockState(blockScanPos).getBlock() != Blocks.BEDROCK && this.world.getBlockState(blockScanPos).getBlock() == SuperTNTMod.WHATEVER_TEST) {
                        this.world.setBlockState(blockScanPos, Blocks.FLOWING_LAVA.getDefaultState());
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
        this.dataManager.set(EntityTNTHellPrimed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTHellPrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTHellPrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
