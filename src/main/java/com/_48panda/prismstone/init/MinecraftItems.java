package com._48panda.prismstone.init;

import com._48panda.prismstone.ModArmorMaterial;
import com._48panda.prismstone.PrismstoneMod;
import com._48panda.prismstone.items.PrismstoneDustItem;
import com._48panda.prismstone.items.armor.PrismstoneGoggles;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class MinecraftItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");
    public static final RegistryObject<Item> REDSTONE = ITEMS.register("redstone", ()->new PrismstoneDustItem(Blocks.REDSTONE_WIRE, new Item.Properties()));
}
