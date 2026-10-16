
package com.jax.funaethermod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {

    private static final String PROTOCOL = "1";

    private static final SimpleChannel CHANNEL =
            NetworkRegistry.ChannelBuilder
                    .named(new ResourceLocation(
                            "funaethermod", "main"))
                    .networkProtocolVersion(() -> PROTOCOL)
                    .clientAcceptedVersions(PROTOCOL::equals)
                    .serverAcceptedVersions(PROTOCOL::equals)
                    .simpleChannel();

    private static boolean registered = false;

    public static void register() {
        if (registered) {
            return;
        }

        registered = true;

        CHANNEL.registerMessage(
                0,
                ShowCorruptFlashPacket.class,
                ShowCorruptFlashPacket::encode,
                ShowCorruptFlashPacket::decode,
                ShowCorruptFlashPacket::handle
        );
    }

    public static void sendCorruptFlash(ServerPlayer player) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new ShowCorruptFlashPacket()
        );
    }
}
