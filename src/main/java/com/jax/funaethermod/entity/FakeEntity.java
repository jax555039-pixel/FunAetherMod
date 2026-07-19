package com.jax.funaethermod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import com.jax.funaethermod.registry.ModSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;



public class FakeEntity extends PathfinderMob {

    private static final double OBSERVE_RANGE = 64.0D;
    private static final double FLOAT_DISTANCE = 5.0D;
    private static final double FLOAT_HEIGHT = 2.0D;
    private static final double FLOAT_SPEED = 0.05D;

    public FakeEntity(EntityType<? extends PathfinderMob> type, Level level) {
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
                new LookAtPlayerGoal(this, Player.class, (float) OBSERVE_RANGE)
        );
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        Player player = this.level().getNearestPlayer(this, OBSERVE_RANGE);

        if (player == null) {
            return;
        }

        // Always stare at the player.
        this.getLookControl().setLookAt(player, 180.0F, 180.0F);
        this.setYRot(this.getYHeadRot());

        // Position 5 blocks in front of where the player is looking.
        Vec3 targetPos = player.position()
                .add(player.getLookAngle().scale(FLOAT_DISTANCE))
                .add(0.0D, FLOAT_HEIGHT, 0.0D);

        // Direction to the target.
        Vec3 movement = targetPos.subtract(this.position());

        // Move slowly toward it.
        if (movement.length() > 0.25D) {
            this.setDeltaMovement(
                    movement.normalize().scale(FLOAT_SPEED)
            );
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }

        this.hasImpulse = true;
    }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(net.minecraft.world.damagesource.DamageSource source) {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
protected SoundEvent getAmbientSound() {
    return ModSounds.FAKE_AMBIENT.get();
}

@Override
public int getAmbientSoundInterval() {
    return 300;
}
}

/*
=========================================================
                 LEARNING CORNER
=========================================================

Constructor
----------
Runs when the FakeEntity is created.
We make it persistent and invulnerable.

createAttributes()
------------------
Gives the entity its stats.
Health, movement speed, follow range, etc.

registerGoals()
---------------
Adds built-in Minecraft AI.
Right now the Fake only has one goal:
Look at nearby players.

tick()
------
Runs 20 times every second.

Every tick we:
1. Find the nearest player.
2. Face the player.
3. Calculate a point 5 blocks in front of them.
4. Slowly float toward that point.

This is custom behavior that Minecraft doesn't provide
with normal AI goals.

hurt()
------
Stops the entity from taking damage.

isInvulnerableTo()
------------------
Makes every damage source fail.

removeWhenFarAway()
-------------------
Keeps the entity loaded even when players leave the area.
*/