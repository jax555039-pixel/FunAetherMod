package com.jax.funaethermod;

import com.jax.funaethermod.entity.Entity2020AttackEntity;
import com.jax.funaethermod.entity.Entity2020Entity;
import com.jax.funaethermod.entity.FakeEntity;
import com.jax.funaethermod.entity.PoorBoyEntity;
import com.jax.funaethermod.entity.RealEntity;
import com.jax.funaethermod.entity.RealObserveEntity;
import com.jax.funaethermod.registry.ModBlocks;
import com.jax.funaethermod.registry.ModEntities;
import com.jax.funaethermod.registry.ModItems;
import com.jax.funaethermod.registry.ModSounds;
import com.jax.funaethermod.world.DimensionPortalHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Mod(FunAetherMod.MODID)
public class FunAetherMod {


    public static final String MODID =
            "funaethermod";


    private static final Logger LOGGER =
            LogUtils.getLogger();


    private static final Random RANDOM =
            new Random();

            private long getWorldDay(ServerLevel level) {

    return level.getDayTime() / 24000L;

}

            
@SubscribeEvent
public void fakeDeath(
        LivingDeathEvent event
) {

    if(event.getEntity() instanceof FakeEntity) {

        fakeActive = false;

        LOGGER.info(
                "Fake removed, new Fake encounters allowed"
        );

    }

}

private void spawnOverworldEncounter(
        ServerPlayer player
) {

    ServerLevel level = player.serverLevel();

    Vec3 look = player.getLookAngle();

    BlockPos pos = BlockPos.containing(
            player.position().add(look.x * 50, 0, look.z * 50)
    );

    pos = level.getHeightmapPos(
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            pos
    );

    if (RANDOM.nextBoolean()) {
        RealEntity entity = ModEntities.REAL.get().create(level);

        if (entity != null) {
            entity.moveTo(pos, player.getYRot(), 0);
            level.addFreshEntity(entity);

            LOGGER.info(
                    "Overworld Real spawned near {}",
                    player.getName().getString()
            );
        }
    } else {
        Entity2020Entity entity = ModEntities.ENTITY2020.get().create(level);

        if (entity != null) {
            entity.moveTo(pos, player.getYRot(), 0);
            level.addFreshEntity(entity);

            LOGGER.info(
                    "Overworld Entity2020 spawned near {}",
                    player.getName().getString()
            );
        }
    }
}

//=============================
//POORBOY ENCOUNTER SETTINGS
//=============================

private static final int POORBOY_ENCOUNTER_TIME = 1200;

    // ==========================
    // AETHER ENCOUNTER SETTINGS
    // ==========================

    private static final int ENCOUNTER_TIME =
            12000;


    private static final Map<ServerPlayer, Integer> encounterTimers =
        new HashMap<>();

    private static final int OVERWORLD_ENCOUNTER_TIME =
            12000;

    private static final Map<ServerPlayer, Integer> overworldEncounterTimers =
            new HashMap<>();


    // ==========================
    // PURGATORY ENCOUNTER SETTINGS
    // ==========================

    private static final int PURGATORY_ENCOUNTER_TIME =
            12000;


    private static final Map<ServerPlayer, Integer> purgatoryEncounterTimers =
            new HashMap<>();

            private static boolean fakeActive = false;

    private static final int FAKE_OVERWORLD_COOLDOWN_DAYS = 2;
    private static final int FAKE_PURGATORY_COOLDOWN_TICKS = 20 * 60 * 10;

    private static final Map<UUID, Long> lastFakeSpawnDayByPlayer = new HashMap<>();
    private static final Map<UUID, Long> lastFakeSpawnTickByPlayer = new HashMap<>();


    // ==========================
    // NATURAL PORTAL SETTINGS
    // ==========================

    private static final Set<String> generatedChunks =
            new HashSet<>();


    private static final int PORTAL_CHANCE =
            250;



    public FunAetherMod() {

    IEventBus modEventBus =
            FMLJavaModLoadingContext.get()
                    .getModEventBus();


    ModBlocks.register(modEventBus);
    ModItems.register(modEventBus);
    ModEntities.register(modEventBus);
    ModSounds.register(modEventBus);


    modEventBus.addListener(
            this::commonSetup
    );


    MinecraftForge.EVENT_BUS.register(this);


    new DimensionPortalHandler();


    LOGGER.info(
            "Fun Aether Mod loaded!"
    );

}



    private void commonSetup(
            final FMLCommonSetupEvent event
    ) {

        event.enqueueWork(() -> {

    SpawnPlacements.register(
            ModEntities.REAL_OBSERVE.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            PathfinderMob::checkMobSpawnRules
    );

    SpawnPlacements.register(
            ModEntities.FAKE.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            PathfinderMob::checkMobSpawnRules
    );

});

        LOGGER.info(
                "Common setup complete!"
        );

    }



    @SubscribeEvent
    public void onServerStarting(
            ServerStartingEvent event
    ) {

        LOGGER.info(
                "Server starting!"
        );

    }


        // ==========================
    // AETHER ENCOUNTER SYSTEM
    // ==========================


    @SubscribeEvent
    public void encounterTick(
            TickEvent.PlayerTickEvent event
    ) {


        if(event.phase != TickEvent.Phase.END)
            return;


        if(!(event.player instanceof ServerPlayer player))
            return;


        if(player.level().isClientSide)
            return;


        if(!player.level()
                .dimension()
                .location()
                .equals(
                        new ResourceLocation(
                                MODID,
                                "aether"
                        )
                )) {

            return;
        }




        int timer =
                encounterTimers.getOrDefault(
                        player,
                        0
                );



        timer++;



        if(timer >= ENCOUNTER_TIME) {

            spawnEncounter(player);

            timer = 0;

        }



        encounterTimers.put(
                player,
                timer
        );

    }





    private void spawnEncounter(
            ServerPlayer player
    ) {


        ServerLevel level =
                player.serverLevel();



        Vec3 look =
                player.getLookAngle();



        BlockPos pos =
                BlockPos.containing(
                        player.position()
                                .add(
                                        look.x * 50,
                                        0,
                                        look.z * 50
                                )
                );



        pos =
                level.getHeightmapPos(
                        Heightmap.Types.MOTION_BLOCKING,
                        pos
                );





        if(RANDOM.nextBoolean()) {


            Entity2020AttackEntity entity =
                    ModEntities.ENTITY2020_ATTACK.get()
                            .create(level);



            if(entity != null) {


                entity.moveTo(
                        pos,
                        player.getYRot(),
                        0
                );



                level.addFreshEntity(entity);



                LOGGER.info(
                        "Entity2020 attack mode observer spawned near {}",
                        player.getName().getString()
                );

            }



        } else {



            RealEntity entity =
                    ModEntities.REAL.get()
                            .create(level);




            if(entity != null) {


                entity.moveTo(
                        pos,
                        player.getYRot(),
                        0
                );



                level.addFreshEntity(entity);



                LOGGER.info(
                        "Real spawned near {}",
                        player.getName().getString()
                );

            }

        }

    }


    // ==========================
    // PURGATORY ENCOUNTER SYSTEM
    // ==========================


    @SubscribeEvent
    public void purgatoryEncounterTick(
            TickEvent.PlayerTickEvent event
    ) {


        if(event.phase != TickEvent.Phase.END)
            return;



        if(!(event.player instanceof ServerPlayer player))
            return;



        if(player.level().isClientSide)
            return;


        if(!player.level()
                .dimension()
                .location()
                .equals(
                        new ResourceLocation(
                                MODID,
                                "purgatory"
                        )
                )) {

            return;

        }




        int timer =
                purgatoryEncounterTimers.getOrDefault(
                        player,
                        0
                );



        timer++;



        if(timer >= PURGATORY_ENCOUNTER_TIME) {


            spawnPurgatoryEncounter(player);


            timer = 0;

        }



        purgatoryEncounterTimers.put(
                player,
                timer
        );

    }





    private void spawnPurgatoryEncounter(
        ServerPlayer player
) {

    ServerLevel level =
            player.serverLevel();


    Vec3 look =
            player.getLookAngle();


    BlockPos pos =
            BlockPos.containing(
                    player.position()
                            .add(
                                    look.x * 50,
                                    0,
                                    look.z * 50
                            )
            );


    pos =
            level.getHeightmapPos(
                    Heightmap.Types.MOTION_BLOCKING,
                    pos
            );


    long day =
            getWorldDay(level);



    // ==========================
    // REAL OBSERVE
    // Day 2+
    // ==========================

    if(RANDOM.nextInt(5) != 0) {


        if(day <= 1)
            return;



        RealObserveEntity entity =
                ModEntities.REAL_OBSERVE.get()
                        .create(level);



        if(entity != null) {


            entity.moveTo(
                    pos,
                    player.getYRot(),
                    0
            );


            level.addFreshEntity(entity);


            LOGGER.info(
                    "RealObserve spawned near {}",
                    player.getName().getString()
            );

        }


        return;

    }




    // ==========================
    // FAKE
    // Day 1: rare, lasts 10 seconds
    // Day 2+: more common, lasts 1.5 minutes
    // ==========================


    if(fakeActive)
        return;

    UUID playerId = player.getUUID();

    if (level.dimension().equals(Level.OVERWORLD)) {
        Long lastSpawnDay = lastFakeSpawnDayByPlayer.get(playerId);

        if (lastSpawnDay != null && day - lastSpawnDay < FAKE_OVERWORLD_COOLDOWN_DAYS) {
            return;
        }

        lastFakeSpawnDayByPlayer.put(playerId, day);
    } else if (level.dimension().equals(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MODID, "purgatory")))) {
        long currentTick = level.getGameTime();
        Long lastSpawnTick = lastFakeSpawnTickByPlayer.get(playerId);

        if (lastSpawnTick != null && currentTick - lastSpawnTick < FAKE_PURGATORY_COOLDOWN_TICKS) {
            return;
        }

        lastFakeSpawnTickByPlayer.put(playerId, currentTick);
    }


    if(day <= 1) {


        // Day 1 Fake chance
        if(RANDOM.nextInt(100) != 0)
            return;


    } else {


        // Day 2+ Fake chance
        if(RANDOM.nextInt(25) != 0)
            return;

    }



    FakeEntity entity =
            ModEntities.FAKE.get()
                    .create(level);



    if(entity != null) {


        entity.moveTo(
                pos,
                player.getYRot(),
                0
        );


        level.addFreshEntity(entity);


        fakeActive = true;


        int lifeTicks;


        if(day <= 1) {

            lifeTicks = 20 * 10; // 10 seconds

        } else {

            lifeTicks = 20 * 90; // 1.5 minutes

        }



        entity.setPersistenceRequired();



        level.getServer().tell(
                new TickTask(
                        level.getServer().getTickCount() + lifeTicks,
                        () -> {

                            if(entity.isAlive()) {

                                entity.discard();

                            }


                            fakeActive = false;

                        }
                )
        );



        LOGGER.info(
                "Fake spawned near {}",
                player.getName().getString()
        );

    }

}

        // ==========================
    // NATURAL PORTAL GENERATION
    // ==========================


    @SubscribeEvent
    public void onChunkLoad(
            ChunkEvent.Load event
    ) {


        if(!(event.getLevel() instanceof ServerLevel level))
            return;


        ResourceKey<Level> overworld = Level.OVERWORLD;
        ResourceKey<Level> aether = ResourceKey.create(
                Registries.DIMENSION,
                new ResourceLocation(MODID, "aether")
        );
        ResourceKey<Level> purgatory = ResourceKey.create(
                Registries.DIMENSION,
                new ResourceLocation(MODID, "purgatory")
        );

        if(!level.dimension().equals(overworld)
                && !level.dimension().equals(aether)
                && !level.dimension().equals(purgatory)) {
            return;
        }


        int chunkX =
                event.getChunk()
                        .getPos()
                        .x;



        int chunkZ =
                event.getChunk()
                        .getPos()
                        .z;

        if (isTooCloseToSpawn(level, chunkX, chunkZ, 20)) {
            return;
        }

        String id =
                level.dimension().location() + ":" + chunkX + "," + chunkZ;



        if(generatedChunks.contains(id))
            return;



        if(RANDOM.nextInt(PORTAL_CHANCE) != 0)
            return;



        generatedChunks.add(id);



        level.getServer()
                .tell(
                        new TickTask(
                                1,
                                () -> {


                                    BlockPos pos =
                                            new BlockPos(
                                                    chunkX * 16 + 8,
                                                    0,
                                                    chunkZ * 16 + 8
                                            );



                                    pos =
                                            level.getHeightmapPos(
                                                    Heightmap.Types.WORLD_SURFACE,
                                                    pos
                                            );


                                    if(level.dimension().equals(aether)) {

                                        buildPurgatoryPortal(
                                                level,
                                                pos
                                        );


                                        LOGGER.info(
                                                "Natural Purgatory portal generated in Aether at X:{} Y:{} Z:{}",
                                                pos.getX(),
                                                pos.getY(),
                                                pos.getZ()
                                        );

                                    } else if(level.dimension().equals(purgatory)) {

                                        buildAetherPortal(
                                                level,
                                                pos
                                        );


                                        LOGGER.info(
                                                "Natural Aether portal generated in Purgatory at X:{} Y:{} Z:{}",
                                                pos.getX(),
                                                pos.getY(),
                                                pos.getZ()
                                        );

                                    } else {

                                        if(RANDOM.nextBoolean()) {


                                            buildAetherPortal(
                                                    level,
                                                    pos
                                            );


                                            LOGGER.info(
                                                    "Natural Aether portal generated at X:{} Y:{} Z:{}",
                                                    pos.getX(),
                                                    pos.getY(),
                                                    pos.getZ()
                                            );


                                        } else {


                                            buildPurgatoryPortal(
                                                    level,
                                                    pos
                                            );


                                            LOGGER.info(
                                                    "Natural Purgatory portal generated at X:{} Y:{} Z:{}",
                                                    pos.getX(),
                                                    pos.getY(),
                                                    pos.getZ()
                                            );
                                        }
                                    }

                                }
                        )
                );

    }

    private boolean isTooCloseToSpawn(
            ServerLevel level,
            int chunkX,
            int chunkZ,
            int minChunks
    ) {
        BlockPos spawn = level.getSharedSpawnPos();

        int spawnChunkX = spawn.getX() >> 4;
        int spawnChunkZ = spawn.getZ() >> 4;

        return Math.abs(chunkX - spawnChunkX) <= minChunks
                && Math.abs(chunkZ - spawnChunkZ) <= minChunks;
    }

        private void buildAetherPortal(
            ServerLevel level,
            BlockPos pos
    ) {


        BlockState frame =
                ModBlocks.AETHER_PORTAL_FRAME
                        .get()
                        .defaultBlockState();



        BlockState portal =
                ModBlocks.AETHER_PORTAL
                        .get()
                        .defaultBlockState();




        for(int x = -2; x <= 2; x++) {


            for(int y = 0; y <= 4; y++) {


                BlockPos place =
                        pos.offset(
                                x,
                                y,
                                0
                        );



                boolean edge =
                        x == -2 ||
                        x == 2 ||
                        y == 0 ||
                        y == 4;



                level.setBlock(
                        place,
                        edge ? frame : portal,
                        3
                );

            }

        }

    }





    private void buildPurgatoryPortal(
        ServerLevel level,
        BlockPos pos
) {

    BlockState portal =
            ModBlocks.PURGATORY_PORTAL
                    .get()
                    .defaultBlockState();


    for(int y = 0; y < 50; y++) {

        BlockPos place =
                pos.above(y);


        level.setBlock(
                place,
                portal,
                3
        );

    }

}

// ==========================
// OVERWORLD ENCOUNTER SYSTEM
// ==========================

@SubscribeEvent
public void overworldEncounterTick(
        TickEvent.PlayerTickEvent event
) {

    if(event.phase != TickEvent.Phase.END)
        return;

    if(!(event.player instanceof ServerPlayer player))
        return;

    if(player.level().isClientSide)
        return;

    if(!player.level()
            .dimension()
            .equals(Level.OVERWORLD)) {
        return;
    }

    int timer =
            overworldEncounterTimers.getOrDefault(
                    player,
                    0
            );

    timer++;

    if(timer >= OVERWORLD_ENCOUNTER_TIME) {
        spawnOverworldEncounter(player);
        timer = 0;
    }

    overworldEncounterTimers.put(
            player,
            timer
    );
}

private void spawnPoorBoyEncounter(
        ServerPlayer player
) {

    ServerLevel level =
            player.serverLevel();

    Vec3 look =
            player.getLookAngle();

    BlockPos pos =
            BlockPos.containing(
                    player.position().add(
                            look.x * 50,
                            0,
                            look.z * 50
                    )
            );

    pos =
            level.getHeightmapPos(
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    pos
            );

    PoorBoyEntity entity =
            ModEntities.POORBOY.get().create(level);

    if(entity != null) {

        entity.moveTo(
                pos,
                player.getYRot(),
                0
        );

        level.addFreshEntity(entity);

        LOGGER.info(
                "PoorBoy spawned near {} at X:{} Y:{} Z:{}",
                player.getName().getString(),
                pos.getX(),
                pos.getY(),
                pos.getZ()
        );
    }
}
}