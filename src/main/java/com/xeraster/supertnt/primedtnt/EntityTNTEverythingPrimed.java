package com.xeraster.supertnt.primedtnt;

import com.xeraster.supertnt.explosions.EverythingExplosion;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityTNTEverythingPrimed extends Entity {

    private static final DataParameter FUSE = EntityDataManager.createKey(EntityTNTEverythingPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityTNTEverythingPrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTEverythingPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
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

    public EverythingExplosion createExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isSmoking) {
        return this.newExplosion(entityIn, x, y, z, strength, false, isSmoking);
    }

    public EverythingExplosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
        EverythingExplosion explosion = new EverythingExplosion(this.world, entityIn, x, y, z, strength, isFlaming, isSmoking);

        if (ForgeEventFactory.onExplosionStart(this.world, explosion)) {
            return explosion;
        } else {
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            return explosion;
        }
    }

    protected void entityInit() {
        this.dataManager.register(EntityTNTEverythingPrimed.FUSE, Integer.valueOf(80));
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
        float f = 30.0F;

        this.createExplosion(this, this.posX, this.posY + (double) (this.height / 16.0F), this.posZ, f, true);
    }

    public IBlockState decideWhatBlockToSpawn() {
        Random rnd1 = new Random();
        int randomNumber1 = rnd1.nextInt(150);
        Block[] blockToSpawn = new Block[151];

        System.out.println("random bf2 number is: " + randomNumber1);
        blockToSpawn[0] = Blocks.AIR;
        blockToSpawn[1] = Blocks.ACACIA_FENCE;
        blockToSpawn[2] = Blocks.BOOKSHELF;
        blockToSpawn[3] = Blocks.BRICK_BLOCK;
        blockToSpawn[4] = Blocks.CAKE;
        blockToSpawn[5] = Blocks.CARPET;
        blockToSpawn[6] = Blocks.CLAY;
        blockToSpawn[7] = Blocks.COAL_ORE;
        blockToSpawn[8] = Blocks.COBBLESTONE;
        blockToSpawn[9] = Blocks.CONCRETE;
        blockToSpawn[10] = Blocks.CRAFTING_TABLE;
        blockToSpawn[11] = Blocks.DIAMOND_BLOCK;
        blockToSpawn[12] = Blocks.DIAMOND_ORE;
        blockToSpawn[13] = Blocks.DIRT;
        blockToSpawn[14] = Blocks.EMERALD_BLOCK;
        blockToSpawn[15] = Blocks.EMERALD_ORE;
        blockToSpawn[16] = Blocks.ENCHANTING_TABLE;
        blockToSpawn[17] = Blocks.END_BRICKS;
        blockToSpawn[18] = Blocks.END_STONE;
        blockToSpawn[19] = Blocks.ENDER_CHEST;
        blockToSpawn[20] = Blocks.FARMLAND;
        blockToSpawn[21] = Blocks.AIR;
        blockToSpawn[22] = Blocks.AIR;
        blockToSpawn[23] = Blocks.FROSTED_ICE;
        blockToSpawn[24] = Blocks.FURNACE;
        blockToSpawn[25] = Blocks.GLASS;
        blockToSpawn[26] = Blocks.GLASS_PANE;
        blockToSpawn[27] = Blocks.GLOWSTONE;
        blockToSpawn[28] = Blocks.GOLD_BLOCK;
        blockToSpawn[29] = Blocks.GOLD_ORE;
        blockToSpawn[30] = Blocks.GRAVEL;
        blockToSpawn[31] = Blocks.HARDENED_CLAY;
        blockToSpawn[32] = Blocks.HAY_BLOCK;
        blockToSpawn[33] = Blocks.ICE;
        blockToSpawn[34] = Blocks.IRON_BLOCK;
        blockToSpawn[35] = Blocks.IRON_ORE;
        blockToSpawn[36] = Blocks.JUKEBOX;
        blockToSpawn[37] = Blocks.LAPIS_BLOCK;
        blockToSpawn[38] = Blocks.LAPIS_ORE;
        blockToSpawn[39] = Blocks.LEVER;
        blockToSpawn[40] = Blocks.MELON_BLOCK;
        blockToSpawn[41] = Blocks.AIR;
        blockToSpawn[42] = Blocks.AIR;
        blockToSpawn[43] = Blocks.MOSSY_COBBLESTONE;
        blockToSpawn[44] = Blocks.NETHER_BRICK;
        blockToSpawn[45] = Blocks.NETHER_BRICK_FENCE;
        blockToSpawn[46] = Blocks.NETHER_BRICK_STAIRS;
        blockToSpawn[47] = Blocks.NETHERRACK;
        blockToSpawn[48] = Blocks.OAK_FENCE;
        blockToSpawn[49] = Blocks.OBSIDIAN;
        blockToSpawn[50] = Blocks.AIR;
        blockToSpawn[51] = Blocks.AIR;
        blockToSpawn[52] = Blocks.AIR;
        blockToSpawn[53] = Blocks.PACKED_ICE;
        blockToSpawn[54] = Blocks.PLANKS;
        blockToSpawn[55] = Blocks.POTATOES;
        blockToSpawn[56] = Blocks.PUMPKIN;
        blockToSpawn[57] = Blocks.QUARTZ_BLOCK;
        blockToSpawn[58] = Blocks.QUARTZ_ORE;
        blockToSpawn[59] = Blocks.QUARTZ_STAIRS;
        blockToSpawn[60] = Blocks.RED_MUSHROOM_BLOCK;
        blockToSpawn[61] = Blocks.RED_NETHER_BRICK;
        blockToSpawn[62] = Blocks.PURPUR_BLOCK;
        blockToSpawn[63] = Blocks.RED_SANDSTONE;
        blockToSpawn[64] = Blocks.RED_SANDSTONE_STAIRS;
        blockToSpawn[65] = Blocks.REDSTONE_BLOCK;
        blockToSpawn[66] = Blocks.REDSTONE_ORE;
        blockToSpawn[67] = Blocks.REDSTONE_TORCH;
        blockToSpawn[68] = Blocks.SANDSTONE;
        blockToSpawn[69] = Blocks.SANDSTONE_STAIRS;
        blockToSpawn[70] = Blocks.SAPLING;
        blockToSpawn[71] = Blocks.STAINED_HARDENED_CLAY;
        blockToSpawn[72] = Blocks.SPRUCE_STAIRS;
        blockToSpawn[73] = Blocks.SLIME_BLOCK;
        blockToSpawn[74] = Blocks.SNOW;
        blockToSpawn[75] = Blocks.SNOW_LAYER;
        blockToSpawn[76] = Blocks.SOUL_SAND;
        blockToSpawn[77] = Blocks.SPONGE;
        blockToSpawn[78] = Blocks.AIR;
        blockToSpawn[79] = Blocks.AIR;
        blockToSpawn[80] = Blocks.AIR;
        blockToSpawn[81] = Blocks.AIR;
        blockToSpawn[82] = Blocks.AIR;
        blockToSpawn[83] = Blocks.AIR;
        blockToSpawn[84] = Blocks.STAINED_HARDENED_CLAY;
        blockToSpawn[85] = Blocks.STONE;
        blockToSpawn[86] = Blocks.STONE_BRICK_STAIRS;
        blockToSpawn[87] = Blocks.STONE_STAIRS;
        blockToSpawn[88] = Blocks.STONEBRICK;
        blockToSpawn[89] = Blocks.TNT;
        blockToSpawn[90] = Blocks.WEB;
        blockToSpawn[91] = Blocks.WHEAT;
        blockToSpawn[92] = Blocks.WOOL;
        blockToSpawn[93] = Blocks.CACTUS;
        blockToSpawn[94] = Blocks.FIRE;
        blockToSpawn[95] = Blocks.FLOWING_LAVA;
        blockToSpawn[96] = Blocks.FLOWING_WATER;
        blockToSpawn[97] = Blocks.GRASS;
        blockToSpawn[98] = Blocks.LEAVES;
        blockToSpawn[99] = Blocks.LEAVES2;
        blockToSpawn[100] = Blocks.AIR;
        blockToSpawn[101] = Blocks.AIR;
        blockToSpawn[102] = Blocks.AIR;
        blockToSpawn[103] = Blocks.AIR;
        blockToSpawn[104] = Blocks.STAINED_GLASS;
        blockToSpawn[105] = Blocks.SAND;
        blockToSpawn[106] = Blocks.AIR;
        blockToSpawn[107] = Blocks.AIR;
        blockToSpawn[108] = Blocks.AIR;
        blockToSpawn[109] = Blocks.AIR;
        blockToSpawn[110] = Blocks.AIR;
        blockToSpawn[111] = Blocks.AIR;
        blockToSpawn[112] = Blocks.AIR;
        blockToSpawn[113] = Blocks.AIR;
        blockToSpawn[114] = Blocks.AIR;
        blockToSpawn[115] = Blocks.AIR;
        blockToSpawn[116] = Blocks.AIR;
        blockToSpawn[117] = Blocks.AIR;
        blockToSpawn[118] = Blocks.AIR;
        blockToSpawn[119] = Blocks.AIR;
        blockToSpawn[120] = Blocks.AIR;
        blockToSpawn[121] = Blocks.AIR;
        blockToSpawn[122] = Blocks.AIR;
        blockToSpawn[123] = Blocks.AIR;
        blockToSpawn[124] = Blocks.AIR;
        blockToSpawn[125] = Blocks.AIR;
        blockToSpawn[126] = Blocks.AIR;
        blockToSpawn[127] = Blocks.AIR;
        blockToSpawn[128] = Blocks.AIR;
        blockToSpawn[129] = Blocks.AIR;
        blockToSpawn[130] = Blocks.AIR;
        blockToSpawn[131] = Blocks.AIR;
        blockToSpawn[132] = Blocks.AIR;
        blockToSpawn[133] = Blocks.AIR;
        blockToSpawn[134] = Blocks.AIR;
        blockToSpawn[135] = Blocks.AIR;
        blockToSpawn[136] = Blocks.AIR;
        blockToSpawn[137] = Blocks.AIR;
        blockToSpawn[138] = Blocks.AIR;
        blockToSpawn[139] = Blocks.AIR;
        blockToSpawn[140] = Blocks.AIR;
        blockToSpawn[141] = Blocks.AIR;
        blockToSpawn[142] = Blocks.AIR;
        blockToSpawn[143] = Blocks.AIR;
        blockToSpawn[144] = Blocks.AIR;
        blockToSpawn[145] = Blocks.AIR;
        blockToSpawn[146] = Blocks.AIR;
        blockToSpawn[147] = Blocks.AIR;
        blockToSpawn[148] = Blocks.AIR;
        blockToSpawn[149] = Blocks.AIR;
        blockToSpawn[150] = Blocks.AIR;
        return blockToSpawn[randomNumber1].getDefaultState();
    }

    public void spawnAMob(BlockPos where) {
        Random rn = new Random();
        int randomDrop = rn.nextInt(84);

        if (randomDrop <= 4 && randomDrop >= 1) {
            EntityZombie entity23 = new EntityZombie(this.world);

            entity23.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity23);
        } else if (randomDrop <= 8 && randomDrop > 4) {
            EntityPig entity22 = new EntityPig(this.world);

            entity22.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity22);
        } else if (randomDrop <= 12 && randomDrop > 8) {
            EntityBat entity21 = new EntityBat(this.world);

            entity21.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity21);
        } else if (randomDrop <= 16 && randomDrop > 12) {
            EntityChicken entity20 = new EntityChicken(this.world);

            entity20.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity20);
        } else if (randomDrop <= 20 && randomDrop > 16) {
            EntityHorse entity19 = new EntityHorse(this.world);

            entity19.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity19);
        } else if (randomDrop <= 24 && randomDrop > 20) {
            EntityMooshroom entity18 = new EntityMooshroom(this.world);

            entity18.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity18);
        } else if (randomDrop <= 28 && randomDrop > 24) {
            EntityOcelot entity17 = new EntityOcelot(this.world);

            entity17.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity17);
        } else if (randomDrop <= 32 && randomDrop > 28) {
            EntitySheep entity16 = new EntitySheep(this.world);

            entity16.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity16);
        } else if (randomDrop <= 36 && randomDrop > 32) {
            EntityVillager entity15 = new EntityVillager(this.world);

            entity15.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity15);
        } else if (randomDrop <= 40 && randomDrop > 36) {
            EntityWolf entity14 = new EntityWolf(this.world);

            entity14.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity14);
        } else if (randomDrop <= 41 && randomDrop > 40) {
            EntityBlaze entity13 = new EntityBlaze(this.world);

            entity13.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity13);
        } else if (randomDrop <= 45 && randomDrop > 41) {
            EntityCaveSpider entity12 = new EntityCaveSpider(this.world);

            entity12.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity12);
        } else if (randomDrop <= 49 && randomDrop > 45) {
            EntityCreeper entity11 = new EntityCreeper(this.world);

            entity11.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity11);
        } else if (randomDrop <= 53 && randomDrop > 49) {
            EntityEnderman entity10 = new EntityEnderman(this.world);

            entity10.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity10);
        } else if ((double) randomDrop <= 53.2D && randomDrop > 53) {
            EntityGhast entity9 = new EntityGhast(this.world);

            entity9.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity9);
        } else if (randomDrop <= 58 && (double) randomDrop > 53.2D) {
            EntityMagmaCube entity8 = new EntityMagmaCube(this.world);

            entity8.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity8);
        } else if (randomDrop <= 62 && randomDrop > 58) {
            EntityPigZombie entity7 = new EntityPigZombie(this.world);

            entity7.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity7);
        } else if (randomDrop <= 66 && randomDrop > 62) {
            EntitySlime entity6 = new EntitySlime(this.world);

            entity6.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity6);
        } else if (randomDrop <= 70 && randomDrop > 66) {
            EntitySnowman entity5 = new EntitySnowman(this.world);

            entity5.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity5);
        } else if (randomDrop <= 74 && randomDrop > 70) {
            EntitySpider entity4 = new EntitySpider(this.world);

            entity4.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity4);
        } else if (randomDrop <= 76 && randomDrop > 74) {
            EntityWitch entity3 = new EntityWitch(this.world);

            entity3.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity3);
        } else if ((double) randomDrop <= 76.15D && randomDrop > 76) {
            EntityWither entity2 = new EntityWither(this.world);

            entity2.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity2);
        } else if (randomDrop <= 80 && (double) randomDrop > 76.15D) {
            EntitySquid entity1 = new EntitySquid(this.world);

            entity1.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(entity1);
        } else {
            EntityCow entity = new EntityCow(this.world);

            entity.setLocationAndAngles((double) where.getX(), (double) where.getY(), (double) where.getZ(), 0.0F, 0.0F);
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
        this.dataManager.set(EntityTNTEverythingPrimed.FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter key) {
        if (EntityTNTEverythingPrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTEverythingPrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }
}
