package com.jax.funaethermod.portal;

import com.jax.funaethermod.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class DimensionPortalManager {

    private static final Set<String> GENERATED_AETHER =
            new HashSet<>();

    private static final Set<String> GENERATED_PURGATORY =
            new HashSet<>();


    // ==========================
    // AETHER RETURN PORTAL
    // ==========================

    public static void generateAetherReturnPortal(
            ServerLevel level,
            BlockPos pos
    ) {

        String id =
                pos.getX()
                + ","
                + pos.getZ();

        if (GENERATED_AETHER.contains(id))
            return;

        GENERATED_AETHER.add(id);

        BlockState portal =
                ModBlocks.AETHER_PORTAL
                        .get()
                        .defaultBlockState();

        for (int y = 0; y < 50; y++) {

            level.setBlock(
                    pos.above(y),
                    portal,
                    3
            );

        }

    }


    // ==========================
    // PURGATORY RETURN PORTAL
    // ==========================

    public static void generatePurgatoryReturnPortal(
            ServerLevel level,
            BlockPos pos
    ) {

        String id =
                pos.getX()
                + ","
                + pos.getZ();

        if (GENERATED_PURGATORY.contains(id))
            return;

        GENERATED_PURGATORY.add(id);

        BlockState portal =
                ModBlocks.PURGATORY_PORTAL
                        .get()
                        .defaultBlockState();

        for (int y = 0; y < 50; y++) {

            level.setBlock(
                    pos.above(y),
                    portal,
                    3
            );

        }

    }

}