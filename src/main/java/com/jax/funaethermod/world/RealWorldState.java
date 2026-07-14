package com.jax.funaethermod.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class RealWorldState extends SavedData {

    private boolean realActive = false;

    public boolean isRealActive() {
        return realActive;
    }

    public void setRealActive(boolean value) {
        this.realActive = value;
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("real_active", realActive);
        return tag;
    }

    public static RealWorldState load(CompoundTag tag) {
        RealWorldState state = new RealWorldState();
        state.realActive = tag.getBoolean("real_active");
        return state;
    }

    public static RealWorldState create() {
        return new RealWorldState();
    }
}