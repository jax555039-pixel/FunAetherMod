package com.jax.funaethermod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AetherPortalFrameBlock extends Block {

    public AetherPortalFrameBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {

        if (!player.getItemInHand(hand).is(Items.WATER_BUCKET)) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (AetherPortalShape.findEmptyAetherPortalShape(level, pos, Direction.Axis.X)
                .map(shape -> {
                    shape.createPortalBlocks();
                    return true;
                })
                .orElse(false)) {

            return InteractionResult.SUCCESS;
        }

        if (AetherPortalShape.findEmptyAetherPortalShape(level, pos, Direction.Axis.Z)
                .map(shape -> {
                    shape.createPortalBlocks();
                    return true;
                })
                .orElse(false)) {

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}