package com._48panda.prismstone.blocks;

import com._48panda.prismstone.prismstone.interfaces.IPrismstoneInteractor;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import com._48panda.prismstone.prismstone.rewrite.RedoneConnectivityFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrismstoneComparatorBlock extends ComparatorBlock implements IPrismstoneInteractor {
    public PrismstoneComparatorBlock(Properties properties, PrismstoneType type) {
        super(properties);
        prismstoneType = type;
    }
    public final PrismstoneType prismstoneType;

    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        int i = superGetInputSignal(level, pos, state);
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction);
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.hasAnalogOutputSignal()) {
            i = blockstate.getAnalogOutputSignal(level, blockpos);
        } else if (i < 15 && RedoneConnectivityFunctions.isPrismstoneConductor(blockstate, level, blockpos, prismstoneType)) {
            blockpos = blockpos.relative(direction);
            blockstate = level.getBlockState(blockpos);
            ItemFrame itemframe = this.getItemFrame(level, direction, blockpos);
            int j = Math.max(itemframe == null ? Integer.MIN_VALUE : itemframe.getAnalogOutput(), blockstate.hasAnalogOutputSignal() ?
                    blockstate.getAnalogOutputSignal(level, blockpos) : Integer.MIN_VALUE);
            if (j != Integer.MIN_VALUE) {
                i = j;
            }
        }

        return i;
    }
    
    @Override
    protected int getAlternateSignal(SignalGetter level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        Direction left = direction.getClockWise();
        Direction right = direction.getCounterClockWise();
        boolean flag = this.sideInputDiodesOnly();
        return Math.max(RedoneConnectivityFunctions.getControlInputSignal(level, pos.relative(left), left, flag, prismstoneType),
                RedoneConnectivityFunctions.getControlInputSignal(level, pos.relative(right), right, flag, prismstoneType));
    }
    @javax.annotation.Nullable
    private ItemFrame getItemFrame(Level level, Direction dir, BlockPos pos) {
        List<ItemFrame> list = level.getEntitiesOfClass(ItemFrame.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), (p_289506_) -> p_289506_ != null && p_289506_.getDirection() == dir);
        return list.size() == 1 ? list.get(0) : null;
    }

    protected int superGetInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction);
        int i = RedoneConnectivityFunctions.getSignal(level, blockpos, direction, prismstoneType);
        if (i >= 15) {
            return i;
        } else {
            BlockState blockstate = level.getBlockState(blockpos);
            return Math.max(i, blockstate.is(prismstoneType.getWireBlock()) ? blockstate.getValue(BlockStateProperties.POWER) : 0);
        }
    }
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
        return type == prismstoneType ? RedoneConnectivityFunctions.getSignal(state, level, pos, direction, type) : 0;
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

    @Override
    public boolean getWeakChanges(BlockState state, LevelReader world, BlockPos pos) {
        return true;
    }
}
