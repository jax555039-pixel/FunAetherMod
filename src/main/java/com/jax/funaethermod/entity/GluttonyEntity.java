package com.jax.funaethermod.entity;

// Extra:
// import com.jax.(mod name).registry.ModSounds;
// import net.minecraft.core.BlockPos;
// import net.minecraft.tags.BlockTags;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

// Extra:
import java.util.Random;

public class GluttonyEntity extends PathfinderMob {

    private static final double ATTACK_SPEED = 1.25D;
    private static final float ATTACK_DAMAGE = 8.0F;

    // Extra:
    // private static final double FOLLOW_RANGE = 160.0D;
    // private static final Random RANDOM = new Random();
    // private int screamTimer = 60;
    private int lifeTimer = 0;
    // private int attackCount = 0;
    // private int aggressionLevel = 0;
    // private double lastPlayerX;
    // private double lastPlayerY;
    // private double lastPlayerZ;
    // private int searchTimer = 0;
    private boolean preparingToLeave = false;
     private int leaveTimer = 0;


    public GluttonyEntity(
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
        


        /*
         * ============================================================
         * OPTIONAL: RANDOM MOVEMENT
         * ============================================================
         */

        
        this.goalSelector.addGoal(
                1,
                new RandomStrollGoal(
                        this,
                        0.6D
                )
        );
        
    }


    @Override
    public void tick() {

        super.tick();

        if (this.level().isClientSide) {
            return;
        }


        /*
         * ============================================================
         * OPTIONAL: LIFE TIMER
         * ============================================================
         */

        /*
        lifeTimer++;

        if (lifeTimer >= 600) {
            aggressionLevel =
                    Math.max(
                            aggressionLevel,
                            1
                    );
        }

        if (lifeTimer >= 1200) {
            aggressionLevel =
                    Math.max(
                            aggressionLevel,
                            2
                    );
        }

        if (lifeTimer >= 2000) {
            aggressionLevel =
                    Math.max(
                            aggressionLevel,
                            3
                    );
        }
        */


        /*
         * ============================================================
         * OPTIONAL: DESPAWN
         * ============================================================
         */

        
        if (lifeTimer >= 2950 && !preparingToLeave) {

            preparingToLeave = true;
            leaveTimer = 0;
        }

        if (preparingToLeave) {

            leaveTimer++;

            if (leaveTimer >= 50) {
                this.discard();
                return;
            }
        }
        


        /*
         * ============================================================
         * OPTIONAL: AGGRESSION / SPEED EVOLUTION
         * ============================================================
         */

        /*
        double speed = 1.30D;

        switch (aggressionLevel) {

            case 0:
                speed = 1.30D;
                break;

            case 1:
                speed = 1.45D;
                break;

            case 2:
                speed = 1.60D;
                break;

            default:
                speed = 1.80D;
                break;
        }
        */


        /*
         * ============================================================
         * OPTIONAL: PLAYER TRACKING
         * ============================================================
         */

        /*
        Player player =
                this.level().getNearestPlayer(
                        this,
                        FOLLOW_RANGE
                );

        if (player != null
                && this.hasLineOfSight(player)) {

            searchTimer = 0;

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

            this.setYRot(
                    this.getYHeadRot()
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
        */


        /*
         * ============================================================
         * OPTIONAL: JITTER
         * ============================================================
         */

        /*
        int jitterChance = 6;

        if (aggressionLevel == 1) {
            jitterChance = 4;

        } else if (aggressionLevel >= 2) {
            jitterChance = 2;
        }

        if (this.random.nextInt(jitterChance) == 0) {

            double power =
                    0.6D
                            + (aggressionLevel * 0.25D);

            this.setDeltaMovement(
                    (this.random.nextDouble() - 0.5D) * power,
                    this.getDeltaMovement().y,
                    (this.random.nextDouble() - 0.5D) * power
            );
        }
        */


        /*
         * ============================================================
         * OPTIONAL: BLOCK BREAKING
         * ============================================================
         */

        /*
        if (player != null) {

            BlockPos playerPos =
                    player.blockPosition();

            for (BlockPos pos :
                    BlockPos.betweenClosed(
                            playerPos.offset(-1, 0, -1),
                            playerPos.offset(1, 2, 1))) {

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
        */


        /*
         * ============================================================
         * OPTIONAL: AMBIENT / GLITCH SOUNDS
         * ============================================================
         */

        /*
        screamTimer++;

        int screamDelay = 400;

        if (screamTimer >= screamDelay) {

            screamTimer = 0;

            this.playSound(
                    ModSounds.(entity name)_GLITCH.get(),
                    1.0F,
                    1.0F
            );
        }
        */
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


    /*
     * ================================================================
     * OPTIONAL: ATTACK COUNT / ESCALATING DAMAGE
     * ================================================================
     */

    /*
    @Override
    public boolean doHurtTarget(
            net.minecraft.world.entity.Entity target) {

        if (target instanceof Player player) {

            attackCount++;

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
                    ModSounds.(entity name)_GLITCH.get(),
                    1.0F,
                    0.8F
            );

            return true;
        }

        return false;
    }
    */


    /*
     * ================================================================
     * OPTIONAL: REDUCED DAMAGE TAKEN
     * ================================================================
     */

    /*
    @Override
    public boolean hurt(
            DamageSource source,
            float amount) {

        float reducedDamage =
                amount * 0.25F;

        return super.hurt(
                source,
                reducedDamage
        );
    }
    */


    /*
     * ================================================================
     * OPTIONAL: NEVER DESPAWN WHEN FAR AWAY
     * ================================================================
     */

    /*
    @Override
    public boolean removeWhenFarAway(
            double distanceToClosestPlayer) {

        return false;
    }
    */


    /*
     * ================================================================
     * OPTIONAL: AMBIENT SOUND
     * ================================================================
     */

    /*
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.(entity name)_AMBIENT.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 600;
    }
    */
}