package com.jax.funaethermod.world;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.util.StructureLoader;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public class StructureManager {

    private static final int X_SPACING = 15;
    private static final int Y_SPACING = 48;
    private static final int Z_SPACING = 15;

    private static final int START_Y = 100;

    private static final int PATTERN_SIZE = 5;

    private static final String HAVEN_ISLAND =
            "haven_island";

    private static final String NORMAL_ISLAND =
            "subsequence_island";

    private static final String CORRUPTED_ISLAND =
            "subsequence_corrupted_island";

        private static final String PORTAL_ISLAND =
                "subsequence_portal_island";


    /*
     * Dimension IDs.
     */
    private static final ResourceLocation HAVEN_DIMENSION =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "haven"
            );

    private static final ResourceLocation SUBSEQUENCE_DIMENSION =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "subsequence"
            );


    /*
     * Called when a ServerLevel loads.
     */
    public static void onLoad(ServerLevel level) {

        ResourceLocation dimension =
                level.dimension().location();


        /*
         * Haven dimension.
         */
        if (dimension.equals(HAVEN_DIMENSION)) {

            generateHavenIsland(level);

            return;
        }


        /*
         * Subsequence dimension.
         *
         * The actual infinite grid should be
         * generated as the player explores.
         */
        if (dimension.equals(SUBSEQUENCE_DIMENSION)) {

            return;
        }
    }


    /*
     * Haven island.
     *
     * This remains completely separate from
     * the Subsequence grid.
     */
    private static void generateHavenIsland(
            ServerLevel level
    ) {

        StructureSavedData data =
                StructureSavedData.get(level);


        /*
         * Reserved coordinates for Haven.
         */
        if (data.isGenerated(
                Integer.MIN_VALUE,
                Integer.MIN_VALUE,
                Integer.MIN_VALUE
        )) {

            return;
        }


        boolean success =
                StructureLoader.loadStructure(
                        level,
                        HAVEN_ISLAND,
                        new BlockPos(
                                0,
                                START_Y,
                                0
                        )
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
     * Generates one Subsequence cell.
     */
    public static void generateIslandGrid(
            ServerLevel level,
            int cellX,
            int cellY,
            int cellZ
    ) {

        /*
         * Make sure this is actually the
         * Subsequence dimension.
         */
        if (!level.dimension()
                .location()
                .equals(SUBSEQUENCE_DIMENSION)) {

            return;
        }


        StructureSavedData data =
                StructureSavedData.get(level);


        /*
         * Don't generate the same cell twice.
         */
        if (data.isGenerated(
                cellX,
                cellY,
                cellZ
        )) {

            return;
        }



        boolean portalIsland = 
                cellX > 0 && cellX % 20 == 0;

        /*
         * Corrupted whenever ANY axis reaches
         * the fifth position in its repeating pattern.
         *
         * I I I I C
         * I I I I C
         * I I I I C
         * I I I I C
         * C C C C C
         */
        boolean corrupted =
                Math.floorMod(
                        cellX,
                        PATTERN_SIZE
                ) == 4

                || Math.floorMod(
                        cellY,
                        PATTERN_SIZE
                ) == 4

                || Math.floorMod(
                        cellZ,
                        PATTERN_SIZE
                ) == 4;


        String structureName;
                
        if (portalIsland) {
                structureName = PORTAL_ISLAND;
        } else if (corrupted) {
                structureName = CORRUPTED_ISLAND; 
        } else {
                structureName = NORMAL_ISLAND;
        }


        /*
         * Convert grid coordinates
         * into world coordinates.
         */
        int worldX =
                cellX * X_SPACING;

        int worldY =
                START_Y
                        + cellY * Y_SPACING;

        int worldZ =
                cellZ * Z_SPACING;


        BlockPos position =
                new BlockPos(
                        worldX,
                        worldY,
                        worldZ
                );


        /*
         * Load the NBT structure.
         */
        boolean success =
                StructureLoader.loadStructure(
                        level,
                        structureName,
                        position
                );


        /*
         * Only mark it generated after
         * successful placement.
         */
        if (success) {

            data.setGenerated(
                    cellX,
                    cellY,
                    cellZ
            );

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