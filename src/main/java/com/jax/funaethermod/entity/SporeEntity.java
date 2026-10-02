package com.jax.funaethermod.entity;

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
import com.jax.funaethermod.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

public class SporeEntity extends PathfinderMob {

    public SporeEntity(EntityType<? extends PathfinderMob> type, Level level) {
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

    private int despawnTimer = 0;

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        despawnTimer++;

        if (despawnTimer >= 600) {

            RandomSource random = this.getRandom();

            for (int i = 0; i < 15; i++) {

                int x = this.blockPosition().getX() + random.nextInt(11) - 5;
                int y = this.blockPosition().getY() + random.nextInt(11) - 5;
                int z = this.blockPosition().getZ() + random.nextInt(11) - 5;

                BlockPos pos = new BlockPos(x, y, z);

                this.level().setBlock(
                    pos,
                    ModBlocks.PURGATORY_GRASS.get().defaultBlockState(),
                    3
                );
            }

            this.discard();
            return;
        }


        Player player = this.level().getNearestPlayer(this, 128.0D);

        if (player == null) {
            return;
        }

        if (this.distanceTo(player) <= 5.0F && player instanceof ServerPlayer serverPlayer) {
        serverPlayer.connection.disconnect(
            Component.literal("error.disconnected.O#QF#")
        );
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
    }

    


        /*
         * ============================================================
         * OPTIONAL: AMBIENT SOUNDS
         * ============================================================
         *
         * Add this field to the class:
         
         private int ambientTimer = 40;
        

        
        ambientTimer++;

        if (ambientTimer >= 120) {
            ambientTimer = 0;

            this.playSound(
                    ModSounds.(entity name)_AMBIENT.get(),
                    1.0F,
                    1.0F
            );
        }
        
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }*/
}
