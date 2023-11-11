package com._48panda.prismstone.prismstone.rewrite;

import com._48panda.prismstone.blocks.wire.AbstractWireBlock;
import com._48panda.prismstone.blocks.wire.PrismstoneWireCrossing;
import com._48panda.prismstone.init.PrismstoneTags;
import com._48panda.prismstone.prismstone.interfaces.IPrismstoneInteractor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RedoneConnectivityFunctions {
        public static int getWireSignal(BlockState state, Direction dir, PrismstoneType type) {
        if (state.getBlock() instanceof AbstractWireBlock block) {
            return block.prismstoneType == type ? state.getValue(AbstractWireBlock.POWER) : 0;
        } else if (state.getBlock() instanceof PrismstoneWireCrossing block) {
            return block.getTypeFromDirection(dir) == type ? PrismstoneWireCrossing.getPowerInDirection(state, dir) : 0;
        }
        return type == PrismstoneType.REDSTONE && state.is(Blocks.REDSTONE_WIRE) ? state.getValue(RedStoneWireBlock.POWER) : 0;
    }
    
    public static boolean isSignalSource(BlockState state, PrismstoneType type) {
        if (type == PrismstoneType.REDSTONE) {
            return state.isSignalSource();
        }
        else if (state.getBlock() instanceof IPrismstoneInteractor block) {
            return block.isSignalSource(state, type);
        }
        return false;
    }
    
    public static boolean canPrismstoneConnectTo(BlockState state, BlockGetter level, BlockPos pos,
                                                 @Nullable Direction direction, PrismstoneType type) {
        if (type == PrismstoneType.REDSTONE) {
            return state.canRedstoneConnectTo(level, pos, direction);
            // if redstone use vanilla interface.
        }
        else if (state.getBlock() instanceof IPrismstoneInteractor block) {
            return block.canPrismstoneConnectTo(state, level, pos, direction, type);
            // if has interface, use that.
        }
        // else assume no connection.
        return false;
    }

    public static boolean canPrismstoneConnectToVertically(BlockState state, BlockGetter level, BlockPos pos,
                                                 @Nullable Direction direction, PrismstoneType type) {
        if (canPrismstoneConnectTo(state, level, pos, direction, type)) return true;
        if (type == PrismstoneType.REDSTONE) {
            return state.canRedstoneConnectTo(level, pos, direction);
            // if redstone use vanilla interface.
        }
        else if (state.getBlock() instanceof IPrismstoneInteractor block) {
            return block.canPrismstoneConnectTo(state, level, pos, direction, type);
            // if has interface, use that.
        }
        // else assume no connection.
        return false;
    }

    public static int getBestNeighborSignal(Level level, BlockPos pos, PrismstoneType type) {
        if (type == PrismstoneType.REDSTONE) {
            return level.getBestNeighborSignal(pos);
        }
        int i = 0;

        for(Direction direction : Direction.values()) {
            int j = getSignal(level, pos.relative(direction), direction, type);
            if (j >= 15) {
                return 15;
            }

            if (j > i) {
                i = j;
            }
        }

        return i;
    }

    public static int getDirectSignal(SignalGetter level, BlockPos pos, Direction direction, PrismstoneType type) {
        return getDirectSignal(level.getBlockState(pos), level, pos, direction, type);
    }
    public static int getControlInputSignal(SignalGetter level, BlockPos pos, Direction dir, boolean hasToBeRepeater, PrismstoneType type) {
        BlockState blockstate = level.getBlockState(pos);
        if (hasToBeRepeater) {
            return DiodeBlock.isDiode(blockstate) ? getDirectSignal(level, pos, dir, type) : 0;
        } else if (blockstate.is(type.getFullBlock())) {
            return 15;
        } else if (blockstate.is(type.getWireBlock())) {
            return blockstate.getValue(RedStoneWireBlock.POWER);
        } else {
            return isSignalSource(blockstate, type) ? getDirectSignal(level, pos, dir, type) : 0;
        }
    }
    private static int getDirectSignal(BlockState state, SignalGetter level, BlockPos pos, Direction direction, PrismstoneType type) {
        if (type == PrismstoneType.REDSTONE) {
            return state.getDirectSignal(level, pos, direction);
        }
        if (state.getBlock() instanceof IPrismstoneInteractor block) {
            return block.getDirectSignal(state, level, pos, direction, type);
        }
        return 0;
    }
    
    public static int getDirectSignalTo(SignalGetter level, BlockPos pos, PrismstoneType type) {
        int i = 0;
        i = Math.max(i, getDirectSignal(level, pos.below(), Direction.DOWN, type));
        if (i >= 15) {
            return i;
        } else {
            i = Math.max(i, getDirectSignal(level, pos.above(), Direction.UP, type));
            if (i >= 15) {
                return i;
            } else {
                i = Math.max(i, getDirectSignal(level, pos.north(), Direction.NORTH, type));
                if (i >= 15) {
                    return i;
                } else {
                    i = Math.max(i, getDirectSignal(level, pos.south(), Direction.SOUTH, type));
                    if (i >= 15) {
                        return i;
                    } else {
                        i = Math.max(i, getDirectSignal(level, pos.west(), Direction.WEST, type));
                        if (i >= 15) {
                            return i;
                        } else {
                            i = Math.max(i, getDirectSignal(level, pos.east(), Direction.EAST, type));
                            return i;
                        }
                    }
                }
            }
        }
    }
    
    public static int getSignal(Level level, BlockPos pos, Direction direction, PrismstoneType type) {
        if (type == PrismstoneType.REDSTONE) {
            return level.getSignal(pos, direction);
        }
        BlockState blockstate = level.getBlockState(pos);
        int i = getSignal(blockstate, level, pos, direction, type);
        return shouldCheckWeakPower(blockstate, level, pos, direction, type) ? Math.max(i, getDirectSignalTo(level, pos, type)) : i;

    }
    
    public static boolean hasSignal(Level level, BlockPos pos, Direction direction, PrismstoneType type) {
        return getSignal(level, pos, direction, type) > 0;
    }

    public static int getSignal(BlockState blockstate, BlockGetter level, BlockPos pos, Direction direction, PrismstoneType type) {
        if (type == PrismstoneType.REDSTONE) {
            return blockstate.getSignal(level, pos, direction);
        }
        else if (blockstate.getBlock() instanceof IPrismstoneInteractor block) {
            return block.getSignal(blockstate, level, pos, direction, type);
        }
        return 0;
    }
    public static boolean shouldCheckWeakPower(BlockState state, Level level, BlockPos pos, Direction dir, PrismstoneType type) {
        if (type == PrismstoneType.REDSTONE) {
            return state.shouldCheckWeakPower(level, pos, dir);
        }
        else if (state.getBlock() instanceof IPrismstoneInteractor block) {
            return block.shouldCheckWeakPower(state, level, pos, dir, type);
        }
        return state.shouldCheckWeakPower(level, pos, dir);
    }
    
    public static boolean isPrismstoneConductor(BlockState state, BlockGetter level, BlockPos pos, PrismstoneType type) {
        if (type == PrismstoneType.REDSTONE) {
            return state.isRedstoneConductor(level, pos);
        }
        else if (state.getBlock() instanceof  IPrismstoneInteractor block) {
            return block.isRedstoneConductor(state, level, pos);
        }
        return state.isRedstoneConductor(level, pos); // If it doesnt differentiate its the same for all of them.
    }
}
