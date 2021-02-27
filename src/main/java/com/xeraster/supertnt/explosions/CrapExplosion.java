package com.xeraster.supertnt.explosions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.xeraster.supertnt.SuperTNTMod;
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
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CrapExplosion extends Explosion {

    private final boolean causesFire;
    private final boolean damagesTerrain;
    private final Random random;
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final Entity exploder;
    private final float size;
    public int explosionSize;
    private final List affectedBlockPositions;
    private final Map playerKnockbackMap;
    private final Vec3d position;

    @SideOnly(Side.CLIENT)
    public CrapExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, List affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, false, true, affectedPositions);
    }

    @SideOnly(Side.CLIENT)
    public CrapExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain, List affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, causesFire, damagesTerrain);
        this.affectedBlockPositions.addAll(affectedPositions);
    }

    public CrapExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean damagesTerrain) {
        super(worldIn, entityIn, x, y, z, size, flaming, damagesTerrain);
        this.explosionSize = 100;
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
        int explosionSizeMinusOne = this.explosionSize - 1;

        int k1;
        int l1;

        for (int f3 = 0; f3 < this.explosionSize; ++f3) {
            for (k1 = 0; k1 < this.explosionSize; ++k1) {
                for (l1 = 0; l1 < this.explosionSize; ++l1) {
                    if (f3 == 0 || f3 == explosionSizeMinusOne || k1 == 0 || k1 == explosionSizeMinusOne || l1 == 0 || l1 == explosionSizeMinusOne) {
                        double i2 = (double) ((float) f3 / ((float) this.explosionSize - 1.0F) * 2.0F - 1.0F);
                        double j2 = (double) ((float) k1 / ((float) this.explosionSize - 1.0F) * 2.0F - 1.0F);
                        double list = (double) ((float) l1 / ((float) this.explosionSize - 1.0F) * 2.0F - 1.0F);
                        double k2 = Math.sqrt(i2 * i2 + j2 * j2 + list * list);

                        i2 /= k2;
                        j2 /= k2;
                        list /= k2;
                        float d12 = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;

                        for (float f1 = 0.3F; d12 > 0.0F; d12 -= f1 * 0.75F) {
                            BlockPos d13 = new BlockPos(d4, d6, d8);
                            IBlockState iblockstate = this.world.getBlockState(d13);

                            if (iblockstate.getMaterial() != Material.AIR) {
                                float d14 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.world, d13, iblockstate) : iblockstate.getBlock().getExplosionResistance(this.world, d13, (Entity) null, this);

                                d12 -= (d14 + 100.3F) * f1;
                            }

                            if (iblockstate.getBlock() == Blocks.TALLGRASS || iblockstate.getBlock() == Blocks.GRASS) {
                                --d12;
                            }

                            if (d12 > 0.0F && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, d13, iblockstate, d12))) {
                                set.add(d13);
                            }

                            d4 += i2 * (double) f1 * 3.0D;
                            d6 += j2 * (double) f1;
                            d8 += list * (double) f1 * 3.0D;
                        }
                    }
                }
            }
        }

        this.affectedBlockPositions.addAll(set);
        float f = this.size * 2.0F;

        k1 = MathHelper.floor(this.x - (double) f - 1.0D);
        l1 = MathHelper.floor(this.x + (double) f + 1.0D);
        int i = MathHelper.floor(this.y - (double) f - 1.0D);
        int i1 = MathHelper.floor(this.y + (double) f + 1.0D);
        int j = MathHelper.floor(this.z - (double) f - 1.0D);
        int j1 = MathHelper.floor(this.z + (double) f + 1.0D);
        List list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double) k1, (double) i, (double) j, (double) l1, (double) i1, (double) j1));

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
                        double d2 = (double) this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double d10 = (1.0D - d0) * d2;

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
                IBlockState rn = this.world.getBlockState(blockpos1);
                Block randomNum = rn.getBlock();

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

                if (rn.getMaterial() != Material.AIR) {
                    if (randomNum.canDropFromExplosion(this)) {
                        randomNum.dropBlockAsItemWithChance(this.world, blockpos1, this.world.getBlockState(blockpos1), 1.0F / this.size, 0);
                    }

                    randomNum.onBlockExploded(this.world, blockpos1, this);
                }
            }
        }

        iterator = this.affectedBlockPositions.iterator();

        while (iterator.hasNext()) {
            blockpos1 = (BlockPos) iterator.next();
            if (this.world.getBlockState(blockpos1).getMaterial() != Material.AIR && this.world.getBlockState(blockpos1).getBlock() != Blocks.BEDROCK) {
                if (this.world.getBlockState(blockpos1).getMaterial() == Material.LEAVES) {
                    this.world.setBlockState(blockpos1, SuperTNTMod.POOP_LEAVES.getDefaultState());
                } else if (this.world.getBlockState(blockpos1).getBlock() != SuperTNTMod.POOP_PLANT && this.world.getBlockState(blockpos1).getBlock() != SuperTNTMod.CRAP_GRASS && this.world.getBlockState(blockpos1).getMaterial() != Material.GLASS) {
                    if (this.world.getBlockState(blockpos1).getMaterial() == Material.WATER) {
                        this.world.setBlockState(blockpos1, SuperTNTMod.BlockLiquidPoop.getDefaultState());
                    } else if (this.world.getBlockState(blockpos1).getMaterial() != Material.AIR && this.world.getBlockState(blockpos1).getMaterial() != Material.LEAVES && this.world.getBlockState(blockpos1).getMaterial() != Material.WATER) {
                        this.world.setBlockState(blockpos1, SuperTNTMod.POOP_BLOCK.getDefaultState());
                        System.out.println(blockpos1);
                        Random rn1 = new Random();
                        int randomNum1 = rn1.nextInt(20);

                        if (randomNum1 >= 18 && this.world.getBlockState(new BlockPos(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ())).getBlock() == SuperTNTMod.POOP_BLOCK && this.world.getBlockState(new BlockPos(blockpos1.getX(), blockpos1.getY() + 1, blockpos1.getZ())).getBlock() == Blocks.AIR) {
                            this.world.setBlockState(new BlockPos(blockpos1.getX(), blockpos1.getY() + 1, blockpos1.getZ()), SuperTNTMod.POOP_PLANT.getDefaultState());
                        } else if (randomNum1 >= 16 && randomNum1 < 18 && this.world.getBlockState(new BlockPos(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ())).getBlock() == SuperTNTMod.POOP_BLOCK && this.world.getBlockState(new BlockPos(blockpos1.getX(), blockpos1.getY() + 1, blockpos1.getZ())).getBlock() == Blocks.AIR) {
                            this.world.setBlockState(new BlockPos(blockpos1.getX(), blockpos1.getY() + 1, blockpos1.getZ()), SuperTNTMod.CRAP_GRASS.getDefaultState());
                        }
                    }
                }
            }
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
