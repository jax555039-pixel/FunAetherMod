package com.jax.funaethermod.entity;

import com.jax.funaethermod.registry.ModSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import java.util.Random;

public class Entity2020AttackEntity extends PathfinderMob {

    private static final double STARE_RANGE = 128.0D;
    private static final double ATTACK_SPEED = 1.25D;
    private static final float ATTACK_DAMAGE = 8.0F;
    private static final Random RANDOM = new Random();
    private boolean hasAttacked = false;

    public Entity2020AttackEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);

        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.40D)
                .add(Attributes.FOLLOW_RANGE, 128.0D)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(
                0,
                new MeleeAttackGoal(this, ATTACK_SPEED, true)
        );

        this.goalSelector.addGoal(
                1,
                new LookAtPlayerGoal(this, Player.class, (float) STARE_RANGE)
        );

        this.targetSelector.addGoal(
                0,
                new NearestAttackableTargetGoal<>(
                        this,
                        Player.class,
                        true
                )
        );
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        if (this.tickCount == 1) {
            this.playSound(this.getAmbientSound(), 1.0F, 1.0F);
        }

        Player player = this.level().getNearestPlayer(this, STARE_RANGE);

        if (player != null) {
            this.getLookControl().setLookAt(player, 180.0F, 180.0F);
            this.setYRot(this.getYHeadRot());
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY2020_ATTACK.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 100;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (target instanceof Player player) {
            hasAttacked = true;

            int outcome = RANDOM.nextInt(4);
            if (outcome == 0) {
                player.hurt(this.damageSources().magic(), Float.MAX_VALUE);
            } else {
                player.setHealth(1.0F);
            }

            this.discard();
            return true;
        }

        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}