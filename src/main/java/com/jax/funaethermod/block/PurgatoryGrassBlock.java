package com.jax.funaethermod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class PurgatoryGrassBlock extends Block {

    public PurgatoryGrassBlock() {

        super(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_GRAY)
                        .strength(0.6F)
                        .sound(SoundType.GRASS)
        );

    }

}


/*
=========================================================
                 LEARNING CORNER
=========================================================

Class
-----
PurgatoryGrassBlock

This creates a custom block called:
"purgatory grass block"

It extends Minecraft's Block class,
so it behaves like a normal Minecraft block.


Constructor
-----------
Runs when Minecraft creates the block.

BlockBehaviour.Properties
-------------------------
This controls the block's properties.


mapColor()
----------
Sets the block's color.

COLOR_GRAY makes it appear gray on maps
and gives it the correct theme for Purgatory.


strength()
----------
Controls breaking speed and explosion resistance.

0.6F is similar to vanilla grass blocks.


sound()
-------
Controls sounds when:
- walking
- breaking
- placing

GRASS gives it normal grass sounds.


Forge 1.20.1 NOTE
-----------------
Old Minecraft versions used:

Material.GRASS

Forge 1.20.1 uses:

BlockBehaviour.Properties.of()

because Material was removed.
=========================================================
*/