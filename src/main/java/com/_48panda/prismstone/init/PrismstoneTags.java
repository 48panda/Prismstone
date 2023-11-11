package com._48panda.prismstone.init;

import com._48panda.prismstone.PrismstoneMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;

public class PrismstoneTags {
    public static class Block {
        public static final TagKey<net.minecraft.world.level.block.Block> PRISMSTONE_WIRE = tag("prismstone_wire");

        private static TagKey<net.minecraft.world.level.block.Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(PrismstoneMod.MODID, name));
        }
    }
}
