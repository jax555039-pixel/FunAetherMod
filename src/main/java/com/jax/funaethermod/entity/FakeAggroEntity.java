package com.jax.funaethermod.entity;

import com.jax.funaethermod.registry.ModSounds;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FakeAggroEntity extends PathfinderMob {

    /*
     * How far Fake can detect a player.
     */
    private static final double OBSERVE_RANGE = 64.0D;

    /*
     * Fake exists for 30 seconds.
     *
     * 20 ticks = 1 second
     * 600 ticks = 30 seconds
     */
    private static final int LIFE_TIME = 600;

    /*
     * Distance required for Fake to
     * teleport the player.
     */
    private static final double TELEPORT_DISTANCE = 1.5D;

    /*
     * Counts how long Fake has existed.
     */
    private int lifeTimer = 0;

    /*
     * SD1-CA dimension.
     *
     * Dimension ID:
     *
     * funaethermod:sd1-ca
     */
    private static final ResourceKey<Level> SD1_CA_DIMENSION =
            ResourceKey.create(
                    Registries.DIMENSION,
                    new ResourceLocation(
                            "funaethermod",
                            "sd1-ca"
                    )
            );

    public FakeAggroEntity(
            EntityType<? extends PathfinderMob> type,
            Level level
    ) {
        super(type, level);

        /*
         * Prevent Minecraft from removing Fake
         * simply because it is far away.
         */
        this.setPersistenceRequired();

        /*
         * Fake cannot be damaged.
         */
        this.setInvulnerable(true);
    }

    /*
     * Entity attributes.
     */
    public static AttributeSupplier.Builder createAttributes() {

        return Mob.createMobAttributes()
                .add(
                        Attributes.MAX_HEALTH,
                        20.0D
                )
                .add(
                        Attributes.MOVEMENT_SPEED,
                        0.40D
                )
                .add(
                        Attributes.FOLLOW_RANGE,
                        OBSERVE_RANGE
                )
                .add(
                        Attributes.KNOCKBACK_RESISTANCE,
                        1.0D
                );
    }

    /*
     * AI goals.
     */
    @Override
    protected void registerGoals() {

        /*
         * Allows Fake to float in water.
         */
        this.goalSelector.addGoal(
                0,
                new FloatGoal(this)
        );

        /*
         * Makes Fake look at nearby players.
         */
        this.goalSelector.addGoal(
                1,
                new LookAtPlayerGoal(
                        this,
                        Player.class,
                        (float) OBSERVE_RANGE
                )
        );

        /*
         * Selects the nearest player as Fake's target.
         */
        this.targetSelector.addGoal(
                1,
                new NearestAttackableTargetGoal<>(
                        this,
                        Player.class,
                        true
                )
        );
    }

    /*
     * Runs every game tick.
     */
    @Override
    public void tick() {

        super.tick();

        /*
         * Only run custom behavior on the server.
         */
        if (this.level().isClientSide) {
            return;
        }

        /*
         * Find the closest player.
         */
        Player player =
                this.level().getNearestPlayer(
                        this,
                        OBSERVE_RANGE
                );

        /*
         * If a player is nearby,
         * Fake chases them and looks at them.
         */
        if (player != null) {

            /*
             * Look directly at the player.
             */
            this.getLookControl().setLookAt(
                    player,
                    180.0F,
                    180.0F
            );

            /*
             * Chase the player.
             */
            this.getNavigation().moveTo(
                    player,
                    1.0D
            );

            /*
             * Check if Fake has reached the player.
             */
            if (
                    player instanceof ServerPlayer serverPlayer
                    && this.distanceTo(serverPlayer) <= TELEPORT_DISTANCE
            ) {

                /*
                 * Get the SD1-CA dimension.
                 */
                ServerLevel targetLevel =
                        serverPlayer
                                .getServer()
                                .getLevel(SD1_CA_DIMENSION);

                /*
                 * Make sure the dimension exists.
                 */
                if (targetLevel != null) {

                    /*
                     * Teleport the player to SD1-CA.
                     *
                     * Change these coordinates if
                     * you want a different arrival point.
                     */
                    serverPlayer.teleportTo(
                            targetLevel,
                            0.5D,
                            195.0D,
                            0.5D,
                            serverPlayer.getYRot(),
                            serverPlayer.getXRot()
                    );

                    /*
                     * Remove Fake after teleporting
                     * the player.
                     */
                    this.discard();

                    return;
                }
            }
        }

        /*
         * Increase lifetime counter.
         */
        lifeTimer++;

        /*
         * Despawn after 30 seconds.
         */
        if (lifeTimer >= LIFE_TIME) {

            this.discard();

            return;
        }
    }

    /*
     * Fake cannot take damage.
     */
    @Override
    public boolean hurt(
            DamageSource source,
            float amount
    ) {
        return false;
    }

    /*
     * Makes Fake completely invulnerable.
     */
    @Override
    public boolean isInvulnerableTo(
            DamageSource source
    ) {
        return true;
    }

    /*
     * Prevents Minecraft from removing Fake
     * because it is far away.
     */
    @Override
    public boolean removeWhenFarAway(
            double distanceToClosestPlayer
    ) {
        return false;
    }

    /*
     * Fake's chase sound.
     */
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.FAKE_CHASE.get();
    }

    /*
     * 320 ticks = 16 seconds.
     */
    @Override
    public int getAmbientSoundInterval() {
        return 320;
    }
}