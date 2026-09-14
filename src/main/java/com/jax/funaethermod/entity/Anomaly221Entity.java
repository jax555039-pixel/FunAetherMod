package com.jax.funaethermod.entity;

import com.jax.funaethermod.registry.ModSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class Anomaly221Entity extends PathfinderMob {

    /*
     * =========================================================
     * SETTINGS
     * =========================================================
     */

    /*
     * Maximum distance at which Anomaly 221 can detect a player.
     */
    private static final double OBSERVE_RANGE = 128.0D;

    /*
     * The distance Anomaly 221 tries to maintain from the player.
     */
    private static final double STALK_DISTANCE = 6.0D;

    /*
     * If 221 gets closer than this, it backs away.
     */
    private static final double MIN_DISTANCE = 4.0D;

    /*
     * 20 ticks = 1 second.
     *
     * 600 ticks = 30 seconds.
     */
    private static final int LIFE_TIME = 600;

    /*
     * How long Blindness lasts when reapplied.
     *
     * 40 ticks = 2 seconds.
     *
     * The effect is constantly refreshed while 221 is stalking,
     * so the player remains blinded for the entire encounter.
     */
    private static final int BLINDNESS_DURATION = 40;

    /*
     * Strength of the Blindness effect.
     *
     * 0 = normal Blindness.
     */
    private static final int BLINDNESS_AMPLIFIER = 0;

    /*
     * How quickly 221 moves.
     *
     * 0.40 is faster than a normal player's movement speed.
     */
    private static final double MOVEMENT_SPEED = 0.40D;

    /*
     * =========================================================
     * LIFETIME
     * =========================================================
     */

    private int lifeTimer = 0;


    /*
     * =========================================================
     * CONSTRUCTOR
     * =========================================================
     */

    public Anomaly221Entity(
            EntityType<? extends Anomaly221Entity> entityType,
            Level level
    ) {
        super(entityType, level);
    }


    /*
     * =========================================================
     * ATTRIBUTES
     * =========================================================
     */

    public static AttributeSupplier.Builder createAttributes() {

        return PathfinderMob.createMobAttributes()

                /*
                 * Health.
                 */
                .add(
                        Attributes.MAX_HEALTH,
                        20.0D
                )

                /*
                 * Movement speed.
                 *
                 * 0.40 makes 221 faster than a normal player.
                 */
                .add(
                        Attributes.MOVEMENT_SPEED,
                        MOVEMENT_SPEED
                )

                /*
                 * Detection range.
                 */
                .add(
                        Attributes.FOLLOW_RANGE,
                        OBSERVE_RANGE
                );
    }


    /*
     * =========================================================
     * AI GOALS
     * =========================================================
     */

    @Override
    protected void registerGoals() {

        /*
         * Priority 0:
         *
         * Anomaly 221's stalking behavior.
         */
        this.goalSelector.addGoal(
                0,
                new StalkPlayerGoal(this)
        );
    }


    /*
     * =========================================================
     * TICK
     * =========================================================
     */

    @Override
    public void tick() {

        super.tick();

        /*
         * Server-side lifetime.
         */
        if (!this.level().isClientSide()) {

            lifeTimer++;

            /*
             * Remove 221 after its lifetime expires.
             */
            if (lifeTimer >= LIFE_TIME) {

                this.discard();

                return;
            }
        }
    }


    /*
     * =========================================================
     * STALKING GOAL
     * =========================================================
     */

    private static class StalkPlayerGoal extends Goal {

        private final Anomaly221Entity anomaly;

        private Player target;


        public StalkPlayerGoal(
                Anomaly221Entity anomaly
        ) {

            this.anomaly = anomaly;

            /*
             * This goal controls:
             *
             * MOVE = movement
             * LOOK = facing the player
             */
            this.setFlags(
                    EnumSet.of(
                            Goal.Flag.MOVE,
                            Goal.Flag.LOOK
                    )
            );
        }


        /*
         * =====================================================
         * CAN USE
         * =====================================================
         */

        @Override
        public boolean canUse() {

            target = findNearestPlayer();

            return target != null;
        }


        /*
         * =====================================================
         * CAN CONTINUE
         * =====================================================
         */

        @Override
        public boolean canContinueToUse() {

            if (target == null) {
                return false;
            }

            if (!target.isAlive()) {
                return false;
            }

            return anomaly.distanceTo(target)
                    <= OBSERVE_RANGE;
        }


        /*
         * =====================================================
         * START
         * =====================================================
         */

        @Override
        public void start() {

            /*
             * Immediately face the player.
             */
            if (target != null) {

                anomaly.getLookControl().setLookAt(
                        target,
                        30.0F,
                        30.0F
                );
            }

            /*
             * Apply Blindness immediately.
             */
            applyBlindness();
        }


        /*
         * =====================================================
         * STOP
         * =====================================================
         */

        @Override
        public void stop() {

            target = null;

            anomaly.getNavigation().stop();
        }


        /*
         * =====================================================
         * TICK
         * =====================================================
         */

        @Override
        public void tick() {

            if (target == null) {
                return;
            }

            /*
             * =================================================
             * ALWAYS FACE PLAYER
             * =================================================
             *
             * 221 constantly turns its head/body toward the
             * player instead of only looking occasionally.
             */
            anomaly.getLookControl().setLookAt(
                    target,
                    30.0F,
                    30.0F
            );

            /*
             * Keep Blindness active.
             */
            applyBlindness();

            /*
             * Calculate distance to player.
             */
            double distance =
                    anomaly.distanceTo(target);


            /*
             * =================================================
             * TOO FAR
             * =================================================
             *
             * Move toward the player until 221 reaches
             * approximately 14 blocks away.
             */
            if (distance > STALK_DISTANCE) {

                anomaly.getNavigation().moveTo(
                        target,
                        1.0D
                );

                return;
            }


            /*
             * =================================================
             * TOO CLOSE
             * =================================================
             *
             * If the player gets within 8 blocks,
             * Anomaly 221 backs away.
             */
            if (distance < MIN_DISTANCE) {

                moveAwayFromPlayer();

                return;
            }


            /*
             * =================================================
             * STALKING RANGE
             * =================================================
             *
             * Between 8 and 14 blocks:
             *
             * STOP.
             *
             * 221 simply watches the player.
             */
            anomaly.getNavigation().stop();
        }


        /*
         * =====================================================
         * FIND PLAYER
         * =====================================================
         */

        private Player findNearestPlayer() {

            return anomaly.level()
                    .getNearestPlayer(
                            anomaly,
                            OBSERVE_RANGE
                    );
        }


        /*
         * =====================================================
         * BLINDNESS
         * =====================================================
         */

        private void applyBlindness() {

            /*
             * Effects should be applied server-side.
             */
            if (anomaly.level().isClientSide()) {
                return;
            }

            if (target == null) {
                return;
            }

            /*
             * Apply Blindness to the player.
             *
             * The effect lasts 2 seconds and is refreshed
             * every tick while 221 is stalking.
             */
            target.addEffect(
                    new MobEffectInstance(
                            MobEffects.BLINDNESS,
                            BLINDNESS_DURATION,
                            BLINDNESS_AMPLIFIER,
                            false,
                            false,
                            true
                    )
            );
        }


        /*
         * =====================================================
         * MOVE AWAY FROM PLAYER
         * =====================================================
         */

        private void moveAwayFromPlayer() {

            if (target == null) {
                return;
            }

            /*
             * Find the direction from the player toward 221.
             */
            double dx =
                    anomaly.getX()
                            - target.getX();

            double dz =
                    anomaly.getZ()
                            - target.getZ();

            /*
             * Calculate the length of the direction vector.
             */
            double length =
                    Math.sqrt(
                            dx * dx
                                    + dz * dz
                    );

            /*
             * Prevent division by zero.
             */
            if (length < 0.001D) {
                return;
            }

            /*
             * Normalize the direction.
             */
            dx /= length;
            dz /= length;

            /*
             * Move 221 farther away.
             *
             * It moves 8 blocks away from the player.
             */
            double destinationX =
                    anomaly.getX()
                            + dx * 8.0D;

            double destinationZ =
                    anomaly.getZ()
                            + dz * 8.0D;

            anomaly.getNavigation().moveTo(
                    destinationX,
                    anomaly.getY(),
                    destinationZ,
                    1.0D
            );
        }
    }


    /*
     * =========================================================
     * AMBIENT SOUND
     * =========================================================
     */

    @Override
    protected SoundEvent getAmbientSound() {

        return ModSounds.REAL_AMBIENT.get();
    }
}