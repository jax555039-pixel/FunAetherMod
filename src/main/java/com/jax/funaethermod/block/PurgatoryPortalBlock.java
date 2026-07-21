package com.jax.funaethermod.block;

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

import java.util.function.Function;


public class PurgatoryPortalBlock extends Block {


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


    public PurgatoryPortalBlock(Properties properties) {
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


        if (level.isClientSide) {
            return;
        }


        if (!(entity instanceof ServerPlayer player)) {
            return;
        }


        if (player.isChangingDimension()) {
            return;
        }


        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        ResourceKey<Level> PURGATORY =
                ResourceKey.create(
                        Registries.DIMENSION,
                        new ResourceLocation(
                                "funaethermod",
                                "purgatory"
                        )
                );


        ServerLevel destination =
                serverLevel
                        .getServer()
                        .getLevel(PURGATORY);


        if (destination == null) {
            return;
        }



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


                            BlockPos safeSpawn =
                                    findSafepurgatorySpawn(
                                            destinationLevel
                                    );



                            movedPlayer.teleportTo(
                                    destinationLevel,
                                    safeSpawn.getX() + 0.5,
                                    safeSpawn.getY(),
                                    safeSpawn.getZ() + 0.5,
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




    private BlockPos findSafepurgatorySpawn(
            ServerLevel level
    ) {


        BlockPos center =
                new BlockPos(
                        0,
                        100,
                        0
                );



        /*
         * Searches a 256 block radius
         * looking for:
         *
         *  - solid floor
         *  - two blocks of player space
         *  - no fluids
         *
         */


        for (int radius = 0; radius < 256; radius++) {


            for (int x = -radius; x <= radius; x++) {


                for (int z = -radius; z <= radius; z++) {


                    BlockPos floor =
                            center.offset(
                                    x,
                                    0,
                                    z
                            );



                    // Drop from sky until we hit terrain

                    while (
                            floor.getY()
                                    > level.getMinBuildHeight()

                            &&
                            level.isEmptyBlock(floor)
                    ) {

                        floor =
                                floor.below();
                    }



                    BlockPos feet =
                            floor.above();



                    BlockPos head =
                            feet.above();
                            
                    // Check that the player can stand here

                    if (
        level.getBlockState(floor)
                .isSolid()

        && level.isEmptyBlock(feet)

        && level.isEmptyBlock(head)

        && level.getFluidState(feet)
                .isEmpty()

        && level.getFluidState(head)
                .isEmpty()

        && level.getBlockState(floor.east())
                .isSolid()

        && level.getBlockState(floor.west())
                .isSolid()

        && level.getBlockState(floor.north())
                .isSolid()

        && level.getBlockState(floor.south())
                .isSolid()
){


                        return feet;
                    }
                }
            }
        }



        // Emergency fallback if no platform is found

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