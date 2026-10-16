
package com.jax.funaethermod.world;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class OverworldStructureSavedData extends SavedData {

    private static final String DATA_NAME =
            "funaethermod_overworld_structures";

    private final Map<String, BlockPos> positions =
            new HashMap<>();

    public OverworldStructureSavedData() {
    }

    public static OverworldStructureSavedData load(
            CompoundTag tag
    ) {

        OverworldStructureSavedData data =
                new OverworldStructureSavedData();

        int count = tag.getInt("StructureCount");

        for (int i = 0; i < count; i++) {

            CompoundTag structure =
                    tag.getCompound("Structure_" + i);

            String name = structure.getString("Name");

            if (!name.isEmpty()) {

                BlockPos position = new BlockPos(
                        structure.getInt("X"),
                        structure.getInt("Y"),
                        structure.getInt("Z")
                );

                data.positions.put(name, position);
            }
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {

        tag.putInt("StructureCount", positions.size());

        int index = 0;

        for (Map.Entry<String, BlockPos> entry
                : positions.entrySet()) {

            CompoundTag structure = new CompoundTag();

            structure.putString("Name", entry.getKey());
            structure.putInt("X", entry.getValue().getX());
            structure.putInt("Y", entry.getValue().getY());
            structure.putInt("Z", entry.getValue().getZ());

            tag.put("Structure_" + index, structure);

            index++;
        }

        return tag;
    }

    public static OverworldStructureSavedData get(
            ServerLevel level
    ) {

        return level.getDataStorage().computeIfAbsent(
                OverworldStructureSavedData::load,
                OverworldStructureSavedData::new,
                DATA_NAME
        );
    }

    public boolean isGenerated(String structureName) {
        return positions.containsKey(structureName);
    }

    public void setGenerated(
            String structureName,
            BlockPos position
    ) {

        positions.put(structureName, position.immutable());
        setDirty();
    }

    public Map<String, BlockPos> getPositions() {
        return Collections.unmodifiableMap(positions);
    }
}
