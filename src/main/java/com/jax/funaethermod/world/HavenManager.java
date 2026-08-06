package com.jax.funaethermod.world;

import com.jax.funaethermod.util.StructureLoader;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class HavenManager {

    public static void onLoad(ServerLevel level) {

        HavenSavedData data =
                HavenSavedData.get(level);

        if (data.isGenerated()) {
            return;
        }

        boolean success =
                StructureLoader.loadStructure(
                        level,
                        "haven_island",
                        new BlockPos(0, 100, 0)
                );

        if (success) {

            data.setGenerated(true);

            System.out.println(
                    "[FunAetherMod] Haven island generated."
            );
        }
    }
}