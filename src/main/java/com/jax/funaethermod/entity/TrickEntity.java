package com.jax.funaethermod.entity;

import com.jax.funaethermod.registry.ModSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TrickEntity extends PathfinderMob {

    private static final double FOLLOW_RANGE = 64.0D;

    /*
     * Slightly slower than a normal player.
     *
     * Player speed is roughly 0.10 blocks/tick while walking.
     * 0.085 gives Trick a noticeable but not painfully slow pace.
     */
    private static final double MOVEMENT_SPEED = 0.085D;

    /*
     * Trick only starts floating when the player is
     * noticeably higher than Trick.
     */
    private static final double FLOAT_HEIGHT_THRESHOLD = 1.0D;

    /*
     * How quickly Trick rises toward the player's height.
     */
    private static final double FLOAT_SPEED = 0.08D;

    /*
     * Blindness duration.
     *
     * 20 ticks = 1 second.
     */
    private static final int BLINDNESS_DURATION = 40;

    /*
     * Prevents Trick from constantly refreshing blindness
     * every single tick.
     */
    private int blindnessCooldown = 0;

    public TrickEntity(
            EntityType<? extends PathfinderMob> type,
            Level level
    ) {
        super(type, level);

        this.setPersistenceRequired();
        this.setInvulnerable(true);
    }

    public static AttributeSupplier.Builder createAttributes() {

        return Mob.createMobAttributes()

                .add(
                        Attributes.MAX_HEALTH,
                        40.0D
                )

                .add(
                        Attributes.MOVEMENT_SPEED,
                        MOVEMENT_SPEED
                )

                .add(
                        Attributes.FOLLOW_RANGE,
                        FOLLOW_RANGE
                )

                .add(
                        Attributes.KNOCKBACK_RESISTANCE,
                        1.0D
                );
    }

    @Override
    protected void registerGoals() {

        /*
         * Look at nearby players.
         */
        this.goalSelector.addGoal(
                0,
                new LookAtPlayerGoal(
                        this,
                        Player.class,
                        (float) FOLLOW_RANGE
                )
        );
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

        /*
         * Find the nearest player.
         */
        Player player = this.level().getNearestPlayer(
                this,
                FOLLOW_RANGE
        );

        if (player == null) {
            this.getNavigation().stop();
            return;
        }

        /*
         * Look toward the player.
         */
        this.getLookControl().setLookAt(
                player,
                180.0F,
                180.0F
        );

        this.setYRot(
                this.getYHeadRot()
        );

        /*
         * =====================================================
         * NORMAL FOLLOWING
         * =====================================================
         *
         * Trick normally walks toward the player.
         */
        double verticalDifference =
                player.getY() - this.getY();

        /*
         * If the player is NOT significantly above Trick,
         * use normal pathfinding.
         */
        if (verticalDifference <= FLOAT_HEIGHT_THRESHOLD) {

            this.getNavigation().moveTo(
                    player,
                    1.0D
            );

        }

        /*
         * =====================================================
         * FLOATING
         * =====================================================
         *
         * Only activate when the player is higher than Trick.
         */
        else {

            /*
             * Stop normal pathfinding while floating.
             */
            this.getNavigation().stop();

            /*
             * Still move horizontally toward the player.
             */
            Vec3 horizontalTarget =
                    new Vec3(
                            player.getX(),
                            this.getY(),
                            player.getZ()
                    );

            Vec3 movement =
                    horizontalTarget.subtract(
                            this.position()
                    );

            if (movement.horizontalDistance() > 0.5D) {

                Vec3 horizontalMovement =
                        new Vec3(
                                movement.x,
                                0.0D,
                                movement.z
                        ).normalize().scale(0.035D);

                /*
                 * Rise toward the player's height.
                 */
                double verticalMovement =
                        Math.min(
                                FLOAT_SPEED,
                                verticalDifference * 0.05D
                        );

                this.setDeltaMovement(
                        horizontalMovement.x,
                        verticalMovement,
                        horizontalMovement.z
                );

                this.hasImpulse = true;
            }
        }

        /*
         * =====================================================
         * BLINDNESS
         * =====================================================
         */

        if (blindnessCooldown > 0) {
            blindnessCooldown--;
        }

        /*
         * Apply blindness when Trick is near the player.
         */
        if (
                this.distanceTo(player) <= 10.0D
                        && blindnessCooldown <= 0
        ) {

            player.addEffect(
                    new MobEffectInstance(
                            MobEffects.BLINDNESS,
                            BLINDNESS_DURATION,
                            0,
                            false,
                            true,
                            true
                    )
            );

            blindnessCooldown = 20;
        }
    }

    @Override
    public boolean hurt(
            DamageSource source,
            float amount
    ) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(
            DamageSource source
    ) {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(
            double distanceToClosestPlayer
    ) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.TRICK_AMBIENT.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 300;
    }
}