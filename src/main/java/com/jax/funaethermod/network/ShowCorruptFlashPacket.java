
package com.jax.funaethermod.network;

import com.jax.funaethermod.client.CorruptFlashOverlay;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShowCorruptFlashPacket {

    public ShowCorruptFlashPacket() {
    }

    public static void encode(
            ShowCorruptFlashPacket packet,
            FriendlyByteBuf buffer) {
    }

    public static ShowCorruptFlashPacket decode(
            FriendlyByteBuf buffer) {
        return new ShowCorruptFlashPacket();
    }

    public static void handle(
            ShowCorruptFlashPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {

        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(
                        Dist.CLIENT,
                        () -> CorruptFlashOverlay::show
                )
        );

        context.setPacketHandled(true);
    }
}
