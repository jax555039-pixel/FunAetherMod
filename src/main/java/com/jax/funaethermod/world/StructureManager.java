
package com.jax.funaethermod.world;

import java.util.Map;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.util.StructureLoader;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class StructureManager {

    private static final int X_SPACING = 15;
    private static final int Y_SPACING = 48;
    private static final int Z_SPACING = 15;

    private static final int START_Y = 100;

    private static final int PATTERN_SIZE = 5;

    private static final int SPAWN_EXCLUSION_CHUNKS = 20;
    private static final int MAX_SPAWN_DISTANCE_CHUNKS = 150;
    private static final int MIN_STRUCTURE_DISTANCE_CHUNKS = 12;
    private static final int MAX_PLACEMENT_ATTEMPTS = 5000;

    // HAVEN
    private static final String HAVEN_ISLAND = "haven_island";

    // CHAPEL
    private static final String CHAPEL_STRUCTURE = "chapel";

    // SD3-HELL
    private static final String HELL_STRCUTURE = "hell";

    // SUBSEQUENCE
    private static final String NORMAL_ISLAND = "subsequence_island";
    private static final String CORRUPTED_ISLAND = "subsequence_corrupted_island";
    private static final String PORTAL_ISLAND = "subsequence_portal_island";

    // OVERWORLD
    private static final String SPAWN_STRUCTURE = "spawn";
    private static final String COOLBOY_HOUSE = "coolboy_house";
    private static final String REALNESS_TOWER = "realness_tower";
    private static final String TRICK_HOUSE = "trick_house";

    private static final ResourceLocation HAVEN_DIMENSION =
            new ResourceLocation(FunAetherMod.MODID, "haven");

    private static final ResourceLocation CHAPEL_DIMENSION =
            new ResourceLocation(FunAetherMod.MODID, "chapel"); 
    private static final ResourceLocation HELL_DIMENSION =
            new ResourceLocation(FunAetherMod.MODID, "sd3-hell");

    private static final ResourceLocation SUBSEQUENCE_DIMENSION =
            new ResourceLocation(FunAetherMod.MODID, "subsequence");

    /*
     * Prevent repeated placement attempts every game tick.
     * Missing structures will be retried periodically.
     */
    private static long lastOverworldAttemptTick = Long.MIN_VALUE;

    /*
     * Called by the existing event system.
     */
    public static void onLoad(ServerLevel level) {

        ResourceLocation dimension =
                level.dimension().location();

        /*
         * Overworld structures.
         */
        if (level.dimension().equals(Level.OVERWORLD)) {
            generateOverworldStructures(level);
            return;
        }

        /*
         * Haven dimension.
         */
        if (dimension.equals(HAVEN_DIMENSION)) {
            generateHavenIsland(level);
            return;
        }

        /*
         * Chapel dimension.
         */
        if (dimension.equals(CHAPEL_DIMENSION)) {
            generateChapel(level);
            return;
        }

        /*
         * Hell dimension.
         */
        if (dimension.equals(HELL_DIMENSION)) {
            generateHell(level);
            return;
        }

        /*
         * Subsequence grid is generated as
         * the player explores.
         */
        if (dimension.equals(SUBSEQUENCE_DIMENSION)) {
            return;
        }
    }

    /*
     * Generates exactly one of each Overworld structure.
     */
    private static void generateOverworldStructures(ServerLevel level) {

        long currentTick = level.getGameTime();

        if (lastOverworldAttemptTick != Long.MIN_VALUE
                && currentTick - lastOverworldAttemptTick < 100) {
            return;
        }

        lastOverworldAttemptTick = currentTick;

        OverworldStructureSavedData data =
                OverworldStructureSavedData.get(level);

        BlockPos spawn = level.getSharedSpawnPos();

        int spawnChunkX = Math.floorDiv(spawn.getX(), 16);
        int spawnChunkZ = Math.floorDiv(spawn.getZ(), 16);

        generateOverworldStructure(
                level, data, SPAWN_STRUCTURE, spawnChunkX, spawnChunkZ
        );

        generateOverworldStructure(
                level, data, COOLBOY_HOUSE, spawnChunkX, spawnChunkZ
        );

        generateOverworldStructure(
                level, data, REALNESS_TOWER, spawnChunkX, spawnChunkZ
        );

        generateOverworldStructure(
                level, data, TRICK_HOUSE, spawnChunkX, spawnChunkZ
        );
    }

    /*
     * Finds a valid location for one structure.
     *
     * Candidate locations are selected in chunk coordinates,
     * then converted into block coordinates for placement.
     */
    private static void generateOverworldStructure(
            ServerLevel level,
            OverworldStructureSavedData data,
            String structureName,
            int spawnChunkX,
            int spawnChunkZ
    ) {

        if (data.isGenerated(structureName)) {
            return;
        }

        for (int attempt = 0;
             attempt < MAX_PLACEMENT_ATTEMPTS;
             attempt++) {

            int chunkX = spawnChunkX
                    + level.random.nextInt(
                            MAX_SPAWN_DISTANCE_CHUNKS * 2 + 1
                    )
                    - MAX_SPAWN_DISTANCE_CHUNKS;

            int chunkZ = spawnChunkZ
                    + level.random.nextInt(
                            MAX_SPAWN_DISTANCE_CHUNKS * 2 + 1
                    )
                    - MAX_SPAWN_DISTANCE_CHUNKS;

            /*
             * Keep every structure at least 20 chunks
             * away from world spawn.
             */
            if (!isFarEnough(
                    chunkX,
                    chunkZ,
                    spawnChunkX,
                    spawnChunkZ,
                    SPAWN_EXCLUSION_CHUNKS
            )) {
                continue;
            }

            /*
             * Keep structures at least 10 chunks apart
             * from previously generated structures.
             */
            boolean tooClose = false;

            for (BlockPos existing : data.getPositions().values()) {

                int existingChunkX =
                        Math.floorDiv(existing.getX(), 16);

                int existingChunkZ =
                        Math.floorDiv(existing.getZ(), 16);

                if (!isFarEnough(
                        chunkX,
                        chunkZ,
                        existingChunkX,
                        existingChunkZ,
                        MIN_STRUCTURE_DISTANCE_CHUNKS
                )) {
                    tooClose = true;
                    break;
                }
            }

            if (tooClose) {
                continue;
            }

            /*
             * Place near the center of the chosen chunk.
             * Use the surface height instead of a fixed Y.
             */
            int worldX = chunkX * 16 + 8;
            int worldZ = chunkZ * 16 + 8;

            int worldY = level.getHeight(
                    Heightmap.Types.WORLD_SURFACE,
                    worldX,
                    worldZ
            );

            BlockPos position =
                    new BlockPos(worldX, worldY, worldZ);

            boolean success = StructureLoader.loadStructure(
                    level,
                    structureName,
                    position
            );

            if (success) {

                data.setGenerated(structureName, position);

                System.out.println(
                        "[FunAetherMod] Generated Overworld structure "
                                + structureName
                                + " at "
                                + position
                );
            }

            /*
             * If the NBT file is missing, stop this attempt
             * and retry later rather than flooding the log.
             */
            return;
        }

        System.out.println(
                "[FunAetherMod] Could not find a valid location for "
                        + structureName
                        + " after "
                        + MAX_PLACEMENT_ATTEMPTS
                        + " attempts."
        );
    }

    /*
     * Uses straight-line distance in chunks.
     */
    private static boolean isFarEnough(
            int chunkX,
            int chunkZ,
            int otherChunkX,
            int otherChunkZ,
            int minimumDistance
    ) {

        long dx = (long) chunkX - otherChunkX;
        long dz = (long) chunkZ - otherChunkZ;

        return dx * dx + dz * dz
                >= (long) minimumDistance * minimumDistance;
    }

    /*
     * Haven island.
     */
    private static void generateHavenIsland(ServerLevel level) {

        StructureSavedData data = StructureSavedData.get(level);

        if (data.isGenerated(
                Integer.MIN_VALUE,
                Integer.MIN_VALUE,
                Integer.MIN_VALUE
        )) {
            return;
        }

        boolean success = StructureLoader.loadStructure(
                level,
                HAVEN_ISLAND,
                new BlockPos(0, START_Y, 0)
        );

        if (success) {

            data.setGenerated(
                    Integer.MIN_VALUE,
                    Integer.MIN_VALUE,
                    Integer.MIN_VALUE
            );

            System.out.println(
                    "[FunAetherMod] Haven island generated."
            );
        }
    }

    /*
     * Chapel structure.
     */
    private static void generateChapel(ServerLevel level) {

        StructureSavedData data = StructureSavedData.get(level);

        if (data.isGenerated(
                Integer.MIN_VALUE,
                Integer.MIN_VALUE,
                Integer.MIN_VALUE + 1
        )) {
            return;
        }

        boolean success = StructureLoader.loadStructure(
                level,
                CHAPEL_STRUCTURE,
                new BlockPos(0, START_Y, 0)
        );

        if (success) {

            data.setGenerated(
                    Integer.MIN_VALUE,
                    Integer.MIN_VALUE,
                    Integer.MIN_VALUE + 1
            );

            System.out.println(
                    "[FunAetherMod] Chapel generated."
            );
        }
    }

    /*
    * Hell structure.
    */

        private static void generateHell(ServerLevel level) {
        
                StructureSavedData data = StructureSavedData.get(level);
        
                if (data.isGenerated(
                        Integer.MIN_VALUE,
                        Integer.MIN_VALUE,
                        Integer.MIN_VALUE + 2
                )) {
                return;
                }
        
                boolean success = StructureLoader.loadStructure(
                        level,
                        HELL_STRCUTURE,
                        new BlockPos(0, START_Y, 0)
                );
        
                if (success) {
        
                data.setGenerated(
                        Integer.MIN_VALUE,
                        Integer.MIN_VALUE,
                        Integer.MIN_VALUE + 2
                );
        
                System.out.println(
                        "[FunAetherMod] Hell structure generated."
                );
                }
        }

    /*
     * Generates one Subsequence cell.
     */
    public static void generateIslandGrid(
            ServerLevel level,
            int cellX,
            int cellY,
            int cellZ
    ) {

        if (!level.dimension()
                .location()
                .equals(SUBSEQUENCE_DIMENSION)) {
            return;
        }

        StructureSavedData data = StructureSavedData.get(level);

        if (data.isGenerated(cellX, cellY, cellZ)) {
            return;
        }

        boolean portalIsland =
                cellX > 0 && cellX % 20 == 0;

        boolean corrupted =
                Math.floorMod(cellX, PATTERN_SIZE) == 4
                || Math.floorMod(cellY, PATTERN_SIZE) == 4
                || Math.floorMod(cellZ, PATTERN_SIZE) == 4;

        String structureName;

        if (portalIsland) {
            structureName = PORTAL_ISLAND;
        } else if (corrupted) {
            structureName = CORRUPTED_ISLAND;
        } else {
            structureName = NORMAL_ISLAND;
        }

        int worldX = cellX * X_SPACING;
        int worldY = START_Y + cellY * Y_SPACING;
        int worldZ = cellZ * Z_SPACING;

        BlockPos position = new BlockPos(worldX, worldY, worldZ);

        boolean success = StructureLoader.loadStructure(
                level,
                structureName,
                position
        );

        if (success) {

            data.setGenerated(cellX, cellY, cellZ);

            System.out.println(
                    "[FunAetherMod] Generated "
                            + structureName
                            + " at "
                            + cellX
                            + ", "
                            + cellY
                            + ", "
                            + cellZ
            );
        }
    }
}
