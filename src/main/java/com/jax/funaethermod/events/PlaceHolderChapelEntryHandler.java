
package com.jax.funaethermod.events;

import com.jax.funaethermod.FunAetherMod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FunAetherMod.MODID)
public class PlaceHolderChapelEntryHandler {

    /*
     * Chapel dimension.
     */
    private static final ResourceKey<Level> CHAPEL_DIMENSION =
            ResourceKey.create(
                    Registries.DIMENSION,
                    new ResourceLocation(
                            FunAetherMod.MODID,
                            "chapel"
                    )
            );

    @SubscribeEvent
    public static void onWakeUp(
            PlayerWakeUpEvent event
    ) {

        // Server players only.
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        // Temporary placeholder: 1 in 7 chance.
        if (player.getRandom().nextInt(7) != 0) {
            return;
        }

        // Get the Chapel dimension.
        ServerLevel chapel =
                player.server.getLevel(
                        CHAPEL_DIMENSION
                );

        if (chapel == null) {

            System.out.println(
                    "[FunAetherMod] Chapel dimension not found."
            );

            return;
        }

        // Chapel arrival location.
        BlockPos spawn =
                new BlockPos(
                        0,
                        115,
                        0
                );

        // Teleport the player to Chapel.
        player.teleportTo(
                chapel,
                spawn.getX() + 0.5D,
                spawn.getY(),
                spawn.getZ() + 0.5D,
                player.getYRot(),
                player.getXRot()
        );

        System.out.println(
                "[FunAetherMod] Player woke up in Chapel."
        );
    }
}
