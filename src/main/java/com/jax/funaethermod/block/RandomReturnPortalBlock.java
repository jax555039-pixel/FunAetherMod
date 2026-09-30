package com.jax.funaethermod.block;

import com.jax.funaethermod.FunAetherMod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraftforge.common.util.ITeleporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class RandomReturnPortalBlock extends Block {

    public static final EnumProperty<Direction.Axis> AXIS =
            BlockStateProperties.HORIZONTAL_AXIS;

    private static final VoxelShape SHAPE =
            Shapes.box(
                    0.0D,
                    0.0D,
                    0.375D,
                    1.0D,
                    1.0D,
                    0.625D
            );

    private static final Random RANDOM =
            new Random();

    public RandomReturnPortalBlock(Properties properties) {

        super(properties);

        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(
                                AXIS,
                                Direction.Axis.X
                        )
        );
    }

    @Override
    public VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {

        return SHAPE;
    }

    @Override
    public void entityInside(
            BlockState state,
            Level level,
            BlockPos pos,
            Entity entity
    ) {

        if (level.isClientSide)
            return;

        if (!(entity instanceof ServerPlayer player))
            return;

        if (player.isChangingDimension())
            return;

        if (!(level instanceof ServerLevel serverLevel))
            return;

        ResourceKey<Level> currentDimension =
                serverLevel.dimension();

        /*
         * =========================================================
         * POSSIBLE DESTINATIONS
         * =========================================================
         *
         * The current dimension will be removed from this list.
         */

        List<ResourceKey<Level>> destinations =
                new ArrayList<>();

        ResourceKey<Level> sd1Ca =
                dimension("sd1_ca");

        ResourceKey<Level> haven =
                dimension("haven");

        ResourceKey<Level> purgatory =
                dimension("purgatory");

        ResourceKey<Level> subsequence =
                dimension("subsequence");

        ResourceKey<Level> aether =
            dimension("aether");

        destinations.add(sd1Ca);
        destinations.add(haven);
        destinations.add(purgatory);
        destinations.add(subsequence);
        destinations.add(aether);

        /*
         * Never send the player back into
         * the dimension they are already in.
         */

        destinations.remove(currentDimension);

        /*
         * If there are somehow no valid destinations,
         * do nothing instead of teleporting incorrectly.
         */

        if (destinations.isEmpty())
            return;

        /*
         * =========================================================
         * RANDOM DESTINATION
         * =========================================================
         */

        ResourceKey<Level> destinationKey =
                destinations.get(
                        RANDOM.nextInt(
                                destinations.size()
                        )
                );

        ServerLevel destination =
                serverLevel
                        .getServer()
                        .getLevel(destinationKey);

        if (destination == null)
            return;

        /*
         * =========================================================
         * TELEPORT
         * =========================================================
         */

        player.changeDimension(
                destination,
                new ITeleporter() {

                    @Override
                    public Entity placeEntity(
                            Entity entity,
                            ServerLevel currentLevel,
                            ServerLevel destinationLevel,
                            float yaw,
                            Function<Boolean, Entity> repositionEntity
                    ) {

                        Entity movedEntity =
                                repositionEntity.apply(false);

                        if (movedEntity instanceof ServerPlayer movedPlayer) {

                            BlockPos safeSpawn;

                            /*
                             * Haven has a specific destination.
                             */

                            if (
                                    destinationLevel
                                            .dimension()
                                            .location()
                                            .equals(
                                                    new ResourceLocation(
                                                            FunAetherMod.MODID,
                                                            "haven"
                                                    )
                                            )
                            ) {

                                safeSpawn =
                                        new BlockPos(
                                                0,
                                                100,
                                                0
                                        );

                            }

                            /*
                             * Every other destination gets
                             * a naturally safe location.
                             */

                            else {

                                safeSpawn =
                                        findSafeSpawn(
                                                destinationLevel
                                        );
                            }

                            movedPlayer.teleportTo(
                                    destinationLevel,
                                    safeSpawn.getX() + 0.5D,
                                    safeSpawn.getY(),
                                    safeSpawn.getZ() + 0.5D,
                                    yaw,
                                    movedPlayer.getXRot()
                            );
                        }

                        movedEntity.setDeltaMovement(
                                0,
                                0,
                                0
                        );

                        return movedEntity;
                    }
                }
        );
    }

    /*
     * =============================================================
     * DIMENSION KEY HELPER
     * =============================================================
     */

    private ResourceKey<Level> dimension(
            String name
    ) {

        return ResourceKey.create(
                Registries.DIMENSION,
                new ResourceLocation(
                        FunAetherMod.MODID,
                        name
                )
        );
    }

    /*
     * =============================================================
     * SAFE SPAWN FINDER
     * =============================================================
     *
     * Searches around 0,120,0 for a location where:
     *
     * - there is solid ground
     * - the player's feet are empty
     * - the player's head is empty
     * - there are no fluids
     * - there is solid terrain around the platform
     *
     */

    private BlockPos findSafeSpawn(
            ServerLevel level
    ) {

        BlockPos center =
                new BlockPos(
                        0,
                        120,
                        0
                );

        for (
                int radius = 0;
                radius < 256;
                radius++
        ) {

            for (
                    int x = -radius;
                    x <= radius;
                    x++
            ) {

                for (
                        int z = -radius;
                        z <= radius;
                        z++
                ) {

                    BlockPos floor =
                            center.offset(
                                    x,
                                    0,
                                    z
                            );

                    /*
                     * Drop downward until terrain
                     * is found.
                     */

                    while (
                            floor.getY()
                                    > level.getMinBuildHeight()

                            &&

                            level.isEmptyBlock(
                                    floor
                            )
                    ) {

                        floor =
                                floor.below();
                    }

                    BlockPos feet =
                            floor.above();

                    BlockPos head =
                            feet.above();

                    /*
                     * Make sure this is actually
                     * somewhere the player can stand.
                     */

                    if (
                            level.getBlockState(
                                    floor
                            ).isSolid()

                            &&

                            level.isEmptyBlock(
                                    feet
                            )

                            &&

                            level.isEmptyBlock(
                                    head
                            )

                            &&

                            level.getFluidState(
                                    feet
                            ).isEmpty()

                            &&

                            level.getFluidState(
                                    head
                            ).isEmpty()

                            &&

                            level.getBlockState(
                                    floor.east()
                            ).isSolid()

                            &&

                            level.getBlockState(
                                    floor.west()
                            ).isSolid()

                            &&

                            level.getBlockState(
                                    floor.north()
                            ).isSolid()

                            &&

                            level.getBlockState(
                                    floor.south()
                            ).isSolid()
                    ) {

                        return feet;
                    }
                }
            }
        }

        /*
         * Emergency fallback.
         */

        return new BlockPos(
                0,
                80,
                0
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {

        builder.add(
                AXIS
        );
    }
}