package com._48panda.prismstone.init;

import com._48panda.prismstone.blocks.*;
import com._48panda.prismstone.blocks.wire.PrismstoneWireCrossing;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import com._48panda.prismstone.PrismstoneMod;
import com._48panda.prismstone.blocks.wire.AbstractWireBlock;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PrismstoneBlocks {
    public static Map<PrismstoneType, RegistryObject<Block>> register(String name, Function<PrismstoneType, Supplier<Block>> provider, Set<PrismstoneType> typesToUse) {
        Map<PrismstoneType, RegistryObject<Block>> blocks = new HashMap<>();
        for (PrismstoneType type: typesToUse) {
            blocks.put(type, BLOCKS.register(name.replace("prismstone", type.getSerializedName()), provider.apply(type)));
        }
        return blocks;
    }

    public static Map<BlockSetType,Map<PrismstoneType, RegistryObject<Block>>>
    register(String name, Function<BlockSetType, Function<PrismstoneType, Supplier<Block>>> provider,
             Set<PrismstoneType> typesToUse, Set<BlockSetType> blockTypes) {
        Map<BlockSetType,Map<PrismstoneType, RegistryObject<Block>>> out = new HashMap<>();
        for (BlockSetType blocktype: blockTypes) {
            Map<PrismstoneType, RegistryObject<Block>> blocks = new HashMap<>();
            Function<PrismstoneType, Supplier<Block>> provider2 = provider.apply(blocktype);
            for (PrismstoneType type : typesToUse) {
                blocks.put(type, BLOCKS.register(name.replace("oak", blocktype.name()).replace("prismstone", type.getSerializedName()), provider2.apply(type)));
            }
            out.put(blocktype, blocks);
        }
        return out;
    }
    public static final Set<PrismstoneType> ALL_TYPES = Arrays.stream(PrismstoneType.values()).collect(Collectors.toSet());
    public static final Set<PrismstoneType> NOT_REDSTONE = ALL_TYPES.stream().filter(x->x!=PrismstoneType.REDSTONE).collect(Collectors.toSet());
    
    public static final Set<BlockSetType> WOODEN_BUTTON_MATS = new HashSet<>(Arrays.asList(BlockSetType.OAK, BlockSetType.SPRUCE, BlockSetType.BIRCH, BlockSetType.JUNGLE,
            BlockSetType.ACACIA, BlockSetType.CHERRY, BlockSetType.DARK_OAK, BlockSetType.MANGROVE, BlockSetType.BAMBOO, BlockSetType.CRIMSON, BlockSetType.WARPED));
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PrismstoneMod.MODID);
    //public static final RegistryObject<Block> EXAMPLE_WIRE = BLOCKS.register("example_wire", () -> new AbstractWireBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE), PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> EXAMPLE_TORCH = BLOCKS.register("example_torch", () -> new PrismstoneTorchBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_TORCH), PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> EXAMPLE_WALL_TORCH = BLOCKS.register("example_wall_torch", () -> new PrismstoneTorchWallBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WALL_TORCH), PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new PrismstoneBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK), PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> EXAMPLE_REPEATER = BLOCKS.register("example_repeater", () -> new PrismstoneRepeaterBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER), PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> EXAMPLE_COMPARATOR = BLOCKS.register("example_comparator", () -> new PrismstoneComparatorBlock(BlockBehaviour.Properties.copy(Blocks.COMPARATOR), PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> EXAMPLE_LEVER = BLOCKS.register("example_lever", () -> new PrismstoneLeverBlock(BlockBehaviour.Properties.copy(Blocks.LEVER), PrismstoneType.EXAMPLE));
    public static final Map<PrismstoneType, RegistryObject<Block>> PRISMSTONE_WIRE = register("prismstone_wire", type -> () -> new AbstractWireBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE), type), NOT_REDSTONE);
    public static final Map<PrismstoneType, RegistryObject<Block>> PRISMSTONE_TORCH = register("prismstone_torch", type -> () -> new PrismstoneTorchBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_TORCH), type), NOT_REDSTONE);
    public static final Map<PrismstoneType, RegistryObject<Block>> PRISMSTONE_WALL_TORCH = register("prismstone_wall_torch", type -> () -> new PrismstoneTorchWallBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WALL_TORCH), type), NOT_REDSTONE);
    public static final Map<PrismstoneType, RegistryObject<Block>> PRISMSTONE_BLOCK = register("prismstone_block", type -> () -> new PrismstoneBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK), type), NOT_REDSTONE);
    public static final Map<PrismstoneType, RegistryObject<Block>> PRISMSTONE_REPEATER = register("prismstone_repeater", type -> () -> new PrismstoneRepeaterBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER), type), NOT_REDSTONE);
    public static final Map<PrismstoneType, RegistryObject<Block>> PRISMSTONE_COMPARATOR = register("prismstone_comparator", type -> () -> new PrismstoneComparatorBlock(BlockBehaviour.Properties.copy(Blocks.COMPARATOR), type), NOT_REDSTONE);

    public static final Map<PrismstoneType, RegistryObject<Block>> PRISMSTONE_LEVER = register("prismstone_lever", type -> () -> new PrismstoneLeverBlock(BlockBehaviour.Properties.copy(Blocks.LEVER), type), NOT_REDSTONE);
    public static final  Map<BlockSetType,Map<PrismstoneType, RegistryObject<Block>>> WOODEN_PRISMSTONE_BUTTON = register("oak_prismstone_button", btype-> type->woodenButton(btype, type), NOT_REDSTONE, WOODEN_BUTTON_MATS);
    public static final Map<PrismstoneType, RegistryObject<Block>> STONE_PRISMSTONE_BUTTON = register("stone_prismstone_button", PrismstoneBlocks::stoneButton, NOT_REDSTONE);
    public static final Map<PrismstoneType, RegistryObject<Block>> POLISHED_BLACKSTONE_PRISMSTONE_BUTTON = register("polished_blackstone_prismstone_button", PrismstoneBlocks::stoneButton, NOT_REDSTONE);
    //public static final RegistryObject<Block> OAK_EXAMPLE_BUTTON = BLOCKS.register("oak_example_button", woodenButton(BlockSetType.OAK, PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> SPRUCE_EXAMPLE_BUTTON = BLOCKS.register("spruce_example_button", woodenButton(BlockSetType.SPRUCE, PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> BIRCH_EXAMPLE_BUTTON = BLOCKS.register("birch_example_button", woodenButton(BlockSetType.BIRCH, PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> JUNGLE_EXAMPLE_BUTTON = BLOCKS.register("jungle_example_button", woodenButton(BlockSetType.JUNGLE, PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> ACACIA_EXAMPLE_BUTTON = BLOCKS.register("acacia_example_button", woodenButton(BlockSetType.ACACIA, PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> CHERRY_EXAMPLE_BUTTON = BLOCKS.register("cherry_example_button", woodenButton(BlockSetType.CHERRY, PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> DARK_OAK_EXAMPLE_BUTTON = BLOCKS.register("dark_oak_example_button", woodenButton(BlockSetType.DARK_OAK, PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> MANGROVE_EXAMPLE_BUTTON = BLOCKS.register("mangrove_example_button", woodenButton(BlockSetType.MANGROVE, PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> BAMBOO_EXAMPLE_BUTTON = BLOCKS.register("bamboo_example_button", woodenButton(BlockSetType.BAMBOO, PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> CRIMSON_EXAMPLE_BUTTON = BLOCKS.register("crimson_example_button", woodenButton(BlockSetType.CRIMSON, PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> WARPED_EXAMPLE_BUTTON = BLOCKS.register("warped_example_button", woodenButton(BlockSetType.WARPED, PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> STONE_EXAMPLE_BUTTON = BLOCKS.register("stone_example_button", stoneButton(PrismstoneType.EXAMPLE));
    //public static final RegistryObject<Block> POLISHED_BLACKSTONE_EXAMPLE_BUTTON = BLOCKS.register("polished_blackstone_example_button", stoneButton(PrismstoneType.EXAMPLE));

    //public static final RegistryObject<Block> HEATSTONE_WIRE = BLOCKS.register("heatstone_wire", () -> new AbstractWireBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE), PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> HEATSTONE_TORCH = BLOCKS.register("heatstone_torch", () -> new PrismstoneTorchBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_TORCH), PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> HEATSTONE_WALL_TORCH = BLOCKS.register("heatstone_wall_torch", () -> new PrismstoneTorchWallBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WALL_TORCH), PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> HEATSTONE_BLOCK = BLOCKS.register("heatstone_block", () -> new PrismstoneBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK), PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> HEATSTONE_REPEATER = BLOCKS.register("heatstone_repeater", () -> new PrismstoneRepeaterBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER), PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> HEATSTONE_COMPARATOR = BLOCKS.register("heatstone_comparator", () -> new PrismstoneComparatorBlock(BlockBehaviour.Properties.copy(Blocks.COMPARATOR), PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> HEATSTONE_LEVER = BLOCKS.register("heatstone_lever", () -> new PrismstoneLeverBlock(BlockBehaviour.Properties.copy(Blocks.LEVER), PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> OAK_HEATSTONE_BUTTON = BLOCKS.register("oak_heatstone_button", woodenButton(BlockSetType.OAK, PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> SPRUCE_HEATSTONE_BUTTON = BLOCKS.register("spruce_heatstone_button", woodenButton(BlockSetType.SPRUCE, PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> BIRCH_HEATSTONE_BUTTON = BLOCKS.register("birch_heatstone_button", woodenButton(BlockSetType.BIRCH, PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> JUNGLE_HEATSTONE_BUTTON = BLOCKS.register("jungle_heatstone_button", woodenButton(BlockSetType.JUNGLE, PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> ACACIA_HEATSTONE_BUTTON = BLOCKS.register("acacia_heatstone_button", woodenButton(BlockSetType.ACACIA, PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> CHERRY_HEATSTONE_BUTTON = BLOCKS.register("cherry_heatstone_button", woodenButton(BlockSetType.CHERRY, PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> DARK_OAK_HEATSTONE_BUTTON = BLOCKS.register("dark_oak_heatstone_button", woodenButton(BlockSetType.DARK_OAK, PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> MANGROVE_HEATSTONE_BUTTON = BLOCKS.register("mangrove_heatstone_button", woodenButton(BlockSetType.MANGROVE, PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> BAMBOO_HEATSTONE_BUTTON = BLOCKS.register("bamboo_heatstone_button", woodenButton(BlockSetType.BAMBOO, PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> CRIMSON_HEATSTONE_BUTTON = BLOCKS.register("crimson_heatstone_button", woodenButton(BlockSetType.CRIMSON, PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> WARPED_HEATSTONE_BUTTON = BLOCKS.register("warped_heatstone_button", woodenButton(BlockSetType.WARPED, PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> STONE_HEATSTONE_BUTTON = BLOCKS.register("stone_heatstone_button", stoneButton(PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> POLISHED_BLACKSTONE_HEATSTONE_BUTTON = BLOCKS.register("polished_blackstone_heatstone_button", stoneButton(PrismstoneType.HEATSTONE));
    //public static final RegistryObject<Block> MENDSTONE_WIRE = BLOCKS.register("mendstone_wire", () -> new AbstractWireBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE), PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> MENDSTONE_TORCH = BLOCKS.register("mendstone_torch", () -> new PrismstoneTorchBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_TORCH), PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> MENDSTONE_WALL_TORCH = BLOCKS.register("mendstone_wall_torch", () -> new PrismstoneTorchWallBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WALL_TORCH), PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> MENDSTONE_BLOCK = BLOCKS.register("mendstone_block", () -> new PrismstoneBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK), PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> MENDSTONE_REPEATER = BLOCKS.register("mendstone_repeater", () -> new PrismstoneRepeaterBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER), PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> MENDSTONE_COMPARATOR = BLOCKS.register("mendstone_comparator", () -> new PrismstoneComparatorBlock(BlockBehaviour.Properties.copy(Blocks.COMPARATOR), PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> MENDSTONE_LEVER = BLOCKS.register("mendstone_lever", () -> new PrismstoneLeverBlock(BlockBehaviour.Properties.copy(Blocks.LEVER), PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> OAK_MENDSTONE_BUTTON = BLOCKS.register("oak_mendstone_button", woodenButton(BlockSetType.OAK, PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> SPRUCE_MENDSTONE_BUTTON = BLOCKS.register("spruce_mendstone_button", woodenButton(BlockSetType.SPRUCE, PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> BIRCH_MENDSTONE_BUTTON = BLOCKS.register("birch_mendstone_button", woodenButton(BlockSetType.BIRCH, PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> JUNGLE_MENDSTONE_BUTTON = BLOCKS.register("jungle_mendstone_button", woodenButton(BlockSetType.JUNGLE, PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> ACACIA_MENDSTONE_BUTTON = BLOCKS.register("acacia_mendstone_button", woodenButton(BlockSetType.ACACIA, PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> CHERRY_MENDSTONE_BUTTON = BLOCKS.register("cherry_mendstone_button", woodenButton(BlockSetType.CHERRY, PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> DARK_OAK_MENDSTONE_BUTTON = BLOCKS.register("dark_oak_mendstone_button", woodenButton(BlockSetType.DARK_OAK, PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> MANGROVE_MENDSTONE_BUTTON = BLOCKS.register("mangrove_mendstone_button", woodenButton(BlockSetType.MANGROVE, PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> BAMBOO_MENDSTONE_BUTTON = BLOCKS.register("bamboo_mendstone_button", woodenButton(BlockSetType.BAMBOO, PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> CRIMSON_MENDSTONE_BUTTON = BLOCKS.register("crimson_mendstone_button", woodenButton(BlockSetType.CRIMSON, PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> WARPED_MENDSTONE_BUTTON = BLOCKS.register("warped_mendstone_button", woodenButton(BlockSetType.WARPED, PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> STONE_MENDSTONE_BUTTON = BLOCKS.register("stone_mendstone_button", stoneButton(PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> POLISHED_BLACKSTONE_MENDSTONE_BUTTON = BLOCKS.register("polished_blackstone_mendstone_button", stoneButton(PrismstoneType.MENDSTONE));
    //public static final RegistryObject<Block> DATASTONE_WIRE = BLOCKS.register("datastone_wire", () -> new AbstractWireBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE), PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> DATASTONE_TORCH = BLOCKS.register("datastone_torch", () -> new PrismstoneTorchBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_TORCH), PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> DATASTONE_WALL_TORCH = BLOCKS.register("datastone_wall_torch", () -> new PrismstoneTorchWallBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WALL_TORCH), PrismstoneType.DATASTONE));
    ////public static final RegistryObject<Block> DATASTONE_BLOCK = BLOCKS.register("datastone_block", () -> new PrismstoneBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK), PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> DATASTONE_REPEATER = BLOCKS.register("datastone_repeater", () -> new PrismstoneRepeaterBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER), PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> DATASTONE_COMPARATOR = BLOCKS.register("datastone_comparator", () -> new PrismstoneComparatorBlock(BlockBehaviour.Properties.copy(Blocks.COMPARATOR), PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> DATASTONE_LEVER = BLOCKS.register("datastone_lever", () -> new PrismstoneLeverBlock(BlockBehaviour.Properties.copy(Blocks.LEVER), PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> OAK_DATASTONE_BUTTON = BLOCKS.register("oak_datastone_button", woodenButton(BlockSetType.OAK, PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> SPRUCE_DATASTONE_BUTTON = BLOCKS.register("spruce_datastone_button", woodenButton(BlockSetType.SPRUCE, PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> BIRCH_DATASTONE_BUTTON = BLOCKS.register("birch_datastone_button", woodenButton(BlockSetType.BIRCH, PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> JUNGLE_DATASTONE_BUTTON = BLOCKS.register("jungle_datastone_button", woodenButton(BlockSetType.JUNGLE, PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> ACACIA_DATASTONE_BUTTON = BLOCKS.register("acacia_datastone_button", woodenButton(BlockSetType.ACACIA, PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> CHERRY_DATASTONE_BUTTON = BLOCKS.register("cherry_datastone_button", woodenButton(BlockSetType.CHERRY, PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> DARK_OAK_DATASTONE_BUTTON = BLOCKS.register("dark_oak_datastone_button", woodenButton(BlockSetType.DARK_OAK, PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> MANGROVE_DATASTONE_BUTTON = BLOCKS.register("mangrove_datastone_button", woodenButton(BlockSetType.MANGROVE, PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> BAMBOO_DATASTONE_BUTTON = BLOCKS.register("bamboo_datastone_button", woodenButton(BlockSetType.BAMBOO, PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> CRIMSON_DATASTONE_BUTTON = BLOCKS.register("crimson_datastone_button", woodenButton(BlockSetType.CRIMSON, PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> WARPED_DATASTONE_BUTTON = BLOCKS.register("warped_datastone_button", woodenButton(BlockSetType.WARPED, PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> STONE_DATASTONE_BUTTON = BLOCKS.register("stone_datastone_button", stoneButton(PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> POLISHED_BLACKSTONE_DATASTONE_BUTTON = BLOCKS.register("polished_blackstone_datastone_button", stoneButton(PrismstoneType.DATASTONE));
    //public static final RegistryObject<Block> SOULSTONE_WIRE = BLOCKS.register("soulstone_wire", () -> new AbstractWireBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE), PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> SOULSTONE_TORCH = BLOCKS.register("soulstone_torch", () -> new PrismstoneTorchBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_TORCH), PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> SOULSTONE_WALL_TORCH = BLOCKS.register("soulstone_wall_torch", () -> new PrismstoneTorchWallBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WALL_TORCH), PrismstoneType.SOULSTONE));
    ////public static final RegistryObject<Block> SOULSTONE_BLOCK = BLOCKS.register("soulstone_block", () -> new PrismstoneBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK), PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> SOULSTONE_REPEATER = BLOCKS.register("soulstone_repeater", () -> new PrismstoneRepeaterBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER), PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> SOULSTONE_COMPARATOR = BLOCKS.register("soulstone_comparator", () -> new PrismstoneComparatorBlock(BlockBehaviour.Properties.copy(Blocks.COMPARATOR), PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> SOULSTONE_LEVER = BLOCKS.register("soulstone_lever", () -> new PrismstoneLeverBlock(BlockBehaviour.Properties.copy(Blocks.LEVER), PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> OAK_SOULSTONE_BUTTON = BLOCKS.register("oak_soulstone_button", woodenButton(BlockSetType.OAK, PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> SPRUCE_SOULSTONE_BUTTON = BLOCKS.register("spruce_soulstone_button", woodenButton(BlockSetType.SPRUCE, PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> BIRCH_SOULSTONE_BUTTON = BLOCKS.register("birch_soulstone_button", woodenButton(BlockSetType.BIRCH, PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> JUNGLE_SOULSTONE_BUTTON = BLOCKS.register("jungle_soulstone_button", woodenButton(BlockSetType.JUNGLE, PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> ACACIA_SOULSTONE_BUTTON = BLOCKS.register("acacia_soulstone_button", woodenButton(BlockSetType.ACACIA, PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> CHERRY_SOULSTONE_BUTTON = BLOCKS.register("cherry_soulstone_button", woodenButton(BlockSetType.CHERRY, PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> DARK_OAK_SOULSTONE_BUTTON = BLOCKS.register("dark_oak_soulstone_button", woodenButton(BlockSetType.DARK_OAK, PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> MANGROVE_SOULSTONE_BUTTON = BLOCKS.register("mangrove_soulstone_button", woodenButton(BlockSetType.MANGROVE, PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> BAMBOO_SOULSTONE_BUTTON = BLOCKS.register("bamboo_soulstone_button", woodenButton(BlockSetType.BAMBOO, PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> CRIMSON_SOULSTONE_BUTTON = BLOCKS.register("crimson_soulstone_button", woodenButton(BlockSetType.CRIMSON, PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> WARPED_SOULSTONE_BUTTON = BLOCKS.register("warped_soulstone_button", woodenButton(BlockSetType.WARPED, PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> STONE_SOULSTONE_BUTTON = BLOCKS.register("stone_soulstone_button", stoneButton(PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> POLISHED_BLACKSTONE_SOULSTONE_BUTTON = BLOCKS.register("polished_blackstone_soulstone_button", stoneButton(PrismstoneType.SOULSTONE));
    //public static final RegistryObject<Block> GROWSTONE_WIRE = BLOCKS.register("growstone_wire", () -> new AbstractWireBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE), PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> GROWSTONE_TORCH = BLOCKS.register("growstone_torch", () -> new PrismstoneTorchBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_TORCH), PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> GROWSTONE_WALL_TORCH = BLOCKS.register("growstone_wall_torch", () -> new PrismstoneTorchWallBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WALL_TORCH), PrismstoneType.GROWSTONE));
    ////public static final RegistryObject<Block> GROWSTONE_BLOCK = BLOCKS.register("growstone_block", () -> new PrismstoneBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK), PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> GROWSTONE_REPEATER = BLOCKS.register("growstone_repeater", () -> new PrismstoneRepeaterBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER), PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> GROWSTONE_COMPARATOR = BLOCKS.register("growstone_comparator", () -> new PrismstoneComparatorBlock(BlockBehaviour.Properties.copy(Blocks.COMPARATOR), PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> GROWSTONE_LEVER = BLOCKS.register("growstone_lever", () -> new PrismstoneLeverBlock(BlockBehaviour.Properties.copy(Blocks.LEVER), PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> OAK_GROWSTONE_BUTTON = BLOCKS.register("oak_growstone_button", woodenButton(BlockSetType.OAK, PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> SPRUCE_GROWSTONE_BUTTON = BLOCKS.register("spruce_growstone_button", woodenButton(BlockSetType.SPRUCE, PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> BIRCH_GROWSTONE_BUTTON = BLOCKS.register("birch_growstone_button", woodenButton(BlockSetType.BIRCH, PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> JUNGLE_GROWSTONE_BUTTON = BLOCKS.register("jungle_growstone_button", woodenButton(BlockSetType.JUNGLE, PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> ACACIA_GROWSTONE_BUTTON = BLOCKS.register("acacia_growstone_button", woodenButton(BlockSetType.ACACIA, PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> CHERRY_GROWSTONE_BUTTON = BLOCKS.register("cherry_growstone_button", woodenButton(BlockSetType.CHERRY, PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> DARK_OAK_GROWSTONE_BUTTON = BLOCKS.register("dark_oak_growstone_button", woodenButton(BlockSetType.DARK_OAK, PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> MANGROVE_GROWSTONE_BUTTON = BLOCKS.register("mangrove_growstone_button", woodenButton(BlockSetType.MANGROVE, PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> BAMBOO_GROWSTONE_BUTTON = BLOCKS.register("bamboo_growstone_button", woodenButton(BlockSetType.BAMBOO, PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> CRIMSON_GROWSTONE_BUTTON = BLOCKS.register("crimson_growstone_button", woodenButton(BlockSetType.CRIMSON, PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> WARPED_GROWSTONE_BUTTON = BLOCKS.register("warped_growstone_button", woodenButton(BlockSetType.WARPED, PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> STONE_GROWSTONE_BUTTON = BLOCKS.register("stone_growstone_button", stoneButton(PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> POLISHED_BLACKSTONE_GROWSTONE_BUTTON = BLOCKS.register("polished_blackstone_growstone_button", stoneButton(PrismstoneType.GROWSTONE));
    //public static final RegistryObject<Block> WARPSTONE_WIRE = BLOCKS.register("warpstone_wire", () -> new AbstractWireBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE), PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> WARPSTONE_TORCH = BLOCKS.register("warpstone_torch", () -> new PrismstoneTorchBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_TORCH), PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> WARPSTONE_WALL_TORCH = BLOCKS.register("warpstone_wall_torch", () -> new PrismstoneTorchWallBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WALL_TORCH), PrismstoneType.WARPSTONE));
    ////public static final RegistryObject<Block> WARPSTONE_BLOCK = BLOCKS.register("warpstone_block", () -> new PrismstoneBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK), PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> WARPSTONE_REPEATER = BLOCKS.register("warpstone_repeater", () -> new PrismstoneRepeaterBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER), PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> WARPSTONE_COMPARATOR = BLOCKS.register("warpstone_comparator", () -> new PrismstoneComparatorBlock(BlockBehaviour.Properties.copy(Blocks.COMPARATOR), PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> WARPSTONE_LEVER = BLOCKS.register("warpstone_lever", () -> new PrismstoneLeverBlock(BlockBehaviour.Properties.copy(Blocks.LEVER), PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> OAK_WARPSTONE_BUTTON = BLOCKS.register("oak_warpstone_button", woodenButton(BlockSetType.OAK, PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> SPRUCE_WARPSTONE_BUTTON = BLOCKS.register("spruce_warpstone_button", woodenButton(BlockSetType.SPRUCE, PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> BIRCH_WARPSTONE_BUTTON = BLOCKS.register("birch_warpstone_button", woodenButton(BlockSetType.BIRCH, PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> JUNGLE_WARPSTONE_BUTTON = BLOCKS.register("jungle_warpstone_button", woodenButton(BlockSetType.JUNGLE, PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> ACACIA_WARPSTONE_BUTTON = BLOCKS.register("acacia_warpstone_button", woodenButton(BlockSetType.ACACIA, PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> CHERRY_WARPSTONE_BUTTON = BLOCKS.register("cherry_warpstone_button", woodenButton(BlockSetType.CHERRY, PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> DARK_OAK_WARPSTONE_BUTTON = BLOCKS.register("dark_oak_warpstone_button", woodenButton(BlockSetType.DARK_OAK, PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> MANGROVE_WARPSTONE_BUTTON = BLOCKS.register("mangrove_warpstone_button", woodenButton(BlockSetType.MANGROVE, PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> BAMBOO_WARPSTONE_BUTTON = BLOCKS.register("bamboo_warpstone_button", woodenButton(BlockSetType.BAMBOO, PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> CRIMSON_WARPSTONE_BUTTON = BLOCKS.register("crimson_warpstone_button", woodenButton(BlockSetType.CRIMSON, PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> WARPED_WARPSTONE_BUTTON = BLOCKS.register("warped_warpstone_button", woodenButton(BlockSetType.WARPED, PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> STONE_WARPSTONE_BUTTON = BLOCKS.register("stone_warpstone_button", stoneButton(PrismstoneType.WARPSTONE));
    //public static final RegistryObject<Block> POLISHED_BLACKSTONE_WARPSTONE_BUTTON = BLOCKS.register("polished_blackstone_warpstone_button", stoneButton(PrismstoneType.WARPSTONE));
    
    public static final Map<PrismstoneType, Map<PrismstoneType, RegistryObject<Block>>> WIRE_CROSSINGS;
    static {
        WIRE_CROSSINGS = new HashMap<>();
        for (PrismstoneType typeX: PrismstoneType.values()) {
            Map<PrismstoneType, RegistryObject<Block>> miniMap = new HashMap<>();
            for (PrismstoneType typeZ : PrismstoneType.values()) {
                miniMap.put(typeZ,  BLOCKS.register("wire_crossing_"+typeX.getSerializedName()+"_"+typeZ.getSerializedName(),
                        ()-> new PrismstoneWireCrossing(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE), typeX, typeZ)));
            }
            WIRE_CROSSINGS.put(typeX, miniMap);
        }
    }

    private static Supplier<Block> woodenButton(BlockSetType p_278239_, PrismstoneType type, FeatureFlag... p_278229_) {
        BlockBehaviour.Properties blockbehaviour$properties = BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
        return ()->new PrismstoneButtonBlock(blockbehaviour$properties, p_278239_, 30, true, type);
    }

    private static Supplier<Block> stoneButton(PrismstoneType type) {
        return ()->new PrismstoneButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), BlockSetType.STONE, 20, false, type);
    }

}
