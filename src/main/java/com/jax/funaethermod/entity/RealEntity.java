package com.jax.funaethermod.entity;

import com.jax.funaethermod.registry.ModSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RealEntity extends PathfinderMob {

    private int screamTimer = 0;
    private int lifeTimer = 0;

    private int attackCount = 0;

    private int aggressionLevel = 0;

    private double lastPlayerX;
    private double lastPlayerY;
    private double lastPlayerZ;

    private int searchTimer = 0;

    private boolean preparingToLeave = false;
    private int leaveTimer = 0;


    public RealEntity(
            EntityType<? extends PathfinderMob> type,
            Level level) {

        super(type, level);

        this.setInvulnerable(true);
        this.setPersistenceRequired();
    }



    public static AttributeSupplier.Builder createAttributes() {

        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.42D)
                .add(Attributes.FOLLOW_RANGE, 160.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D);
    }



    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(
                0,
                new MeleeAttackGoal(this, 1.25D, true)
        );


        this.goalSelector.addGoal(
                1,
                new RandomStrollGoal(this, 0.6D)
        );


        this.goalSelector.addGoal(
                2,
                new LookAtPlayerGoal(
                        this,
                        Player.class,
                        80.0F
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
    }



    @Override
    public void tick() {

        super.tick();


        if (this.level().isClientSide)
            return;



        lifeTimer++;


        // ================= DESPAWN =================

        if (lifeTimer >= 2950 && !preparingToLeave) {

            preparingToLeave = true;

            leaveTimer = 0;

            this.playSound(
                    ModSounds.REAL_GLITCH.get(),
                    0.7F,
                    0.6F
            );
        }


        if (preparingToLeave) {

            leaveTimer++;


            if (leaveTimer >= 50) {

                this.discard();
                return;
            }
        }



        Player player =
                this.level().getNearestPlayer(
                        this,
                        80.0D
                );



        double speed = 1.35D;


        if (aggressionLevel == 1) {

            speed = 1.5D;

        } else if (aggressionLevel >= 2) {

            speed = 1.7D;
        }



        // ================= TRACKING =================


        if (player != null && this.hasLineOfSight(player)) {


            lastPlayerX = player.getX();
            lastPlayerY = player.getY();
            lastPlayerZ = player.getZ();



            this.getNavigation().moveTo(
                    player,
                    speed
            );


            this.getLookControl().setLookAt(
                    player,
                    180F,
                    180F
            );
        }


        else {


            searchTimer++;


            if (searchTimer >= 40) {

                searchTimer = 0;


                this.getNavigation().moveTo(
                        lastPlayerX,
                        lastPlayerY,
                        lastPlayerZ,
                        1.0D
                );
            }
        }




        // ================= JITTER =================


        int jitterChance = 6;


        if (aggressionLevel == 1) {

            jitterChance = 4;

        } else if (aggressionLevel >= 2) {

            jitterChance = 2;
        }



        if (this.random.nextInt(jitterChance) == 0) {


            double power =
                    0.6D + (aggressionLevel * 0.25D);



            this.setDeltaMovement(

                    (this.random.nextDouble() - 0.5D) * power,

                    this.getDeltaMovement().y,

                    (this.random.nextDouble() - 0.5D) * power
            );
        }




        // ================= BLOCK BREAKING =================


        if (player != null) {


            BlockPos playerPos =
                    player.blockPosition();



            for (BlockPos pos :
                    BlockPos.betweenClosed(
                            playerPos.offset(-1,0,-1),
                            playerPos.offset(1,2,1))) {


                var state =
                        level().getBlockState(pos);



                if (state.is(BlockTags.DOORS)
                        || state.is(BlockTags.WOODEN_DOORS)
                        || state.is(BlockTags.LEAVES)) {


                    level().destroyBlock(
                            pos,
                            false
                    );
                }
            }
        }




        // ================= SOUND =================


        screamTimer++;


        int screamDelay =
                180 - (aggressionLevel * 30);


        if (screamTimer >= screamDelay) {


            screamTimer = 0;


            this.playSound(
                    ModSounds.REAL_GLITCH.get(),
                    1.0F,
                    1.0F
            );
        }
    }




    @Override
    public boolean doHurtTarget(
            net.minecraft.world.entity.Entity target) {


        if (target instanceof Player player) {


            attackCount++;



            if (attackCount == 2) {

                aggressionLevel = 1;

            }


            if (attackCount >= 4) {

                aggressionLevel = 2;
            }




            float damage;



            if (attackCount <= 1) {

                damage = 4.0F;

            } else if (attackCount <= 3) {

                damage = 6.0F;

            } else {

                damage = 8.0F;
            }



            player.hurt(
                    this.damageSources().mobAttack(this),
                    damage
            );



            this.playSound(
                    ModSounds.REAL_GLITCH.get(),
                    1.0F,
                    0.8F
            );


            return true;
        }



        return false;
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