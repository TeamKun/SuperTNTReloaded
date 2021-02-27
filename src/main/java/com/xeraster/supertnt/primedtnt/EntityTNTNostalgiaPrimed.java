package com.xeraster.supertnt.primedtnt;

import com.xeraster.supertnt.SoundHandler;
import com.xeraster.supertnt.explosions.DiamondExplosion;
import com.xeraster.supertnt.init.Items;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityTNTNostalgiaPrimed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTNostalgiaPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityTNTNostalgiaPrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTNostalgiaPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
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
        this.dataManager.register(EntityTNTNostalgiaPrimed.FUSE, Integer.valueOf(80));
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
        ITextComponent blah3 = Serializer.fromJsonLenient("\'Battlefront 2\'");
        ITextComponent blah4 = Serializer.fromJsonLenient("\'was the greatest\'");
        ITextComponent blah5 = Serializer.fromJsonLenient("\'game of all time\'");
        ITextComponent blah6 = Serializer.fromJsonLenient("\'\'");

        this.world.setBlockState(new BlockPos(this.posX, this.posY, this.posZ), Blocks.STANDING_SIGN.getDefaultState());
        TileEntitySign tileentitysign1 = (TileEntitySign) this.world.getTileEntity(new BlockPos(this.posX, this.posY, this.posZ));

        System.out.println(tileentitysign1.signText);
        Random rnd0 = new Random();
        int randomNumber0 = rnd0.nextInt(7);

        System.out.println("random number is: " + randomNumber0);
        if (randomNumber0 <= 2) {
            Random tileentitychest1 = new Random();
            int haloSword = tileentitychest1.nextInt(7);
            SoundEvent[] jukebox1 = new SoundEvent[7];

            System.out.println("random bf2 number is: " + haloSword);
            jukebox1[0] = SoundHandler.WRISTROCKETS;
            jukebox1[1] = SoundHandler.BF2QUOTE1;
            jukebox1[2] = SoundHandler.BF2QUOTE2;
            jukebox1[3] = SoundHandler.BF2QUOTE3;
            jukebox1[4] = SoundHandler.BF2QUOTE4;
            jukebox1[5] = SoundHandler.BF2QUOTE5;
            jukebox1[6] = SoundHandler.BF2QUOTE6;
            blah3 = Serializer.fromJsonLenient("\'Battlefront 2\'");
            blah4 = Serializer.fromJsonLenient("\'was the greatest\'");
            blah5 = Serializer.fromJsonLenient("\'game of all time\'");
            blah6 = Serializer.fromJsonLenient("\'\'");
            this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, jukebox1[haloSword], SoundCategory.RECORDS, 4.0F, 1.0F);
            this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY, this.posZ), Blocks.CHEST.getDefaultState());
            TileEntityChest haloRecord = (TileEntityChest) this.world.getTileEntity(new BlockPos(this.posX + 1.0D, this.posY, this.posZ));
            ItemStack haloSword1 = new ItemStack(Items.LIGHTSABER, 1);

            haloRecord.setInventorySlotContents(0, haloSword1);
            this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY, this.posZ), Blocks.JUKEBOX.getDefaultState());
            TileEntityJukebox jukebox2 = (TileEntityJukebox) this.world.getTileEntity(new BlockPos(this.posX - 1.0D, this.posY, this.posZ));
            ItemStack starRecord = new ItemStack(Items.STARWARSRECORD);

            jukebox2.setRecord(starRecord.copy());
            ((BlockJukebox) Blocks.JUKEBOX).insertRecord(this.world, new BlockPos(this.posX - 1.0D, this.posY, this.posZ), Blocks.JUKEBOX.getDefaultState(), starRecord);
            this.world.playEvent((EntityPlayer) null, 1010, new BlockPos(this.posX - 1.0D, this.posY, this.posZ), Item.getIdFromItem(Items.STARWARSRECORD));
        } else if (randomNumber0 == 3) {
            blah3 = Serializer.fromJsonLenient("\'Remember\'");
            blah4 = Serializer.fromJsonLenient("\'Windows 95?\'");
            blah5 = Serializer.fromJsonLenient("\'\'");
            blah6 = Serializer.fromJsonLenient("\'Start me up\'");
            this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.WIN95, SoundCategory.RECORDS, 4.0F, 1.0F);
        } else if (randomNumber0 == 4) {
            blah3 = Serializer.fromJsonLenient("\'Remember\'");
            blah4 = Serializer.fromJsonLenient("\'Windows 98?\'");
            blah5 = Serializer.fromJsonLenient("\'\'");
            blah6 = Serializer.fromJsonLenient("\'I memeber..\'");
            this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.WIN98, SoundCategory.RECORDS, 4.0F, 1.0F);
        } else if (randomNumber0 == 5) {
            blah3 = Serializer.fromJsonLenient("\'Remember\'");
            blah4 = Serializer.fromJsonLenient("\'Dial up?\'");
            blah5 = Serializer.fromJsonLenient("\'\'");
            blah6 = Serializer.fromJsonLenient("\'I memeber..\'");
            this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.DIALUP, SoundCategory.RECORDS, 4.0F, 1.0F);
        } else if (randomNumber0 >= 6) {
            blah3 = Serializer.fromJsonLenient("\'Never forget\'");
            blah4 = Serializer.fromJsonLenient("\'Halo 3\'");
            blah5 = Serializer.fromJsonLenient("\'A great game\'");
            blah6 = Serializer.fromJsonLenient("\'I memeber..\'");
            this.world.setBlockState(new BlockPos(this.posX + 1.0D, this.posY, this.posZ), Blocks.CHEST.getDefaultState());
            TileEntityChest tileentitychest11 = (TileEntityChest) this.world.getTileEntity(new BlockPos(this.posX + 1.0D, this.posY, this.posZ));
            ItemStack haloSword2 = new ItemStack(Items.ENERGYSWORD, 1);

            tileentitychest11.setInventorySlotContents(0, haloSword2);
            this.world.setBlockState(new BlockPos(this.posX - 1.0D, this.posY, this.posZ), Blocks.JUKEBOX.getDefaultState());
            TileEntityJukebox jukebox11 = (TileEntityJukebox) this.world.getTileEntity(new BlockPos(this.posX - 1.0D, this.posY, this.posZ));
            ItemStack haloRecord1 = new ItemStack(Items.HALORECORD);

            jukebox11.setRecord(haloRecord1);
            ((BlockJukebox) Blocks.JUKEBOX).insertRecord(this.world, new BlockPos(this.posX - 1.0D, this.posY, this.posZ), Blocks.JUKEBOX.getDefaultState(), haloRecord1);
            this.world.playEvent((EntityPlayer) null, 1010, new BlockPos(this.posX - 1.0D, this.posY, this.posZ), Item.getIdFromItem(Items.HALORECORD));
        }

        tileentitysign1.signText[0] = blah3;
        tileentitysign1.signText[1] = blah4;
        tileentitysign1.signText[2] = blah5;
        tileentitysign1.signText[3] = blah6;
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
        this.dataManager.set(EntityTNTNostalgiaPrimed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTNostalgiaPrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTNostalgiaPrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
