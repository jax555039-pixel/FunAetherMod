package com.jax.funaethermod.entity;

import com.jax.funaethermod.registry.ModSounds;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RealObserveEntity extends PathfinderMob {

    private enum ObserverState {
        WATCHING,
        DISTURBED,
        PANICKING,
        TRANSFORMING
    }

    private ObserverState state = ObserverState.WATCHING;
    private int ambientTimer = 40;
    private int stareTimer = 0;
    private int stateTimer = 0;
    private int transformTimer = 0;
    private boolean transforming = false;

    public RealObserveEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setInvulnerable(true);
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 128.0F));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        Player player = this.level().getNearestPlayer(this, 128.0D);
        if (player == null) {
            return;
        }

        if (state != ObserverState.TRANSFORMING) {
            this.getLookControl().setLookAt(player, 180.0F, 180.0F);
            this.setYRot(this.getYHeadRot());
        }

        ambientTimer++;
        if (ambientTimer >= 120) {
            ambientTimer = 0;
            this.playSound(ModSounds.REAL_AMBIENT.get(), 1.0F, 1.0F);
        }

        double dot = player.getLookAngle()
                .normalize()
                .dot(this.position().subtract(player.position()).normalize());

        boolean lookingAtObserver = dot > 0.95D;

        if (lookingAtObserver) {
            stareTimer++;
        } else {
            stareTimer = 0;
            if (state != ObserverState.TRANSFORMING) {
                state = ObserverState.WATCHING;
                stateTimer = 0;
            }
        }

        if (stareTimer >= 80) {
            state = ObserverState.DISTURBED;
        }

        if (state == ObserverState.DISTURBED && stareTimer >= 160) {
            state = ObserverState.PANICKING;
        }

        if (state == ObserverState.TRANSFORMING) {
            transformTimer++;
            if (transformTimer == 1) {
                this.playSound(ModSounds.REAL_TRANSFORM.get(), 1.0F, 1.0F);
            }
            if (transformTimer >= 80) {
                transformIntoReal(player);
            }
        }

        if (this.distanceTo(player) <= 3.0F && state != ObserverState.TRANSFORMING) {
            state = ObserverState.TRANSFORMING;
            transformTimer = 0;
        }
    }

    private void transformIntoReal(Player player) {
        if (transforming) {
            return;
        }

        transforming = true;
        this.discard();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
}
