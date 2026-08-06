package com.jax.funaethermod.events;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.world.HavenManager;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FunAetherMod.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void levelTick(TickEvent.LevelTickEvent event) {

        if (event.phase != TickEvent.Phase.END)
            return;

        if (!(event.level instanceof ServerLevel level))
            return;

        if (level.dimension()
                .location()
                .toString()
                .equals("funaethermod:haven")) {

            HavenManager.onLoad(level);
        }
    }
}