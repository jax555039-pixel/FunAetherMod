package com.jax.funaethermod.util;

import java.util.Optional;

import com.jax.funaethermod.FunAetherMod;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class StructureLoader {

    public static boolean loadStructure(
            ServerLevel level,
            String structureName,
            BlockPos pos
    ) {

        StructureTemplateManager manager =
                level.getStructureManager();

        Optional<StructureTemplate> optionalTemplate =
                manager.get(
                        new ResourceLocation(
                                FunAetherMod.MODID,
                                structureName
                        )
                );

        if (optionalTemplate.isEmpty()) {

            System.out.println(
                    "[FunAetherMod] Couldn't find structure: "
                            + structureName
            );

            return false;
        }

        StructureTemplate template =
                optionalTemplate.get();

        template.placeInWorld(
                level,
                pos,
                pos,
                new StructurePlaceSettings(),
                level.random,
                2
        );

        System.out.println(
                "[FunAetherMod] Loaded structure "
                        + structureName
                        + " at "
                        + pos
        );

        return true;
    }
}