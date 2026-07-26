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

    private int lifeTimer = 0;

    public PoorBoyEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);

        this.setPersistenceRequired();
        this.setInvulnerable(true);
    }

    public static AttributeSupplier.Builder createAttributes() {

        return Mob.createMobAttributes()

                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18D)
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

        if (this.level().isClientSide)
            return;

        Player player =
                this.level().getNearestPlayer(
                        this,
                        OBSERVE_RANGE
                );

        if (player != null) {

            // Look directly at the player.
            this.getLookControl().setLookAt(
                    player,
                    180.0F,
                    180.0F
            );

            this.setYRot(this.getYHeadRot());
        }

        // Lifetime
        lifeTimer++;

        if (lifeTimer >= getLifeTime()) {
            this.discard();
        }
    }

    private int getLifeTime() {

        long day =
                this.level().getDayTime() / 24000;

        // Day 1 = 10 seconds
        if (day <= 0) {
            return 20 * 10;
        }

        // Day 2+ = 60 seconds
        return 20 * 60;
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