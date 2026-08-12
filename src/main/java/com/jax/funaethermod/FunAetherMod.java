package com.jax.funaethermod;

import com.jax.funaethermod.registry.ModBlocks;
import com.jax.funaethermod.registry.ModEntities;
import com.jax.funaethermod.registry.ModItems;
import com.jax.funaethermod.registry.ModSounds;
import com.jax.funaethermod.world.DimensionPortalHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Mod(FunAetherMod.MODID)
public class FunAetherMod {
    public static final String MODID = "funaethermod";

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Random RANDOM = new Random();
    private static final int PORTAL_CHANCE = 200;

    private static final String REALNESS_NAME = "realness_12321";
    private static final String FAKESHADOW_NAME = "xxfakeshadowxx";
    private static final String COOLBOY_NAME = "coolboy_2012";

    private final Set<String> generatedChunks = new HashSet<>();

    private int fakePlayerConversationTimer = 0;
    private int fakePlayerConversationStep = 0;
    private String activeFakePlayerName = null;
    private boolean realnessHasJoinedThisSession = false;
    private boolean fakeShadowHasJoinedThisSession = false;
    private boolean coolboyHasJoinedThisSession = false;

    private long getWorldDay(ServerLevel level) {
        return level.getDayTime() / 24000L;
    }

    public FunAetherMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        new DimensionPortalHandler();

        LOGGER.info("Fun Aether Mod loaded!");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting!");
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        String name = player.getName().getString();
        player.serverLevel().getServer().getPlayerList().broadcastSystemMessage(
                Component.literal(name + " joined the game"),
                false
        );
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        String name = player.getName().getString();
        player.serverLevel().getServer().getPlayerList().broadcastSystemMessage(
                Component.literal(name + " left the game"),
                false
        );
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (event.getServer() == null) {
            return;
        }

        ServerLevel level = event.getServer().getLevel(Level.OVERWORLD);
        if (level == null) {
            return;
        }

        long day = getWorldDay(level);
        if (day < 2) {
            return;
        }

        if (fakePlayerConversationTimer > 0) {
            fakePlayerConversationTimer--;
            return;
        }

        if (activeFakePlayerName == null) {
            if (RANDOM.nextInt(3) != 0) {
                return;
            }

            String candidate = null;
            if (!realnessHasJoinedThisSession && RANDOM.nextBoolean()) {
                candidate = REALNESS_NAME;
            } else if (!fakeShadowHasJoinedThisSession && day >= 2 && RANDOM.nextBoolean()) {
                candidate = FAKESHADOW_NAME;
            } else if (!coolboyHasJoinedThisSession && day >= 2) {
                candidate = COOLBOY_NAME;
            }

            if (candidate == null) {
                return;
            }

            activeFakePlayerName = candidate;
            fakePlayerConversationStep = 0;
        }

        switch (fakePlayerConversationStep) {
            case 0 -> {
                level.getServer().getPlayerList().broadcastSystemMessage(
                        Component.literal(activeFakePlayerName + " joined the game"),
                        false
                );
                fakePlayerConversationStep = 1;
                fakePlayerConversationTimer = 20 * 10;
            }
            case 1 -> {
                String message = switch (activeFakePlayerName) {
                    case REALNESS_NAME -> "my skin is gone";
                    case FAKESHADOW_NAME -> "where's my friends";
                    case COOLBOY_NAME -> "they're crawling in my face";
                    default -> "i want to go home";
                };

                if (activeFakePlayerName.equals(COOLBOY_NAME)) {
                    message = "they're crawling in my face";
                }
                if (activeFakePlayerName.equals(FAKESHADOW_NAME)) {
                    message = "where's my friends";
                }
                if (activeFakePlayerName.equals(REALNESS_NAME)) {
                    message = "my skin is gone";
                }

                level.getServer().getPlayerList().broadcastSystemMessage(
                        Component.literal(activeFakePlayerName + ": " + message),
                        false
                );
                fakePlayerConversationStep = 2;
                fakePlayerConversationTimer = 20 * 10;
            }
            case 2 -> {
                level.getServer().getPlayerList().broadcastSystemMessage(
                        Component.literal(activeFakePlayerName + " left the game"),
                        false
                );
                if (activeFakePlayerName.equals(REALNESS_NAME)) {
                    realnessHasJoinedThisSession = true;
                } else if (activeFakePlayerName.equals(FAKESHADOW_NAME)) {
                    fakeShadowHasJoinedThisSession = true;
                } else if (activeFakePlayerName.equals(COOLBOY_NAME)) {
                    coolboyHasJoinedThisSession = true;
                }
                activeFakePlayerName = null;
                fakePlayerConversationStep = 0;
                fakePlayerConversationTimer = 20 * 60 * 3;
            }
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        ResourceKey<Level> overworld = Level.OVERWORLD;
        ResourceKey<Level> aether = ResourceKey.create(
                Registries.DIMENSION,
                new ResourceLocation(MODID, "aether")
        );
        ResourceKey<Level> purgatory = ResourceKey.create(
                Registries.DIMENSION,
                new ResourceLocation(MODID, "purgatory")
        );

        if (!level.dimension().equals(overworld)
                && !level.dimension().equals(aether)
                && !level.dimension().equals(purgatory)) {
            return;
        }

        int chunkX = event.getChunk().getPos().x;
        int chunkZ = event.getChunk().getPos().z;

        if (isTooCloseToSpawn(level, chunkX, chunkZ, 20)) {
            return;
        }

        String id = level.dimension().location() + ":" + chunkX + "," + chunkZ;
        if (generatedChunks.contains(id)) {
            return;
        }

        if (RANDOM.nextInt(PORTAL_CHANCE) != 0) {
            return;
        }

        generatedChunks.add(id);

        level.getServer().tell(new TickTask(1, () -> {
            BlockPos pos = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);
            pos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos);

            if (level.dimension().equals(aether)) {
                buildPurgatoryPortal(level, pos);
                LOGGER.info(
                        "Natural Purgatory portal generated in Aether at X:{} Y:{} Z:{}",
                        pos.getX(), pos.getY(), pos.getZ()
                );
            } else if (level.dimension().equals(purgatory)) {
                buildAetherPortal(level, pos);
                LOGGER.info(
                        "Natural Aether portal generated in Purgatory at X:{} Y:{} Z:{}",
                        pos.getX(), pos.getY(), pos.getZ()
                );
            } else {
                if (RANDOM.nextBoolean()) {
                    buildAetherPortal(level, pos);
                    LOGGER.info(
                            "Natural Aether portal generated at X:{} Y:{} Z:{}",
                            pos.getX(), pos.getY(), pos.getZ()
                    );
                } else {
                    buildPurgatoryPortal(level, pos);
                    LOGGER.info(
                            "Natural Purgatory portal generated at X:{} Y:{} Z:{}",
                            pos.getX(), pos.getY(), pos.getZ()
                    );
                }
            }
        }));
    }

    private boolean isTooCloseToSpawn(ServerLevel level, int chunkX, int chunkZ, int minChunks) {
        BlockPos spawn = level.getSharedSpawnPos();
        int spawnChunkX = spawn.getX() >> 4;
        int spawnChunkZ = spawn.getZ() >> 4;

        return Math.abs(chunkX - spawnChunkX) <= minChunks
                && Math.abs(chunkZ - spawnChunkZ) <= minChunks;
    }

    private void buildAetherPortal(ServerLevel level, BlockPos pos) {
        BlockState frame = ModBlocks.AETHER_PORTAL_FRAME.get().defaultBlockState();
        BlockState portal = ModBlocks.AETHER_PORTAL.get().defaultBlockState();

        for (int x = -2; x <= 2; x++) {
            for (int y = 0; y <= 4; y++) {
                BlockPos place = pos.offset(x, y, 0);
                boolean edge = x == -2 || x == 2 || y == 0 || y == 4;
                level.setBlock(place, edge ? frame : portal, 3);
            }
        }
    }

    private void buildPurgatoryPortal(ServerLevel level, BlockPos pos) {
        BlockState portal = ModBlocks.PURGATORY_PORTAL.get().defaultBlockState();

        for (int y = 0; y < 50; y++) {
            BlockPos place = pos.above(y);
            level.setBlock(place, portal, 3);
        }
    }
}
