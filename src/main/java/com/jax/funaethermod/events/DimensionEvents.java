package com.jax.funaethermod.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.jax.funaethermod.FunAetherMod;

@Mod.EventBusSubscriber(modid = FunAetherMod.MODID)
public class DimensionEvents {

    @SubscribeEvent
    public static void onDimensionChange(
            PlayerEvent.PlayerChangedDimensionEvent event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player))
            return;


        if (
    player.level()
    .dimension()
    .location()
    .toString()
    .equals("funaethermod:haven")
) {

    /*player.teleportTo(
            0.5,
            112,
            0.5
    );*/

    player.setYRot(0);
    player.setXRot(0);
}
    }
}