package com._48panda.prismstone.blocks.wire;

import com._48panda.prismstone.PrismstoneMod;
import com._48panda.prismstone.init.PrismstoneTags;
import com._48panda.prismstone.prismstone.interfaces.imlementations.PrismstoneInteractorBlock;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import com._48panda.prismstone.prismstone.rewrite.RedoneConnectivityFunctions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class PrismstoneWireCrossing extends PrismstoneInteractorBlock {
    public static final BooleanProperty NORTH = BooleanProperty.create("north_goes_up");
    public static final BooleanProperty EAST = BooleanProperty.create("east_goes_up");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south_goes_up");
    public static final BooleanProperty WEST = BooleanProperty.create("west_goes_up");
    public static final IntegerProperty POWERX = IntegerProperty.create("powerx", 0, 15);
    public static final IntegerProperty POWERZ = IntegerProperty.create("powery", 0, 15);
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));
    
    private static final VoxelShape SHAPE_DOT = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D);
    private static final Map<Direction, VoxelShape> SHAPES_FLOOR = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Direction.SOUTH, Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Direction.EAST, Block.box(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Direction.WEST, Block.box(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D)));
    private static final Map<Direction, VoxelShape> SHAPES_UP = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Shapes.or(SHAPES_FLOOR.get(Direction.NORTH), Block.box(3.0D, 0.0D, 0.0D, 13.0D, 16.0D, 1.0D)), Direction.SOUTH, Shapes.or(SHAPES_FLOOR.get(Direction.SOUTH), Block.box(3.0D, 0.0D, 15.0D, 13.0D, 16.0D, 16.0D)), Direction.EAST, Shapes.or(SHAPES_FLOOR.get(Direction.EAST), Block.box(15.0D, 0.0D, 3.0D, 16.0D, 16.0D, 13.0D)), Direction.WEST, Shapes.or(SHAPES_FLOOR.get(Direction.WEST), Block.box(0.0D, 0.0D, 3.0D, 1.0D, 16.0D, 13.0D))));
    private static final Map<BlockState, VoxelShape> SHAPES_CACHE = Maps.newHashMap();

    private static final float PARTICLE_DENSITY = 0.2F;
    
    public final PrismstoneType typeX;
    public final PrismstoneType typeZ;
    
    public PrismstoneWireCrossing(Properties properties, PrismstoneType typeX, PrismstoneType typeZ) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false)
                .setValue(SOUTH, false).setValue(WEST, false).setValue(POWERX, 0).setValue(POWERZ, 0));
        for (BlockState blockstate : this.getStateDefinition().getPossibleStates()) {
            if (blockstate.getValue(POWERX) == 0 && blockstate.getValue(POWERZ) == 0) {
                SHAPES_CACHE.put(blockstate, this.calculateShape(blockstate));
            }
        }
        this.typeX = typeX;
        this.typeZ = typeZ;
    }

    private VoxelShape calculateShape(BlockState state) {
        VoxelShape voxelshape = SHAPE_DOT;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if (state.getValue(PROPERTY_BY_DIRECTION.get(direction))) {
                voxelshape = Shapes.or(voxelshape, SHAPES_UP.get(direction));
            } else {
                voxelshape = Shapes.or(voxelshape, SHAPES_FLOOR.get(direction));
            }
        }

        return voxelshape;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPES_CACHE.get(state.setValue(POWERX, 0).setValue(POWERZ, 0));
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.getConnectionState(ctx.getLevel(), defaultBlockState(), ctx.getClickedPos());
    }
    private BlockState getConnectionState(BlockGetter level, BlockState state, BlockPos pos) {
        return getMissingConnections(level, defaultBlockState().setValue(POWERX, state.getValue(POWERX)).setValue(POWERZ, state.getValue(POWERZ)), pos);
    }

    private BlockState getMissingConnections(BlockGetter level, BlockState state, BlockPos pos) {
        boolean flag = !level.getBlockState(pos.above()).isRedstoneConductor(level, pos);

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = this.getConnectingSide(level, pos, direction, flag);
            state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), redstoneside == RedstoneSide.UP);
        }

        return state;
    }
    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState state2, LevelAccessor level, BlockPos pos, BlockPos pos2) {
        if (dir == Direction.DOWN) {
            return state;
        } else if (dir == Direction.UP) {
            return this.getConnectionState(level, state, pos);
        } else {
            RedstoneSide redstoneside = this.getConnectingSide(level, pos, dir);
            return this.getConnectionState(level, defaultBlockState()
                    .setValue(POWERX, state.getValue(POWERX))
                    .setValue(POWERZ, state.getValue(POWERZ))
                    .setValue(PROPERTY_BY_DIRECTION.get(dir), redstoneside == RedstoneSide.UP), pos);
        }
    }

    public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, int int1, int int2) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = state.getValue(PROPERTY_BY_DIRECTION.get(direction)) ? RedstoneSide.SIDE : RedstoneSide.UP;
            if (!level.getBlockState(blockpos$mutableblockpos.setWithOffset(pos, direction)).is(PrismstoneTags.Block.PRISMSTONE_WIRE)) {
                blockpos$mutableblockpos.move(Direction.DOWN);
                BlockState blockstate = level.getBlockState(blockpos$mutableblockpos);
                if (blockstate.is(PrismstoneTags.Block.PRISMSTONE_WIRE)) {
                    BlockPos blockpos = blockpos$mutableblockpos.relative(direction.getOpposite());
                    level.neighborShapeChanged(direction.getOpposite(), level.getBlockState(blockpos), blockpos$mutableblockpos, blockpos, int1, int2);
                }

                blockpos$mutableblockpos.setWithOffset(pos, direction).move(Direction.UP);
                BlockState blockstate1 = level.getBlockState(blockpos$mutableblockpos);
                if (blockstate1.is(PrismstoneTags.Block.PRISMSTONE_WIRE)) {
                    BlockPos blockpos1 = blockpos$mutableblockpos.relative(direction.getOpposite());
                    level.neighborShapeChanged(direction.getOpposite(), level.getBlockState(blockpos1), blockpos$mutableblockpos, blockpos1, int1, int2);
                }
            }
        }
    }

    private RedstoneSide getConnectingSide(BlockGetter level, BlockPos pos, Direction dir) {
        return this.getConnectingSide(level, pos, dir, !RedoneConnectivityFunctions.isPrismstoneConductor(level.getBlockState(pos.above()), level, pos, getTypeFromDirection(dir)));
    }

    private RedstoneSide getConnectingSide(BlockGetter level, BlockPos pos, Direction dir, boolean couldgoup) {
        PrismstoneType type = getTypeFromDirection(dir);
        BlockPos blockpos = pos.relative(dir);
        BlockState blockstate = level.getBlockState(blockpos);
        if (couldgoup) {
            boolean flag = blockstate.getBlock() instanceof TrapDoorBlock || this.canSurviveOn(level, blockpos, blockstate);
            if (flag && RedoneConnectivityFunctions.canPrismstoneConnectToVertically(level.getBlockState(blockpos.above()), level, blockpos.above(), dir, type)) {
                if (blockstate.isFaceSturdy(level, blockpos, dir.getOpposite())) {
                    return RedstoneSide.UP;
                }

                return RedstoneSide.SIDE;
            }
        }

        if (RedoneConnectivityFunctions.canPrismstoneConnectTo(blockstate, level, blockpos, dir, type)) {
            return RedstoneSide.SIDE;
        } else if (RedoneConnectivityFunctions.isPrismstoneConductor(blockstate, level, blockpos, type)) {
            return RedstoneSide.NONE;
        } else {
            BlockPos blockPosBelow = blockpos.below();
            return RedoneConnectivityFunctions.canPrismstoneConnectToVertically(level.getBlockState(blockPosBelow), level, blockPosBelow, dir, type) ? RedstoneSide.SIDE : RedstoneSide.NONE;
        }
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = level.getBlockState(blockpos);
        return this.canSurviveOn(level, blockpos, blockstate);
    }

    private boolean canSurviveOn(BlockGetter level, BlockPos pos, BlockState state) {
        return state.isFaceSturdy(level, pos, Direction.UP) || state.is(Blocks.HOPPER);
    }

    private void updatePowerStrength(Level level, BlockPos pos, BlockState state) {
        int i = this.calculateTargetStrength(level, pos, Direction.Axis.X);
        if (state.getValue(POWERX) != i) {
            if (level.getBlockState(pos) == state) {
                level.setBlock(pos, state.setValue(POWERX, i), 2);
            }

            Set<BlockPos> set = Sets.newHashSet();
            set.add(pos);

            for(Direction direction : Direction.values()) {
                set.add(pos.relative(direction));
            }

            for(BlockPos blockpos : set) {
                level.updateNeighborsAt(blockpos, this);
            }
        }
        i = this.calculateTargetStrength(level, pos, Direction.Axis.Z);
        if (state.getValue(POWERZ) != i) {
            if (level.getBlockState(pos) == state) {
                level.setBlock(pos, state.setValue(POWERZ, i), 2);
            }

            Set<BlockPos> set = Sets.newHashSet();
            set.add(pos);

            for(Direction direction : Direction.values()) {
                set.add(pos.relative(direction));
            }

            for(BlockPos blockpos : set) {
                level.updateNeighborsAt(blockpos, this);
            }
        }
    }

    private int calculateTargetStrength(Level level, BlockPos pos, Direction.Axis axis) {
        PrismstoneType type = getTypeFromDirection(axis);
        RedstoneWirePowerController.shouldSignal = false;
        int i = RedoneConnectivityFunctions.getBestNeighborSignal(level, pos, type);
        RedstoneWirePowerController.shouldSignal = true;
        int j = 0;
        if (i < 15) {
            for(Direction direction : Direction.Plane.HORIZONTAL) {
                if (direction.getAxis() != axis) continue;
                BlockPos blockpos = pos.relative(direction);
                BlockState blockstate = level.getBlockState(blockpos);
                j = Math.max(j, RedoneConnectivityFunctions.getWireSignal(blockstate, direction, type));
                BlockPos blockpos1 = pos.above();
                if (RedoneConnectivityFunctions.isPrismstoneConductor(blockstate, level, blockpos, type) &&
                        !RedoneConnectivityFunctions.isPrismstoneConductor(level.getBlockState(blockpos1), level, blockpos1, type)) {
                    j = Math.max(j, RedoneConnectivityFunctions.getWireSignal(level.getBlockState(blockpos.above()), direction, type));
                } else if (!RedoneConnectivityFunctions.isPrismstoneConductor(blockstate, level, blockpos, type)) {
                    j = Math.max(j, RedoneConnectivityFunctions.getWireSignal(level.getBlockState(blockpos.below()), direction, type));
                }
            }
        }
        return Math.max(i, j - 1);
    }

    private void checkCornerChangeAt(Level level, BlockPos pos) {
        if (level.getBlockState(pos).is(this)) {
            level.updateNeighborsAt(pos, this);

            for(Direction direction : Direction.values()) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }

        }
    }

    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean p_55634_) {
        if (!state2.is(state.getBlock()) && !level.isClientSide) {
            this.updatePowerStrength(level, pos, state);

            for(Direction direction : Direction.Plane.VERTICAL) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }

            this.updateNeighborsOfNeighboringWires(level, pos);
        }
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean flag) {
        if (!flag && !state.is(state2.getBlock())) {
            super.onRemove(state, level, pos, state2, flag);
            if (!level.isClientSide) {
                for(Direction direction : Direction.values()) {
                    level.updateNeighborsAt(pos.relative(direction), this);
                }

                this.updatePowerStrength(level, pos, state);
                this.updateNeighborsOfNeighboringWires(level, pos);
            }
        }
    }

    private void updateNeighborsOfNeighboringWires(Level level, BlockPos pos) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            this.checkCornerChangeAt(level, pos.relative(direction));
        }

        for(Direction direction1 : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.relative(direction1);
            if (RedoneConnectivityFunctions.isPrismstoneConductor(level.getBlockState(blockpos), level, blockpos, getTypeFromDirection(direction1))) {
                this.checkCornerChangeAt(level, blockpos.above());
            } else {
                this.checkCornerChangeAt(level, blockpos.below());
            }
        }
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos1, Block block, BlockPos pos2, boolean flag) {
        if (!level.isClientSide) {
            if (state.canSurvive(level, pos1)) {
                this.updatePowerStrength(level, pos1, state);
                
            } else {
                dropResources(state, level, pos1);
                level.removeBlock(pos1, false);
            }
        }
    }
    private void spawnParticlesAlongLine(Level level, RandomSource source, BlockPos pos, Vec3 vector, Direction dir, Direction dir2, float float1, float float2, Direction d) {
        float f = float2 - float1;
        if (!(source.nextFloat() >= 0.2F * f)) {
            float f1 = 0.4375F;
            float f2 = float1 + f * source.nextFloat();
            double d0 = 0.5D + (double)(0.4375F * (float)dir.getStepX()) + (double)(f2 * (float)dir2.getStepX());
            double d1 = 0.5D + (double)(0.4375F * (float)dir.getStepY()) + (double)(f2 * (float)dir2.getStepY());
            double d2 = 0.5D + (double)(0.4375F * (float)dir.getStepZ()) + (double)(f2 * (float)dir2.getStepZ());
            level.addParticle(getTypeFromDirection(d).getParticle(), (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, 0.0D, 0.0D, 0.0D);
        }
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            int i = PrismstoneWireCrossing.getPowerInDirection(state, direction);
            if (i != 0) {
                PrismstoneType type = getTypeFromDirection(direction);
                if (state.getValue(PROPERTY_BY_DIRECTION.get(direction))) {
                    this.spawnParticlesAlongLine(level, random, pos, type.COLORS[i], direction, Direction.UP, -0.5F, 0.5F, direction);
                    this.spawnParticlesAlongLine(level, random, pos, type.COLORS[i], Direction.DOWN, direction, 0.0F, 0.5F, direction);
                }
            }
        }
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                return state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default:
                return super.mirror(state, mirror);
        }
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }
    
    
    
    
    
    
    
    
    
    public static int getPowerInDirection(BlockState state, Direction dir) {
        return dir.getAxis() == Direction.Axis.X ? state.getValue(POWERX) : state.getValue(POWERZ);
    }

    public PrismstoneType getTypeFromDirection(Direction dir) {
        return dir.getAxis() == Direction.Axis.X ? typeX : typeZ;
    }
    public PrismstoneType getTypeFromDirection(Direction.Axis axis) {
        return axis == Direction.Axis.X ? typeX : typeZ;
    }

    @Override
    public boolean isSignalSource(BlockState state, PrismstoneType type) {
        return RedstoneWirePowerController.shouldSignal;
    }

    @Override
    public int getSignal(BlockState blockstate, BlockGetter level, BlockPos pos, Direction direction, PrismstoneType type) {
        if (type != getTypeFromDirection(direction)) return 0;
        if (RedstoneWirePowerController.shouldSignal && direction != Direction.DOWN) {
            return getPowerInDirection(blockstate, direction);
        } else {
            return 0;
        }
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction, PrismstoneType type) {
        return !RedstoneWirePowerController.shouldSignal ? 0 : this.getSignal(state, level, pos, direction, type);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH);
        builder.add(SOUTH);
        builder.add(EAST);
        builder.add(WEST);
        builder.add(POWERX);
        builder.add(POWERZ);
    }
}
