package com._48panda.prismstone.prismstone.rewrite;

import com._48panda.prismstone.blocks.wire.AbstractWireBlock;
import com._48panda.prismstone.init.PrismstoneBlocks;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public enum PrismstoneType implements StringRepresentable {
    REDSTONE("redstone",
            RedStoneWireBlock.getColorForPower(15), RedStoneWireBlock.getColorForPower(1), RedStoneWireBlock.getColorForPower(0)),
    HEATSTONE("heatstone", new Vec3(1, (float)132/255, 0),
            new Vec3(89f/255, 46f/255, 0), new Vec3(31f/255, 16f/255, 0)),

    MENDSTONE("mendstone", new Vec3(1, 247f/255, 0),
            new Vec3(94f/255, 91f/255, 0), new Vec3(33f/255, 32f/255, 0)),
    DATASTONE("datastone", new Vec3(17f/255, 1, 0),
            new Vec3(3f/255, 46f/255, 0), new Vec3(1f/255, 18f/255, 0)),
    SOULSTONE("soulstone", new Vec3(0, 195f/255, 1),
            new Vec3(0, 68f/255, 89f/255), new Vec3(0, 27f/255, 36f/255)),
    GROWSTONE("growstone", new Vec3(0.5, 0, 1),
            new Vec3(28f/255, 0, 56f/255), new Vec3(14f/255, 0, 28f/255)),
    WARPSTONE("warpstone", new Vec3(1, 0, 1),
            new Vec3(92f/255, 0, 92f/255), new Vec3(46f/255, 0, 46f/255));
    
    private RegistryObject<Block> wireBlock;
    private RegistryObject<Block> fullBlock;
    private final String name;
    private final Vec3 poweredColor;
    private final Vec3 lowPowerColor;
    private final Vec3 offColor;
    private final DustParticleOptions particle;
    public final Vec3[] COLORS;
    public void registerWire(RegistryObject<Block> block) {
        wireBlock = block;
    }
    public void registerBlock(RegistryObject<Block> block) {
        fullBlock = block;
    }
    public static void registerAll() {
        REDSTONE.registerWire(RegistryObject.create(new ResourceLocation("minecraft:redstone_wire"), ForgeRegistries.BLOCKS));
        REDSTONE.registerBlock(RegistryObject.create(new ResourceLocation("minecraft:redstone_block"), ForgeRegistries.BLOCKS));
        //HEATSTONE.registerWire(PrismstoneBlocks.HEATSTONE_WIRE);
        //HEATSTONE.registerBlock(PrismstoneBlocks.HEATSTONE_BLOCK);
        //MENDSTONE.registerWire(PrismstoneBlocks.MENDSTONE_WIRE);
        //MENDSTONE.registerBlock(PrismstoneBlocks.MENDSTONE_BLOCK);
        //DATASTONE.registerWire(PrismstoneBlocks.DATASTONE_WIRE);
        //DATASTONE.registerBlock(PrismstoneBlocks.DATASTONE_BLOCK);
        //SOULSTONE.registerWire(PrismstoneBlocks.SOULSTONE_WIRE);
        //SOULSTONE.registerBlock(PrismstoneBlocks.SOULSTONE_BLOCK);
        //GROWSTONE.registerWire(PrismstoneBlocks.GROWSTONE_WIRE);
        //GROWSTONE.registerBlock(PrismstoneBlocks.GROWSTONE_BLOCK);
        //WARPSTONE.registerWire(PrismstoneBlocks.WARPSTONE_WIRE);
        //WARPSTONE.registerBlock(PrismstoneBlocks.WARPSTONE_BLOCK);
        PrismstoneBlocks.NOT_REDSTONE.forEach(x->x.registerBlock(PrismstoneBlocks.PRISMSTONE_BLOCK.get(x)));
        PrismstoneBlocks.NOT_REDSTONE.forEach(x->x.registerWire(PrismstoneBlocks.PRISMSTONE_WIRE.get(x)));
    }
    PrismstoneType(String name, Vec3 poweredColor, Vec3 lowPowerColor, Vec3 offColor) {
        this.wireBlock = null;
        this.fullBlock = null;
        COLORS = AbstractWireBlock.MakeColors(poweredColor, lowPowerColor, offColor, 16);
        this.name = name;
        this.poweredColor = poweredColor;
        this.lowPowerColor = lowPowerColor;
        this.offColor = offColor;
        this.particle = new DustParticleOptions(poweredColor.toVector3f(), 1.0F);
    }

    PrismstoneType(String name, int poweredColor, int lowPowerColor, int offColor) {
        this.wireBlock = null;
        this.fullBlock = null;
        this.name = name;
        this.poweredColor = Vec3.fromRGB24(poweredColor);
        this.lowPowerColor = Vec3.fromRGB24(lowPowerColor);
        this.offColor = Vec3.fromRGB24(offColor);
        COLORS = AbstractWireBlock.MakeColors(this.poweredColor, this.lowPowerColor, this.offColor, 16);
        this.particle = new DustParticleOptions(Vec3.fromRGB24(poweredColor).toVector3f(), 1.0F);
    }

    public DustParticleOptions getParticle() {
        return particle;
    }

    public Block getWireBlock() {
        return wireBlock.get();
    }
    public Block getFullBlock() {
        return fullBlock.get();
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public Vec3 getPoweredColor() {
        return poweredColor;
    }

    public Vec3 getlowPowerColor() {
        return lowPowerColor;
    }

    public Vec3 getOffColor() {
        return offColor;
    }
}
