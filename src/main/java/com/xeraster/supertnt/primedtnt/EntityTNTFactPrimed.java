package com.xeraster.supertnt.primedtnt;

import com.xeraster.supertnt.SoundHandler;
import com.xeraster.supertnt.explosions.DiamondExplosion;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityTNTFactPrimed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTFactPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityTNTFactPrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTFactPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
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
        this.dataManager.register(EntityTNTFactPrimed.FUSE, Integer.valueOf(80));
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
        Random rnd1 = new Random();
        int randomNumber1 = rnd1.nextInt(57);
        SoundEvent[] soundToPlay = new SoundEvent[58];

        System.out.println("random facttnt number is: " + randomNumber1);
        soundToPlay[0] = SoundHandler.FACT1;
        soundToPlay[1] = SoundHandler.FACT2;
        soundToPlay[2] = SoundHandler.FACT3;
        soundToPlay[3] = SoundHandler.FACT4;
        soundToPlay[4] = SoundHandler.FACT5;
        soundToPlay[5] = SoundHandler.FACT6;
        soundToPlay[6] = SoundHandler.FACT7;
        soundToPlay[7] = SoundHandler.FACT8;
        soundToPlay[8] = SoundHandler.FACT9;
        soundToPlay[9] = SoundHandler.FACT10;
        soundToPlay[10] = SoundHandler.FACT11;
        soundToPlay[11] = SoundHandler.FACT12;
        soundToPlay[12] = SoundHandler.FACT13;
        soundToPlay[13] = SoundHandler.FACT14;
        soundToPlay[14] = SoundHandler.FACT15;
        soundToPlay[15] = SoundHandler.FACT16;
        soundToPlay[16] = SoundHandler.FACT17;
        soundToPlay[17] = SoundHandler.FACT18;
        soundToPlay[18] = SoundHandler.FACT19;
        soundToPlay[19] = SoundHandler.FACT20;
        soundToPlay[20] = SoundHandler.FACT21;
        soundToPlay[21] = SoundHandler.FACT22;
        soundToPlay[22] = SoundHandler.FACT23;
        soundToPlay[23] = SoundHandler.FACT24;
        soundToPlay[24] = SoundHandler.FACT25;
        soundToPlay[25] = SoundHandler.FACT26;
        soundToPlay[26] = SoundHandler.FACT27;
        soundToPlay[27] = SoundHandler.FACT28;
        soundToPlay[28] = SoundHandler.FACT29;
        soundToPlay[29] = SoundHandler.FACT30;
        soundToPlay[30] = SoundHandler.FACT31;
        soundToPlay[31] = SoundHandler.FACT32;
        soundToPlay[32] = SoundHandler.FACT33;
        soundToPlay[33] = SoundHandler.FACT34;
        soundToPlay[34] = SoundHandler.FACT35;
        soundToPlay[35] = SoundHandler.FACT36;
        soundToPlay[36] = SoundHandler.FACT37;
        soundToPlay[37] = SoundHandler.FACT38;
        soundToPlay[38] = SoundHandler.FACT39;
        soundToPlay[39] = SoundHandler.FACT40;
        soundToPlay[40] = SoundHandler.FACT41;
        soundToPlay[41] = SoundHandler.FACT42;
        soundToPlay[42] = SoundHandler.FACT43;
        soundToPlay[43] = SoundHandler.FACT44;
        soundToPlay[44] = SoundHandler.FACT45;
        soundToPlay[45] = SoundHandler.FACT46;
        soundToPlay[46] = SoundHandler.FACT47;
        soundToPlay[47] = SoundHandler.FACT48;
        soundToPlay[48] = SoundHandler.FACT49;
        soundToPlay[49] = SoundHandler.FACT50;
        soundToPlay[50] = SoundHandler.FACT51;
        soundToPlay[51] = SoundHandler.FACT52;
        soundToPlay[52] = SoundHandler.FACT53;
        soundToPlay[53] = SoundHandler.FACT54;
        soundToPlay[54] = SoundHandler.FACT55;
        soundToPlay[55] = SoundHandler.FACT56;
        soundToPlay[56] = SoundHandler.FACT57;
        soundToPlay[57] = SoundHandler.FACT57;
        ITextComponent[] factMessageToShow = new ITextComponent[] { Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact1") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact2") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact3") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact4") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact5") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact6") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact7") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact8") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact9") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact10") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact11") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact12") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact13") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact14") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact15") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact16") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact17") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact18") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact19") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact20") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact21") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact22") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact23") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact24") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact25") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact26") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact27") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact28") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact29") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact30") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact31") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact32") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact33") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact34") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact35") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact36") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact37") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact38") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact39") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact40") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact41") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact42") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact43") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact44") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact45") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact46") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact47") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact48") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact49") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact50") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact51") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact52") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact53") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact54") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact55") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact56") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact57") + "\'"), Serializer.fromJsonLenient("\'" + I18n.translateToLocal("fact.fact57") + "\'")};

        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, soundToPlay[randomNumber1], SoundCategory.RECORDS, 6.0F, 1.0F);
        System.out.println("sound being played is: " + soundToPlay[randomNumber1].toString());
        System.out.println("with a registry name of: " + soundToPlay[randomNumber1].getRegistryName());
        System.out.println("and with a sound name of: " + soundToPlay[randomNumber1].getSoundName());
        ITextComponent factMessage = Serializer.fromJsonLenient("\'" + I18n.translateToLocal("tile.nomobtnt.name") + "\'");

        Minecraft.getMinecraft().player.sendMessage(factMessageToShow[randomNumber1]);
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
        this.dataManager.set(EntityTNTFactPrimed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTFactPrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTFactPrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
