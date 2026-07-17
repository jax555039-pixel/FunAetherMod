package com.jax.funaethermod.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Entity2020Entity extends PathfinderMob {

    private static final double STARE_RANGE = 128.0D;
    private static final double DAMAGE_RANGE = 10.0D;

    private int damageCooldown = 0;

    public Entity2020Entity(
            EntityType<? extends PathfinderMob> type,
            Level level
    ) {
        super(type, level);

        this.setPersistenceRequired();
        this.setInvulnerable(true);
    }

    public static AttributeSupplier.Builder createAttributes() {

        return Mob.createMobAttributes()

                .add(Attributes.MAX_HEALTH, 100.0D)

                .add(Attributes.MOVEMENT_SPEED, 0.0D)

                .add(Attributes.FOLLOW_RANGE, 128.0D)

                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(
                0,
                new LookAtPlayerGoal(
                        this,
                        Player.class,
                        128.0F
                )
        );
    }

    @Override
    public void tick() {

        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        Player player = this.level().getNearestPlayer(
                this,
                STARE_RANGE
        );

        if (player == null) {
            return;
        }

        this.getLookControl().setLookAt(
                player,
                180.0F,
                180.0F
        );

        this.setYRot(this.getYHeadRot());

        if (damageCooldown > 0) {
            damageCooldown--;
        }

        if (this.distanceTo(player) <= DAMAGE_RANGE && damageCooldown <= 0) {

            player.hurt(
                    this.damageSources().magic(),
                    2.0F
            );

            damageCooldown = 20;
        }
    }
        @Override
    public boolean hurt(
            net.minecraft.world.damagesource.DamageSource source,
            float amount
    ) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(
            net.minecraft.world.damagesource.DamageSource source
    ) {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(
            double distanceToClosestPlayer
    ) {
        return false;
    }
}