package com._48panda.prismstone.prismstone.interfaces;

import com._48panda.prismstone.blocks.PrismstoneBlock;
import com._48panda.prismstone.blocks.wire.PrismstoneWireCrossing;
import com._48panda.prismstone.init.PrismstoneBlocks;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IPrismstoneInteractor {
    // This is an interface Block classes may implement to be able to interact with prismstone
    boolean isSignalSource(BlockState state, PrismstoneType type); // Is it capable of outputting prismstone
    default boolean canPrismstoneConnectTo(BlockState state, BlockGetter level, BlockPos pos,
                                                         @Nullable Direction direction, PrismstoneType type) {
        if (state.is(type.getWireBlock())) {
            return true;
        }
        if (direction != null && state.getBlock() instanceof  PrismstoneWireCrossing block) {
            return block.getTypeFromDirection(direction) == type;
        }
        else {
            return isSignalSource(state, type) && direction != null;
        }
    };
    int getSignal(BlockState blockstate, BlockGetter level, BlockPos pos, Direction direction, PrismstoneType type);

    default boolean shouldCheckWeakPower(BlockState state, Level level, BlockPos pos, Direction dir, PrismstoneType type) {
        return isRedstoneConductor(state, level, pos);
    }

    default boolean isRedstoneConductor(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isRedstoneConductor(level, pos);
    }

    int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction, PrismstoneType type);
}
