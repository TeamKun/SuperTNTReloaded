package com.xeraster.supertnt.explosions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MobExplosion extends Explosion {

    private final boolean causesFire;
    private final boolean damagesTerrain;
    private final Random random;
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final Entity exploder;
    private final float size;
    public int numberOfThingsSpawned;
    private final List affectedBlockPositions;
    private final Map playerKnockbackMap;
    private final Vec3d position;

    @SideOnly(Side.CLIENT)
    public MobExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, List affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, false, true, affectedPositions);
    }

    @SideOnly(Side.CLIENT)
    public MobExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain, List affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, causesFire, damagesTerrain);
        this.affectedBlockPositions.addAll(affectedPositions);
    }

    public MobExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean damagesTerrain) {
        super(worldIn, entityIn, x, y, z, size, flaming, damagesTerrain);
        this.numberOfThingsSpawned = 0;
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
        System.out.println("Running doExplosionA override in MobExplosion class");
        HashSet set = Sets.newHashSet();
        boolean i = true;

        int k1;
        int l1;

        for (int f3 = -55; f3 < 56; ++f3) {
            for (k1 = -55; k1 < 56; ++k1) {
                for (l1 = -55; l1 < 56; ++l1) {
                    if (f3 == -55 || f3 == 55 || k1 == -55 || k1 == 55 || l1 == -55 || l1 == 55) {
                        double i2 = (double) ((float) f3 / 15.0F * 2.0F - 1.0F);
                        double j2 = (double) ((float) k1 / 15.0F * 2.0F - 1.0F);
                        double list = (double) ((float) l1 / 15.0F * 2.0F - 1.0F);
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

                            if (iblockstate.getMaterial() != Material.AIR && iblockstate.getBlock() != Blocks.TALLGRASS) {
                                d12 = -1.0F;
                            }

                            if (iblockstate.getBlock() == Blocks.TALLGRASS) {
                                ;
                            }

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

        this.affectedBlockPositions.addAll(set);
        float f = this.size * 2.0F;

        k1 = MathHelper.floor(this.x - (double) f - 5.0D);
        l1 = MathHelper.floor(this.x + (double) f + 5.0D);
        int i_ = MathHelper.floor(this.y - (double) f - 5.0D);
        int i1 = MathHelper.floor(this.y + (double) f + 5.0D);
        int j = MathHelper.floor(this.z - (double) f - 5.0D);
        int j1 = MathHelper.floor(this.z + (double) f + 5.0D);
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

        Iterator mobsSpawned;
        BlockPos blockpos1;

        if (this.damagesTerrain) {
            mobsSpawned = this.affectedBlockPositions.iterator();

            while (mobsSpawned.hasNext()) {
                blockpos1 = (BlockPos) mobsSpawned.next();
                IBlockState iblockstate = this.world.getBlockState(blockpos1);
                Block block = iblockstate.getBlock();

                if (spawnParticles) {
                    double d0 = (double) ((float) blockpos1.getX() + this.world.rand.nextFloat());
                    double d1 = (double) ((float) blockpos1.getY() + this.world.rand.nextFloat());
                    double d2 = (double) ((float) blockpos1.getZ() + this.world.rand.nextFloat());
                    double d3 = d0 - this.x;
                    double d4 = d1 - this.y;
                    double d5 = d2 - this.z;
                    double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5D / (d6 / (double) this.size + 0.1D);

                    d7 *= (double) (this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
                    d3 *= d7;
                    d4 *= d7;
                    d5 *= d7;
                    this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.x) / 2.0D, (d1 + this.y) / 2.0D, (d2 + this.z) / 2.0D, d3, d4, d5, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
                }

                if (iblockstate.getMaterial() != Material.AIR) {
                    if (block.canDropFromExplosion(this)) {
                        block.dropBlockAsItemWithChance(this.world, blockpos1, this.world.getBlockState(blockpos1), 1.0F / this.size, 0);
                    }

                    block.onBlockExploded(this.world, blockpos1, this);
                }
            }
        }

        mobsSpawned = this.affectedBlockPositions.iterator();

        while (mobsSpawned.hasNext()) {
            blockpos1 = (BlockPos) mobsSpawned.next();
            if (this.world.getBlockState(blockpos1).getMaterial() == Material.AIR && this.world.getBlockState(blockpos1.down()).isFullBlock() && this.random.nextInt(2) == 0) {
                this.spawnmobz((double) blockpos1.getX(), (double) blockpos1.getY(), (double) blockpos1.getZ());
            }
        }

        System.out.println(this.numberOfThingsSpawned + " mobs were spawned!");
        ITextComponent mobsSpawned1 = Serializer.fromJsonLenient("\'" + this.numberOfThingsSpawned + " mobs were spawned!\'");

        Minecraft.getMinecraft().player.sendMessage(mobsSpawned1);
    }

    public Map getPlayerKnockbackMap() {
        return this.playerKnockbackMap;
    }

    @Nullable
    public EntityLivingBase getExplosivePlacedBy() {
        return this.exploder == null ? null : (this.exploder instanceof EntityTNTLargePrimed ? ((EntityTNTLargePrimed) this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase) this.exploder : null));
    }

    public void spawnmobz(double i, double j, double k) {
        Random rn = new Random();
        int randomDrop = rn.nextInt(84);

        if (randomDrop <= 4 && randomDrop >= 1) {
            EntityZombie entity23 = new EntityZombie(this.world);

            entity23.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity23);
        } else if (randomDrop <= 8 && randomDrop > 4) {
            EntityPig entity22 = new EntityPig(this.world);

            entity22.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity22);
        } else if (randomDrop <= 12 && randomDrop > 8) {
            EntityBat entity21 = new EntityBat(this.world);

            entity21.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity21);
        } else if (randomDrop <= 16 && randomDrop > 12) {
            EntityChicken entity20 = new EntityChicken(this.world);

            entity20.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity20);
        } else if (randomDrop <= 20 && randomDrop > 16) {
            EntityHorse entity19 = new EntityHorse(this.world);

            entity19.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity19);
        } else if (randomDrop <= 24 && randomDrop > 20) {
            EntityMooshroom entity18 = new EntityMooshroom(this.world);

            entity18.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity18);
        } else if (randomDrop <= 28 && randomDrop > 24) {
            EntityOcelot entity17 = new EntityOcelot(this.world);

            entity17.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity17);
        } else if (randomDrop <= 32 && randomDrop > 28) {
            EntitySheep entity16 = new EntitySheep(this.world);

            entity16.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity16);
        } else if (randomDrop <= 36 && randomDrop > 32) {
            EntityVillager entity15 = new EntityVillager(this.world);

            entity15.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity15);
        } else if (randomDrop <= 40 && randomDrop > 36) {
            EntityWolf entity14 = new EntityWolf(this.world);

            entity14.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity14);
        } else if (randomDrop <= 41 && randomDrop > 40) {
            EntityBlaze entity13 = new EntityBlaze(this.world);

            entity13.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity13);
        } else if (randomDrop <= 45 && randomDrop > 41) {
            EntityCaveSpider entity12 = new EntityCaveSpider(this.world);

            entity12.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity12);
        } else if (randomDrop <= 49 && randomDrop > 45) {
            EntityCreeper entity11 = new EntityCreeper(this.world);

            entity11.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity11);
        } else if (randomDrop <= 53 && randomDrop > 49) {
            EntityEnderman entity10 = new EntityEnderman(this.world);

            entity10.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity10);
        } else if ((double) randomDrop <= 53.2D && randomDrop > 53) {
            EntityGhast entity9 = new EntityGhast(this.world);

            entity9.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity9);
        } else if (randomDrop <= 58 && (double) randomDrop > 53.2D) {
            EntityMagmaCube entity8 = new EntityMagmaCube(this.world);

            entity8.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity8);
        } else if (randomDrop <= 62 && randomDrop > 58) {
            EntityPigZombie entity7 = new EntityPigZombie(this.world);

            entity7.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity7);
        } else if (randomDrop <= 66 && randomDrop > 62) {
            EntitySlime entity6 = new EntitySlime(this.world);

            entity6.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity6);
        } else if (randomDrop <= 70 && randomDrop > 66) {
            EntitySnowman entity5 = new EntitySnowman(this.world);

            entity5.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity5);
        } else if (randomDrop <= 74 && randomDrop > 70) {
            EntitySpider entity4 = new EntitySpider(this.world);

            entity4.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity4);
        } else if (randomDrop <= 76 && randomDrop > 74) {
            EntityWitch entity3 = new EntityWitch(this.world);

            entity3.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity3);
        } else if ((double) randomDrop <= 76.15D && randomDrop > 76) {
            EntityWither entity2 = new EntityWither(this.world);

            entity2.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity2);
        } else if (randomDrop <= 80 && (double) randomDrop > 76.15D) {
            EntitySquid entity1 = new EntitySquid(this.world);

            entity1.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            this.world.spawnEntity(entity1);
        } else {
            EntityCow entity = new EntityCow(this.world);

            entity.setLocationAndAngles(this.x, this.y, this.z, 0.0F, 0.0F);
            this.world.spawnEntity(entity);
        }

        ++this.numberOfThingsSpawned;
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
