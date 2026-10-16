
package com.jax.funaethermod.entity;

import com.jax.funaethermod.registry.ModSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PoorBoyEntity extends PathfinderMob {

    private static final double OBSERVE_RANGE = 64.0D;
    private static final double TRIGGER_RANGE = 15.0D;
    private static final double ASCENT_SPEED = 0.50D;
    private static final double DESPAWN_HEIGHT = 128.0D;

    private boolean ascending = false;
    private double ascentStartY = 0.0D;

    public PoorBoyEntity(
            EntityType<? extends PathfinderMob> type,
            Level level
    ) {
        super(type, level);

        this.setPersistenceRequired();
        this.setInvulnerable(true);

        // Poorboy floats instead of falling.
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, OBSERVE_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(
                0,
                new LookAtPlayerGoal(
                        this,
                        Player.class,
                        (float) OBSERVE_RANGE
                )
        );
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        // Once triggered, Poorboy only flies upward.
        if (ascending) {
            this.setDeltaMovement(
                    0.0D,
                    ASCENT_SPEED,
                    0.0D
            );

            // Despawn after rising 32 blocks.
            if (this.getY() - ascentStartY >= DESPAWN_HEIGHT) {
                this.discard();
            }

            return;
        }

        // Find the nearest player within observation range.
        Player player = this.level().getNearestPlayer(
                this,
                OBSERVE_RANGE
        );

        if (player == null) {
            return;
        }

        // Keep looking directly at the player.
        this.getLookControl().setLookAt(
                player,
                180.0F,
                180.0F
        );

        this.setYRot(this.getYHeadRot());

        // Trigger the escape when the player gets within 15 blocks.
        if (this.distanceToSqr(player)
                <= TRIGGER_RANGE * TRIGGER_RANGE) {

            ascending = true;
            ascentStartY = this.getY();

            // Play Poorboy's existing crying sound on takeoff.
            this.playSound(
                    ModSounds.POORBOY_CRY.get(),
                    1.0F,
                    0.7F
            );

            this.setDeltaMovement(
                    0.0D,
                    ASCENT_SPEED,
                    0.0D
            );
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
        return ModSounds.POORBOY_CRY.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 900;
    }
}
