package com.jax.funaethermod.entity;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.registry.ModEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(
        modid = FunAetherMod.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class EntitySpawnerSpawnHandler {

    /*
     * =========================================================
     * SETTINGS
     * =========================================================
     */

    // 20 ticks = 1 second
    // Minimum random delay = 5 minutes
    private static final int MIN_SPAWN_INTERVAL = 20 * 60 * 5;

    // Maximum random delay = 15 minutes
    private static final int MAX_SPAWN_INTERVAL = 20 * 60 * 15;

    // 30% chance to actually spawn an EntitySpawner
    private static final float SPAWN_CHANCE = 0.30F;

    // Distance from the player
    private static final int MIN_DISTANCE = 16;
    private static final int MAX_DISTANCE = 32;

    /*
     * Current timer.
     */
    private static int spawnTimer = 0;

    /*
     * The randomly selected time until the next attempt.
     */
    private static int nextSpawnInterval = getRandomSpawnInterval();


    /*
     * =========================================================
     * SERVER TICK
     * =========================================================
     */

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {

        /*
         * Only run once per tick.
         */
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        /*
         * Server only.
         */
        if (event.level.isClientSide()) {
            return;
        }

        /*
         * Use the Overworld as the global timer.
         */
        if (event.level.dimension() != Level.OVERWORLD) {
            return;
        }

        spawnTimer++;

        /*
         * Wait until the randomly selected interval
         * has been reached.
         */
        if (spawnTimer < nextSpawnInterval) {
            return;
        }

        /*
         * Reset timer.
         */
        spawnTimer = 0;

        /*
         * Pick a NEW random interval for the next attempt.
         *
         * This means the delay changes every time.
         */
        nextSpawnInterval = getRandomSpawnInterval();

        /*
         * 30% spawn roll.
         */
        if (event.level.random.nextFloat() >= SPAWN_CHANCE) {
            return;
        }

        /*
         * Get online players.
         */
        List<ServerPlayer> players =
                event.level.getServer()
                        .getPlayerList()
                        .getPlayers();

        if (players.isEmpty()) {
            return;
        }

        /*
         * Pick a random player.
         */
        ServerPlayer player =
                players.get(
                        event.level.random.nextInt(
                                players.size()
                        )
                );

        /*
         * Spawn in that player's current dimension.
         */
        ServerLevel playerLevel =
                player.serverLevel();

        spawnEntitySpawner(playerLevel, player);
    }


    /*
     * =========================================================
     * RANDOM SPAWN INTERVAL
     * =========================================================
     */

    private static int getRandomSpawnInterval() {

        return MIN_SPAWN_INTERVAL
                + (int) (
                        Math.random()
                                * (
                                MAX_SPAWN_INTERVAL
                                        - MIN_SPAWN_INTERVAL
                                )
                );
    }


    /*
     * =========================================================
     * SPAWN ENTITY SPAWNER
     * =========================================================
     */

    private static void spawnEntitySpawner(
            ServerLevel level,
            ServerPlayer player
    ) {

        BlockPos spawnPos =
                findSpawnPosition(level, player);

        if (spawnPos == null) {
            return;
        }

        /*
         * EntitySpawner type.
         */
        EntityType<?> type =
                ModEntities.ENTITY_SPAWNER.get();

        Entity entity =
                type.create(level);

        if (entity == null) {
            return;
        }

        /*
         * Position it.
         */
        entity.moveTo(
                spawnPos.getX() + 0.5D,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5D,
                level.random.nextFloat() * 360.0F,
                0.0F
        );

        /*
         * Add it to the world.
         */
        level.addFreshEntity(entity);
    }


    /*
     * =========================================================
     * FIND VALID SPAWN POSITION
     * =========================================================
     */

    private static BlockPos findSpawnPosition(
            ServerLevel level,
            ServerPlayer player
    ) {

        /*
         * Try multiple random positions.
         */
        for (int attempt = 0; attempt < 20; attempt++) {

            int distance =
                    MIN_DISTANCE
                            + level.random.nextInt(
                            MAX_DISTANCE - MIN_DISTANCE + 1
                    );

            /*
             * Random direction.
             */
            double angle =
                    level.random.nextDouble()
                            * Math.PI * 2.0D;

            int offsetX =
                    (int) Math.round(
                            Math.cos(angle) * distance
                    );

            int offsetZ =
                    (int) Math.round(
                            Math.sin(angle) * distance
                    );

            BlockPos playerPos =
                    player.blockPosition();

            BlockPos position =
                    playerPos.offset(
                            offsetX,
                            0,
                            offsetZ
                    );

            /*
             * Find the highest surface.
             */
            int y =
                    level.getHeight(
                            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                            position.getX(),
                            position.getZ()
                    );

            BlockPos groundPos =
                    new BlockPos(
                            position.getX(),
                            y - 1,
                            position.getZ()
                    );

            BlockPos spawnPos =
                    groundPos.above();

            /*
             * World bounds.
             */
            if (!level.isInWorldBounds(spawnPos)) {
                continue;
            }

            /*
             * Ground must be solid.
             */
            if (!level.getBlockState(groundPos)
                    .isSolid()) {
                continue;
            }

            /*
             * EntitySpawner needs one block of space.
             */
            if (!level.getBlockState(spawnPos)
                    .isAir()) {
                continue;
            }

            /*
             * And another clear block above it.
             */
            if (!level.getBlockState(spawnPos.above())
                    .isAir()) {
                continue;
            }

            return spawnPos;
        }

        return null;
    }
}