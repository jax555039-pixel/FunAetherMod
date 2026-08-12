package com.jax.funaethermod.world;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.registry.ModBlocks;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(
        modid = FunAetherMod.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class NaturalPortalGenerationHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Random RANDOM = new Random();

    /*
     * =========================================================
     * SETTINGS
     * =========================================================
     */

    /*
     * 1 in 20 loaded chunks will attempt generation. this is the default value. you can change it to 1 for testing.
     *
     * Set this to 1 while testing if you want a portal
     * generated almost immediately.
     */
    private static final int PORTAL_CHANCE = 20;

    /*
     * Natural portals cannot generate within 20 chunks
     * of the world's shared spawn.
     */
    private static final int MIN_SPAWN_DISTANCE_CHUNKS = 20;

    /*
     * Natural portals must be at least 50 chunks away
     * from another natural portal in the same dimension.
     */
    private static final int MIN_PORTAL_DISTANCE_CHUNKS = 50;

    /*
     * =========================================================
     * GENERATED PORTALS
     * =========================================================
     *
     * This stores portals generated during the current server
     * session.
     *
     * Dimension is stored with each portal so portals in
     * different dimensions do not interfere with each other.
     */

    private static final List<GeneratedPortal> GENERATED_PORTALS =
            new ArrayList<>();


    /*
     * =========================================================
     * CHUNK LOAD
     * =========================================================
     */

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {

        /*
         * Server side only.
         */
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        /*
         * Diagnostic message.
         *
         * This confirms that the Forge chunk-load event
         * is reaching this handler.
         */
        LOGGER.info(
                "NATURAL PORTAL HANDLER FIRED: dimension={}, chunk=({}, {})",
                level.dimension().location(),
                event.getChunk().getPos().x,
                event.getChunk().getPos().z
        );

        /*
         * Only handle the Overworld, Aether, and Purgatory.
         */
        if (!isValidDimension(level)) {
            return;
        }

        int chunkX = event.getChunk().getPos().x;
        int chunkZ = event.getChunk().getPos().z;


        /*
         * =====================================================
         * SPAWN DISTANCE
         * =====================================================
         */

        if (isTooCloseToSpawn(
                level,
                chunkX,
                chunkZ,
                MIN_SPAWN_DISTANCE_CHUNKS
        )) {

            LOGGER.info(
                    "Natural portal blocked: chunk ({}, {}) is too close to spawn.",
                    chunkX,
                    chunkZ
            );

            return;
        }


        /*
         * =====================================================
         * PORTAL DISTANCE
         * =====================================================
         */

        if (isTooCloseToAnotherPortal(
                level,
                chunkX,
                chunkZ,
                MIN_PORTAL_DISTANCE_CHUNKS
        )) {

            LOGGER.info(
                    "Natural portal blocked: chunk ({}, {}) is too close to another portal.",
                    chunkX,
                    chunkZ
            );

            return;
        }


        /*
         * =====================================================
         * RANDOM CHANCE
         * =====================================================
         */

        if (RANDOM.nextInt(PORTAL_CHANCE) != 0) {

            return;
        }


        /*
         * =====================================================
         * RESERVE PORTAL LOCATION
         * =====================================================
         *
         * Store it immediately so another chunk-load event
         * cannot schedule another portal too close to it.
         */

        GENERATED_PORTALS.add(
                new GeneratedPortal(
                        level.dimension(),
                        chunkX,
                        chunkZ
                )
        );


        LOGGER.info(
                "Natural portal generation accepted at chunk ({}, {}) in {}",
                chunkX,
                chunkZ,
                level.dimension().location()
        );


        /*
         * Generate one server tick later.
         */
        level.getServer().tell(
                new TickTask(
                        1,
                        () -> generatePortal(
                                level,
                                chunkX,
                                chunkZ
                        )
                )
        );
    }


    /*
     * =========================================================
     * GENERATE PORTAL
     * =========================================================
     */

    private static void generatePortal(
            ServerLevel level,
            int chunkX,
            int chunkZ
    ) {

        /*
         * Center of the chunk.
         */
        BlockPos pos = new BlockPos(
                chunkX * 16 + 8,
                0,
                chunkZ * 16 + 8
        );


        /*
         * Find the surface.
         */
        pos = level.getHeightmapPos(
                Heightmap.Types.WORLD_SURFACE,
                pos
        );


        /*
         * =====================================================
         * AETHER
         * =====================================================
         *
         * Aether naturally generates a Purgatory portal.
         */

        if (isAether(level)) {

            buildPurgatoryPortal(level, pos);

            LOGGER.info(
                    "Natural Purgatory portal generated in Aether at X:{} Y:{} Z:{}",
                    pos.getX(),
                    pos.getY(),
                    pos.getZ()
            );

            return;
        }


        /*
         * =====================================================
         * PURGATORY
         * =====================================================
         *
         * Purgatory naturally generates an Aether portal.
         */

        if (isPurgatory(level)) {

            buildAetherPortal(level, pos);

            LOGGER.info(
                    "Natural Aether portal generated in Purgatory at X:{} Y:{} Z:{}",
                    pos.getX(),
                    pos.getY(),
                    pos.getZ()
            );

            return;
        }


        /*
         * =====================================================
         * OVERWORLD
         * =====================================================
         *
         * Overworld randomly generates either portal.
         */

        if (isOverworld(level)) {

            if (RANDOM.nextBoolean()) {

                buildAetherPortal(level, pos);

                LOGGER.info(
                        "Natural Aether portal generated in Overworld at X:{} Y:{} Z:{}",
                        pos.getX(),
                        pos.getY(),
                        pos.getZ()
                );

            } else {

                buildPurgatoryPortal(level, pos);

                LOGGER.info(
                        "Natural Purgatory portal generated in Overworld at X:{} Y:{} Z:{}",
                        pos.getX(),
                        pos.getY(),
                        pos.getZ()
                );
            }
        }
    }


    /*
     * =========================================================
     * DIMENSION CHECKS
     * =========================================================
     */

    private static boolean isValidDimension(ServerLevel level) {

        return isOverworld(level)
                || isAether(level)
                || isPurgatory(level);
    }


    private static boolean isOverworld(ServerLevel level) {

        return level.dimension().equals(Level.OVERWORLD);
    }


    private static boolean isAether(ServerLevel level) {

        ResourceKey<Level> aether =
                ResourceKey.create(
                        Registries.DIMENSION,
                        new ResourceLocation(
                                FunAetherMod.MODID,
                                "aether"
                        )
                );

        return level.dimension().equals(aether);
    }


    private static boolean isPurgatory(ServerLevel level) {

        ResourceKey<Level> purgatory =
                ResourceKey.create(
                        Registries.DIMENSION,
                        new ResourceLocation(
                                FunAetherMod.MODID,
                                "purgatory"
                        )
                );

        return level.dimension().equals(purgatory);
    }


    /*
     * =========================================================
     * SPAWN DISTANCE
     * =========================================================
     *
     * Uses real radial chunk distance.
     *
     * Example:
     *
     * 20 chunks away = allowed boundary
     * less than 20 = blocked
     */

    private static boolean isTooCloseToSpawn(
            ServerLevel level,
            int chunkX,
            int chunkZ,
            int minimumDistance
    ) {

        BlockPos spawn =
                level.getSharedSpawnPos();

        int spawnChunkX =
                spawn.getX() >> 4;

        int spawnChunkZ =
                spawn.getZ() >> 4;


        int distanceX =
                chunkX - spawnChunkX;

        int distanceZ =
                chunkZ - spawnChunkZ;


        double distance =
                Math.sqrt(
                        (double) distanceX * distanceX
                                +
                        (double) distanceZ * distanceZ
                );


        return distance < minimumDistance;
    }


    /*
     * =========================================================
     * PORTAL DISTANCE
     * =========================================================
     *
     * Uses real radial chunk distance.
     *
     * A portal must be at least 50 chunks away from another
     * natural portal in the same dimension.
     */

    private static boolean isTooCloseToAnotherPortal(
            ServerLevel level,
            int chunkX,
            int chunkZ,
            int minimumDistance
    ) {

        for (GeneratedPortal portal : GENERATED_PORTALS) {

            /*
             * Only compare portals in the same dimension.
             */
            if (!portal.dimension().equals(
                    level.dimension()
            )) {
                continue;
            }


            int distanceX =
                    chunkX - portal.chunkX();

            int distanceZ =
                    chunkZ - portal.chunkZ();


            double distance =
                    Math.sqrt(
                            (double) distanceX * distanceX
                                    +
                            (double) distanceZ * distanceZ
                    );


            if (distance < minimumDistance) {

                return true;
            }
        }

        return false;
    }


    /*
     * =========================================================
     * BUILD AETHER PORTAL
     * =========================================================
     */

    private static void buildAetherPortal(
            ServerLevel level,
            BlockPos pos
    ) {

        BlockState frame =
                ModBlocks.AETHER_PORTAL_FRAME
                        .get()
                        .defaultBlockState();

        BlockState portal =
                ModBlocks.AETHER_PORTAL
                        .get()
                        .defaultBlockState();


        for (int x = -2; x <= 2; x++) {

            for (int y = 0; y <= 4; y++) {

                BlockPos place =
                        pos.offset(
                                x,
                                y,
                                0
                        );


                boolean edge =
                        x == -2
                                || x == 2
                                || y == 0
                                || y == 4;


                level.setBlock(
                        place,
                        edge ? frame : portal,
                        3
                );
            }
        }
    }


    /*
     * =========================================================
     * BUILD PURGATORY PORTAL
     * =========================================================
     */

    private static void buildPurgatoryPortal(
            ServerLevel level,
            BlockPos pos
    ) {

        BlockState portal =
                ModBlocks.PURGATORY_PORTAL
                        .get()
                        .defaultBlockState();


        for (int y = 0; y < 50; y++) {

            BlockPos place =
                    pos.above(y);

            level.setBlock(
                    place,
                    portal,
                    3
            );
        }
    }


    /*
     * =========================================================
     * PORTAL RECORD
     * =========================================================
     */

    private record GeneratedPortal(
            ResourceKey<Level> dimension,
            int chunkX,
            int chunkZ
    ) {
    }
}