
package com.jax.funaethermod.entity;

import com.jax.funaethermod.registry.ModSounds;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import com.jax.funaethermod.registry.ModBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import com.jax.funaethermod.network.ModNetwork;
import net.minecraft.server.level.ServerPlayer;

public class CorruptEntity extends PathfinderMob {

    private static final double ATTACK_SPEED = 1.25D;
    private static final float ATTACK_DAMAGE = 8.0F;



    public CorruptEntity(
            EntityType<? extends PathfinderMob> type,
            Level level) {

        super(type, level);

        // Extra:
        // this.setPersistenceRequired();
        // this.setInvulnerable(true);
    }


    public static AttributeSupplier.Builder createAttributes() {

        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 700.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.37D)
                .add(Attributes.FOLLOW_RANGE, 160.0D)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE);

        // Extra:
        // .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }


    @Override
    protected void registerGoals() {


        /*
        * ============================================================
        * MAIN: SOUNDS
        * ============================================================
        */

        if (this.tickCount == 1) {
            this.playSound(this.getAmbientSound(), 1.0F, 1.0F);
        }

        /*
         * ============================================================
         * MAIN: ATTACKING
         * ============================================================
         */

        this.goalSelector.addGoal(
                0,
                new MeleeAttackGoal(
                        this,
                        ATTACK_SPEED,
                        true
                )
        );

        this.targetSelector.addGoal(
                0,
                new NearestAttackableTargetGoal<>(
                        this,
                        Player.class,
                        true
                )
        );


        /*
         * ============================================================
         * OPTIONAL: LOOKING AT PLAYER
         * ============================================================
        */

        this.goalSelector.addGoal(
                2,
                new LookAtPlayerGoal(
                        this,
                        Player.class,
                        80.0F
                )
        );
        

    }


    @Override
    public void tick() {

        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        
for (Player player : this.level().getEntitiesOfClass(
        Player.class,
        this.getBoundingBox()
)) {
    if (player instanceof ServerPlayer serverPlayer
            && this.getBoundingBox().intersects(
                    player.getBoundingBox())) {

        ModNetwork.sendCorruptFlash(serverPlayer);

        // play the corrupt_screen_flash sound effect
        this.level().playSound(
                null,
                this.blockPosition(),
                ModSounds.CORRUPT_SCREEN_FLASH.get(),
                SoundSource.HOSTILE,
                1.0F,
                1.0F
        );

        this.discard();
        return;
    }
}


        // Corrupted block placement logic
        BlockPos blockPos = this.blockPosition().below();
        BlockState state = ModBlocks.PURGATORY_GRASS.get().defaultBlockState();

        if (this.level().getBlockState(blockPos).isSolidRender(this.level(), blockPos)) {
            this.level().setBlock(blockPos, state, 3);
        }
    }

    

    

    /*
     * ================================================================
     * MAIN: CUSTOM ATTACK
     * ================================================================
     */

    @Override
    public boolean doHurtTarget(
            net.minecraft.world.entity.Entity target) {

        if (target instanceof Player player) {

            float damage = ATTACK_DAMAGE;

            player.hurt(
                    this.damageSources().mobAttack(this),
                    damage
            );

            return true;
        }

        return false;
    }

    // MAIN: SOUNDS PT: 2

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.CORRUPT_CHASE.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 100;
    }
}