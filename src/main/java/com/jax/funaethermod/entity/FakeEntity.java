package com.jax.funaethermod.entity;

import com.jax.funaethermod.registry.ModSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FakeEntity extends PathfinderMob {

    /*
     * How far Fake can detect players.
     */
    private static final double OBSERVE_RANGE = 128.0D;

    /*
     * 20 ticks = 1 second.
     * 600 ticks = 30 seconds.
     */
    private static final int LIFE_TIME = 600;

    /*
     * Lifetime counter.
     */
    private int lifeTimer = 0;


    public FakeEntity(
            EntityType<? extends PathfinderMob> type,
            Level level
    ) {
        super(type, level);

        /*
         * Fake cannot be damaged.
         */
        this.setInvulnerable(true);

        /*
         * Prevent Minecraft from removing Fake
         * because it is far away.
         */
        this.setPersistenceRequired();
    }


    /*
     * ENTITY ATTRIBUTES
     */
    public static AttributeSupplier.Builder createAttributes() {

        return Mob.createMobAttributes()

                .add(
                        Attributes.MAX_HEALTH,
                        20.0D
                )

                /*
                 * Zero movement speed.
                 * Fake does not walk.
                 */
                .add(
                        Attributes.MOVEMENT_SPEED,
                        0.0D
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
     * No AI goals.
     *
     * Fake does not chase or move.
     */
    @Override
    protected void registerGoals() {
    }


    /*
     * Runs every game tick.
     */
    @Override
    public void tick() {

        super.tick();

        /*
         * Only run the custom behavior
         * on the server.
         */
        if (this.level().isClientSide) {
            return;
        }


        /*
         * Find the nearest player.
         */
        Player player =
                this.level().getNearestPlayer(
                        this,
                        OBSERVE_RANGE
                );


        /*
         * If there is a player nearby,
         * look directly at them.
         */
        if (player != null) {

            this.getLookControl().setLookAt(
                    player,
                    180.0F,
                    180.0F
            );

            this.setYRot(
                    this.getYHeadRot()
            );
        }


        /*
         * Increase lifetime.
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
     * Fake is completely invulnerable.
     */
    @Override
    public boolean isInvulnerableTo(
            DamageSource source
    ) {
        return true;
    }


    /*
     * Prevent Minecraft from removing Fake
     * because it is far away.
     */
    @Override
    public boolean removeWhenFarAway(
            double distanceToClosestPlayer
    ) {
        return false;
    }


    /*
     * Fake's ambient sound.
     */
    @Override
    protected SoundEvent getAmbientSound() {

        return ModSounds.FAKE_AMBIENT.get();
    }


    /*
     * 300 ticks = 15 seconds.
     */
    @Override
    public int getAmbientSoundInterval() {

        return 300;
    }
}