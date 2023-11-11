package com._48panda.prismstone.blocks;

import com._48panda.prismstone.prismstone.interfaces.IPrismstoneInteractor;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;

public class PrismstoneButtonBlock extends ButtonBlock implements IPrismstoneInteractor {
    public PrismstoneButtonBlock(Properties properties, BlockSetType blockset, int p_273212_, boolean p_272786_, PrismstoneType type) {
        super(properties, blockset, p_273212_, p_272786_);
        prismstoneType = type;
    }
    
    public final PrismstoneType prismstoneType;

    @Override
    public boolean isSignalSource(BlockState state, PrismstoneType type) {
        return type == prismstoneType && super.isSignalSource(state);
    }

    @Override
    public int getSignal(BlockState blockstate, BlockGetter level, BlockPos pos, Direction direction, PrismstoneType type) {
        return type == prismstoneType ? super.getSignal(blockstate, level, pos, direction) : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction, PrismstoneType type) {
        return type == prismstoneType ? super.getDirectSignal(state, level, pos, direction) : 0;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return isSignalSource(state, PrismstoneType.REDSTONE);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return canPrismstoneConnectTo(state, level, pos, direction, PrismstoneType.REDSTONE);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return getSignal(state, level, pos, dir, PrismstoneType.REDSTONE);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return getDirectSignal(state, level, pos, dir, PrismstoneType.REDSTONE);
    }
}
