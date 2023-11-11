package com._48panda.prismstone.init;

import com._48panda.prismstone.PrismstoneMod;
import com._48panda.prismstone.ModArmorMaterial;
import com._48panda.prismstone.items.PrismstoneDustItem;
import com._48panda.prismstone.items.armor.PrismstoneGoggles;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class PrismstoneItems {
    
    public static Map<PrismstoneType, RegistryObject<Item>> blockItem(Map<PrismstoneType, RegistryObject<Block>> blocks, Function<RegistryObject<Block>, Supplier<Item>> supplier) {
        Map<PrismstoneType, RegistryObject<Item>> items = new HashMap<>();
        blocks.forEach((k,v) -> items.put(k, ITEMS.register(v.getId().getPath(), supplier.apply(v))));
        return items;
    }
    public static Map<PrismstoneType, RegistryObject<Item>> blockItem(String name, Map<PrismstoneType, RegistryObject<Block>> blocks, Function<RegistryObject<Block>, Supplier<Item>> supplier) {
        Map<PrismstoneType, RegistryObject<Item>> items = new HashMap<>();
        blocks.forEach((k,v) -> items.put(k, ITEMS.register(name.replace("prismstone", k.getSerializedName()), supplier.apply(v))));
        return items;
    }
    public static Map<BlockSetType, Map<PrismstoneType, RegistryObject<Item>>> blockItemButton(Map<BlockSetType, Map<PrismstoneType, RegistryObject<Block>>> blocks, Function<RegistryObject<Block>, Supplier<Item>> supplier) {
        Map<BlockSetType, Map<PrismstoneType, RegistryObject<Item>>> items = new HashMap<>();
        blocks.forEach((k,v) -> {
            HashMap<PrismstoneType, RegistryObject<Item>> itemsInner = new HashMap<>(); 
            v.forEach((k2,v2)->itemsInner.put(k2, ITEMS.register(v2.getId().getPath(), supplier.apply(v2))));
            items.put(k, itemsInner);
        });
        return items;
    }
    public static Map<PrismstoneType, RegistryObject<Item>> register(String name, Function<PrismstoneType, Supplier<Item>> supplier, Set<PrismstoneType> typeSet) {
        Map<PrismstoneType, RegistryObject<Item>> items = new HashMap<>();
        typeSet.forEach(x -> items.put(x, ITEMS.register(name.replace("prismstone", x.getSerializedName()), supplier.apply(x))));
        return items;
    }
    public static Map<PrismstoneType, RegistryObject<Item>> blockItem(Map<PrismstoneType, RegistryObject<Block>> standing, Map<PrismstoneType, RegistryObject<Block>> wall, Function<RegistryObject<Block>, Function<RegistryObject<Block>, Supplier<Item>>> supplier) {
        Map<PrismstoneType, RegistryObject<Item>> items = new HashMap<>();
        standing.keySet().forEach(x->items.put(x,ITEMS.register(standing.get(x).getId().getPath(), supplier.apply(standing.get(x)).apply(wall.get(x)))));
        return items;
    }
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PrismstoneMod.MODID);
    //public static final RegistryObject<Item> REDSTONE_GOGGLES = ITEMS.register("redstone_goggles", () -> new PrismstoneGoggles(ArmorTiers.GOGGLES, ArmorItem.Type.HELMET, new Item.Properties(), PrismstoneType.REDSTONE));
    //public static final RegistryObject<Item> HEATSTONE_DUST = ITEMS.register("heatstone_dust", () -> new PrismstoneDustItem(PrismstoneBlocks.HEATSTONE_WIRE.get(), new Item.Properties()));
    //public static final RegistryObject<Item> HEATSTONE_TORCH = ITEMS.register("heatstone_torch", () -> new StandingAndWallBlockItem(PrismstoneBlocks.HEATSTONE_TORCH.get(), PrismstoneBlocks.HEATSTONE_WALL_TORCH.get(), new Item.Properties(), Direction.DOWN));
    //public static final RegistryObject<Item> HEATSTONE_BLOCK = ITEMS.register("heatstone_block", () -> new BlockItem(PrismstoneBlocks.HEATSTONE_BLOCK.get(), new Item.Properties()));
    //public static final RegistryObject<Item> HEATSTONE_REPEATER = ITEMS.register("heatstone_repeater", () -> new BlockItem(PrismstoneBlocks.HEATSTONE_REPEATER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> HEATSTONE_COMPARATOR = ITEMS.register("heatstone_comparator", () -> new BlockItem(PrismstoneBlocks.HEATSTONE_COMPARATOR.get(), new Item.Properties()));
    //public static final RegistryObject<Item> HEATSTONE_LEVER = ITEMS.register("heatstone_lever", () -> new BlockItem(PrismstoneBlocks.HEATSTONE_LEVER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> HEATSTONE_GOGGLES = ITEMS.register("heatstone_goggles", () -> new PrismstoneGoggles(ArmorTiers.GOGGLES, ArmorItem.Type.HELMET, new Item.Properties(), PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Item> OAK_HEATSTONE_BUTTON = ITEMS.register("oak_heatstone_button", () -> new BlockItem(PrismstoneBlocks.OAK_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SPRUCE_HEATSTONE_BUTTON = ITEMS.register("spruce_heatstone_button", () -> new BlockItem(PrismstoneBlocks.SPRUCE_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BIRCH_HEATSTONE_BUTTON = ITEMS.register("birch_heatstone_button", () -> new BlockItem(PrismstoneBlocks.BIRCH_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> JUNGLE_HEATSTONE_BUTTON = ITEMS.register("jungle_heatstone_button", () -> new BlockItem(PrismstoneBlocks.JUNGLE_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> ACACIA_HEATSTONE_BUTTON = ITEMS.register("acacia_heatstone_button", () -> new BlockItem(PrismstoneBlocks.ACACIA_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CHERRY_HEATSTONE_BUTTON = ITEMS.register("cherry_heatstone_button", () -> new BlockItem(PrismstoneBlocks.CHERRY_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DARK_OAK_HEATSTONE_BUTTON = ITEMS.register("dark_oak_heatstone_button", () -> new BlockItem(PrismstoneBlocks.DARK_OAK_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MANGROVE_HEATSTONE_BUTTON = ITEMS.register("mangrove_heatstone_button", () -> new BlockItem(PrismstoneBlocks.MANGROVE_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BAMBOO_HEATSTONE_BUTTON = ITEMS.register("bamboo_heatstone_button", () -> new BlockItem(PrismstoneBlocks.BAMBOO_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CRIMSON_HEATSTONE_BUTTON = ITEMS.register("crimson_heatstone_button", () -> new BlockItem(PrismstoneBlocks.CRIMSON_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPED_HEATSTONE_BUTTON = ITEMS.register("warped_heatstone_button", () -> new BlockItem(PrismstoneBlocks.WARPED_HEATSTONE_BUTTON.get(), new Item.Properties()));
    public static final Map<PrismstoneType, RegistryObject<Item>> PRISMSTONE_GOGGLES = register("prismstone_goggles", type->()->new PrismstoneGoggles(ArmorTiers.GOGGLES, ArmorItem.Type.HELMET, new Item.Properties(), type), PrismstoneBlocks.ALL_TYPES);
    public static final Map<PrismstoneType, RegistryObject<Item>> PRISMSTONE_DUST = blockItem("prismstone_dust", PrismstoneBlocks.PRISMSTONE_WIRE, block-> () -> new PrismstoneDustItem(block.get(), new Item.Properties()));
    public static final Map<PrismstoneType, RegistryObject<Item>> PRISMSTONE_TORCH = blockItem(PrismstoneBlocks.PRISMSTONE_TORCH, PrismstoneBlocks.PRISMSTONE_WALL_TORCH, standing->wall->()->new StandingAndWallBlockItem(standing.get(), wall.get(), new Item.Properties(), Direction.DOWN));
    public static final Map<PrismstoneType, RegistryObject<Item>> PRISMSTONE_BLOCK = blockItem(PrismstoneBlocks.PRISMSTONE_BLOCK, block-> () -> new BlockItem(block.get(), new Item.Properties()));
    public static final Map<PrismstoneType, RegistryObject<Item>> PRISMSTONE_REPEATER = blockItem(PrismstoneBlocks.PRISMSTONE_REPEATER, block-> () -> new BlockItem(block.get(), new Item.Properties()));
    public static final Map<PrismstoneType, RegistryObject<Item>> PRISMSTONE_COMPARATOR = blockItem(PrismstoneBlocks.PRISMSTONE_COMPARATOR, block-> () -> new BlockItem(block.get(), new Item.Properties()));
    public static final Map<PrismstoneType, RegistryObject<Item>> PRISMSTONE_LEVER = blockItem(PrismstoneBlocks.PRISMSTONE_LEVER, block-> () -> new BlockItem(block.get(), new Item.Properties()));
    public static final Map<BlockSetType, Map<PrismstoneType, RegistryObject<Item>>> WOODEN_PRISMSTONE_BUTTON = blockItemButton(PrismstoneBlocks.WOODEN_PRISMSTONE_BUTTON, block->()->new BlockItem(block.get(), new Item.Properties()));
    public static final Map<PrismstoneType, RegistryObject<Item>> STONE_PRISMSTONE_BUTTON = blockItem(PrismstoneBlocks.STONE_PRISMSTONE_BUTTON, block-> () -> new BlockItem(block.get(), new Item.Properties()));
    public static final Map<PrismstoneType, RegistryObject<Item>> POLISHED_BLACKSTONE_PRISMSTONE_BUTTON = blockItem(PrismstoneBlocks.POLISHED_BLACKSTONE_PRISMSTONE_BUTTON, block-> () -> new BlockItem(block.get(), new Item.Properties()));


    //public static final RegistryObject<Item> STONE_HEATSTONE_BUTTON = ITEMS.register("stone_heatstone_button", () -> new BlockItem(PrismstoneBlocks.STONE_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> POLISHED_BLACKSTONE_HEATSTONE_BUTTON = ITEMS.register("polished_blackstone_heatstone_button", () -> new BlockItem(PrismstoneBlocks.POLISHED_BLACKSTONE_HEATSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MENDSTONE_DUST = ITEMS.register("mendstone_dust", () -> new PrismstoneDustItem(PrismstoneBlocks.MENDSTONE_WIRE.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MENDSTONE_TORCH = ITEMS.register("mendstone_torch", () -> new StandingAndWallBlockItem(PrismstoneBlocks.MENDSTONE_TORCH.get(), PrismstoneBlocks.MENDSTONE_WALL_TORCH.get(), new Item.Properties(), Direction.DOWN));
    ////public static final RegistryObject<Item> MENDSTONE_BLOCK = ITEMS.register("mendstone_block", () -> new BlockItem(PrismstoneBlocks.MENDSTONE_BLOCK.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MENDSTONE_REPEATER = ITEMS.register("mendstone_repeater", () -> new BlockItem(PrismstoneBlocks.MENDSTONE_REPEATER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MENDSTONE_COMPARATOR = ITEMS.register("mendstone_comparator", () -> new BlockItem(PrismstoneBlocks.MENDSTONE_COMPARATOR.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MENDSTONE_LEVER = ITEMS.register("mendstone_lever", () -> new BlockItem(PrismstoneBlocks.MENDSTONE_LEVER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MENDSTONE_GOGGLES = ITEMS.register("mendstone_goggles", () -> new PrismstoneGoggles(ArmorTiers.GOGGLES, ArmorItem.Type.HELMET, new Item.Properties(), PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Item> OAK_MENDSTONE_BUTTON = ITEMS.register("oak_mendstone_button", () -> new BlockItem(PrismstoneBlocks.OAK_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SPRUCE_MENDSTONE_BUTTON = ITEMS.register("spruce_mendstone_button", () -> new BlockItem(PrismstoneBlocks.SPRUCE_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BIRCH_MENDSTONE_BUTTON = ITEMS.register("birch_mendstone_button", () -> new BlockItem(PrismstoneBlocks.BIRCH_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> JUNGLE_MENDSTONE_BUTTON = ITEMS.register("jungle_mendstone_button", () -> new BlockItem(PrismstoneBlocks.JUNGLE_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> ACACIA_MENDSTONE_BUTTON = ITEMS.register("acacia_mendstone_button", () -> new BlockItem(PrismstoneBlocks.ACACIA_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CHERRY_MENDSTONE_BUTTON = ITEMS.register("cherry_mendstone_button", () -> new BlockItem(PrismstoneBlocks.CHERRY_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DARK_OAK_MENDSTONE_BUTTON = ITEMS.register("dark_oak_mendstone_button", () -> new BlockItem(PrismstoneBlocks.DARK_OAK_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MANGROVE_MENDSTONE_BUTTON = ITEMS.register("mangrove_mendstone_button", () -> new BlockItem(PrismstoneBlocks.MANGROVE_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BAMBOO_MENDSTONE_BUTTON = ITEMS.register("bamboo_mendstone_button", () -> new BlockItem(PrismstoneBlocks.BAMBOO_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CRIMSON_MENDSTONE_BUTTON = ITEMS.register("crimson_mendstone_button", () -> new BlockItem(PrismstoneBlocks.CRIMSON_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPED_MENDSTONE_BUTTON = ITEMS.register("warped_mendstone_button", () -> new BlockItem(PrismstoneBlocks.WARPED_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> STONE_MENDSTONE_BUTTON = ITEMS.register("stone_mendstone_button", () -> new BlockItem(PrismstoneBlocks.STONE_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> POLISHED_BLACKSTONE_MENDSTONE_BUTTON = ITEMS.register("polished_blackstone_mendstone_button", () -> new BlockItem(PrismstoneBlocks.POLISHED_BLACKSTONE_MENDSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DATASTONE_DUST = ITEMS.register("datastone_dust", () -> new PrismstoneDustItem(PrismstoneBlocks.DATASTONE_WIRE.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DATASTONE_TORCH = ITEMS.register("datastone_torch", () -> new StandingAndWallBlockItem(PrismstoneBlocks.DATASTONE_TORCH.get(), PrismstoneBlocks.DATASTONE_WALL_TORCH.get(), new Item.Properties(), Direction.DOWN));
    ////public static final RegistryObject<Item> DATASTONE_BLOCK = ITEMS.register("datastone_block", () -> new BlockItem(PrismstoneBlocks.DATASTONE_BLOCK.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DATASTONE_REPEATER = ITEMS.register("datastone_repeater", () -> new BlockItem(PrismstoneBlocks.DATASTONE_REPEATER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DATASTONE_COMPARATOR = ITEMS.register("datastone_comparator", () -> new BlockItem(PrismstoneBlocks.DATASTONE_COMPARATOR.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DATASTONE_LEVER = ITEMS.register("datastone_lever", () -> new BlockItem(PrismstoneBlocks.DATASTONE_LEVER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DATASTONE_GOGGLES = ITEMS.register("datastone_goggles", () -> new PrismstoneGoggles(ArmorTiers.GOGGLES, ArmorItem.Type.HELMET, new Item.Properties(), PrismstoneType.DATASTONE));
    //public static final RegistryObject<Item> OAK_DATASTONE_BUTTON = ITEMS.register("oak_datastone_button", () -> new BlockItem(PrismstoneBlocks.OAK_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SPRUCE_DATASTONE_BUTTON = ITEMS.register("spruce_datastone_button", () -> new BlockItem(PrismstoneBlocks.SPRUCE_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BIRCH_DATASTONE_BUTTON = ITEMS.register("birch_datastone_button", () -> new BlockItem(PrismstoneBlocks.BIRCH_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> JUNGLE_DATASTONE_BUTTON = ITEMS.register("jungle_datastone_button", () -> new BlockItem(PrismstoneBlocks.JUNGLE_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> ACACIA_DATASTONE_BUTTON = ITEMS.register("acacia_datastone_button", () -> new BlockItem(PrismstoneBlocks.ACACIA_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CHERRY_DATASTONE_BUTTON = ITEMS.register("cherry_datastone_button", () -> new BlockItem(PrismstoneBlocks.CHERRY_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DARK_OAK_DATASTONE_BUTTON = ITEMS.register("dark_oak_datastone_button", () -> new BlockItem(PrismstoneBlocks.DARK_OAK_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MANGROVE_DATASTONE_BUTTON = ITEMS.register("mangrove_datastone_button", () -> new BlockItem(PrismstoneBlocks.MANGROVE_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BAMBOO_DATASTONE_BUTTON = ITEMS.register("bamboo_datastone_button", () -> new BlockItem(PrismstoneBlocks.BAMBOO_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CRIMSON_DATASTONE_BUTTON = ITEMS.register("crimson_datastone_button", () -> new BlockItem(PrismstoneBlocks.CRIMSON_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPED_DATASTONE_BUTTON = ITEMS.register("warped_datastone_button", () -> new BlockItem(PrismstoneBlocks.WARPED_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> STONE_DATASTONE_BUTTON = ITEMS.register("stone_datastone_button", () -> new BlockItem(PrismstoneBlocks.STONE_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> POLISHED_BLACKSTONE_DATASTONE_BUTTON = ITEMS.register("polished_blackstone_datastone_button", () -> new BlockItem(PrismstoneBlocks.POLISHED_BLACKSTONE_DATASTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SOULSTONE_DUST = ITEMS.register("soulstone_dust", () -> new PrismstoneDustItem(PrismstoneBlocks.SOULSTONE_WIRE.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SOULSTONE_TORCH = ITEMS.register("soulstone_torch", () -> new StandingAndWallBlockItem(PrismstoneBlocks.SOULSTONE_TORCH.get(), PrismstoneBlocks.SOULSTONE_WALL_TORCH.get(), new Item.Properties(), Direction.DOWN));
    ////public static final RegistryObject<Item> SOULSTONE_BLOCK = ITEMS.register("soulstone_block", () -> new BlockItem(PrismstoneBlocks.SOULSTONE_BLOCK.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SOULSTONE_REPEATER = ITEMS.register("soulstone_repeater", () -> new BlockItem(PrismstoneBlocks.SOULSTONE_REPEATER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SOULSTONE_COMPARATOR = ITEMS.register("soulstone_comparator", () -> new BlockItem(PrismstoneBlocks.SOULSTONE_COMPARATOR.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SOULSTONE_LEVER = ITEMS.register("soulstone_lever", () -> new BlockItem(PrismstoneBlocks.SOULSTONE_LEVER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SOULSTONE_GOGGLES = ITEMS.register("soulstone_goggles", () -> new PrismstoneGoggles(ArmorTiers.GOGGLES, ArmorItem.Type.HELMET, new Item.Properties(), PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Item> OAK_SOULSTONE_BUTTON = ITEMS.register("oak_soulstone_button", () -> new BlockItem(PrismstoneBlocks.OAK_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SPRUCE_SOULSTONE_BUTTON = ITEMS.register("spruce_soulstone_button", () -> new BlockItem(PrismstoneBlocks.SPRUCE_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BIRCH_SOULSTONE_BUTTON = ITEMS.register("birch_soulstone_button", () -> new BlockItem(PrismstoneBlocks.BIRCH_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> JUNGLE_SOULSTONE_BUTTON = ITEMS.register("jungle_soulstone_button", () -> new BlockItem(PrismstoneBlocks.JUNGLE_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> ACACIA_SOULSTONE_BUTTON = ITEMS.register("acacia_soulstone_button", () -> new BlockItem(PrismstoneBlocks.ACACIA_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CHERRY_SOULSTONE_BUTTON = ITEMS.register("cherry_soulstone_button", () -> new BlockItem(PrismstoneBlocks.CHERRY_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DARK_OAK_SOULSTONE_BUTTON = ITEMS.register("dark_oak_soulstone_button", () -> new BlockItem(PrismstoneBlocks.DARK_OAK_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MANGROVE_SOULSTONE_BUTTON = ITEMS.register("mangrove_soulstone_button", () -> new BlockItem(PrismstoneBlocks.MANGROVE_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BAMBOO_SOULSTONE_BUTTON = ITEMS.register("bamboo_soulstone_button", () -> new BlockItem(PrismstoneBlocks.BAMBOO_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CRIMSON_SOULSTONE_BUTTON = ITEMS.register("crimson_soulstone_button", () -> new BlockItem(PrismstoneBlocks.CRIMSON_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPED_SOULSTONE_BUTTON = ITEMS.register("warped_soulstone_button", () -> new BlockItem(PrismstoneBlocks.WARPED_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> STONE_SOULSTONE_BUTTON = ITEMS.register("stone_soulstone_button", () -> new BlockItem(PrismstoneBlocks.STONE_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> POLISHED_BLACKSTONE_SOULSTONE_BUTTON = ITEMS.register("polished_blackstone_soulstone_button", () -> new BlockItem(PrismstoneBlocks.POLISHED_BLACKSTONE_SOULSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> GROWSTONE_DUST = ITEMS.register("growstone_dust", () -> new PrismstoneDustItem(PrismstoneBlocks.GROWSTONE_WIRE.get(), new Item.Properties()));
    //public static final RegistryObject<Item> GROWSTONE_TORCH = ITEMS.register("growstone_torch", () -> new StandingAndWallBlockItem(PrismstoneBlocks.GROWSTONE_TORCH.get(), PrismstoneBlocks.GROWSTONE_WALL_TORCH.get(), new Item.Properties(), Direction.DOWN));
    ////public static final RegistryObject<Item> GROWSTONE_BLOCK = ITEMS.register("growstone_block", () -> new BlockItem(PrismstoneBlocks.GROWSTONE_BLOCK.get(), new Item.Properties()));
    //public static final RegistryObject<Item> GROWSTONE_REPEATER = ITEMS.register("growstone_repeater", () -> new BlockItem(PrismstoneBlocks.GROWSTONE_REPEATER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> GROWSTONE_COMPARATOR = ITEMS.register("growstone_comparator", () -> new BlockItem(PrismstoneBlocks.GROWSTONE_COMPARATOR.get(), new Item.Properties()));
    //public static final RegistryObject<Item> GROWSTONE_LEVER = ITEMS.register("growstone_lever", () -> new BlockItem(PrismstoneBlocks.GROWSTONE_LEVER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> GROWSTONE_GOGGLES = ITEMS.register("growstone_goggles", () -> new PrismstoneGoggles(ArmorTiers.GOGGLES, ArmorItem.Type.HELMET, new Item.Properties(), PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Item> OAK_GROWSTONE_BUTTON = ITEMS.register("oak_growstone_button", () -> new BlockItem(PrismstoneBlocks.OAK_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SPRUCE_GROWSTONE_BUTTON = ITEMS.register("spruce_growstone_button", () -> new BlockItem(PrismstoneBlocks.SPRUCE_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BIRCH_GROWSTONE_BUTTON = ITEMS.register("birch_growstone_button", () -> new BlockItem(PrismstoneBlocks.BIRCH_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> JUNGLE_GROWSTONE_BUTTON = ITEMS.register("jungle_growstone_button", () -> new BlockItem(PrismstoneBlocks.JUNGLE_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> ACACIA_GROWSTONE_BUTTON = ITEMS.register("acacia_growstone_button", () -> new BlockItem(PrismstoneBlocks.ACACIA_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CHERRY_GROWSTONE_BUTTON = ITEMS.register("cherry_growstone_button", () -> new BlockItem(PrismstoneBlocks.CHERRY_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DARK_OAK_GROWSTONE_BUTTON = ITEMS.register("dark_oak_growstone_button", () -> new BlockItem(PrismstoneBlocks.DARK_OAK_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MANGROVE_GROWSTONE_BUTTON = ITEMS.register("mangrove_growstone_button", () -> new BlockItem(PrismstoneBlocks.MANGROVE_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BAMBOO_GROWSTONE_BUTTON = ITEMS.register("bamboo_growstone_button", () -> new BlockItem(PrismstoneBlocks.BAMBOO_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CRIMSON_GROWSTONE_BUTTON = ITEMS.register("crimson_growstone_button", () -> new BlockItem(PrismstoneBlocks.CRIMSON_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPED_GROWSTONE_BUTTON = ITEMS.register("warped_growstone_button", () -> new BlockItem(PrismstoneBlocks.WARPED_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> STONE_GROWSTONE_BUTTON = ITEMS.register("stone_growstone_button", () -> new BlockItem(PrismstoneBlocks.STONE_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> POLISHED_BLACKSTONE_GROWSTONE_BUTTON = ITEMS.register("polished_blackstone_growstone_button", () -> new BlockItem(PrismstoneBlocks.POLISHED_BLACKSTONE_GROWSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPSTONE_DUST = ITEMS.register("warpstone_dust", () -> new PrismstoneDustItem(PrismstoneBlocks.WARPSTONE_WIRE.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPSTONE_TORCH = ITEMS.register("warpstone_torch", () -> new StandingAndWallBlockItem(PrismstoneBlocks.WARPSTONE_TORCH.get(), PrismstoneBlocks.WARPSTONE_WALL_TORCH.get(), new Item.Properties(), Direction.DOWN));
    ////public static final RegistryObject<Item> WARPSTONE_BLOCK = ITEMS.register("warpstone_block", () -> new BlockItem(PrismstoneBlocks.WARPSTONE_BLOCK.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPSTONE_REPEATER = ITEMS.register("warpstone_repeater", () -> new BlockItem(PrismstoneBlocks.WARPSTONE_REPEATER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPSTONE_COMPARATOR = ITEMS.register("warpstone_comparator", () -> new BlockItem(PrismstoneBlocks.WARPSTONE_COMPARATOR.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPSTONE_LEVER = ITEMS.register("warpstone_lever", () -> new BlockItem(PrismstoneBlocks.WARPSTONE_LEVER.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPSTONE_GOGGLES = ITEMS.register("warpstone_goggles", () -> new PrismstoneGoggles(ArmorTiers.GOGGLES, ArmorItem.Type.HELMET, new Item.Properties(), PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Item> OAK_WARPSTONE_BUTTON = ITEMS.register("oak_warpstone_button", () -> new BlockItem(PrismstoneBlocks.OAK_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> SPRUCE_WARPSTONE_BUTTON = ITEMS.register("spruce_warpstone_button", () -> new BlockItem(PrismstoneBlocks.SPRUCE_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BIRCH_WARPSTONE_BUTTON = ITEMS.register("birch_warpstone_button", () -> new BlockItem(PrismstoneBlocks.BIRCH_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> JUNGLE_WARPSTONE_BUTTON = ITEMS.register("jungle_warpstone_button", () -> new BlockItem(PrismstoneBlocks.JUNGLE_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> ACACIA_WARPSTONE_BUTTON = ITEMS.register("acacia_warpstone_button", () -> new BlockItem(PrismstoneBlocks.ACACIA_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CHERRY_WARPSTONE_BUTTON = ITEMS.register("cherry_warpstone_button", () -> new BlockItem(PrismstoneBlocks.CHERRY_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> DARK_OAK_WARPSTONE_BUTTON = ITEMS.register("dark_oak_warpstone_button", () -> new BlockItem(PrismstoneBlocks.DARK_OAK_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> MANGROVE_WARPSTONE_BUTTON = ITEMS.register("mangrove_warpstone_button", () -> new BlockItem(PrismstoneBlocks.MANGROVE_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> BAMBOO_WARPSTONE_BUTTON = ITEMS.register("bamboo_warpstone_button", () -> new BlockItem(PrismstoneBlocks.BAMBOO_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> CRIMSON_WARPSTONE_BUTTON = ITEMS.register("crimson_warpstone_button", () -> new BlockItem(PrismstoneBlocks.CRIMSON_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> WARPED_WARPSTONE_BUTTON = ITEMS.register("warped_warpstone_button", () -> new BlockItem(PrismstoneBlocks.WARPED_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> STONE_WARPSTONE_BUTTON = ITEMS.register("stone_warpstone_button", () -> new BlockItem(PrismstoneBlocks.STONE_WARPSTONE_BUTTON.get(), new Item.Properties()));
    //public static final RegistryObject<Item> POLISHED_BLACKSTONE_WARPSTONE_BUTTON = ITEMS.register("polished_blackstone_warpstone_button", () -> new BlockItem(PrismstoneBlocks.POLISHED_BLACKSTONE_WARPSTONE_BUTTON.get(), new Item.Properties()));
    
    public static class ArmorTiers {
        public static final ArmorMaterial GOGGLES = new ModArmorMaterial(
                "goggles",
                -1,
                new int[] {0,0,0,0},
                0,
                SoundEvents.ARMOR_EQUIP_LEATHER, 
                0,
                0,
                () -> Ingredient.EMPTY
        );
    }
}
