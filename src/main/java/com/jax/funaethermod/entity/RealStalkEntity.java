package com.jax.funaethermod.entity;

// Optional:
// import com.jax.(mod name).registry.ModSounds;
// import net.minecraft.sounds.SoundEvent;
// import net.minecraft.world.effect.MobEffectInstance;
// import net.minecraft.world.effect.MobEffects;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import com.jax.funaethermod.FunAetherMod;
import java.util.EnumSet;

public class RealStalkEntity extends PathfinderMob {

    /*
     * =========================================================
     * SETTINGS
     * =========================================================
     */

    /*
     * Maximum distance at which the entity can detect a player.
     */
    private static final double OBSERVE_RANGE = 128.0D;

    /*
     * Distance the entity tries to maintain from the player.
     */
    private static final double STALK_DISTANCE = 40.0D;

    /*
     * If the entity gets closer than this,
     * it backs away from the player.
     */
    private static final double MIN_DISTANCE = 30.0D;

    /*
     * Movement speed while stalking.
     */
    private static final double MOVEMENT_SPEED = 0.40D;


    /*
     * =========================================================
     * OPTIONAL SETTINGS
     * =========================================================
     */

    /*
    private static final int LIFE_TIME = 600;

    private static final int BLINDNESS_DURATION = 40;

    private static final int BLINDNESS_AMPLIFIER = 0;
    */


    /*
     * =========================================================
     * OPTIONAL LIFETIME
     * =========================================================
     */

    /*
    private int lifeTimer = 0;
    */


    /*
     * =========================================================
     * CONSTRUCTOR
     * =========================================================
     */

    public RealStalkEntity(
            EntityType<? extends RealStalkEntity> entityType,
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

                .add(
                        Attributes.MAX_HEALTH,
                        20.0D
                )

                .add(
                        Attributes.MOVEMENT_SPEED,
                        MOVEMENT_SPEED
                )

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
         * Main stalking behavior.
         *
         * MOVE = controls movement.
         * LOOK = controls looking at the player.
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
         * Optional lifetime system.
         */

        /*
        if (!this.level().isClientSide()) {

            lifeTimer++;

            if (lifeTimer >= LIFE_TIME) {

                this.discard();

                return;
            }
        }
        */
    }


    /*
     * =========================================================
     * STALKING GOAL
     * =========================================================
     */

    private static class StalkPlayerGoal extends Goal {

        private final RealStalkEntity entity;

        private Player target;


        public StalkPlayerGoal(
                RealStalkEntity entity
        ) {

            this.entity = entity;

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

            return entity.distanceTo(target)
                    <= OBSERVE_RANGE;
        }


        /*
         * =====================================================
         * START
         * =====================================================
         */

        @Override
        public void start() {

            if (target != null) {

                entity.getLookControl().setLookAt(
                        target,
                        30.0F,
                        30.0F
                );
            }

            /*
             * Optional:
             * Apply Blindness when stalking begins.
             */

            /*
            applyBlindness();
            */
        }


        /*
         * =====================================================
         * STOP
         * =====================================================
         */

        @Override
        public void stop() {

            target = null;

            entity.getNavigation().stop();
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
             */

            entity.getLookControl().setLookAt(
                    target,
                    30.0F,
                    30.0F
            );


            /*
             * Optional:
             * Keep Blindness active.
             */

            /*
            applyBlindness();
            */


            /*
             * Calculate distance to player.
             */

            double distance =
                    entity.distanceTo(target);


            /*
             * =================================================
             * TOO FAR
             * =================================================
             *
             * Move toward the player until the entity reaches
             * its stalking distance.
             */

            if (distance > STALK_DISTANCE) {

                entity.getNavigation().moveTo(
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
             * Back away when the player gets too close.
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
             * Stop moving and watch the player.
             */

            entity.getNavigation().stop();
        }


        /*
         * =====================================================
         * FIND PLAYER
         * =====================================================
         */

        private Player findNearestPlayer() {

            return entity.level()
                    .getNearestPlayer(
                            entity,
                            OBSERVE_RANGE
                    );
        }


        /*
         * =====================================================
         * OPTIONAL: BLINDNESS
         * =====================================================
         */

        /*
        private void applyBlindness() {

            if (entity.level().isClientSide()) {
                return;
            }

            if (target == null) {
                return;
            }

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
        */


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
             * Find the direction from the player toward the entity.
             */

            double dx =
                    entity.getX()
                            - target.getX();

            double dz =
                    entity.getZ()
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
             * Move farther away from the player.
             */

            double destinationX =
                    entity.getX()
                            + dx * 8.0D;

            double destinationZ =
                    entity.getZ()
                            + dz * 8.0D;


            entity.getNavigation().moveTo(
                    destinationX,
                    entity.getY(),
                    destinationZ,
                    1.0D
            );
        }
    }


    /*
     * =========================================================
     * OPTIONAL: AMBIENT SOUND
     * =========================================================
     */

    /*
    @Override
    protected SoundEvent getAmbientSound() {

        return ModSounds.(entity name)_AMBIENT.get();
    }
    */
}