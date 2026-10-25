
package com.jax.funaethermod.events;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.world.StructureManager;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FunAetherMod.MODID)
public class ModEvents {

    private static boolean overworldDebugLogged = false;

    @SubscribeEvent
    public static void levelTick(TickEvent.LevelTickEvent event) {

        if (event.phase != TickEvent.Phase.END)
            return;

        if (!(event.level instanceof ServerLevel level))
            return;

        /*
         * Overworld structures.
         */
        if (level.dimension().equals(Level.OVERWORLD)) {

            if (!overworldDebugLogged) {
                System.out.println(
                        "[FunAetherMod DEBUG] Overworld tick detected!"
                );
                overworldDebugLogged = true;
            }

            StructureManager.onLoad(level);
        }

        /*
         * Haven dimension.
         */
        if (level.dimension().location().toString()
                .equals("funaethermod:haven")) {

            StructureManager.onLoad(level);
        }

        /*
         * Chapel dimension.
         */
        if (level.dimension().location().toString()
                .equals("funaethermod:chapel")) {

            StructureManager.onLoad(level);
        }

        /*
         * Hell dimension.
         */
        if (level.dimension().location().toString()
                .equals("funaethermod:hell")) {

            StructureManager.onLoad(level);
        }
    }
}
