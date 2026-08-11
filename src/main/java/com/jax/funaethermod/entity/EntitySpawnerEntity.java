package com.jax.funaethermod.entity;

import com.jax.funaethermod.registry.ModEntities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class EntitySpawnerEntity extends PathfinderMob {


public EntitySpawnerEntity(
        EntityType<? extends PathfinderMob> type,
        Level level
) {
    super(type, level);
}

public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.2D)
            .add(Attributes.FOLLOW_RANGE, 64.0D);
}

@Override
protected void registerGoals() {
    // No AI behavior.
}

@Override
public void tick() {

    super.tick();

    // Server side only.
    if (this.level().isClientSide)
        return;

    EntityType<?> entityType =
            getRandomEntityType();

    if (entityType == null)
        return;

    Entity spawnedEntity =
            entityType.create(
                    this.level()
            );

    if (spawnedEntity == null)
        return;

    // Spawn at this entity's location.
    spawnedEntity.moveTo(
            this.getX(),
            this.getY(),
            this.getZ(),
            this.getYRot(),
            this.getXRot()
    );

    // Copy current movement.
    spawnedEntity.setDeltaMovement(
            this.getDeltaMovement()
    );

    this.level().addFreshEntity(
            spawnedEntity
    );

    // Remove the EntitySpawner.
    this.discard();
}

/**
 * Temporary random entity selection.
 *
 * This will later be replaced with environment-based
 * selection using things such as:
 *
 * - Dimension
 * - Time of day
 * - Weather
 * - Player health
 * - Surrounding blocks
 * - Other environmental conditions
 */
private EntityType<?> getRandomEntityType() {
    List<EntityType<?>> possibleEntities =
            List.of(
                    ModEntities.ENTITY2020.get(),
                    ModEntities.ENTITY2020_ATTACK.get(),
                    ModEntities.FAKE.get(),
                    ModEntities.POORBOY.get(),
                    ModEntities.REAL_OBSERVE.get()
            );

    return possibleEntities.get(
            this.random.nextInt(
                    possibleEntities.size()
            )
    );

    
}
}

