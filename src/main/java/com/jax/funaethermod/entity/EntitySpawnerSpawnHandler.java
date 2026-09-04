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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //
    // Testing:
    // 20 * 30 = 600 ticks = 30 seconds
    //
    // Normal:
    // 20 * 60 * 5 = 5 minutes
    private static final int MIN_SPAWN_INTERVAL = 20 * 60 * 5;

    // Testing:
    // 20 * 30 = 600 ticks = 30 seconds
    //
    // Normal:
    // 20 * 60 * 15 = 15 minutes
    private static final int MAX_SPAWN_INTERVAL = 20 * 60 * 15;

    // 30% chance normally.
    // Testing is 100%.
    private static final float SPAWN_CHANCE = 0.30F;

    // Distance from the player.
    private static final int MIN_DISTANCE = 16;
    private static final int MAX_DISTANCE = 32;

    /*
     * =========================================================
     * DIMENSION TIMERS
     * =========================================================
     *
     * Each dimension gets its own timer.
     *
     * This is important because LevelTickEvent fires separately
     * for every loaded dimension.
     */
    private static final Map<Level, Integer> SPAWN_TIMERS =
            new HashMap<>();

    private static final Map<Level, Integer> NEXT_SPAWN_INTERVALS =
            new HashMap<>();

    /*
     * =========================================================
     * LEVEL TICK
     * =========================================================
     */

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {

        // Only run once per tick.
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        // Never run this spawning system on the client.
        if (event.level.isClientSide()) {
            return;
        }

        // Convert the level into a ServerLevel.
        if (!(event.level instanceof ServerLevel level)) {
            return;
        }

        /*
         * =====================================================
         * GET THIS DIMENSION'S TIMER
         * =====================================================
         */

        int spawnTimer =
                SPAWN_TIMERS.getOrDefault(
                        level,
                        0
                );

        int nextSpawnInterval =
                NEXT_SPAWN_INTERVALS.computeIfAbsent(
                        level,
                        ignored -> getRandomSpawnInterval()
                );

        spawnTimer++;

        /*
         * Debug message once every second.
         */
        if (spawnTimer % 20 == 0) {

            System.out.println(
                    "EntitySpawnerSpawnHandler fired in dimension: "
                            + level.dimension().location()
                            + " | Timer: "
                            + spawnTimer
                            + "/"
                            + nextSpawnInterval
            );
        }

        /*
         * Save the timer before returning.
         */
        SPAWN_TIMERS.put(
                level,
                spawnTimer
        );

        /*
         * =====================================================
         * WAIT FOR SPAWN TIMER
         * =====================================================
         */

        if (spawnTimer < nextSpawnInterval) {
            return;
        }

        /*
         * =====================================================
         * TIMER REACHED
         * =====================================================
         */

        System.out.println(
                "EntitySpawner timer reached "
                        + nextSpawnInterval
                        + " ticks in dimension: "
                        + level.dimension().location()
        );

        // Reset this dimension's timer.
        SPAWN_TIMERS.put(
                level,
                0
        );

        // Pick the next interval.
        NEXT_SPAWN_INTERVALS.put(
                level,
                getRandomSpawnInterval()
        );

        /*
         * =====================================================
         * SPAWN CHANCE
         * =====================================================
         */

        if (level.random.nextFloat() >= SPAWN_CHANCE) {

            System.out.println(
                    "EntitySpawner spawn chance failed in dimension: "
                            + level.dimension().location()
            );

            return;
        }

        /*
         * =====================================================
         * FIND PLAYERS IN THIS DIMENSION
         * =====================================================
         */

        List<ServerPlayer> players =
                level.getServer()
                        .getPlayerList()
                        .getPlayers()
                        .stream()
                        .filter(player ->
                                player.serverLevel() == level
                        )
                        .toList();

        /*
         * If nobody is currently in this dimension,
         * don't spawn anything here.
         */
        if (players.isEmpty()) {

            System.out.println(
                    "EntitySpawner could not spawn in "
                            + level.dimension().location()
                            + " because there are no players in this dimension."
            );

            return;
        }

        /*
         * Pick a random player who is actually inside
         * this dimension.
         */
        ServerPlayer player =
                players.get(
                        level.random.nextInt(
                                players.size()
                        )
                );

        System.out.println(
                "EntitySpawner attempting to spawn near "
                        + player.getName().getString()
                        + " in dimension: "
                        + level.dimension().location()
        );

        /*
         * Spawn the EntitySpawner.
         */
        spawnEntitySpawner(
                level,
                player
        );
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
                findSpawnPosition(
                        level,
                        player
                );

        /*
         * Couldn't find a valid location.
         */
        if (spawnPos == null) {

            System.out.println(
                    "EntitySpawner could not find a valid spawn position in dimension: "
                            + level.dimension().location()
            );

            return;
        }

        /*
         * Get our custom EntitySpawner entity type.
         */
        EntityType<?> type =
                ModEntities.ENTITY_SPAWNER.get();

        /*
         * Try to create the entity.
         */
        Entity entity =
                type.create(level);

        /*
         * Entity creation failed.
         */
        if (entity == null) {

            System.out.println(
                    "EntitySpawner type.create() returned null in dimension: "
                            + level.dimension().location()
            );

            return;
        }

        /*
         * Move the EntitySpawner into position.
         */
        entity.moveTo(
                spawnPos.getX() + 0.5D,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5D,
                level.random.nextFloat() * 360.0F,
                0.0F
        );

        /*
         * Add the entity to the world.
         */
        level.addFreshEntity(entity);

        /*
         * Debug message confirming successful spawn.
         */
        System.out.println(
                "EntitySpawner spawned at "
                        + spawnPos.getX()
                        + ", "
                        + spawnPos.getY()
                        + ", "
                        + spawnPos.getZ()
                        + " in dimension: "
                        + level.dimension().location()
        );
    }

    /*
     * =========================================================
     * FIND SPAWN POSITION
     * =========================================================
     */

    private static BlockPos findSpawnPosition(
            ServerLevel level,
            ServerPlayer player
    ) {

        /*
         * Try up to 20 random locations.
         */
        for (int attempt = 0; attempt < 20; attempt++) {

            /*
             * Pick a random distance between
             * MIN_DISTANCE and MAX_DISTANCE.
             */
            int distance =
                    MIN_DISTANCE
                            + level.random.nextInt(
                            MAX_DISTANCE
                                    - MIN_DISTANCE
                                    + 1
                    );

            /*
             * Pick a random direction.
             */
            double angle =
                    level.random.nextDouble()
                            * Math.PI
                            * 2.0D;

            /*
             * Convert the angle into X/Z movement.
             */
            int offsetX =
                    (int) Math.round(
                            Math.cos(angle)
                                    * distance
                    );

            int offsetZ =
                    (int) Math.round(
                            Math.sin(angle)
                                    * distance
                    );

            /*
             * Start from the player's position.
             */
            BlockPos playerPos =
                    player.blockPosition();

            /*
             * Move to the random X/Z position.
             */
            BlockPos position =
                    playerPos.offset(
                            offsetX,
                            0,
                            offsetZ
                    );

            /*
             * Find the highest valid ground position.
             */
            int y =
                    level.getHeight(
                            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                            position.getX(),
                            position.getZ()
                    );

            /*
             * The block directly underneath the spawn
             * position is the ground.
             */
            BlockPos groundPos =
                    new BlockPos(
                            position.getX(),
                            y - 1,
                            position.getZ()
                    );

            /*
             * Spawn one block above the ground.
             */
            BlockPos spawnPos =
                    groundPos.above();

            /*
             * Make sure the position is inside the world.
             */
            if (!level.isInWorldBounds(spawnPos)) {
                continue;
            }

            /*
             * Make sure there is solid ground.
             */
            if (!level.getBlockState(groundPos)
                    .isSolid()) {
                continue;
            }

            /*
             * Make sure the EntitySpawner's block is empty.
             */
            if (!level.getBlockState(spawnPos)
                    .isAir()) {
                continue;
            }

            /*
             * Make sure there is another empty block above it.
             *
             * This gives the entity room to exist.
             */
            if (!level.getBlockState(spawnPos.above())
                    .isAir()) {
                continue;
            }

            /*
             * Found a valid location.
             */
            return spawnPos;
        }

        /*
         * All 20 attempts failed.
         */
        return null;
    }
}