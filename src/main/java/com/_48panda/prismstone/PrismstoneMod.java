package com._48panda.prismstone;

import com._48panda.prismstone.blocks.*;
import com._48panda.prismstone.blocks.wire.AbstractWireBlock;
import com._48panda.prismstone.blocks.wire.PrismstoneWireCrossing;
import com._48panda.prismstone.init.MinecraftItems;
import com._48panda.prismstone.init.PrismstoneBlocks;
import com._48panda.prismstone.init.PrismstoneItems;
import com._48panda.prismstone.items.armor.PrismstoneGoggles;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PrismstoneMod.MODID)
public class PrismstoneMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "prismstone";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> PRISMSTONE = CREATIVE_MODE_TABS.register("prismstone_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> PrismstoneItems.PRISMSTONE_DUST.get(PrismstoneType.DATASTONE).get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(Items.REDSTONE);
                PrismstoneItems.PRISMSTONE_DUST.values().stream().map(RegistryObject::get).forEach(output::accept);
                output.accept(Items.REDSTONE_TORCH);
                PrismstoneItems.PRISMSTONE_TORCH.values().stream().map(RegistryObject::get).forEach(output::accept);
                output.accept(Items.REPEATER);
                PrismstoneItems.PRISMSTONE_REPEATER.values().stream().map(RegistryObject::get).forEach(output::accept);
                output.accept(Items.COMPARATOR);
                PrismstoneItems.PRISMSTONE_COMPARATOR.values().stream().map(RegistryObject::get).forEach(output::accept);
                output.accept(Items.REDSTONE_BLOCK);
                PrismstoneItems.PRISMSTONE_BLOCK.values().stream().map(RegistryObject::get).forEach(output::accept);
                output.accept(Items.LEVER);
                PrismstoneItems.PRISMSTONE_LEVER.values().stream().map(RegistryObject::get).forEach(output::accept);
                PrismstoneItems.PRISMSTONE_GOGGLES.values().stream().map(RegistryObject::get).forEach(output::accept);
                PrismstoneBlocks.WOODEN_BUTTON_MATS.forEach(mat -> {
                    output.accept(switch (mat.name()) {
                        case "oak" -> Items.OAK_BUTTON;
                        case "spruce" -> Items.SPRUCE_BUTTON;
                        case "birch" -> Items.BIRCH_BUTTON;
                        case "jungle" -> Items.JUNGLE_BUTTON;
                        case "acacia" -> Items.ACACIA_BUTTON;
                        case "cherry" -> Items.CHERRY_BUTTON;
                        case "dark_oak" -> Items.DARK_OAK_BUTTON;
                        case "mangrove" -> Items.MANGROVE_BUTTON;
                        case "bamboo" -> Items.BAMBOO_BUTTON;
                        case "crimson" -> Items.CRIMSON_BUTTON;
                        case "warped" -> Items.WARPED_BUTTON;
                        default -> Items.BARRIER;
                    });

                    PrismstoneItems.WOODEN_PRISMSTONE_BUTTON.get(mat).values().stream().map(RegistryObject::get).forEach(output::accept);
                });
                output.accept(Items.STONE_BUTTON);
                PrismstoneItems.STONE_PRISMSTONE_BUTTON.values().stream().map(RegistryObject::get).forEach(output::accept);
                output.accept(Items.POLISHED_BLACKSTONE_BUTTON);
                PrismstoneItems.POLISHED_BLACKSTONE_PRISMSTONE_BUTTON.values().stream().map(RegistryObject::get).forEach(output::accept);
                // Add the heatstone item to the tab. For your own tabs, this method is preferred over the event
            }).build());
    

    public PrismstoneMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
    
        // Register the Deferred Register to the mod event bus so blocks get registered
        PrismstoneBlocks.BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        PrismstoneItems.ITEMS.register(modEventBus);
        MinecraftItems.ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        PrismstoneType.registerAll();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    /*@SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(PrismstoneItems.REDSTONE_GOGGLES);
        }
    }*/
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public static class ClientModEvents
    {        
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            PrismstoneBlocks.PRISMSTONE_WIRE.values().stream().map(RegistryObject::get).forEach(x->
                    ItemBlockRenderTypes.setRenderLayer(x, RenderType.cutout()));
            PrismstoneBlocks.PRISMSTONE_TORCH.values().stream().map(RegistryObject::get).forEach(x->
                    ItemBlockRenderTypes.setRenderLayer(x, RenderType.cutout()));
            PrismstoneBlocks.PRISMSTONE_WALL_TORCH.values().stream().map(RegistryObject::get).forEach(x->
                    ItemBlockRenderTypes.setRenderLayer(x, RenderType.cutout()));
            PrismstoneBlocks.PRISMSTONE_REPEATER.values().stream().map(RegistryObject::get).forEach(x->
                    ItemBlockRenderTypes.setRenderLayer(x, RenderType.cutout()));
            PrismstoneBlocks.PRISMSTONE_COMPARATOR.values().stream().map(RegistryObject::get).forEach(x->
                    ItemBlockRenderTypes.setRenderLayer(x, RenderType.cutout()));

            PrismstoneBlocks.WIRE_CROSSINGS.forEach((k,v) -> v.forEach((k2,v2)->
                    ItemBlockRenderTypes.setRenderLayer(v2.get(), RenderType.cutout())));
        }
                
        
        
        public static boolean isPlayerWearingGoggles() {
            assert Minecraft.getInstance().player != null;
            return Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof PrismstoneGoggles;
        }

        public static PrismstoneType getGoggleColor() {
            assert Minecraft.getInstance().player != null;
            if (Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof PrismstoneGoggles goggles) {
                return goggles.prismstoneType;
            }
            return PrismstoneType.REDSTONE;
        }
        
        @SubscribeEvent
        public static void registerBlockColors(final RegisterColorHandlersEvent.Block event) {
            event.getBlockColors().register((state, $0, $1, tintIndex) -> {
                if (state.getBlock() instanceof AbstractWireBlock block) {
                    return block.getColorForPower(state.getValue(AbstractWireBlock.POWER));
                }
                return 0;
            }, PrismstoneBlocks.PRISMSTONE_WIRE.values().stream().map(RegistryObject::get).toArray(Block[]::new));
            
            event.getBlockColors().register((state, $0, $1, tintIndex) -> {
                if (state.getBlock() instanceof PrismstoneWireCrossing block) {
                    return tintIndex == 0 ?
                            color(block.typeX.COLORS[state.getValue(PrismstoneWireCrossing.POWERX)])
                            : color(block.typeZ.COLORS[state.getValue(PrismstoneWireCrossing.POWERZ)]);
                }
                return 0;
        }
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.REDSTONE ).get(PrismstoneType.REDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.HEATSTONE).get(PrismstoneType.REDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.MENDSTONE).get(PrismstoneType.REDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.DATASTONE).get(PrismstoneType.REDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.SOULSTONE).get(PrismstoneType.REDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.GROWSTONE).get(PrismstoneType.REDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.WARPSTONE).get(PrismstoneType.REDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.REDSTONE ).get(PrismstoneType.HEATSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.HEATSTONE).get(PrismstoneType.HEATSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.MENDSTONE).get(PrismstoneType.HEATSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.DATASTONE).get(PrismstoneType.HEATSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.SOULSTONE).get(PrismstoneType.HEATSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.GROWSTONE).get(PrismstoneType.HEATSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.WARPSTONE).get(PrismstoneType.HEATSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.REDSTONE ).get(PrismstoneType.MENDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.HEATSTONE).get(PrismstoneType.MENDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.MENDSTONE).get(PrismstoneType.MENDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.DATASTONE).get(PrismstoneType.MENDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.SOULSTONE).get(PrismstoneType.MENDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.GROWSTONE).get(PrismstoneType.MENDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.WARPSTONE).get(PrismstoneType.MENDSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.REDSTONE ).get(PrismstoneType.DATASTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.HEATSTONE).get(PrismstoneType.DATASTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.MENDSTONE).get(PrismstoneType.DATASTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.DATASTONE).get(PrismstoneType.DATASTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.SOULSTONE).get(PrismstoneType.DATASTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.GROWSTONE).get(PrismstoneType.DATASTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.WARPSTONE).get(PrismstoneType.DATASTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.REDSTONE ).get(PrismstoneType.SOULSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.HEATSTONE).get(PrismstoneType.SOULSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.MENDSTONE).get(PrismstoneType.SOULSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.DATASTONE).get(PrismstoneType.SOULSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.SOULSTONE).get(PrismstoneType.SOULSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.GROWSTONE).get(PrismstoneType.SOULSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.WARPSTONE).get(PrismstoneType.SOULSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.REDSTONE ).get(PrismstoneType.GROWSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.HEATSTONE).get(PrismstoneType.GROWSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.MENDSTONE).get(PrismstoneType.GROWSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.DATASTONE).get(PrismstoneType.GROWSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.SOULSTONE).get(PrismstoneType.GROWSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.GROWSTONE).get(PrismstoneType.GROWSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.WARPSTONE).get(PrismstoneType.GROWSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.REDSTONE ).get(PrismstoneType.WARPSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.HEATSTONE).get(PrismstoneType.WARPSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.MENDSTONE).get(PrismstoneType.WARPSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.DATASTONE).get(PrismstoneType.WARPSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.SOULSTONE).get(PrismstoneType.WARPSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.GROWSTONE).get(PrismstoneType.WARPSTONE).get()
                    ,PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.WARPSTONE).get(PrismstoneType.WARPSTONE).get()
            );
            
            //PrismstoneBlocks.WIRE_CROSSINGS.values().stream().map(Map::values).flatMap(Collection::stream).map(RegistryObject::get).toArray(Block[]::new)

            event.getBlockColors().register((state, $0, $1, tintIndex) -> {
                if (state.getBlock() instanceof PrismstoneTorchBlock block) {
                    return color(state.getValue(PrismstoneTorchBlock.LIT) ? block.prismstoneType.getPoweredColor() : block.prismstoneType.getOffColor());
                }
                return 0;
            }, Stream.concat(PrismstoneBlocks.PRISMSTONE_TORCH.values().stream(),
                    PrismstoneBlocks.PRISMSTONE_WALL_TORCH.values().stream()).map(RegistryObject::get).toArray(Block[]::new));

            event.getBlockColors().register((state, $0, $1, tintIndex) -> {
                if (state.getBlock() instanceof PrismstoneRepeaterBlock block) {
                    return color(state.getValue(PrismstoneRepeaterBlock.POWERED) ? block.prismstoneType.getPoweredColor() : block.prismstoneType.getOffColor());
                }
                return 0;
            }, PrismstoneBlocks.PRISMSTONE_REPEATER.values().stream().map(RegistryObject::get).toArray(Block[]::new));
            event.getBlockColors().register((state, $0, $1, tintIndex) -> {
                assert Minecraft.getInstance().player != null;
                if (state.getBlock() instanceof PrismstoneComparatorBlock block) {
                    return color(tintIndex == 1 ? block.prismstoneType.getPoweredColor() : block.prismstoneType.getOffColor());
                }
                return 0;
            }, PrismstoneBlocks.PRISMSTONE_COMPARATOR.values().stream().map(RegistryObject::get).toArray(Block[]::new));
            event.getBlockColors().register((state, $0, $1, tintIndex) -> {
                if (state.getBlock() instanceof PrismstoneBlock block) {
                    return color(block.prismstoneType.getPoweredColor());
                }
                return color(PrismstoneType.REDSTONE.getPoweredColor());
            }, PrismstoneBlocks.NOT_REDSTONE.stream().map(PrismstoneBlocks.PRISMSTONE_BLOCK::get).map(RegistryObject::get).toArray(Block[]::new));
            event.getBlockColors().register((state, $0, $1, tintIndex) -> {
                if (state.getBlock() instanceof PrismstoneLeverBlock block) {
                    return isPlayerWearingGoggles() && block.prismstoneType == getGoggleColor() ?
                            color(state.getValue(LeverBlock.POWERED)?block.prismstoneType.getPoweredColor():block.prismstoneType.getOffColor()) : Mth.color(0.5f,0.5f,0.5f);
                }
                if (state.is(Blocks.LEVER)) {
                    return isPlayerWearingGoggles() && getGoggleColor() == PrismstoneType.REDSTONE ?
                            color(state.getValue(LeverBlock.POWERED)?PrismstoneType.REDSTONE.getPoweredColor():PrismstoneType.REDSTONE.getOffColor()) : Mth.color(0.5f,0.5f,0.5f);
                }
                return -1;
            }, Stream.concat(PrismstoneBlocks.PRISMSTONE_LEVER.values().stream().map(RegistryObject::get), Stream.of(Blocks.LEVER)).toArray(Block[]::new));
            event.getBlockColors().register((state, $0, $1, tintIndex) -> {
                if (state.getBlock() instanceof PrismstoneButtonBlock block) {
                    return isPlayerWearingGoggles() && getGoggleColor() == block.prismstoneType ?
                            color(state.getValue(ButtonBlock.POWERED) ? block.prismstoneType.getPoweredColor() : block.prismstoneType.getOffColor()) : Mth.color(0.25f, 0.25f, 0.25f);
                }
                return isPlayerWearingGoggles() && getGoggleColor() == PrismstoneType.REDSTONE ?
                        color(state.getValue(LeverBlock.POWERED)?PrismstoneType.REDSTONE.getPoweredColor():PrismstoneType.REDSTONE.getOffColor()) : Mth.color(0.25f,0.25f,0.25f);

                    },
                    Stream.concat(
                            Stream.concat(PrismstoneBlocks.WOODEN_PRISMSTONE_BUTTON.values().stream().map(Map::values).flatMap(Collection::stream),
                            Stream.concat(PrismstoneBlocks.STONE_PRISMSTONE_BUTTON.values().stream(),
                                        PrismstoneBlocks.POLISHED_BLACKSTONE_PRISMSTONE_BUTTON.values().stream())
                                    ).map(RegistryObject::get),
                            Stream.of(Blocks.OAK_BUTTON
                            ,Blocks.SPRUCE_BUTTON
                            ,Blocks.BIRCH_BUTTON
                            ,Blocks.JUNGLE_BUTTON
                            ,Blocks.ACACIA_BUTTON
                            ,Blocks.CHERRY_BUTTON
                            ,Blocks.DARK_OAK_BUTTON
                            ,Blocks.MANGROVE_BUTTON
                            ,Blocks.BAMBOO_BUTTON
                            ,Blocks.CRIMSON_BUTTON
                            ,Blocks.WARPED_BUTTON
                            ,Blocks.STONE_BUTTON
                            ,Blocks.POLISHED_BLACKSTONE_BUTTON)).toArray(Block[]::new)
                    );
        }
        
        @SubscribeEvent
        public static void registerItemColors(final RegisterColorHandlersEvent.Item event) {
            event.getItemColors().register((stack, tintindex) -> {
                if (stack.getItem() instanceof BlockItem blockItem) {
                    if (blockItem.getBlock() instanceof PrismstoneTorchBlock block) {
                        return tintindex == 1 ? color(block.prismstoneType.getPoweredColor()) : -1;
                    }
                }
                return -1;
            }, PrismstoneItems.PRISMSTONE_TORCH.values().stream().map(RegistryObject::get).toArray(ItemLike[]::new));

            event.getItemColors().register((stack, tintindex) -> {
                if (stack.getItem() instanceof BlockItem blockItem) {
                    if (blockItem.getBlock() instanceof AbstractWireBlock block) {
                        return tintindex == 0 ? color(block.prismstoneType.getPoweredColor()) : -1;
                    }
                }
                return -1;
            }, PrismstoneItems.PRISMSTONE_DUST.values().stream().map(RegistryObject::get).toArray(ItemLike[]::new));

            event.getItemColors().register((stack, tintindex) -> {
                if (stack.getItem() instanceof BlockItem blockItem) {
                    if (blockItem.getBlock() instanceof PrismstoneRepeaterBlock block) {
                        return tintindex == 1 ? color(block.prismstoneType.getOffColor()) : -1;
                    }
                }
                return -1;
            }, PrismstoneItems.PRISMSTONE_REPEATER.values().stream().map(RegistryObject::get).toArray(ItemLike[]::new));

            event.getItemColors().register((stack, tintindex) -> {
                if (stack.getItem() instanceof BlockItem blockItem) {
                    if (blockItem.getBlock() instanceof PrismstoneComparatorBlock block) {
                        return tintindex == 1 ? color(block.prismstoneType.getOffColor()) : -1;
                    }
                }
                return -1;
            }, PrismstoneItems.PRISMSTONE_COMPARATOR.values().stream().map(RegistryObject::get).toArray(ItemLike[]::new));
            event.getItemColors().register((stack, tintindex) -> {
                if (stack.getItem() instanceof BlockItem blockItem) {
                    if (blockItem.getBlock() instanceof PrismstoneBlock block) {
                        return color(block.prismstoneType.getPoweredColor());
                    }
                }
                return -1;
            }, PrismstoneItems.PRISMSTONE_BLOCK.values().stream().map(RegistryObject::get).toArray(ItemLike[]::new));
            event.getItemColors().register((stack, tintindex) -> {
                if (stack.getItem() instanceof PrismstoneGoggles goggles) {
                    return tintindex == 1 ? color(goggles.prismstoneType.getPoweredColor()) : -1;
                }
                return -1;
            }, PrismstoneItems.PRISMSTONE_GOGGLES.values().stream().map(RegistryObject::get).toArray(ItemLike[]::new));
            event.getItemColors().register((stack, tintindex) -> {
                if (stack.getItem() instanceof BlockItem blockItem) {
                    if (blockItem.getBlock() instanceof PrismstoneLeverBlock block) {
                        return tintindex == 1 ? color(block.prismstoneType.getlowPowerColor()) : -1;
                    }
                    return tintindex == 1 ? color(PrismstoneType.REDSTONE.getlowPowerColor()) : -1;
                }
                return -1;
            }, Stream.concat(PrismstoneItems.PRISMSTONE_LEVER.values().stream().map(RegistryObject::get),Stream.of(Items.LEVER)).toArray(ItemLike[]::new));

            event.getItemColors().register((stack, tintindex) -> {
                if (stack.getItem() instanceof BlockItem blockItem) {
                    if (blockItem.getBlock() instanceof PrismstoneButtonBlock block) {
                        return color(block.prismstoneType.getlowPowerColor());
                    }
                    return color(PrismstoneType.REDSTONE.getlowPowerColor());
                }
                return -1;
            },Stream.concat(
                    Stream.concat(PrismstoneItems.WOODEN_PRISMSTONE_BUTTON.values().stream().map(Map::values).flatMap(Collection::stream),
                            Stream.concat(PrismstoneItems.STONE_PRISMSTONE_BUTTON.values().stream(),
                                    PrismstoneItems.POLISHED_BLACKSTONE_PRISMSTONE_BUTTON.values().stream())
                    ).map(RegistryObject::get),
                    Stream.of(Items.OAK_BUTTON
                            ,Items.SPRUCE_BUTTON
                            ,Items.BIRCH_BUTTON
                            ,Items.JUNGLE_BUTTON
                            ,Items.ACACIA_BUTTON
                            ,Items.CHERRY_BUTTON
                            ,Items.DARK_OAK_BUTTON
                            ,Items.MANGROVE_BUTTON
                            ,Items.BAMBOO_BUTTON
                            ,Items.CRIMSON_BUTTON
                            ,Items.WARPED_BUTTON
                            ,Items.STONE_BUTTON
                            ,Items.POLISHED_BLACKSTONE_BUTTON)).toArray(ItemLike[]::new));
        }
    }
    public static int color(Vec3 x) {
        return Mth.color((float) x.x, (float) x.y, (float) x.z);
    }
    public static int transparent() {
        return FastColor.ARGB32.color(255, 0, 0, 0);
    }
}
