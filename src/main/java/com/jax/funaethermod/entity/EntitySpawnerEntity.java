package com.jax.funaethermod.entity;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.registry.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;


import java.util.ArrayList;
import java.util.List;

public class EntitySpawnerEntity extends PathfinderMob {

    /*
     * 10 minutes in Minecraft ticks.
     *
     * 20 ticks = 1 second
     * 60 seconds = 1 minute
     * 10 minutes = 12,000 ticks
     */
    private static final int SPAWN_INTERVAL = 20; 

    /*
     * 30% chance to actually spawn an entity
     * when the timer reaches 10 minutes.
     */
    private static final float SPAWN_CHANCE = 1.0F;

    /*
     * Player health threshold.
     *
     * 50% health or below = LOW HEALTH
     * Above 50% = HIGH HEALTH
     */
    private static final float LOW_HEALTH_PERCENT = 0.50F;

    private int spawnTimer = 0;

    public EntitySpawnerEntity(
            EntityType<? extends PathfinderMob> type,
            Level level
    ) {
        super(type, level);

        this.setPersistenceRequired();
    }

    /*
     * No AI behavior.
     */
    @Override
    protected void registerGoals() {
    }

    /*
     * Required because EntitySpawnerEntity extends PathfinderMob.
     *
     * The spawner itself does not need meaningful attributes,
     * but PathfinderMob still requires an attribute supplier.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes();
    }

    @Override
    public void tick() {

        super.tick();

        /*
         * Server side only.
         */
        if (this.level().isClientSide) {
            return;
        }

        spawnTimer++;

        /*
         * Wait exactly 10 minutes before making a spawn roll.
         */
        if (spawnTimer < SPAWN_INTERVAL) {
            return;
        }

        /*
         * Reset the timer.
         *
         * Even if the 30% roll fails, the next attempt
         * will happen another 10 minutes later.
         */
        spawnTimer = 0;

        /*
         * 30% chance to spawn something.
         */
        if (this.random.nextFloat() > SPAWN_CHANCE) {
            return;
        }

        Player player = this.level().getNearestPlayer(
                this,
                128.0D
        );

        if (player == null) {
            return;
        }

        /*
         * Determine the environment around the player.
         */
        boolean isCave = isPlayerInCave(player);

        boolean raining = this.level().isRaining();

        boolean clearWeather = !raining;

        boolean night = this.level().isNight();

        boolean lowHealth = isLowHealth(player);

        /*
         * Dimension checks.
         */
        ResourceLocation dimension =
                this.level().dimension().location();

        boolean overworld =
                dimension.equals(Level.OVERWORLD.location());

        boolean aether =
                dimension.equals(
                        new ResourceLocation(
                                FunAetherMod.MODID,
                                "aether"
                        )
                );

        boolean purgatory =
                dimension.equals(
                        new ResourceLocation(
                                FunAetherMod.MODID,
                                "purgatory"
                        )
                );

                boolean subsequence = 
                dimension.equals(
                        new ResourceLocation(
                                FunAetherMod.MODID,
                                "subsequence"
                        )
                );

        boolean sd1-ca = 
            dimension.equals(
                new ResourceLocation(
                   FunAetherMod.MODID
                    "sd1-ca"
                )
            );

        /*
         * Find an entity that is valid for the
         * current environment.
         */
        EntityType<?> entityType = getValidEntityType(
                isCave,
                raining,
                clearWeather,
                night,
                lowHealth,
                overworld,
                aether,
                purgatory,
                subsequence'
                sd1-ca
        );

        /*
         * No entity matched the current environment.
         */
        if (entityType == null) {
            return;
        }

        /*
         * Create the entity.
         */
        Entity spawnedEntity =
                entityType.create(this.level());

        if (spawnedEntity == null) {
            return;
        }

        /*
         * Spawn it at the EntitySpawner's location.
         */
        spawnedEntity.moveTo(
                this.getX(),
                this.getY(),
                this.getZ(),
                this.getYRot(),
                this.getXRot()
        );

        /*
         * Copy movement.
         */
        spawnedEntity.setDeltaMovement(
                this.getDeltaMovement()
        );

        /*
         * Add the entity to the world.
         */
        this.level().addFreshEntity(spawnedEntity);

        /*
         * Remove the EntitySpawner after
         * successfully spawning an entity.
         */
        this.discard();
    }

    /*
     * Determines whether the nearest player is inside a cave.
     *
     * A player is considered to be in a cave when they
     * cannot see the sky.
     *
     * This means:
     *
     * - Underground = cave
     * - Inside a closed structure = cave
     * - Under a solid ceiling = cave
     * - Normal outdoor surface = land
     */
    private boolean isPlayerInCave(Player player) {

        return !player.level().canSeeSky(
                player.blockPosition()
        );
    }

    /*
     * Determines whether the player has low health.
     *
     * Low health = 50% or less.
     */
    private boolean isLowHealth(Player player) {

        float maxHealth = player.getMaxHealth();

        if (maxHealth <= 0.0F) {
            return false;
        }

        return player.getHealth()
                <= maxHealth * LOW_HEALTH_PERCENT;
    }

    /*
     * Selects a valid entity based on the
     * environment rules.
     */
    private EntityType<?> getValidEntityType(
            boolean isCave,
            boolean raining,
            boolean clearWeather,
            boolean night,
            boolean lowHealth,
            boolean overworld,
            boolean aether,
            boolean purgatory,
            boolean subsequence,
            boolean sd1-ca
    ) {

        List<EntityType<?>> possibleEntities =
                new ArrayList<>();


                /*
                *=====================================
                *Subsequence
                *=====================================
                */

                if (subsequence) {

                    possibleEntities.add(
                            ModEntities.ENTITY2020.get()
                    );

                    possibleEntities.add(
                            ModEntities.ENTITY2020_ATTACK.get()
                    );

                    return getRandomEntity(
                            possibleEntities
                    );
                }

        /*
        * =========================================================
        * SD1-CA
        * =========================================================
        */

        if (sd1-ca) {

        possibleEntities.add(
            ModEntities.FAKE.get()
        );


            possibleEntities.add(
            ModEntities.FAKE_AGGRO.get()
        );
            


        /*
         * =========================================================
         * POORBOY
         * =========================================================
         *
         * Requirements:
         *
         * Rain
         * Any dimension
         * Any time
         * Any player health
         *
         * Works both inside caves and on land.
         */
        if (raining) {

            possibleEntities.add(
                    ModEntities.POORBOY.get()
            );
        }


        /*
         * =========================================================
         * REAL OBSERVE
         * =========================================================
         *
         * Requirements:
         *
         * Clear weather
         * Overworld and aether
         * Can spawn in caves or on land.
         */
        if (
                clearWeather
                        && (overworld || aether)
                        
        ) {

            possibleEntities.add(
                    ModEntities.REAL_OBSERVE.get()
            );
        }


        /*
         * =========================================================
         * FAKE
         * =========================================================
         *
         * Requirements:
         *
         * Any weather
         * Overworld OR Purgatory
         * Any time
         * Any player health
         *
         * Can spawn in caves or on land.
         */
        if (
                overworld
                        || purgatory
                                    || sd1-ca
        ) {

            possibleEntities.add(
                    ModEntities.FAKE.get()
            );
        }




/*
* ==========================================================
* FAKE AGGRO
* ==========================================================
*/

if (
        (overworld || sd1-ca)
            && !lowhealth
    ) {

        possibleEntities.add(
            ModEntities.FAKE_AGGRO.get()
        );
}
        

        /*
 * =========================================================
 * TRICK
 * =========================================================
 *
 * Requirements:
 *
 * Any weather
 * Any player health
 * Purgatory
 * Day or night
 *
 * Therefore:
 * Purgatory is the only required condition.
 */
if (purgatory) {

    possibleEntities.add(
            ModEntities.TRICK.get()
    );
}


        /*
         * =========================================================
         * CAVE RULE
         * =========================================================
         *
         * If the player is underground, ONLY:
         *
         * - PoorBoy
         * - RealObserve
         * - Fake
         *
         * are allowed.
         *
         * Therefore stop here.
         */
        if (isCave) {

            return getRandomEntity(
                    possibleEntities
            );
        }


        /*
         * =========================================================
         * ENTITY 2020
         * =========================================================
         *
         * Requirements:
         *
         * Any weather
         * Night
         * Any player health
         * Overworld OR Aether
         * Land only
         */
        if (
                night
                        && (overworld || aether)
        ) {

            possibleEntities.add(
                    ModEntities.ENTITY2020.get()
            );
        }


        /*
         * =========================================================
         * ENTITY 2020 ATTACK
         * =========================================================
         *
         * Requirements:
         *
         * Same as Entity2020
         * BUT player must have HIGH health.
         */
        if (
                night
                        && (overworld || aether)
                        && !lowHealth
        ) {

            possibleEntities.add(
                    ModEntities.ENTITY2020_ATTACK.get()
            );
        }


        /*
         * Choose randomly from every entity that
         * passed its environmental requirements.
         */
        return getRandomEntity(
                possibleEntities
        );
    }

    /*
     * Picks one entity from the valid list.
     */
    private EntityType<?> getRandomEntity(
            List<EntityType<?>> entities
    ) {

        if (entities.isEmpty()) {
            return null;
        }

        return entities.get(
                this.random.nextInt(
                        entities.size()
                )
        );
}
}
