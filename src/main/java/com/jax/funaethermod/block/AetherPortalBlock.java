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


public class AetherPortalBlock extends Block {


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


    public AetherPortalBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(AXIS, Direction.Axis.X)
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


        // Prevent instant re-triggering
        if (player.isChangingDimension()) {
            return;
        }


        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }


        ResourceKey<Level> AETHER =
                ResourceKey.create(
                        Registries.DIMENSION,
                        new ResourceLocation(
                                "funaethermod",
                                "aether"
                        )
                );


        ServerLevel destination =
                serverLevel
                        .getServer()
                        .getLevel(AETHER);


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


    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(AXIS);
    }
}