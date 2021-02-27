package com.xeraster.supertnt.primedtnt;

import com.xeraster.supertnt.SuperTNTMod;
import com.xeraster.supertnt.explosions.DiamondExplosion;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

public class EntityTNTChristmasPrimed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTChristmasPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;
    public String todaysDate1;

    public EntityTNTChristmasPrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTChristmasPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
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
        this.dataManager.register(EntityTNTChristmasPrimed.FUSE, Integer.valueOf(80));
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
        ITextComponent christmasMessage = Serializer.fromJsonLenient("\'It is §2§lC§4§lh§2§lr§4§li§2§ls§4§lt§2§lm§4§la§2§ls§f!!!!!1!!11!!ONE!!!1\'");
        ITextComponent noneChristmasMessage = Serializer.fromJsonLenient("\'It is currently §6§lnot Christmas §rright now.\'");

        this.world.getMinecraftServer().saveAllWorlds(false);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM");
        LocalDate christmas = LocalDate.of(2020, 12, 25);
        LocalDate now = LocalDate.now();

        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        int x;
        int y;
        int z;
        BlockPos blockScanPos;

        if (now.getDayOfMonth() == christmas.getDayOfMonth() && now.getMonth() == christmas.getMonth()) {
            Minecraft.getMinecraft().player.sendMessage(christmasMessage);

            for (x = -20; x < 20; ++x) {
                for (y = -20; y < 20; ++y) {
                    for (z = -20; z < 20; ++z) {
                        blockScanPos = new BlockPos(this.posX + (double) x, this.posY + (double) y, this.posZ + (double) z);
                        this.world.getBlockState(blockScanPos);
                        if (this.world.getBlockState(blockScanPos).getMaterial() != Material.AIR && this.world.getBlockState(blockScanPos).getBlock() != Blocks.BEDROCK && this.world.getBlockState(blockScanPos).getMaterial() != Material.AIR) {
                            this.world.setBlockState(blockScanPos, SuperTNTMod.YES_ANSWER.getDefaultState());
                        }
                    }
                }
            }
        } else if (now.getDayOfMonth() != christmas.getDayOfMonth() || now.getMonth() != christmas.getMonth()) {
            Minecraft.getMinecraft().player.sendMessage(noneChristmasMessage);

            for (x = -20; x < 20; ++x) {
                for (y = -20; y < 20; ++y) {
                    for (z = -20; z < 20; ++z) {
                        blockScanPos = new BlockPos(this.posX + (double) x, this.posY + (double) y, this.posZ + (double) z);
                        this.world.getBlockState(blockScanPos);
                        if (this.world.getBlockState(blockScanPos).getMaterial() != Material.AIR && this.world.getBlockState(blockScanPos).getBlock() != Blocks.BEDROCK && this.world.getBlockState(blockScanPos).getMaterial() != Material.AIR) {
                            this.world.setBlockState(blockScanPos, SuperTNTMod.NO_ANSWER.getDefaultState());
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
        this.dataManager.set(EntityTNTChristmasPrimed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTChristmasPrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTChristmasPrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
