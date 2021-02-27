package com.xeraster.supertnt.primedtnt;

import com.xeraster.supertnt.explosions.DiamondExplosion;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityTNTMobPrimed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTMobPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityTNTMobPrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTMobPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
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
        this.dataManager.register(EntityTNTMobPrimed.FUSE, Integer.valueOf(80));
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
        Random rn = new Random();
        int randomDrop = rn.nextInt(84);

        if (randomDrop <= 4 && randomDrop >= 1) {
            EntityZombie entity23 = new EntityZombie(this.world);

            entity23.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity23);
        } else if (randomDrop <= 8 && randomDrop > 4) {
            EntityPig entity22 = new EntityPig(this.world);

            entity22.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity22);
        } else if (randomDrop <= 12 && randomDrop > 8) {
            EntityBat entity21 = new EntityBat(this.world);

            entity21.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity21);
        } else if (randomDrop <= 16 && randomDrop > 12) {
            EntityChicken entity20 = new EntityChicken(this.world);

            entity20.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity20);
        } else if (randomDrop <= 20 && randomDrop > 16) {
            EntityHorse entity19 = new EntityHorse(this.world);

            entity19.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity19);
        } else if (randomDrop <= 24 && randomDrop > 20) {
            EntityMooshroom entity18 = new EntityMooshroom(this.world);

            entity18.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity18);
        } else if (randomDrop <= 28 && randomDrop > 24) {
            EntityOcelot entity17 = new EntityOcelot(this.world);

            entity17.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity17);
        } else if (randomDrop <= 32 && randomDrop > 28) {
            EntitySheep entity16 = new EntitySheep(this.world);

            entity16.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity16);
        } else if (randomDrop <= 36 && randomDrop > 32) {
            EntityVillager entity15 = new EntityVillager(this.world);

            entity15.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity15);
        } else if (randomDrop <= 40 && randomDrop > 36) {
            EntityWolf entity14 = new EntityWolf(this.world);

            entity14.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity14);
        } else if (randomDrop <= 41 && randomDrop > 40) {
            EntityBlaze entity13 = new EntityBlaze(this.world);

            entity13.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity13);
        } else if (randomDrop <= 45 && randomDrop > 41) {
            EntityCaveSpider entity12 = new EntityCaveSpider(this.world);

            entity12.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity12);
        } else if (randomDrop <= 49 && randomDrop > 45) {
            EntityCreeper entity11 = new EntityCreeper(this.world);

            entity11.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity11);
        } else if (randomDrop <= 53 && randomDrop > 49) {
            EntityEnderman entity10 = new EntityEnderman(this.world);

            entity10.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity10);
        } else if ((double) randomDrop <= 53.2D && randomDrop > 53) {
            EntityGhast entity9 = new EntityGhast(this.world);

            entity9.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity9);
        } else if (randomDrop <= 58 && (double) randomDrop > 53.2D) {
            EntityMagmaCube entity8 = new EntityMagmaCube(this.world);

            entity8.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity8);
        } else if (randomDrop <= 62 && randomDrop > 58) {
            EntityPigZombie entity7 = new EntityPigZombie(this.world);

            entity7.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity7);
        } else if (randomDrop <= 66 && randomDrop > 62) {
            EntitySlime entity6 = new EntitySlime(this.world);

            entity6.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity6);
        } else if (randomDrop <= 70 && randomDrop > 66) {
            EntitySnowman entity5 = new EntitySnowman(this.world);

            entity5.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity5);
        } else if (randomDrop <= 74 && randomDrop > 70) {
            EntitySpider entity4 = new EntitySpider(this.world);

            entity4.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity4);
        } else if (randomDrop <= 76 && randomDrop > 74) {
            EntityWitch entity3 = new EntityWitch(this.world);

            entity3.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity3);
        } else if ((double) randomDrop <= 76.15D && randomDrop > 76) {
            EntityWither entity2 = new EntityWither(this.world);

            entity2.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity2);
        } else if (randomDrop <= 80 && (double) randomDrop > 76.15D) {
            EntitySquid entity1 = new EntitySquid(this.world);

            entity1.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity1);
        } else {
            EntityCow entity = new EntityCow(this.world);

            entity.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntity(entity);
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
        this.dataManager.set(EntityTNTMobPrimed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTMobPrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTMobPrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
