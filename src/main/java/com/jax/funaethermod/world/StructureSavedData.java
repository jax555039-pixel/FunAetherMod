package com.jax.funaethermod.world;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class StructureSavedData extends SavedData {

    private static final String DATA_NAME =
            "funaethermod_structure";

    private final Set<String> generatedCells =
            new HashSet<>();

    public StructureSavedData() {
    }

    public static StructureSavedData load(CompoundTag tag) {

        StructureSavedData data =
                new StructureSavedData();

        int count =
                tag.getInt("GeneratedCount");

        for (int i = 0; i < count; i++) {

            data.generatedCells.add(
                    tag.getString("Cell_" + i)
            );
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {

        tag.putInt(
                "GeneratedCount",
                generatedCells.size()
        );

        int index = 0;

        for (String cell : generatedCells) {

            tag.putString(
                    "Cell_" + index,
                    cell
            );

            index++;
        }

        return tag;
    }

    public static StructureSavedData get(
            ServerLevel level
    ) {

        return level.getDataStorage()
                .computeIfAbsent(
                        StructureSavedData::load,
                        StructureSavedData::new,
                        DATA_NAME
                );
    }

    private String cellKey(
            int cellX,
            int cellY,
            int cellZ
    ) {

        return cellX + ","
                + cellY + ","
                + cellZ;
    }

    public boolean isGenerated(
            int cellX,
            int cellY,
            int cellZ
    ) {

        return generatedCells.contains(
                cellKey(cellX, cellY, cellZ)
        );
    }

    public void setGenerated(
            int cellX,
            int cellY,
            int cellZ
    ) {

        generatedCells.add(
                cellKey(cellX, cellY, cellZ)
        );

        setDirty();
    }
}