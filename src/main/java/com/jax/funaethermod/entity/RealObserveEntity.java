package com.jax.funaethermod.entity;

import com.jax.funaethermod.registry.ModEntities;
import com.jax.funaethermod.registry.ModSounds;

import net.minecraft.server.level.ServerLevel;
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
    UNCOMFORTABLE,
    BREAKING,
    TRANSFORMING
}

private ObserverState state = ObserverState.WATCHING;

private int ambientTimer = 0;
private int stareTimer = 0;
private int stateTimer = 0;

private boolean active = true;
private boolean transforming = false;

    public RealObserveEntity(
            EntityType<? extends PathfinderMob> type,
            Level level) {

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


        if (!active || this.level().isClientSide)
            return;



        Player player =
                this.level().getNearestPlayer(
                        this,
                        128.0D
                );


        if (player == null)
            return;



        // ================= LOOKING =================

        this.getLookControl().setLookAt(
                player,
                180.0F,
                180.0F
        );


        this.setYRot(
                this.getYHeadRot()
        );




        // ================= HUM =================

        ambientTimer++;


        if (ambientTimer >= 80) {

            ambientTimer = 0;


            double distance =
                    this.distanceTo(player);


            float volume =
                    Math.max(
                            0.05F,
                            1.0F - ((float) distance / 80.0F)
                    );


            this.playSound(
                    ModSounds.REAL_AMBIENT.get(),
                    volume,
                    1.0F
            );
        }




        



        // 10 seconds staring

        if (player.hasLineOfSight(this)) {

    stareTimer++;

} else {

    stareTimer = 0;

    state = ObserverState.WATCHING;
    stateTimer = 0;
}

switch (state) {

    case WATCHING:

        if (stareTimer >= 120) {

            state = ObserverState.UNCOMFORTABLE;
            stateTimer = 0;
        }

        break;

    case UNCOMFORTABLE:

        stateTimer++;

        // tiny head twitch
        if (random.nextInt(35) == 0) {

            this.setYRot(this.getYRot() + random.nextInt(12) - 6);
        }

        if (stateTimer >= 80) {

            state = ObserverState.BREAKING;
            stateTimer = 0;
        }

        break;

    case BREAKING:

        stateTimer++;

        // body jitter
        if (random.nextInt(5) == 0) {

            this.setDeltaMovement(
                    (random.nextDouble() - 0.5D) * 0.08D,
                    0,
                    (random.nextDouble() - 0.5D) * 0.08D
            );
        }

        if (stateTimer >= 40) {

            state = ObserverState.TRANSFORMING;
            stateTimer = 0;
        }

        break;

    case TRANSFORMING:

        stateTimer++;

        if (stateTimer == 1) {

            this.playSound(
                    ModSounds.REAL_GLITCH.get(),
                    1.0F,
                    0.8F
            );
        }

        if (stateTimer >= 20 &&
                this.level().getDayTime() >= 24000) {

            transformIntoReal(player);
        }

        break;
}





        // ================= CLOSE =================

        if (this.distanceTo(player) <= 12.0F) {

            active = false;

            this.discard();
        }
    }




    private void transformIntoReal(Player player) {


        if (transforming)
            return;


        transforming = true;


        if (level() instanceof ServerLevel serverLevel) {


            var real =
                    ModEntities.REAL.get()
                            .create(serverLevel);



            if (real != null) {


                double distance = 14.0D;

double x =
        player.getX()
        - player.getLookAngle().x * distance;

double z =
        player.getZ()
        - player.getLookAngle().z * distance;



                real.moveTo(
                        x,
                        player.getY(),
                        z
                );


                serverLevel.addFreshEntity(real);
            }
        }


        this.playSound(
                ModSounds.REAL_GLITCH.get(),
                1.0F,
                0.8F
        );


        this.discard();
    }




    @Override
    public boolean hurt(
            DamageSource source,
            float amount) {

        return false;
    }



    @Override
    public boolean isInvulnerableTo(
            DamageSource source) {

        return true;
    }



    @Override
    public boolean removeWhenFarAway(
            double distanceToClosestPlayer) {

        return false;
    }
}