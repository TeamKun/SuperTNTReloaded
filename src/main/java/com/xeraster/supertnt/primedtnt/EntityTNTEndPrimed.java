package com.xeraster.supertnt.primedtnt;

import com.xeraster.supertnt.explosions.EndExplosion;
import javax.annotation.Nullable;
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

public class EntityTNTEndPrimed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTEndPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityTNTEndPrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTEndPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
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

    public EndExplosion createExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isSmoking) {
        return this.newExplosion(entityIn, x, y, z, strength, false, isSmoking);
    }

    public EndExplosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
        EndExplosion explosion = new EndExplosion(this.world, entityIn, x, y, z, strength, isFlaming, isSmoking);

        if (ForgeEventFactory.onExplosionStart(this.world, explosion)) {
            return explosion;
        } else {
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            return explosion;
        }
    }

    protected void entityInit() {
        this.dataManager.register(EntityTNTEndPrimed.FUSE, Integer.valueOf(80));
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
        boolean destroyBedrock = false;
        int blocksLookedAt = 0;
        float totalSize = 40.0F;
        short maxBounds = 200;
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
        ITextComponent lagWarning = Serializer.fromJsonLenient("\'Please note that your game is going to lag for quite some time. You will be given status updates on the explosion\'s computational progress so you know that it is actually doing something instead of being frozen.\'");

        Minecraft.getMinecraft().player.sendMessage(lagWarning);
        ITextComponent explodeProgress = Serializer.fromJsonLenient("\'error\'");

        for (int circleHeight = 0; circleHeight < maxBounds + 1; ++circleHeight) {
            float currentProgress = (float) circleHeight;
            double progressPercent = (double) (currentProgress / (float) maxBounds * 100.0F);

            explodeProgress = Serializer.fromJsonLenient("\'The explosion is currently: §6" + progressPercent + "% §fcompleted\'");
            Minecraft.getMinecraft().player.sendMessage(explodeProgress);
            if (circleHeight == maxBounds - 1) {
                ITextComponent radius = Serializer.fromJsonLenient("\'Now is where the GPU starts rendering. It may temporarily freeze or go slower, but it\'s still working.\'");

                Minecraft.getMinecraft().player.sendMessage(radius);
            }

            int i = Math.abs(circleHeight);

            for (float i_ = 0.0F; i_ < (float) i_; i_ = (float) ((double) i_ + 0.5D)) {
                for (float j = 0.0F; (double) j < 6.283185307179586D * (double) i_; j = (float) ((double) j + 0.5D)) {
                    if (this.posY + (double) Math.abs(circleHeight - maxBounds) <= 256.0D) {
                        BlockPos blockScanPos = new BlockPos((double) ((int) Math.floor(this.posX + Math.sin((double) j) * (Math.pow((double) i_, 2.0D) / (double) maxBounds))), this.posY + (double) Math.abs(circleHeight - maxBounds), (double) ((int) Math.floor(this.posZ + Math.cos((double) j) * (Math.pow((double) i_, 2.0D) / (double) maxBounds))));

                        if (this.world.getBlockState(blockScanPos).getBlock() != Blocks.BEDROCK && this.world.getBlockState(blockScanPos).getBlock() != Blocks.AIR) {
                            this.world.setBlockState(blockScanPos, Blocks.AIR.getDefaultState());
                            ++blocksLookedAt;
                        } else if (this.world.getBlockState(blockScanPos).getBlock() == Blocks.BEDROCK && destroyBedrock) {
                            this.world.setBlockState(blockScanPos, Blocks.AIR.getDefaultState());
                        }

                        if (this.posY + (double) (Math.abs(circleHeight - maxBounds) * -1) > 0.0D) {
                            blockScanPos = new BlockPos((double) ((int) Math.floor(this.posX + Math.sin((double) j) * (Math.pow((double) i_, 2.0D) / (double) maxBounds))), this.posY + (double) (Math.abs(circleHeight - maxBounds) * -1), (double) ((int) Math.floor(this.posZ + Math.cos((double) j) * (Math.pow((double) i_, 2.0D) / (double) maxBounds))));
                            if (this.world.getBlockState(blockScanPos).getBlock() != Blocks.BEDROCK && this.world.getBlockState(blockScanPos).getBlock() != Blocks.AIR) {
                                this.world.setBlockState(blockScanPos, Blocks.AIR.getDefaultState());
                                ++blocksLookedAt;
                            } else if (this.world.getBlockState(blockScanPos).getBlock() == Blocks.BEDROCK && destroyBedrock) {
                                this.world.setBlockState(blockScanPos, Blocks.AIR.getDefaultState());
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Blocks looked at= " + blocksLookedAt);
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
        this.dataManager.set(EntityTNTEndPrimed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTEndPrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTEndPrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
