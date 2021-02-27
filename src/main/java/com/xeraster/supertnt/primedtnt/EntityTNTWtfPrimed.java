package com.xeraster.supertnt.primedtnt;

import com.xeraster.supertnt.SoundHandler;
import com.xeraster.supertnt.explosions.MassiveExplosion;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityTNTWtfPrimed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTWtfPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityTNTWtfPrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTWtfPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
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
        this.dataManager.register(EntityTNTWtfPrimed.FUSE, Integer.valueOf(80));
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
        boolean snd1Played = false;
        boolean snd2Played = false;
        boolean snd3Played = false;
        boolean snd4Played = false;
        boolean snd5Played = false;
        boolean snd6Played = false;
        boolean snd7Played = false;
        boolean snd8Played = false;
        boolean snd9Played = false;
        boolean snd10Played = false;
        boolean snd11Played = false;
        boolean snd12Played = false;
        boolean snd13Played = false;
        boolean snd14Played = false;
        boolean snd15Played = false;
        boolean snd16Played = false;
        boolean snd17Played = false;
        boolean snd18Played = false;
        boolean snd19Played = false;
        float totalSize = 1000.0F;
        boolean destroyBedrock = false;
        ITextComponent bedrockFalse = Serializer.fromJsonLenient("\'Destroy bedrock set to false. This explosion will not destroy bedrock\'");
        ITextComponent bedrockFalseTip = Serializer.fromJsonLenient("\'To destroy bedrock, place tnt on top of bedrock block before igniting.\'");
        ITextComponent bedrockTrue = Serializer.fromJsonLenient("\'Destroy Bedrock set to true. This explosion will destroy bedrock.\'");

        if (this.world.getBlockState(new BlockPos(this.posX, this.posY - 1.0D, this.posZ)).getBlock() == Blocks.BEDROCK) {
            destroyBedrock = true;
            Minecraft.getMinecraft().player.sendMessage(bedrockTrue);
            System.out.println("destroy bedrock true");
        } else {
            destroyBedrock = false;
            Minecraft.getMinecraft().player.sendMessage(bedrockFalse);
            Minecraft.getMinecraft().player.sendMessage(bedrockFalseTip);
            System.out.println("destroy bedrock false");
        }

        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE20, SoundCategory.BLOCKS, 4.0F, 1.0F);
        ITextComponent lagWarning = Serializer.fromJsonLenient("\'Please note that your game is going to lag for quite some time. You will be given status updates on the explosion\'s computational progress so you know that it is actually doing something instead of being frozen.\'");

        Minecraft.getMinecraft().player.sendMessage(lagWarning);
        ITextComponent explodeProgress = Serializer.fromJsonLenient("\'error\'");

        for (int x = -500; x < 500; ++x) {
            float currentProgress = (float) (x + 500);
            double progressPercent = (double) (currentProgress / totalSize * 100.0F);

            explodeProgress = Serializer.fromJsonLenient("\'The explosion is currently: §6" + progressPercent + "% §fcompleted\'");
            if (progressPercent > 3.0D && !snd1Played) {
                snd1Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE1, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 5.0D && !snd2Played) {
                snd2Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE2, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 8.0D && !snd3Played) {
                snd3Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE3, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 10.0D && !snd4Played) {
                snd4Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE4, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 15.0D && !snd5Played) {
                snd5Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE5, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 20.0D && !snd6Played) {
                snd6Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE6, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 25.0D && !snd7Played) {
                snd7Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE7, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 30.0D && !snd8Played) {
                snd8Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE8, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 35.0D && !snd9Played) {
                snd9Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE9, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 40.0D && !snd10Played) {
                snd10Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE10, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 45.0D && !snd11Played) {
                snd11Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE11, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 50.0D && !snd12Played) {
                snd12Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE12, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 55.0D && !snd13Played) {
                snd13Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE13, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 60.0D && !snd14Played) {
                snd14Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE14, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 65.0D && !snd15Played) {
                snd15Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE15, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 70.0D && !snd16Played) {
                snd16Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE16, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 75.0D && !snd17Played) {
                snd17Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE17, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 80.0D && !snd18Played) {
                snd18Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE18, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            if (progressPercent > 98.0D && !snd19Played) {
                snd19Played = true;
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.SPACE19, SoundCategory.BLOCKS, 4.0F, 1.0F);
            }

            Minecraft.getMinecraft().player.sendMessage(explodeProgress);

            for (int y = 256; y > -1; --y) {
                for (int z = -500; z < 500; ++z) {
                    BlockPos blockScanPos = new BlockPos(this.posX + (double) x, (double) y, this.posZ + (double) z);

                    this.world.getBlockState(blockScanPos);
                    if (this.world.getBlockState(blockScanPos).getMaterial() != Material.AIR) {
                        if (this.world.getBlockState(blockScanPos).getBlock() == Blocks.BEDROCK && destroyBedrock) {
                            this.world.setBlockState(blockScanPos, Blocks.AIR.getDefaultState());
                        } else if (this.world.getBlockState(blockScanPos).getBlock() != Blocks.BEDROCK || destroyBedrock) {
                            this.world.setBlockState(blockScanPos, Blocks.AIR.getDefaultState());
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
        this.dataManager.set(EntityTNTWtfPrimed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTWtfPrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTWtfPrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
