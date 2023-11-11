package com._48panda.prismstone.blocks;

import com._48panda.prismstone.PrismstoneMod;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import com._48panda.prismstone.prismstone.interfaces.imlementations.PrismstoneInteractorTorchBlock;
import com._48panda.prismstone.prismstone.rewrite.RedoneConnectivityFunctions;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class PrismstoneTorchBlock extends PrismstoneInteractorTorchBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private final Map<BlockGetter, List<PrismstoneTorchBlock.Toggle>> RECENT_TOGGLES = new WeakHashMap<>();
    public final PrismstoneType prismstoneType;

    public PrismstoneTorchBlock(BlockBehaviour.Properties properties, PrismstoneType prismstoneType) {
        super(properties, prismstoneType.getParticle());
        this.prismstoneType = prismstoneType;
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.TRUE));
    }

    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean flag) {
        for(Direction direction : Direction.values()) {
            level.updateNeighborsAt(pos.relative(direction), this);
        }

    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean flag) {
        if (!flag) {
            for(Direction direction : Direction.values()) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }

        }
    }

    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir, PrismstoneType type) {
        return state.getValue(LIT) && Direction.UP != dir && type == prismstoneType ? 15 : 0;
    }

    protected boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) {
        return RedoneConnectivityFunctions.hasSignal(level, pos.below(), Direction.DOWN, prismstoneType);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean flag = this.hasNeighborSignal(level, pos, state);
        List<PrismstoneTorchBlock.Toggle> list = RECENT_TOGGLES.get(level);

        while(list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L) {
            list.remove(0);
        }

        if (state.getValue(LIT)) {
            if (flag) {
                level.setBlock(pos, state.setValue(LIT, Boolean.FALSE), 3);
                if (isToggledTooFrequently(level, pos, true)) {
                    level.levelEvent(1502, pos, 0);
                    level.scheduleTick(pos, level.getBlockState(pos).getBlock(), 160);
                }
            }
        } else if (!flag && !isToggledTooFrequently(level, pos, false)) {
            level.setBlock(pos, state.setValue(LIT, Boolean.TRUE), 3);
        }

    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block otherblock, BlockPos otherpos, boolean flag) {
        if (state.getValue(LIT) == this.hasNeighborSignal(level, pos, state) && !level.getBlockTicks().willTickThisTick(pos, this)) {
            level.scheduleTick(pos, this, 2);
        }

    }

    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir, PrismstoneType type) {
        return dir == Direction.DOWN && type == prismstoneType ? RedoneConnectivityFunctions.getSignal(state, level, pos, dir, type) : 0;
    }

    public boolean isSignalSource(BlockState stage, PrismstoneType type) {
        return type == prismstoneType;
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            double d0 = (double)pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
            double d1 = (double)pos.getY() + 0.7D + (random.nextDouble() - 0.5D) * 0.2D;
            double d2 = (double)pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
            level.addParticle(this.flameParticle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    private boolean isToggledTooFrequently(Level level, BlockPos pos, boolean flag) {
        List<PrismstoneTorchBlock.Toggle> list = RECENT_TOGGLES.computeIfAbsent(level, ($) -> {
            return Lists.newArrayList();
        });
        if (flag) {
            list.add(new PrismstoneTorchBlock.Toggle(pos.immutable(), level.getGameTime()));
        }

        int i = 0;

        for(int j = 0; j < list.size(); ++j) {
            PrismstoneTorchBlock.Toggle PrismstoneTorchBlock$toggle = list.get(j);
            if (PrismstoneTorchBlock$toggle.pos.equals(pos)) {
                ++i;
                if (i >= 8) {
                    return true;
                }
            }
        }

        return false;
    }

    public static class Toggle {
        final BlockPos pos;
        final long when;

        public Toggle(BlockPos pos, long when) {
            this.pos = pos;
            this.when = when;
        }
    }
}
