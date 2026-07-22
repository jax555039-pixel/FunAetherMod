package com.jax.funaethermod;

import com.jax.funaethermod.entity.Entity2020Entity;
import com.jax.funaethermod.entity.RealEntity;
import com.jax.funaethermod.entity.RealObserveEntity;
import com.jax.funaethermod.entity.FakeEntity;

import com.jax.funaethermod.registry.ModBlocks;
import com.jax.funaethermod.registry.ModEntities;
import com.jax.funaethermod.registry.ModItems;
import com.jax.funaethermod.registry.ModSounds;

import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.event.TickEvent;
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


@Mod(FunAetherMod.MODID)
public class FunAetherMod {


    public static final String MODID =
            "funaethermod";


    private static final Logger LOGGER =
            LogUtils.getLogger();


    private static final Random RANDOM =
            new Random();



    // ==========================
    // AETHER ENCOUNTER SETTINGS
    // ==========================

    private static final int ENCOUNTER_TIME =
            12000;


    private static final Map<ServerPlayer, Integer> encounterTimers =
            new HashMap<>();



    // ==========================
    // PURGATORY ENCOUNTER SETTINGS
    // ==========================

    private static final int PURGATORY_ENCOUNTER_TIME =
            12000;


    private static final Map<ServerPlayer, Integer> purgatoryEncounterTimers =
            new HashMap<>();



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



        LOGGER.info(
                "Fun Aether Mod loaded!"
        );

    }



    private void commonSetup(
            final FMLCommonSetupEvent event
    ) {

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


            Entity2020Entity entity =
                    ModEntities.ENTITY2020.get()
                            .create(level);



            if(entity != null) {


                entity.moveTo(
                        pos,
                        player.getYRot(),
                        0
                );



                level.addFreshEntity(entity);



                LOGGER.info(
                        "Entity2020 spawned near {}",
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




        if(RANDOM.nextBoolean()) {



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



        } else {



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



                LOGGER.info(
                        "Fake spawned near {}",
                        player.getName().getString()
                );

            }

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



        // only generate in overworld
        if(!level.dimension()
                .equals(Level.OVERWORLD))
            return;



        int chunkX =
                event.getChunk()
                        .getPos()
                        .x;



        int chunkZ =
                event.getChunk()
                        .getPos()
                        .z;



        String id =
                chunkX + "," + chunkZ;



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



                                    // randomly choose portal type

                                    if(RANDOM.nextBoolean()) {


                                        buildAetherPortal(
                                                level,
                                                pos
                                        );


                                        LOGGER.info(
                                                "Natural Aether portal generated at {}",
                                                pos
                                        );


                                    } else {


                                        buildPurgatoryPortal(
                                                level,
                                                pos
                                        );


                                        LOGGER.info(
                                                "Natural Purgatory portal generated at {}",
                                                pos
                                        );

                                    }

                                }
                        )
                );

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
}