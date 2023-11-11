package com._48panda.prismstone.datagen;

import com._48panda.prismstone.PrismstoneMod;
import com._48panda.prismstone.init.PrismstoneBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PrismstoneMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //blockWithItem(PrismstoneBlocks.DATASTONE_BLOCK, "prismstone_block");
    }
    
    protected ResourceLocation blockTexture(String name) {
        return new ResourceLocation(PrismstoneMod.MODID, ModelProvider.BLOCK_FOLDER + "/" + name);
    }
    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject, String name) {
        simpleBlockWithItem(blockRegistryObject.get(), models().cubeAll(name(blockRegistryObject.get()), blockTexture(name)));
    }
}
