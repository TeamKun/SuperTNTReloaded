//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.mcreator.waterworld.world.dimension;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.mcreator.waterworld.WaterworldModElements;
import net.mcreator.waterworld.WaterworldModElements.ModElement;
import net.mcreator.waterworld.WaterworldModElements.ModElement.Tag;
import net.mcreator.waterworld.item.WaterworldItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPattern.PatternHelper;
import net.minecraft.block.pattern.BlockPattern.PortalInfo;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.village.PointOfInterestManager.Status;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.OverworldChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.GenerationStage.Carving;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.layer.IslandLayer;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.common.extensions.IForgeDimension.SleepResult;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@Tag
public class WaterworldDimension extends ModElement {
    @ObjectHolder("waterworld:waterworld")
    public static final ModDimension dimension = null;
    @ObjectHolder("waterworld:waterworld_portal")
    public static final WaterworldDimension.CustomPortalBlock portal = null;
    public static DimensionType type = null;
    private static Biome[] dimensionBiomes;
    private static PointOfInterestType poi = null;
    public static final TicketType<BlockPos> CUSTOM_PORTAL = TicketType.create("waterworld_portal", Vec3i::compareTo, 300);

    public WaterworldDimension(WaterworldModElements instance) {
        super(instance, 1);
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void registerDimension(Register<ModDimension> event) {
        event.getRegistry().register((new WaterworldDimension.CustomModDimension()).setRegistryName("waterworld"));
    }

    @SubscribeEvent
    public void onRegisterDimensionsEvent(RegisterDimensionsEvent event) {
        if (DimensionType.byName(new ResourceLocation("waterworld:waterworld")) == null) {
            DimensionManager.registerDimension(new ResourceLocation("waterworld:waterworld"), dimension, (PacketBuffer)null, true);
        }

        type = DimensionType.byName(new ResourceLocation("waterworld:waterworld"));
    }

    public void init(FMLCommonSetupEvent event) {
        dimensionBiomes = new Biome[]{(Biome)ForgeRegistries.BIOMES.getValue(new ResourceLocation("ocean")), (Biome)ForgeRegistries.BIOMES.getValue(new ResourceLocation("frozen_ocean")), (Biome)ForgeRegistries.BIOMES.getValue(new ResourceLocation("deep_ocean")), (Biome)ForgeRegistries.BIOMES.getValue(new ResourceLocation("warm_ocean")), (Biome)ForgeRegistries.BIOMES.getValue(new ResourceLocation("lukewarm_ocean")), (Biome)ForgeRegistries.BIOMES.getValue(new ResourceLocation("cold_ocean")), (Biome)ForgeRegistries.BIOMES.getValue(new ResourceLocation("deep_warm_ocean")), (Biome)ForgeRegistries.BIOMES.getValue(new ResourceLocation("deep_lukewarm_ocean")), (Biome)ForgeRegistries.BIOMES.getValue(new ResourceLocation("deep_cold_ocean")), (Biome)ForgeRegistries.BIOMES.getValue(new ResourceLocation("deep_frozen_ocean"))};
    }

    public void initElements() {
        this.elements.blocks.add(() -> {
            return new WaterworldDimension.CustomPortalBlock();
        });
        this.elements.items.add(() -> {
            return (Item)(new WaterworldItem()).setRegistryName("waterworld");
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void clientLoad(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(portal, RenderType.getTranslucent());
    }

    @SubscribeEvent
    public void registerPointOfInterest(Register<PointOfInterestType> event) {
        try {
            Method method = ObfuscationReflectionHelper.findMethod(PointOfInterestType.class, "func_226359_a_", new Class[]{String.class, Set.class, Integer.TYPE, Integer.TYPE});
            method.setAccessible(true);
            poi = (PointOfInterestType)method.invoke((Object)null, "waterworld_portal", Sets.newHashSet(ImmutableSet.copyOf(portal.getStateContainer().getValidStates())), 0, 1);
            event.getRegistry().register(poi);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static class BiomeProviderCustom extends BiomeProvider {
        private Layer genBiomes;
        private static boolean biomesPatched = false;

        public BiomeProviderCustom(World world) {
            super(new HashSet(Arrays.asList(WaterworldDimension.dimensionBiomes)));
            this.genBiomes = this.getBiomeLayer(world.getSeed());
            if (!biomesPatched) {
                Iterator var2 = this.biomes.iterator();

                while(var2.hasNext()) {
                    final Biome biome = (Biome)var2.next();
                    biome.addCarver(Carving.AIR, Biome.createCarver(new CaveWorldCarver(ProbabilityConfig::deserialize, 256) {
                        {
                            this.carvableBlocks = ImmutableSet.of(Blocks.STONE.getDefaultState().getBlock(), biome.getSurfaceBuilder().getConfig().getTop().getBlock(), biome.getSurfaceBuilder().getConfig().getUnder().getBlock());
                        }
                    }, new ProbabilityConfig(0.14285715F)));
                }

                biomesPatched = true;
            }

        }

        public Biome getNoiseBiome(int x, int y, int z) {
            return this.genBiomes.func_215738_a(x, z);
        }

        private Layer getBiomeLayer(long seed) {
            LongFunction<IExtendedNoiseRandom<LazyArea>> contextFactory = (l) -> {
                return new LazyAreaLayerContext(25, seed, l);
            };
            IAreaFactory<LazyArea> parentLayer = IslandLayer.INSTANCE.apply((IExtendedNoiseRandom)contextFactory.apply(1L));
            IAreaFactory<LazyArea> biomeLayer = (new WaterworldDimension.BiomeLayerCustom()).apply((IExtendedNoiseRandom)contextFactory.apply(200L), parentLayer);
            biomeLayer = ZoomLayer.NORMAL.apply((IExtendedNoiseRandom)contextFactory.apply(1000L), biomeLayer);
            biomeLayer = ZoomLayer.NORMAL.apply((IExtendedNoiseRandom)contextFactory.apply(1001L), biomeLayer);
            biomeLayer = ZoomLayer.NORMAL.apply((IExtendedNoiseRandom)contextFactory.apply(1002L), biomeLayer);
            biomeLayer = ZoomLayer.NORMAL.apply((IExtendedNoiseRandom)contextFactory.apply(1003L), biomeLayer);
            biomeLayer = ZoomLayer.NORMAL.apply((IExtendedNoiseRandom)contextFactory.apply(1004L), biomeLayer);
            biomeLayer = ZoomLayer.NORMAL.apply((IExtendedNoiseRandom)contextFactory.apply(1005L), biomeLayer);
            return new Layer(biomeLayer);
        }
    }

    public static class BiomeLayerCustom implements IC0Transformer {
        public BiomeLayerCustom() {
        }

        public int apply(INoiseRandom context, int value) {
            return Registry.BIOME.getId(WaterworldDimension.dimensionBiomes[context.random(WaterworldDimension.dimensionBiomes.length)]);
        }
    }

    public static class ChunkProviderModded extends OverworldChunkGenerator {
        public ChunkProviderModded(IWorld world, BiomeProvider provider) {
            super(world, provider, new OverworldGenSettings() {
                public BlockState getDefaultBlock() {
                    return Blocks.STONE.getDefaultState();
                }

                public BlockState getDefaultFluid() {
                    return Blocks.WATER.getDefaultState();
                }
            });
            this.randomSeed.skip(5349);
        }

        @Override
        public int getSeaLevel() {
            return 200;
        }

        public void spawnMobs(ServerWorld worldIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs) {
        }
    }

    public static class CustomDimension extends Dimension {
        private WaterworldDimension.BiomeProviderCustom biomeProviderCustom = null;

        public CustomDimension(World world, DimensionType type) {
            super(world, type, 0.5F);
            this.nether = false;
        }

        @Override
        public int getSeaLevel() {
            return 200;
        }

        @OnlyIn(Dist.CLIENT)
        public Vec3d getFogColor(float cangle, float ticks) {
            return new Vec3d(0.752941176471D, 0.847058823529D, 1.0D);
        }

        public ChunkGenerator<?> createChunkGenerator() {
            if (this.biomeProviderCustom == null) {
                this.biomeProviderCustom = new WaterworldDimension.BiomeProviderCustom(this.world);
            }

            return new WaterworldDimension.ChunkProviderModded(this.world, this.biomeProviderCustom);
        }

        public boolean isSurfaceWorld() {
            return true;
        }

        public boolean canRespawnHere() {
            return true;
        }

        @OnlyIn(Dist.CLIENT)
        public boolean doesXZShowFog(int x, int z) {
            return false;
        }

        public SleepResult canSleepAt(PlayerEntity player, BlockPos pos) {
            return SleepResult.ALLOW;
        }

        @Nullable
        public BlockPos findSpawn(ChunkPos chunkPos, boolean checkValid) {
            return null;
        }

        @Nullable
        public BlockPos findSpawn(int x, int z, boolean checkValid) {
            return null;
        }

        public boolean doesWaterVaporize() {
            return false;
        }

        public float calculateCelestialAngle(long worldTime, float partialTicks) {
            double d0 = MathHelper.frac((double)worldTime / 24000.0D - 0.25D);
            double d1 = 0.5D - Math.cos(d0 * 3.141592653589793D) / 2.0D;
            return (float)(d0 * 2.0D + d1) / 3.0F;
        }
    }

    public static class CustomModDimension extends ModDimension {
        public CustomModDimension() {
        }

        public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
            return WaterworldDimension.CustomDimension::new;
        }
    }

    public static class TeleporterDimensionMod implements ITeleporter {
        private Vec3d lastPortalVec;
        private Direction teleportDirection;
        protected final ServerWorld world;
        protected final Random random;

        public TeleporterDimensionMod(ServerWorld worldServer, Vec3d lastPortalVec, Direction teleportDirection) {
            this.world = worldServer;
            this.random = new Random(worldServer.getSeed());
            this.lastPortalVec = lastPortalVec;
            this.teleportDirection = teleportDirection;
        }

        @Nullable
        public PortalInfo placeInExistingPortal(BlockPos p_222272_1_, Vec3d p_222272_2_, Direction directionIn, double p_222272_4_, double p_222272_6_, boolean p_222272_8_) {
            PointOfInterestManager pointofinterestmanager = this.world.getPointOfInterestManager();
            pointofinterestmanager.ensureLoadedAndValid(this.world, p_222272_1_, 128);
            List<PointOfInterest> list = pointofinterestmanager.getInSquare((p_226705_0_) -> {
                return p_226705_0_ == WaterworldDimension.poi;
            }, p_222272_1_, 128, Status.ANY).collect(Collectors.toList());
            Optional<PointOfInterest> optional = list.stream().min(Comparator.<PointOfInterest>comparingDouble((p_226706_1_) -> {
                return p_226706_1_.getPos().distanceSq(p_222272_1_);
            }).thenComparingInt((p_226704_0_) -> {
                return p_226704_0_.getPos().getY();
            }));
            return (PortalInfo)optional.map((p_226707_7_) -> {
                BlockPos blockpos = p_226707_7_.getPos();
                this.world.getChunkProvider().registerTicket(WaterworldDimension.CUSTOM_PORTAL, new ChunkPos(blockpos), 3, blockpos);
                PatternHelper blockpattern$patternhelper = WaterworldDimension.CustomPortalBlock.createPatternHelper(this.world, blockpos);
                return blockpattern$patternhelper.getPortalInfo(directionIn, blockpos, p_222272_6_, p_222272_2_, p_222272_4_);
            }).orElse((PortalInfo)null);
        }

        public boolean placeInPortal(Entity p_222268_1_, float p_222268_2_) {
            Vec3d vec3d = this.lastPortalVec;
            Direction direction = this.teleportDirection;
            PortalInfo blockpattern$portalinfo = this.placeInExistingPortal(new BlockPos(p_222268_1_), p_222268_1_.getMotion(), direction, vec3d.x, vec3d.y, p_222268_1_ instanceof PlayerEntity);
            if (blockpattern$portalinfo == null) {
                return false;
            } else {
                Vec3d vec3d1 = blockpattern$portalinfo.pos;
                Vec3d vec3d2 = blockpattern$portalinfo.motion;
                p_222268_1_.setMotion(vec3d2);
                p_222268_1_.rotationYaw = p_222268_2_ + (float)blockpattern$portalinfo.rotation;
                p_222268_1_.moveForced(vec3d1.x, vec3d1.y, vec3d1.z);
                return true;
            }
        }

        public boolean makePortal(Entity entityIn) {
            boolean i = true;
            double d0 = -1.0D;
            int j = MathHelper.floor(entityIn.getPosX());
            int k = MathHelper.floor(entityIn.getPosY());
            int l = MathHelper.floor(entityIn.getPosZ());
            int i1 = j;
            int j1 = k;
            int k1 = l;
            int l1 = 0;
            int i2 = this.random.nextInt(4);
            Mutable blockpos$mutable = new Mutable();

            int l5;
            double d3;
            int j6;
            double d4;
            int i7;
            int l7;
            int l8;
            int k9;
            int i10;
            int k10;
            int i11;
            int j11;
            int k11;
            double d6;
            double d8;
            for(l5 = j - 16; l5 <= j + 16; ++l5) {
                d3 = (double)l5 + 0.5D - entityIn.getPosX();

                for(j6 = l - 16; j6 <= l + 16; ++j6) {
                    d4 = (double)j6 + 0.5D - entityIn.getPosZ();

                    label274:
                    for(i7 = this.world.getActualHeight() - 1; i7 >= 0; --i7) {
                        if (this.world.isAirBlock(blockpos$mutable.setPos(l5, i7, j6))) {
                            while(i7 > 0 && this.world.isAirBlock(blockpos$mutable.setPos(l5, i7 - 1, j6))) {
                                --i7;
                            }

                            for(l7 = i2; l7 < i2 + 4; ++l7) {
                                l8 = l7 % 2;
                                k9 = 1 - l8;
                                if (l7 % 4 >= 2) {
                                    l8 = -l8;
                                    k9 = -k9;
                                }

                                for(i10 = 0; i10 < 3; ++i10) {
                                    for(k10 = 0; k10 < 4; ++k10) {
                                        for(i11 = -1; i11 < 4; ++i11) {
                                            j11 = l5 + (k10 - 1) * l8 + i10 * k9;
                                            k11 = i7 + i11;
                                            int k5 = j6 + (k10 - 1) * k9 - i10 * l8;
                                            blockpos$mutable.setPos(j11, k11, k5);
                                            if (i11 < 0 && !this.world.getBlockState(blockpos$mutable).getMaterial().isSolid() || i11 >= 0 && !this.world.isAirBlock(blockpos$mutable)) {
                                                continue label274;
                                            }
                                        }
                                    }
                                }

                                d6 = (double)i7 + 0.5D - entityIn.getPosY();
                                d8 = d3 * d3 + d6 * d6 + d4 * d4;
                                if (d0 < 0.0D || d8 < d0) {
                                    d0 = d8;
                                    i1 = l5;
                                    j1 = i7;
                                    k1 = j6;
                                    l1 = l7 % 4;
                                }
                            }
                        }
                    }
                }
            }

            if (d0 < 0.0D) {
                for(l5 = j - 16; l5 <= j + 16; ++l5) {
                    d3 = (double)l5 + 0.5D - entityIn.getPosX();

                    for(j6 = l - 16; j6 <= l + 16; ++j6) {
                        d4 = (double)j6 + 0.5D - entityIn.getPosZ();

                        label212:
                        for(i7 = this.world.getActualHeight() - 1; i7 >= 0; --i7) {
                            if (this.world.isAirBlock(blockpos$mutable.setPos(l5, i7, j6))) {
                                while(i7 > 0 && this.world.isAirBlock(blockpos$mutable.setPos(l5, i7 - 1, j6))) {
                                    --i7;
                                }

                                for(l7 = i2; l7 < i2 + 2; ++l7) {
                                    l8 = l7 % 2;
                                    k9 = 1 - l8;

                                    for(i10 = 0; i10 < 4; ++i10) {
                                        for(k10 = -1; k10 < 4; ++k10) {
                                            i11 = l5 + (i10 - 1) * l8;
                                            j11 = i7 + k10;
                                            k11 = j6 + (i10 - 1) * k9;
                                            blockpos$mutable.setPos(i11, j11, k11);
                                            if (k10 < 0 && !this.world.getBlockState(blockpos$mutable).getMaterial().isSolid() || k10 >= 0 && !this.world.isAirBlock(blockpos$mutable)) {
                                                continue label212;
                                            }
                                        }
                                    }

                                    d6 = (double)i7 + 0.5D - entityIn.getPosY();
                                    d8 = d3 * d3 + d6 * d6 + d4 * d4;
                                    if (d0 < 0.0D || d8 < d0) {
                                        d0 = d8;
                                        i1 = l5;
                                        j1 = i7;
                                        k1 = j6;
                                        l1 = l7 % 2;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            l5 = i1;
            int k2 = j1;
            int k6 = k1;
            j6 = l1 % 2;
            int i3 = 1 - j6;
            if (l1 % 4 >= 2) {
                j6 = -j6;
                i3 = -i3;
            }

            int j7;
            if (d0 < 0.0D) {
                j1 = MathHelper.clamp(j1, 70, this.world.getActualHeight() - 10);
                k2 = j1;

                for(j7 = -1; j7 <= 1; ++j7) {
                    for(i7 = 1; i7 < 3; ++i7) {
                        for(l7 = -1; l7 < 3; ++l7) {
                            l8 = l5 + (i7 - 1) * j6 + j7 * i3;
                            k9 = k2 + l7;
                            i10 = k6 + (i7 - 1) * i3 - j7 * j6;
                            boolean flag = l7 < 0;
                            blockpos$mutable.setPos(l8, k9, i10);
                            this.world.setBlockState(blockpos$mutable, flag ? Blocks.PRISMARINE_BRICKS.getDefaultState().getBlock().getDefaultState() : Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }

            for(j7 = -1; j7 < 3; ++j7) {
                for(i7 = -1; i7 < 4; ++i7) {
                    if (j7 == -1 || j7 == 2 || i7 == -1 || i7 == 3) {
                        blockpos$mutable.setPos(l5 + j7 * j6, k2 + i7, k6 + j7 * i3);
                        this.world.setBlockState(blockpos$mutable, Blocks.PRISMARINE_BRICKS.getDefaultState().getBlock().getDefaultState(), 3);
                    }
                }
            }

            BlockState blockstate = (BlockState)WaterworldDimension.portal.getDefaultState().with(NetherPortalBlock.AXIS, j6 == 0 ? Axis.Z : Axis.X);

            for(i7 = 0; i7 < 2; ++i7) {
                for(l7 = 0; l7 < 3; ++l7) {
                    blockpos$mutable.setPos(l5 + i7 * j6, k2 + l7, k6 + i7 * i3);
                    this.world.setBlockState(blockpos$mutable, blockstate, 18);
                    this.world.getPointOfInterestManager().add(blockpos$mutable, WaterworldDimension.poi);
                }
            }

            return true;
        }

        public Entity placeEntity(Entity entity, ServerWorld serverworld, ServerWorld serverworld1, float yaw, Function<Boolean, Entity> repositionEntity) {
            double d0 = entity.getPosX();
            double d1 = entity.getPosY();
            double d2 = entity.getPosZ();
            if (entity instanceof ServerPlayerEntity) {
                entity.setLocationAndAngles(d0, d1, d2, yaw, entity.rotationPitch);
                if (!this.placeInPortal(entity, yaw)) {
                    this.makePortal(entity);
                    this.placeInPortal(entity, yaw);
                }

                entity.setWorld(serverworld1);
                serverworld1.addDuringPortalTeleport((ServerPlayerEntity)entity);
                ((ServerPlayerEntity)entity).connection.setPlayerLocation(entity.getPosX(), entity.getPosY(), entity.getPosZ(), yaw, entity.rotationPitch);
                return entity;
            } else {
                Vec3d vec3d = entity.getMotion();
                BlockPos blockpos = new BlockPos(d0, d1, d2);
                PortalInfo blockpattern$portalinfo = this.placeInExistingPortal(blockpos, vec3d, this.teleportDirection, this.lastPortalVec.x, this.lastPortalVec.y, entity instanceof PlayerEntity);
                if (blockpattern$portalinfo == null) {
                    return null;
                } else {
                    blockpos = new BlockPos(blockpattern$portalinfo.pos);
                    vec3d = blockpattern$portalinfo.motion;
                    float f = (float)blockpattern$portalinfo.rotation;
                    Entity entityNew = entity.getType().create(serverworld1);
                    if (entityNew != null) {
                        entityNew.copyDataFromOld(entity);
                        entityNew.moveToBlockPosAndAngles(blockpos, entityNew.rotationYaw + f, entityNew.rotationPitch);
                        entityNew.setMotion(vec3d);
                        serverworld1.addFromAnotherDimension(entityNew);
                    }

                    return entityNew;
                }
            }
        }
    }

    public static class CustomPortalBlock extends NetherPortalBlock {
        public CustomPortalBlock() {
            super(Properties.create(Material.PORTAL).doesNotBlockMovement().tickRandomly().hardnessAndResistance(-1.0F).sound(SoundType.GLASS).lightValue(15).noDrops());
            this.setRegistryName("waterworld_portal");
        }

        public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        }

        public void portalSpawn(World world, BlockPos pos) {
            WaterworldDimension.CustomPortalBlock.Size portalsize = this.isValid(world, pos);
            if (portalsize != null) {
                portalsize.placePortalBlocks();
            }

        }

        @Nullable
        public WaterworldDimension.CustomPortalBlock.Size isValid(IWorld worldIn, BlockPos pos) {
            WaterworldDimension.CustomPortalBlock.Size netherportalblock$size = new WaterworldDimension.CustomPortalBlock.Size(worldIn, pos, Axis.X);
            if (netherportalblock$size.isValid() && netherportalblock$size.portalBlockCount == 0) {
                return netherportalblock$size;
            } else {
                WaterworldDimension.CustomPortalBlock.Size netherportalblock$size1 = new WaterworldDimension.CustomPortalBlock.Size(worldIn, pos, Axis.Z);
                return netherportalblock$size1.isValid() && netherportalblock$size1.portalBlockCount == 0 ? netherportalblock$size1 : null;
            }
        }

        public static PatternHelper createPatternHelper(IWorld p_181089_0_, BlockPos worldIn) {
            Axis direction$axis = Axis.Z;
            WaterworldDimension.CustomPortalBlock.Size netherportalblock$size = new WaterworldDimension.CustomPortalBlock.Size(p_181089_0_, worldIn, Axis.X);
            LoadingCache<BlockPos, CachedBlockInfo> loadingcache = BlockPattern.createLoadingCache(p_181089_0_, true);
            if (!netherportalblock$size.isValid()) {
                direction$axis = Axis.X;
                netherportalblock$size = new WaterworldDimension.CustomPortalBlock.Size(p_181089_0_, worldIn, Axis.Z);
            }

            if (!netherportalblock$size.isValid()) {
                return new PatternHelper(worldIn, Direction.NORTH, Direction.UP, loadingcache, 1, 1, 1);
            } else {
                int[] aint = new int[AxisDirection.values().length];
                Direction direction = netherportalblock$size.rightDir.rotateYCCW();
                BlockPos blockpos = netherportalblock$size.bottomLeft.up(netherportalblock$size.getHeight() - 1);
                AxisDirection[] var8 = AxisDirection.values();
                int var9 = var8.length;

                int var10;
                for(var10 = 0; var10 < var9; ++var10) {
                    AxisDirection direction$axisdirection = var8[var10];
                    PatternHelper blockpattern$patternhelper = new PatternHelper(direction.getAxisDirection() == direction$axisdirection ? blockpos : blockpos.offset(netherportalblock$size.rightDir, netherportalblock$size.getWidth() - 1), Direction.getFacingFromAxis(direction$axisdirection, direction$axis), Direction.UP, loadingcache, netherportalblock$size.getWidth(), netherportalblock$size.getHeight(), 1);

                    for(int i = 0; i < netherportalblock$size.getWidth(); ++i) {
                        for(int j = 0; j < netherportalblock$size.getHeight(); ++j) {
                            CachedBlockInfo cachedblockinfo = blockpattern$patternhelper.translateOffset(i, j, 1);
                            if (!cachedblockinfo.getBlockState().isAir()) {
                                ++aint[direction$axisdirection.ordinal()];
                            }
                        }
                    }
                }

                AxisDirection direction$axisdirection1 = AxisDirection.POSITIVE;
                AxisDirection[] var17 = AxisDirection.values();
                var10 = var17.length;

                for(int var18 = 0; var18 < var10; ++var18) {
                    AxisDirection direction$axisdirection2 = var17[var18];
                    if (aint[direction$axisdirection2.ordinal()] < aint[direction$axisdirection1.ordinal()]) {
                        direction$axisdirection1 = direction$axisdirection2;
                    }
                }

                return new PatternHelper(direction.getAxisDirection() == direction$axisdirection1 ? blockpos : blockpos.offset(netherportalblock$size.rightDir, netherportalblock$size.getWidth() - 1), Direction.getFacingFromAxis(direction$axisdirection1, direction$axis), Direction.UP, loadingcache, netherportalblock$size.getWidth(), netherportalblock$size.getHeight(), 1);
            }
        }

        public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
            Axis direction$axis = facing.getAxis();
            Axis direction$axis1 = (Axis)stateIn.get(AXIS);
            boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
            return !flag && facingState.getBlock() != this && !(new WaterworldDimension.CustomPortalBlock.Size(worldIn, currentPos, direction$axis1)).func_208508_f() ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }

        @OnlyIn(Dist.CLIENT)
        public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
            for(int i = 0; i < 4; ++i) {
                double px = (double)((float)pos.getX() + random.nextFloat());
                double py = (double)((float)pos.getY() + random.nextFloat());
                double pz = (double)((float)pos.getZ() + random.nextFloat());
                double vx = ((double)random.nextFloat() - 0.5D) / 2.0D;
                double vy = ((double)random.nextFloat() - 0.5D) / 2.0D;
                double vz = ((double)random.nextFloat() - 0.5D) / 2.0D;
                int j = random.nextInt(4) - 1;
                if (world.getBlockState(pos.west()).getBlock() != this && world.getBlockState(pos.east()).getBlock() != this) {
                    px = (double)pos.getX() + 0.5D + 0.25D * (double)j;
                    vx = (double)(random.nextFloat() * 2.0F * (float)j);
                } else {
                    pz = (double)pos.getZ() + 0.5D + 0.25D * (double)j;
                    vz = (double)(random.nextFloat() * 2.0F * (float)j);
                }

                world.addParticle(ParticleTypes.PORTAL, px, py, pz, vx, vy, vz);
            }

            if (random.nextInt(110) == 0) {
                world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.portal.ambient")), SoundCategory.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
            }

        }

        public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
            if (!entity.isPassenger() && !entity.isBeingRidden() && entity.isNonBoss() && !entity.world.isRemote) {
                if (entity.timeUntilPortal > 0) {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                } else if (entity.dimension != WaterworldDimension.type) {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                    this.teleportToDimension(entity, pos, WaterworldDimension.type);
                } else {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                    this.teleportToDimension(entity, pos, DimensionType.OVERWORLD);
                }
            }

        }

        private void teleportToDimension(Entity entity, BlockPos pos, DimensionType destinationType) {
            entity.changeDimension(destinationType, this.getTeleporterForDimension(entity, pos, entity.getServer().getWorld(destinationType)));
        }

        private WaterworldDimension.TeleporterDimensionMod getTeleporterForDimension(Entity entity, BlockPos pos, ServerWorld nextWorld) {
            PatternHelper bph = createPatternHelper(entity.world, pos);
            double d0 = bph.getForwards().getAxis() == Axis.X ? (double)bph.getFrontTopLeft().getZ() : (double)bph.getFrontTopLeft().getX();
            double d1 = bph.getForwards().getAxis() == Axis.X ? entity.getPosZ() : entity.getPosX();
            d1 = Math.abs(MathHelper.pct(d1 - (double)(bph.getForwards().rotateY().getAxisDirection() == AxisDirection.NEGATIVE ? 1 : 0), d0, d0 - (double)bph.getWidth()));
            double d2 = MathHelper.pct(entity.getPosY() - 1.0D, (double)bph.getFrontTopLeft().getY(), (double)(bph.getFrontTopLeft().getY() - bph.getHeight()));
            return new WaterworldDimension.TeleporterDimensionMod(nextWorld, new Vec3d(d1, d2, 0.0D), bph.getForwards());
        }

        public static class Size {
            private final IWorld world;
            private final Axis axis;
            private final Direction rightDir;
            private final Direction leftDir;
            private int portalBlockCount;
            @Nullable
            private BlockPos bottomLeft;
            private int height;
            private int width;

            public Size(IWorld worldIn, BlockPos pos, Axis axisIn) {
                this.world = worldIn;
                this.axis = axisIn;
                if (axisIn == Axis.X) {
                    this.leftDir = Direction.EAST;
                    this.rightDir = Direction.WEST;
                } else {
                    this.leftDir = Direction.NORTH;
                    this.rightDir = Direction.SOUTH;
                }

                for(BlockPos blockpos = pos; pos.getY() > blockpos.getY() - 21 && pos.getY() > 0 && this.func_196900_a(worldIn.getBlockState(pos.down())); pos = pos.down()) {
                }

                int i = this.getDistanceUntilEdge(pos, this.leftDir) - 1;
                if (i >= 0) {
                    this.bottomLeft = pos.offset(this.leftDir, i);
                    this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);
                    if (this.width < 2 || this.width > 21) {
                        this.bottomLeft = null;
                        this.width = 0;
                    }
                }

                if (this.bottomLeft != null) {
                    this.height = this.calculatePortalHeight();
                }

            }

            protected int getDistanceUntilEdge(BlockPos pos, Direction directionIn) {
                int i;
                BlockPos blockpos;
                for(i = 0; i < 22; ++i) {
                    blockpos = pos.offset(directionIn, i);
                    if (!this.func_196900_a(this.world.getBlockState(blockpos)) || this.world.getBlockState(blockpos.down()).getBlock() != Blocks.PRISMARINE_BRICKS.getDefaultState().getBlock()) {
                        break;
                    }
                }

                blockpos = pos.offset(directionIn, i);
                return this.world.getBlockState(blockpos).getBlock() == Blocks.PRISMARINE_BRICKS.getDefaultState().getBlock() ? i : 0;
            }

            public int getHeight() {
                return this.height;
            }

            public int getWidth() {
                return this.width;
            }

            protected int calculatePortalHeight() {
                int i;
                BlockPos blockpos;
                label57:
                for(this.height = 0; this.height < 21; ++this.height) {
                    for(i = 0; i < this.width; ++i) {
                        blockpos = this.bottomLeft.offset(this.rightDir, i).up(this.height);
                        BlockState blockstate = this.world.getBlockState(blockpos);
                        if (!this.func_196900_a(blockstate)) {
                            break label57;
                        }

                        Block block = blockstate.getBlock();
                        if (block == WaterworldDimension.portal) {
                            ++this.portalBlockCount;
                        }

                        BlockPos framePos;
                        if (i == 0) {
                            framePos = blockpos.offset(this.leftDir);
                            if (this.world.getBlockState(framePos).getBlock() != Blocks.PRISMARINE_BRICKS.getDefaultState().getBlock()) {
                                break label57;
                            }
                        } else if (i == this.width - 1) {
                            framePos = blockpos.offset(this.rightDir);
                            if (this.world.getBlockState(framePos).getBlock() != Blocks.PRISMARINE_BRICKS.getDefaultState().getBlock()) {
                                break label57;
                            }
                        }
                    }
                }

                for(i = 0; i < this.width; ++i) {
                    blockpos = this.bottomLeft.offset(this.rightDir, i).up(this.height);
                    if (this.world.getBlockState(blockpos).getBlock() != Blocks.PRISMARINE_BRICKS.getDefaultState().getBlock()) {
                        this.height = 0;
                        break;
                    }
                }

                if (this.height <= 21 && this.height >= 3) {
                    return this.height;
                } else {
                    this.bottomLeft = null;
                    this.width = 0;
                    this.height = 0;
                    return 0;
                }
            }

            protected boolean func_196900_a(BlockState pos) {
                Block block = pos.getBlock();
                return pos.isAir() || block == Blocks.FIRE || block == WaterworldDimension.portal;
            }

            public boolean isValid() {
                return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
            }

            public void placePortalBlocks() {
                for(int i = 0; i < this.width; ++i) {
                    BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i);

                    for(int j = 0; j < this.height; ++j) {
                        this.world.setBlockState(blockpos.up(j), (BlockState)WaterworldDimension.portal.getDefaultState().with(NetherPortalBlock.AXIS, this.axis), 18);
                    }
                }

            }

            private boolean func_196899_f() {
                return this.portalBlockCount >= this.width * this.height;
            }

            public boolean func_208508_f() {
                return this.isValid() && this.func_196899_f();
            }
        }
    }
}
