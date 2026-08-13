package com.jax.funaethermod.entity;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.registry.ModSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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

    private static final double MOVEMENT_SPEED = 0.085D;

    private static final double FLOAT_HEIGHT_THRESHOLD = 1.0D;

    private static final double FLOAT_SPEED = 0.08D;

    private static final int BLINDNESS_DURATION = 40;

    private int blindnessCooldown = 0;

    /*
     * Subsequence dimension.
     */
    private static final ResourceKey<Level> SUBSEQUENCE_DIMENSION =
            ResourceKey.create(
                    Registries.DIMENSION,
                    new ResourceLocation(
                            FunAetherMod.MODID,
                            "subsequence"
                    )
            );

    /*
     * Prevents Trick from teleporting the same player
     * repeatedly every tick.
     */
    private int teleportCooldown = 0;

    /*
     * Distance at which Trick teleports the player.
     */
    private static final double TELEPORT_DISTANCE = 2.0D;

    /*
     * Location inside Subsequence where the player arrives.
     */
    private static final BlockPos SUBSEQUENCE_SPAWN =
            new BlockPos(0, 100, 0);

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

        if (this.level().isClientSide) {
            return;
        }

        /*
         * Teleport cooldown.
         */
        if (teleportCooldown > 0) {
            teleportCooldown--;
        }

        /*
         * Find nearest player.
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
         * Look toward player.
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
         * TELEPORT PLAYER TO SUBSEQUENCE
         * =====================================================
         */

        if (
                teleportCooldown <= 0
                        && this.distanceTo(player) <= TELEPORT_DISTANCE
        ) {

            teleportPlayerToSubsequence(player);

            return;
        }

        /*
         * =====================================================
         * NORMAL FOLLOWING
         * =====================================================
         */

        double verticalDifference =
                player.getY() - this.getY();

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
         */

        else {

            this.getNavigation().stop();

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
                        )
                        .normalize()
                        .scale(0.035D);

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

    /*
     * =========================================================
     * TELEPORT METHOD
     * =========================================================
     */

    private void teleportPlayerToSubsequence(
            Player player
    ) {

        /*
         * Only server players can be teleported
         * between dimensions.
         */
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        ServerLevel subsequence =
                serverPlayer.server.getLevel(
                        SUBSEQUENCE_DIMENSION
                );

        /*
         * Dimension doesn't exist.
         */
        if (subsequence == null) {

            System.out.println(
                    "[FunAetherMod] Subsequence dimension not found."
            );

            return;
        }

        /*
         * Teleport the player.
         */
        serverPlayer.teleportTo(
                subsequence,
                SUBSEQUENCE_SPAWN.getX() + 0.5D,
                SUBSEQUENCE_SPAWN.getY(),
                SUBSEQUENCE_SPAWN.getZ() + 0.5D,
                serverPlayer.getYRot(),
                serverPlayer.getXRot()
        );

        /*
         * Cooldown prevents repeated teleporting.
         */
        teleportCooldown = 100;

        System.out.println(
                "[FunAetherMod] Trick teleported "
                        + serverPlayer.getName().getString()
                        + " to Subsequence."
        );
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
        return 600;
    }
}