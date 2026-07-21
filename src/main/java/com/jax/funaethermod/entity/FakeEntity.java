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
import net.minecraft.world.phys.Vec3;


public class FakeEntity extends PathfinderMob {


    private static final double OBSERVE_RANGE = 64.0D;

    // Distance from player
    private static final double FLOAT_DISTANCE = 5.0D;

    // How high above the player it floats
    private static final double FLOAT_HEIGHT = 2.0D;

    // Floating speed
    private static final double FLOAT_SPEED = 0.05D;


    // How long Fake exists
    private int lifeTimer = 0;



    public FakeEntity(EntityType<? extends PathfinderMob> type, Level level) {

        super(type, level);

        this.setPersistenceRequired();
        this.setInvulnerable(true);

    }



    public static AttributeSupplier.Builder createAttributes() {

        return Mob.createMobAttributes()

                .add(Attributes.MAX_HEALTH, 20.0D)

                .add(
                        Attributes.MOVEMENT_SPEED,
                        0.18D
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



        if(this.level().isClientSide) {
            return;
        }



        Player player =
                this.level()
                        .getNearestPlayer(
                                this,
                                OBSERVE_RANGE
                        );



        if(player == null) {
            return;
        }




        // Look directly at player
        this.getLookControl()
                .setLookAt(
                        player,
                        180.0F,
                        180.0F
                );


        this.setYRot(
                this.getYHeadRot()
        );





        // Position 5 blocks in front of player
        Vec3 targetPos =
                player.position()

                .add(
                        player.getLookAngle()
                                .scale(FLOAT_DISTANCE)
                )

                .add(
                        0.0D,
                        FLOAT_HEIGHT,
                        0.0D
                );




        // Move toward target position
        Vec3 movement =
                targetPos.subtract(
                        this.position()
                );



        if(movement.length() > 0.25D) {

            this.setDeltaMovement(
                    movement.normalize()
                            .scale(FLOAT_SPEED)
            );

        } else {

            this.setDeltaMovement(
                    Vec3.ZERO
            );

        }



        this.hasImpulse = true;





        // Lifetime counter

        lifeTimer++;


        if(lifeTimer >= getLifeTime()) {

            this.discard();

        }

    }






    private int getLifeTime() {


        long day =
                this.level()
                        .getDayTime()
                        / 24000;



        // First day: 10 seconds
        if(day <= 0) {

            return 200;

        }



        // Later days: 1 minute
        return 1200;

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
-----------
Runs when FakeEntity is created.

Makes Fake:
- Persistent
- Invulnerable


createAttributes()
------------------
Defines Fake's stats.

Controls:
- Health
- Speed
- Detection range
- Knockback resistance


registerGoals()
---------------
Adds normal Minecraft AI.

Currently Fake only has:
- Looking at nearby players


tick()
------
Runs 20 times every second.

Fake does:

1. Finds the closest player.

2. Turns its head toward them.

3. Calculates a position:
   
   Player looking direction
            +
       5 blocks forward
            +
        2 blocks upward

4. Slowly floats toward that location.

5. Counts how long it has existed.

6. Removes itself after its lifetime.


getLifeTime()
-------------
Controls Fake's appearance duration.

Day 1:
200 ticks = 10 seconds

Later days:
1200 ticks = 60 seconds


hurt()
------
Prevents Fake from taking damage.


isInvulnerableTo()
------------------
Blocks all damage sources.


removeWhenFarAway()
-------------------
Stops Minecraft from deleting Fake
when the player leaves.


getAmbientSound()
-----------------
Plays Fake's custom sound.

=========================================================
*/