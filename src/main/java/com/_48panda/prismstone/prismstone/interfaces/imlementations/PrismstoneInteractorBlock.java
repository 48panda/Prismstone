package com._48panda.prismstone.prismstone.interfaces.imlementations;

import com._48panda.prismstone.prismstone.interfaces.IPrismstoneInteractor;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class PrismstoneInteractorBlock extends Block implements IPrismstoneInteractor {    
    public PrismstoneInteractorBlock(Properties properties) {
        super(properties);
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
