package com.jax.funaethermod.entity;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.registry.ModSounds;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import com.jax.funaethermod.registry.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

public class HollowEntity extends PathfinderMob {

     private static final ResourceKey<Level> AETHER_DIMENSION =
            ResourceKey.create(
                    Registries.DIMENSION,
                    new ResourceLocation(
                            FunAetherMod.MODID,
                            "aether"
                        )
            );
                 

    public HollowEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setInvulnerable(true);
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
                new LookAtPlayerGoal(this, Player.class, 128.0F)
        );
    }

    private int stareTimer = 0;

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

        this.getLookControl().setLookAt(player, 180.0F, 180.0F);
        this.setYRot(this.getYHeadRot());

        double dot = player.getLookAngle()
                .normalize()
                .dot(
                        this.position()
                                .subtract(player.position())
                                .normalize()
                );

        boolean lookingAtEntity = dot > 0.95D;

             if (lookingAtEntity) {
                     stareTimer++;
                                } else {
                                    stareTimer = 0;
                                        }

                                        if (stareTimer >= 80 && player instanceof ServerPlayer serverPlayer) {

            int outcome = this.getRandom().nextInt(4);

            if (outcome < 3) {
                serverPlayer.connection.disconnect(
                    Component.literal("NIRVANA")
                );
                return;

        } else {
        ServerLevel aether =
                serverPlayer.server.getLevel(
                        AETHER_DIMENSION
                );
        }
    }


        /*
         * ============================================================
         * OPTIONAL: OBSERVER STATES
         * ============================================================
         *
         * Add these fields to the class if you want to use this system:
         *
         * private enum ObserverState {
         *     WATCHING,
         *     DISTURBED,
         *     PANICKING
         * }
         *
         * private ObserverState state = ObserverState.WATCHING;
         * private int stareTimer = 0;
         * private int stateTimer = 0;
         */

        /*
        if (lookingAtEntity) {
            stareTimer++;
        } else {
            stareTimer = 0;
            state = ObserverState.WATCHING;
            stateTimer = 0;
        }

        if (stareTimer >= 80) {
            state = ObserverState.DISTURBED;
        }

        if (state == ObserverState.DISTURBED
                && stareTimer >= 160) {
            state = ObserverState.PANICKING;
        }
        */


        /*
         * ============================================================
         * OPTIONAL: AMBIENT SOUNDS
         * ============================================================
         *
         * Add this field to the class:
         *
         * private int ambientTimer = 40;
         */

        /*
        ambientTimer++;

        if (ambientTimer >= 120) {
            ambientTimer = 0;

            this.playSound(
                    ModSounds.HOLLOW_AMBIENT.get(),
                    1.0F,
                    1.0F
            );
        }
        */


        /*
         * ============================================================
         * OPTIONAL: TRANSFORMATION
         * ============================================================
         *
         * Add these fields to the class:
         *
         * private int transformTimer = 0;
         * private boolean transforming = false;
         */

        /*
        if (this.distanceTo(player) <= 3.0F) {
            transformTimer++;
        }

        if (transformTimer >= 80) {
            transformIntoEntity(player);
        }
        */


        /*
         * ============================================================
         * OPTIONAL: TRANSFORMATION METHOD
         * ============================================================
         */

        /*
        private void transformIntoEntity(Player player) {

            if (transforming) {
                return;
            }

            transforming = true;

            HollowEntity hollowEntity =
                    new HollowEntity(
                            ModEntities.HOLLOW.get(),
                            this.level()
                    );

            if (hollowEntity != null) {

                hollowEntity.moveTo(
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        this.getYRot(),
                        this.getXRot()
                );

                this.level().addFreshEntity(hollowEntity);
            }

            this.discard();
        }
        */


        /*
         * ============================================================
         * OPTIONAL: TRANSFORMATION SOUND
         * ============================================================
         */

        /*
        this.playSound(
                ModSounds.HOLLOW_TRANSFORM.get(),
                1.0F,
                1.0F
        );
        */


        /*
         * ============================================================
         * OPTIONAL: LOGGER / DEBUG MESSAGES
         * ============================================================
         */

        /*
        LOGGER.info(
                "Hollow is observing {}",
                player.getName().getString()
        );
        */
    }


    /*
     * ================================================================
     * OPTIONAL: DAMAGE IMMUNITY
     * ================================================================
     */

    /*
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
    */
}