package com.jax.funaethermod.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class SsIslandGenerator {

    /*
     * Structure size:
     *
     * X = 15
     * Y = 48
     * Z = 15
     */
    private static final int X_SPACING = 15;
    private static final int Y_SPACING = 48;
    private static final int Z_SPACING = 15;

    /*
     * How many grid cells to generate
     * around the player.
     *
     * X/Z = 2 means 5 x 5 cells.
     * Y = 2 means 5 vertical layers.
     *
     * Total:
     *
     * 5 x 5 x 5 = 125 cells
     */
    private static final int X_RADIUS = 2;
    private static final int Y_RADIUS = 2;
    private static final int Z_RADIUS = 2;


    /*
     * Starting Y coordinate of the grid.
     *
     * This needs to match StructureManager.
     */
    private static final int START_Y = 100;


    /*
     * Called for a player in the
     * Subsequence dimension.
     */
    public static void generateAroundPlayer(
            ServerLevel level,
            ServerPlayer player
    ) {

        /*
         * Get the player's grid coordinates.
         */
        int centerCellX =
                Math.floorDiv(
                        player.blockPosition().getX(),
                        X_SPACING
                );

        int centerCellY =
                Math.floorDiv(
                        player.blockPosition().getY() - START_Y,
                        Y_SPACING
                );

        int centerCellZ =
                Math.floorDiv(
                        player.blockPosition().getZ(),
                        Z_SPACING
                );


        /*
         * Generate a 3D cube of cells
         * around the player.
         */
        for (
                int cellX = centerCellX - X_RADIUS;
                cellX <= centerCellX + X_RADIUS;
                cellX++
        ) {

            for (
                    int cellY = centerCellY - Y_RADIUS;
                    cellY <= centerCellY + Y_RADIUS;
                    cellY++
            ) {

                for (
                        int cellZ = centerCellZ - Z_RADIUS;
                        cellZ <= centerCellZ + Z_RADIUS;
                        cellZ++
                ) {

                    StructureManager.generateIslandGrid(
                            level,
                            cellX,
                            cellY,
                            cellZ
                    );
                }
            }
        }
    }
}