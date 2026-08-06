package com.jax.funaethermod.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class HavenSavedData extends SavedData {

    private static final String DATA_NAME = "funaethermod_haven";

    private boolean havenGenerated = false;

    public HavenSavedData() {
    }

    public static HavenSavedData load(CompoundTag tag) {

        HavenSavedData data = new HavenSavedData();

        data.havenGenerated =
                tag.getBoolean("HavenGenerated");

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {

        tag.putBoolean(
                "HavenGenerated",
                havenGenerated
        );

        return tag;
    }

    public static HavenSavedData get(ServerLevel level) {

        return level.getDataStorage().computeIfAbsent(
                HavenSavedData::load,
                HavenSavedData::new,
                DATA_NAME
        );
    }

    public boolean isGenerated() {
        return havenGenerated;
    }

    public void setGenerated(boolean value) {

        havenGenerated = value;

        setDirty();
    }
}