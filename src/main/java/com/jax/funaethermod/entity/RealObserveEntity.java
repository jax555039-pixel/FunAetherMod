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

    private int ambientTimer = 0;
    private int stareTimer = 0;

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




        // ================= STARE =================

        if (player.hasLineOfSight(this)) {

            stareTimer++;

        } else {

            stareTimer = 0;
        }



        // 10 seconds staring

        if (stareTimer >= 200) {


            if (this.level().getDayTime() >= 24000) {

                transformIntoReal(player);
            }
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


                double angle =
                        Math.random()
                        * Math.PI
                        * 2;


                double distance =
                        10
                        + Math.random()
                        * 10;



                double x =
                        player.getX()
                        + Math.cos(angle)
                        * distance;


                double z =
                        player.getZ()
                        + Math.sin(angle)
                        * distance;



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