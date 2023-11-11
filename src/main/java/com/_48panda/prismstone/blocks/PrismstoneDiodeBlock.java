package com._48panda.prismstone.blocks;

import com._48panda.prismstone.prismstone.interfaces.imlementations.PrismstoneInteractorHorizontalDirectionalBlock;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import com._48panda.prismstone.prismstone.rewrite.RedoneConnectivityFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public abstract class PrismstoneDiodeBlock extends PrismstoneInteractorHorizontalDirectionalBlock {
    public PrismstoneDiodeBlock(Properties properties, PrismstoneType prismstoneType) {
        super(properties);
        this.prismstoneType = prismstoneType;
    }
    public final PrismstoneType prismstoneType;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below());
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!this.isLocked(level, pos, state)) {
            boolean flag = state.getValue(POWERED);
            boolean flag1 = this.shouldTurnOn(level, pos, state);
            if (flag && !flag1) {
                level.setBlock(pos, state.setValue(POWERED, Boolean.FALSE), 2);
            } else if (!flag) {
                level.setBlock(pos, state.setValue(POWERED, Boolean.TRUE), 2);
                if (!flag1) {
                    level.scheduleTick(pos, this, this.getDelay(state), TickPriority.VERY_HIGH);
                }
            }

        }
    }

    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir, PrismstoneType type) {
        return RedoneConnectivityFunctions.getSignal(state, level, pos, dir, type);
    }

    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir, PrismstoneType type) {
        if (type != prismstoneType) {
            return 0;
        }
        if (!state.getValue(POWERED)) {
            return 0;
        } else {
            return state.getValue(FACING) == dir ? this.getOutputSignal(level, pos, state) : 0;
        }
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos otherpos, boolean flag) {
        if (state.canSurvive(level, pos)) {
            this.checkTickOnNeighbor(level, pos, state);
        } else {
            BlockEntity blockentity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
            dropResources(state, level, pos, blockentity);
            level.removeBlock(pos, false);

            for(Direction direction : Direction.values()) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }

        }
    }

    protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
        if (!this.isLocked(level, pos, state)) {
            boolean flag = state.getValue(POWERED);
            boolean flag1 = this.shouldTurnOn(level, pos, state);
            if (flag != flag1 && !level.getBlockTicks().willTickThisTick(pos, this)) {
                TickPriority tickpriority = TickPriority.HIGH;
                if (this.shouldPrioritize(level, pos, state)) {
                    tickpriority = TickPriority.EXTREMELY_HIGH;
                } else if (flag) {
                    tickpriority = TickPriority.VERY_HIGH;
                }

                level.scheduleTick(pos, this, this.getDelay(state), tickpriority);
            }
        }
    }

    public boolean isLocked(LevelReader level, BlockPos pos, BlockState state) {
        return false;
    }

    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        return this.getInputSignal(level, pos, state) > 0;
    }

    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
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

    protected int getAlternateSignal(SignalGetter level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        Direction left = direction.getClockWise();
        Direction right = direction.getCounterClockWise();
        boolean flag = this.sideInputDiodesOnly();
        return Math.max(RedoneConnectivityFunctions.getControlInputSignal(level, pos.relative(left), left, flag, prismstoneType),
                RedoneConnectivityFunctions.getControlInputSignal(level, pos.relative(right), right, flag, prismstoneType));
    }

    public boolean isSignalSource(BlockState state, PrismstoneType type) {
        return type == prismstoneType;
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack itemStack) {
        if (this.shouldTurnOn(level, pos, state)) {
            level.scheduleTick(pos, this, 1);
        }

    }

    public void onPlace(BlockState state1, Level level, BlockPos pos, BlockState state2, boolean flag) {
        this.updateNeighborsInFront(level, pos, state1);
    }

    public void onRemove(BlockState state1, Level level, BlockPos pos, BlockState state, boolean flag) {
        if (!flag && !state1.is(state.getBlock())) {
            super.onRemove(state1, level, pos, state, flag);
            this.updateNeighborsInFront(level, pos, state1);
        }
    }

    protected void updateNeighborsInFront(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(level, pos, level.getBlockState(pos),
                java.util.EnumSet.of(direction.getOpposite()), false).isCanceled())
            return;
        level.neighborChanged(blockpos, this, pos);
        level.updateNeighborsAtExceptFromFacing(blockpos, this, direction);
    }

    protected boolean sideInputDiodesOnly() {
        return false;
    }

    protected int getOutputSignal(BlockGetter level, BlockPos pos, BlockState state) {
        return 15;
    }

    public static boolean isDiode(BlockState state) {
        return DiodeBlock.isDiode(state);
    }

    public boolean shouldPrioritize(BlockGetter level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING).getOpposite();
        BlockState blockstate = level.getBlockState(pos.relative(direction));
        return isDiode(blockstate) && blockstate.getValue(FACING) != direction;
    }

    protected abstract int getDelay(BlockState state);
}
