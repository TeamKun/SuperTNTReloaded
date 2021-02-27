package com.xeraster.supertnt.explosions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.xeraster.supertnt.SuperTNTMod;
import com.xeraster.supertnt.init.Items;
import com.xeraster.supertnt.primedtnt.EntityTNTLargePrimed;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EverythingExplosion extends Explosion {

    private final boolean causesFire;
    private final boolean damagesTerrain;
    private final Random random;
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final Entity exploder;
    private final float size;
    private final List affectedBlockPositions;
    private final Map playerKnockbackMap;
    private final Vec3d position;

    @SideOnly(Side.CLIENT)
    public EverythingExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, List affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, false, true, affectedPositions);
    }

    @SideOnly(Side.CLIENT)
    public EverythingExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain, List affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, causesFire, damagesTerrain);
        this.affectedBlockPositions.addAll(affectedPositions);
    }

    public EverythingExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean damagesTerrain) {
        super(worldIn, entityIn, x, y, z, size, flaming, damagesTerrain);
        this.random = new Random();
        this.affectedBlockPositions = Lists.newArrayList();
        this.playerKnockbackMap = Maps.newHashMap();
        this.world = worldIn;
        this.exploder = entityIn;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.causesFire = flaming;
        this.damagesTerrain = damagesTerrain;
        this.position = new Vec3d(this.x, this.y, this.z);
    }

    public void doExplosionA() {
        HashSet set = Sets.newHashSet();
        boolean i = true;

        int k1;
        int l1;

        for (int f3 = -10; f3 < 11; ++f3) {
            for (k1 = -10; k1 < 11; ++k1) {
                for (l1 = -10; l1 < 11; ++l1) {
                    if (f3 == -10 || f3 == 10 || k1 == -10 || k1 == 10 || l1 == -10 || l1 == 10) {
                        double i2 = (double) ((float) f3 / 10.0F * 2.0F - 1.0F);
                        double j2 = (double) ((float) k1 / 15.0F * 2.0F - 1.0F);
                        double list = (double) ((float) l1 / 10.0F * 2.0F - 1.0F);
                        double k2 = Math.sqrt(i2 * i2 + j2 * j2 + list * list);

                        i2 /= k2;
                        j2 /= k2;
                        list /= k2;
                        float d12 = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;

                        for (float f1 = 0.3F; d12 > 0.0F; d12 -= 0.22500001F) {
                            BlockPos d13 = new BlockPos(d4, d6, d8);
                            IBlockState iblockstate = this.world.getBlockState(d13);

                            if (iblockstate.getMaterial() == Material.AIR) {
                                ;
                            }

                            if (iblockstate.getMaterial() != Material.AIR) {
                                ;
                            }

                            if (iblockstate.getBlock() != Blocks.BEDROCK) {
                                if (d12 > 0.0F && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, d13, iblockstate, d12))) {
                                    set.add(d13);
                                }

                                d4 += i2 * 0.30000001192092896D;
                                d6 += j2 * 0.30000001192092896D;
                                d8 += list * 0.30000001192092896D;
                            }
                        }
                    }
                }
            }
        }

        this.affectedBlockPositions.addAll(set);
        float f = this.size * 2.0F;

        k1 = MathHelper.floor(this.x - (double) f - 1.0D);
        l1 = MathHelper.floor(this.x + (double) f + 1.0D);
        int i_ = MathHelper.floor(this.y - (double) f - 1.0D);
        int i1 = MathHelper.floor(this.y + (double) f + 1.0D);
        int j = MathHelper.floor(this.z - (double) f - 1.0D);
        int j1 = MathHelper.floor(this.z + (double) f + 1.0D);
        List list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double) k1, (double) i_, (double) j, (double) l1, (double) i1, (double) j1));

        ForgeEventFactory.onExplosionDetonate(this.world, this, list, (double) f);
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for (int k = 0; k < list.size(); ++k) {
            Entity entity = (Entity) list.get(k);

            if (!entity.isImmuneToExplosions()) {
                double d0 = entity.getDistance(this.x, this.y, this.z) / (double) f;

                if (d0 <= 1.0D) {
                    double d5 = entity.posX - this.x;
                    double d7 = entity.posY + (double) entity.getEyeHeight() - this.y;
                    double d9 = entity.posZ - this.z;
                    double d1 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d1 != 0.0D) {
                        d5 /= d1;
                        d7 /= d1;
                        d9 /= d1;
                        double d14 = (double) this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double d10 = (1.0D - d0) * d14;

                        entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f + 1.0D)));
                        double d11 = d10;

                        if (entity instanceof EntityLivingBase) {
                            d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, d10);
                        }

                        entity.motionX += d5 * d11;
                        entity.motionY += d7 * d11;
                        entity.motionZ += d9 * d11;
                        if (entity instanceof EntityPlayer) {
                            EntityPlayer entityplayer = (EntityPlayer) entity;

                            if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying)) {
                                this.playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }

    }

    public void doExplosionB(boolean spawnParticles) {
        this.world.playSound((EntityPlayer) null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        if (this.size >= 2.0F && this.damagesTerrain) {
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D, new int[0]);
        } else {
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D, new int[0]);
        }

        Iterator iterator;
        BlockPos blockpos1;

        if (this.damagesTerrain) {
            iterator = this.affectedBlockPositions.iterator();

            while (iterator.hasNext()) {
                blockpos1 = (BlockPos) iterator.next();
                IBlockState blockScanPos = this.world.getBlockState(blockpos1);
                Block iblockstate2 = blockScanPos.getBlock();

                if (spawnParticles) {
                    double rndMobOrBlock = (double) ((float) blockpos1.getX() + this.world.rand.nextFloat());
                    double rndMoreAir = (double) ((float) blockpos1.getY() + this.world.rand.nextFloat());
                    double tntToPutInChest = (double) ((float) blockpos1.getZ() + this.world.rand.nextFloat());
                    double randomNum = rndMobOrBlock - this.x;
                    double redsolocup = rndMoreAir - this.y;
                    double actionreplay = tntToPutInChest - this.z;
                    double ps2keyboard = (double) MathHelper.sqrt(randomNum * randomNum + redsolocup * redsolocup + actionreplay * actionreplay);

                    randomNum /= ps2keyboard;
                    redsolocup /= ps2keyboard;
                    actionreplay /= ps2keyboard;
                    double sonic2 = 0.5D / (ps2keyboard / (double) this.size + 0.1D);

                    sonic2 *= (double) (this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
                    randomNum *= sonic2;
                    redsolocup *= sonic2;
                    actionreplay *= sonic2;
                    this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (rndMobOrBlock + this.x) / 2.0D, (rndMoreAir + this.y) / 2.0D, (tntToPutInChest + this.z) / 2.0D, randomNum, redsolocup, actionreplay, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, rndMobOrBlock, rndMoreAir, tntToPutInChest, randomNum, redsolocup, actionreplay, new int[0]);
                }

                if (blockScanPos.getMaterial() != Material.AIR) {
                    if (iblockstate2.canDropFromExplosion(this)) {
                        iblockstate2.dropBlockAsItemWithChance(this.world, blockpos1, this.world.getBlockState(blockpos1), 1.0F / this.size, 0);
                    }

                    iblockstate2.onBlockExploded(this.world, blockpos1, this);
                }
            }
        }

        iterator = this.affectedBlockPositions.iterator();

        while (iterator.hasNext()) {
            blockpos1 = (BlockPos) iterator.next();
            BlockPos blockScanPos1 = new BlockPos(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());

            this.world.getBlockState(blockScanPos1);
            if (this.world.getBlockState(blockScanPos1).getBlock() != Blocks.BEDROCK) {
                Random rndMobOrBlock1 = new Random();
                int mobOrBlock = rndMobOrBlock1.nextInt(120);

                if (mobOrBlock >= 1) {
                    Random rndMoreAir1 = new Random();
                    int shouldMakeMoreAir = rndMoreAir1.nextInt(20);

                    if (shouldMakeMoreAir < 5) {
                        this.world.setBlockState(blockScanPos1, this.decideWhatBlockToSpawn());
                        if (this.world.getBlockState(blockScanPos1).getBlock() == Blocks.CHEST) {
                            Block[] tntToPutInChest1 = new Block[] { SuperTNTMod.LARGE_TNT, SuperTNTMod.MASSIVE_TNT, SuperTNTMod.SUPER_TNT, SuperTNTMod.DOOM_TNT, SuperTNTMod.END_TNT, SuperTNTMod.OCEAN_TNT, SuperTNTMod.SPONGE_TNT, SuperTNTMod.WAVE_TNT, SuperTNTMod.SAVE_TNT, SuperTNTMod.CAVE_TNT, SuperTNTMod.LAVA_TNT, SuperTNTMod.MOB_TNT, SuperTNTMod.MOB2_TNT, SuperTNTMod.MOB3_TNT, SuperTNTMod.NOMOB_TNT, SuperTNTMod.CHRISTMAS_TNT, SuperTNTMod.BIOME_TNT, SuperTNTMod.CLUSTER_TNT, SuperTNTMod.CRACK_TNT, SuperTNTMod.DIAMOND_TNT, SuperTNTMod.DIAMONDNUKE_TNT, SuperTNTMod.FIRE_TNT, SuperTNTMod.FLAT_TNT, SuperTNTMod.FOREST_TNT, SuperTNTMod.HELL_TNT, SuperTNTMod.HOUSE_TNT, SuperTNTMod.ISLAND_TNT, SuperTNTMod.KIMJONG_TNT, SuperTNTMod.LGBT_TNT, SuperTNTMod.MINE_TNT, SuperTNTMod.NOSTALGIA_TNT, SuperTNTMod.RAKE_TNT, SuperTNTMod.SNOW_TNT, SuperTNTMod.SUPERCLUSTER_TNT, SuperTNTMod.THUNDER_TNT, SuperTNTMod.TIME_TNT, SuperTNTMod.TREE_TNT, SuperTNTMod.TRUMP_TNT, SuperTNTMod.WEATHER_TNT, SuperTNTMod.WEED_TNT, Blocks.TNT};
                            Random rn = new Random();
                            int randomNum1 = rn.nextInt(40);
                            TileEntityChest tileentitychest1 = (TileEntityChest) this.world.getTileEntity(blockScanPos1);
                            ItemStack redsolocup1 = new ItemStack(Items.REDSOLOCUP, 2);
                            ItemStack snescontroller = new ItemStack(Items.SNESCONTROLLER, 2);
                            ItemStack actionreplay1 = new ItemStack(Items.ACTIONREPLAY, 2);
                            ItemStack energysword = new ItemStack(Items.ENERGYSWORD, 2);
                            ItemStack ps2keyboard1 = new ItemStack(Items.PS2KEYBOARD, 3);
                            ItemStack floppy = new ItemStack(Items.FLOPPY, 4);
                            ItemStack sonic21 = new ItemStack(Items.SONIC2, 2);
                            ItemStack ram = new ItemStack(Items.SIMMRAM, 2);
                            ItemStack nokia = new ItemStack(Items.NOKIA, 2);
                            ItemStack typeOfTNT = new ItemStack(Item.getItemFromBlock(tntToPutInChest1[randomNum1]), 1);

                            tileentitychest1.setInventorySlotContents(0, redsolocup1);
                            tileentitychest1.setInventorySlotContents(1, snescontroller);
                            tileentitychest1.setInventorySlotContents(2, actionreplay1);
                            tileentitychest1.setInventorySlotContents(3, energysword);
                            tileentitychest1.setInventorySlotContents(4, ps2keyboard1);
                            tileentitychest1.setInventorySlotContents(5, floppy);
                            tileentitychest1.setInventorySlotContents(6, sonic21);
                            tileentitychest1.setInventorySlotContents(7, ram);
                            tileentitychest1.setInventorySlotContents(8, nokia);
                            tileentitychest1.setInventorySlotContents(9, typeOfTNT);
                        }
                    } else if (shouldMakeMoreAir >= 5) {
                        this.world.setBlockState(blockScanPos1, Blocks.AIR.getDefaultState());
                    }
                } else {
                    this.spawnAMob(blockScanPos1);
                }
            }
        }

    }

    public IBlockState decideWhatBlockToSpawn() {
        Random rnd1 = new Random();
        int randomNumber1 = rnd1.nextInt(106);
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
        blockToSpawn[19] = Blocks.CHEST;
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

    public Map getPlayerKnockbackMap() {
        return this.playerKnockbackMap;
    }

    @Nullable
    public EntityLivingBase getExplosivePlacedBy() {
        return this.exploder == null ? null : (this.exploder instanceof EntityTNTLargePrimed ? ((EntityTNTLargePrimed) this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase) this.exploder : null));
    }

    public void clearAffectedBlockPositions() {
        this.affectedBlockPositions.clear();
    }

    public List getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }

    public Vec3d getPosition() {
        return this.position;
    }
}
