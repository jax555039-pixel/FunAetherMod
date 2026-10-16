
package com.jax.funaethermod.client;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(
        modid = "funaethermod",
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class CorruptFlashOverlay {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    "funaethermod",
                    "textures/gui/corrupt_flash.png"
            );

    private static long endGameTime = -1;

    public static void show() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level != null) {
            endGameTime = minecraft.level.getGameTime() + 60;
        }
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(
                "corrupt_flash",
                (gui, graphics, partialTick, width, height) -> {
                    Minecraft minecraft = Minecraft.getInstance();

                    if (minecraft.level == null
                            || minecraft.level.getGameTime() >= endGameTime) {
                        return;
                    }

                    RenderSystem.enableBlend();

                    graphics.blit(
                            TEXTURE,
                            0, 0,
                            0, 0,
                            width, height,
                            256, 256
                    );

                    RenderSystem.disableBlend();
                }
        );
    }
}
