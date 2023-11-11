package com._48panda.prismstone.items;

import com._48panda.prismstone.blocks.wire.AbstractWireBlock;
import com._48panda.prismstone.init.PrismstoneBlocks;
import com._48panda.prismstone.init.PrismstoneTags;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class PrismstoneDustItem extends BlockItem {

    public PrismstoneDustItem(Block block, Properties properties) {
        super(block, properties);
        this.block = block;
    }
    private Block block;
    
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        InteractionResult result = this.placeCrossing(new BlockPlaceContext(ctx));
        if (result.consumesAction()) {
            return result;
        }
        return super.useOn(ctx);
    }

    @Nullable
    protected BlockState getPlacementStateCrossing(BlockPlaceContext ctx) {
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        PrismstoneType type;
        if (state.getBlock() instanceof AbstractWireBlock wireBlock) {
            type = wireBlock.prismstoneType;
        } else if (state.is(Blocks.REDSTONE_WIRE)) {
            type = PrismstoneType.REDSTONE;
        } else {
            return null;
        }
        boolean isNS = state.getValue(RedStoneWireBlock.NORTH).isConnected() && state.getValue(RedStoneWireBlock.SOUTH).isConnected() &&
                       !(state.getValue(RedStoneWireBlock.EAST).isConnected() || state.getValue(RedStoneWireBlock.WEST).isConnected());
        boolean isEW = state.getValue(RedStoneWireBlock.EAST).isConnected() && state.getValue(RedStoneWireBlock.WEST).isConnected() &&
                !(state.getValue(RedStoneWireBlock.NORTH).isConnected() || state.getValue(RedStoneWireBlock.SOUTH).isConnected());
        if (block instanceof AbstractWireBlock block2) {
            if (block2.prismstoneType == type) {
                return null;
            }
            if (isNS) {
                return PrismstoneBlocks.WIRE_CROSSINGS.get(block2.prismstoneType).get(type).get().getStateForPlacement(ctx);
            }
            if (isEW) {
                return PrismstoneBlocks.WIRE_CROSSINGS.get(type).get(block2.prismstoneType).get().getStateForPlacement(ctx);
            }
        } else {
            if (PrismstoneType.REDSTONE == type) {
                return null;
            }
            if (isNS) {
                return PrismstoneBlocks.WIRE_CROSSINGS.get(PrismstoneType.REDSTONE).get(type).get().getStateForPlacement(ctx);
            }
            if (isEW) {
                return PrismstoneBlocks.WIRE_CROSSINGS.get(type).get(PrismstoneType.REDSTONE).get().getStateForPlacement(ctx);
            }
        }
        return null;
    }

    public InteractionResult placeCrossing(BlockPlaceContext ctx) {
        if (!this.getBlock().isEnabled(ctx.getLevel().enabledFeatures())) {
            return InteractionResult.FAIL; // If this block is disabled?
        } else if (!ctx.canPlace()) {
            return InteractionResult.FAIL; // If you cant place
        } else {
            BlockPlaceContext blockplacecontext = ctx;
            if (ctx.getLevel().getBlockState(ctx.getClickedPos().relative(ctx.getClickedFace().getOpposite())).is(PrismstoneTags.Block.PRISMSTONE_WIRE)) {
                 blockplacecontext = BlockPlaceContext.at(ctx, ctx.getClickedPos().relative(ctx.getClickedFace().getOpposite(), 2), ctx.getClickedFace());
            }
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockstate = getPlacementStateCrossing(blockplacecontext);
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(blockplacecontext, blockstate)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    Level level = blockplacecontext.getLevel();
                    Player player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    /*if (blockstate1.is(blockstate.getBlock())) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                        this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
                        blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                        }
                    }*/
                    SoundType soundtype = blockstate1.getSoundType(level, blockpos, ctx.getPlayer());
                    level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, ctx.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
                    if (player == null || !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }
}
