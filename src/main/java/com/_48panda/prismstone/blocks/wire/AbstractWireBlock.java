package com._48panda.prismstone.blocks.wire;

import com._48panda.prismstone.init.PrismstoneTags;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import com._48panda.prismstone.PrismstoneMod;
import com._48panda.prismstone.prismstone.interfaces.imlementations.PrismstoneInteractorBlock;
import com._48panda.prismstone.prismstone.rewrite.RedoneConnectivityFunctions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class AbstractWireBlock extends PrismstoneInteractorBlock {
    public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
    public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
    public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
    public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));
    private static final VoxelShape SHAPE_DOT = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D);
    private static final Map<Direction, VoxelShape> SHAPES_FLOOR = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Direction.SOUTH, Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Direction.EAST, Block.box(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Direction.WEST, Block.box(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D)));
    private static final Map<Direction, VoxelShape> SHAPES_UP = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Shapes.or(SHAPES_FLOOR.get(Direction.NORTH), Block.box(3.0D, 0.0D, 0.0D, 13.0D, 16.0D, 1.0D)), Direction.SOUTH, Shapes.or(SHAPES_FLOOR.get(Direction.SOUTH), Block.box(3.0D, 0.0D, 15.0D, 13.0D, 16.0D, 16.0D)), Direction.EAST, Shapes.or(SHAPES_FLOOR.get(Direction.EAST), Block.box(15.0D, 0.0D, 3.0D, 16.0D, 16.0D, 13.0D)), Direction.WEST, Shapes.or(SHAPES_FLOOR.get(Direction.WEST), Block.box(0.0D, 0.0D, 3.0D, 1.0D, 16.0D, 13.0D))));
    private static final Map<BlockState, VoxelShape> SHAPES_CACHE = Maps.newHashMap();
    public final Vec3[] COLORS;
    /*Util.make(new Vec3[16], (p_154319_) -> {
        for(int i = 0; i <= 15; ++i) {
            float f = (float)i / 15.0F;
            float f1 = f * 0.6F + (f > 0.0F ? 0.4F : 0.3F);
            float f2 = Mth.clamp(f * f * 0.7F - 0.5F, 0.0F, 1.0F);
            float f3 = Mth.clamp(f * f * 0.6F - 0.7F, 0.0F, 1.0F);
            p_154319_[i] = new Vec3((double)f1, (double)f2, (double)f3);
        }

    });*/
    public final PrismstoneType prismstoneType;
    public static Vec3[] MakeColors(Vec3 max_power, Vec3 min_power, Vec3 off, int num_colors) {
        Vec3[] colors = new Vec3[num_colors];
        colors[0] = off;
        for (int i=1 ; i<num_colors ; i++) {
            float t = (float)(i - 1) / (num_colors - 2);
            float r = (float) Mth.clamp(min_power.x * (1-t) + max_power.x * t, 0, 1);
            float g = (float) Mth.clamp(min_power.y * (1-t) + max_power.y * t, 0, 1);
            float b = (float) Mth.clamp(min_power.z * (1-t) + max_power.z * t, 0, 1);
            colors[i] = new Vec3(r, g, b);
        }
        return colors;
    }

    private final BlockState crossState;


    public boolean isSignalSource(BlockState state, PrismstoneType type) {
        return type == prismstoneType;
    }
    
    public int getSignal(BlockState blockstate, BlockGetter level, BlockPos pos, Direction direction, PrismstoneType type) {
        if (type != prismstoneType) return 0;
        if (RedstoneWirePowerController.shouldSignal && direction != Direction.DOWN) {
            int i = blockstate.getValue(POWER);
            if (i == 0) {
                return 0;
            } else {
                return direction != Direction.UP && 
                        !this.getConnectionState(level, blockstate, pos)
                                .getValue(PROPERTY_BY_DIRECTION.get(direction.getOpposite())).isConnected() ? 0 : i;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction, PrismstoneType type) {
        return RedstoneWirePowerController.shouldSignal && type == prismstoneType ? RedoneConnectivityFunctions.getSignal(state, level, pos, direction, type) : 0;
    }


    //END OF NEW METHODS

    private RedstoneSide getConnectingSide(BlockGetter level, BlockPos pos, Direction dir, boolean couldGoUp) {
        BlockPos blockpos = pos.relative(dir);
        BlockState blockstate = level.getBlockState(blockpos);
        if (couldGoUp) {
            boolean flag = blockstate.getBlock() instanceof TrapDoorBlock || this.canSurviveOn(level, blockpos, blockstate);
            if (flag && RedoneConnectivityFunctions.canPrismstoneConnectToVertically(level.getBlockState(blockpos.above()), level, blockpos.above(), dir, prismstoneType)) {
                if (blockstate.isFaceSturdy(level, blockpos, dir.getOpposite())) {
                    return RedstoneSide.UP;
                }
                return RedstoneSide.SIDE;
            }
        }

        if (RedoneConnectivityFunctions.canPrismstoneConnectTo(blockstate, level, blockpos, dir, prismstoneType)) {
            return RedstoneSide.SIDE;
        } else if (RedoneConnectivityFunctions.isPrismstoneConductor(blockstate, level, blockpos, prismstoneType)) {
            return RedstoneSide.NONE;
        } else {
            BlockPos blockPosBelow = blockpos.below();
            return RedoneConnectivityFunctions.canPrismstoneConnectToVertically(level.getBlockState(blockPosBelow), level, blockPosBelow, dir, prismstoneType) ? RedstoneSide.SIDE : RedstoneSide.NONE;
        }
    }

    private int calculateTargetStrength(Level level, BlockPos pos) {
        RedstoneWirePowerController.shouldSignal = false;
        int i = RedoneConnectivityFunctions.getBestNeighborSignal(level, pos, prismstoneType);
        RedstoneWirePowerController.shouldSignal = true;
        int j = 0;
        if (i < 15) {
            for(Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos blockpos = pos.relative(direction);
                BlockState blockstate = level.getBlockState(blockpos);
                j = Math.max(j, RedoneConnectivityFunctions.getWireSignal(blockstate, direction, prismstoneType));
                BlockPos blockpos1 = pos.above();
                if (RedoneConnectivityFunctions.isPrismstoneConductor(blockstate, level, blockpos, prismstoneType)
                        && !RedoneConnectivityFunctions.isPrismstoneConductor(level.getBlockState(blockpos1), level, blockpos1, prismstoneType)) {
                    j = Math.max(j, RedoneConnectivityFunctions.getWireSignal(level.getBlockState(blockpos.above()), direction, prismstoneType));
                } else if (!RedoneConnectivityFunctions.isPrismstoneConductor(blockstate, level, blockpos, prismstoneType)) {
                    j = Math.max(j, RedoneConnectivityFunctions.getWireSignal(level.getBlockState(blockpos.below()), direction, prismstoneType));
                }
            }
        }

        return Math.max(i, j - 1);
    }
    
    //END OF CHANGED METHODS

    public AbstractWireBlock(BlockBehaviour.Properties properties, PrismstoneType type) {
        super(properties);
        this.prismstoneType = type;
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, RedstoneSide.NONE).setValue(EAST, RedstoneSide.NONE).setValue(SOUTH, RedstoneSide.NONE).setValue(WEST, RedstoneSide.NONE).setValue(POWER, 0));
        this.crossState = this.defaultBlockState().setValue(NORTH, RedstoneSide.SIDE).setValue(EAST, RedstoneSide.SIDE).setValue(SOUTH, RedstoneSide.SIDE).setValue(WEST, RedstoneSide.SIDE);

        for(BlockState blockstate : this.getStateDefinition().getPossibleStates()) {
            if (blockstate.getValue(POWER) == 0) {
                SHAPES_CACHE.put(blockstate, this.calculateShape(blockstate));
            }
        }
        COLORS = MakeColors(prismstoneType.getPoweredColor(),prismstoneType.getlowPowerColor(),prismstoneType.getOffColor(),16);
    }

    private VoxelShape calculateShape(BlockState state) {
        VoxelShape voxelshape = SHAPE_DOT;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = state.getValue(PROPERTY_BY_DIRECTION.get(direction));
            if (redstoneside == RedstoneSide.SIDE) {
                voxelshape = Shapes.or(voxelshape, SHAPES_FLOOR.get(direction));
            } else if (redstoneside == RedstoneSide.UP) {
                voxelshape = Shapes.or(voxelshape, SHAPES_UP.get(direction));
            }
        }

        return voxelshape;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext collisionContext) {
        return SHAPES_CACHE.get(state.setValue(POWER, 0));
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.getConnectionState(ctx.getLevel(), this.crossState, ctx.getClickedPos());
    }
    
    private BlockState getConnectionState(BlockGetter level, BlockState state, BlockPos pos) {
        boolean flag = isDot(state);
        state = this.getMissingConnections(level, this.defaultBlockState().setValue(POWER, state.getValue(POWER)), pos);
        if (flag && isDot(state)) {
            return state;
        } else {
            boolean flag1 = state.getValue(NORTH).isConnected();
            boolean flag2 = state.getValue(SOUTH).isConnected();
            boolean flag3 = state.getValue(EAST).isConnected();
            boolean flag4 = state.getValue(WEST).isConnected();
            boolean flag5 = !flag1 && !flag2;
            boolean flag6 = !flag3 && !flag4;
            if (!flag4 && flag5) {
                state = state.setValue(WEST, RedstoneSide.SIDE);
            }

            if (!flag3 && flag5) {
                state = state.setValue(EAST, RedstoneSide.SIDE);
            }

            if (!flag1 && flag6) {
                state = state.setValue(NORTH, RedstoneSide.SIDE);
            }

            if (!flag2 && flag6) {
                state = state.setValue(SOUTH, RedstoneSide.SIDE);
            }

            return state;
        }
    }

    private BlockState getMissingConnections(BlockGetter level, BlockState state, BlockPos pos) {
        boolean flag = !RedoneConnectivityFunctions.isPrismstoneConductor(level.getBlockState(pos.above()),level, pos, prismstoneType);

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if (!state.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected()) {
                RedstoneSide redstoneside = this.getConnectingSide(level, pos, direction, flag);
                state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), redstoneside);
            }
        }

        return state;
    }

    public BlockState updateShape(BlockState state, Direction dir, BlockState otherstate, LevelAccessor level, BlockPos pos, BlockPos otherpos) {
        if (dir == Direction.DOWN) {
            return state;
        } else if (dir == Direction.UP) {
            return this.getConnectionState(level, state, pos);
        } else {
            RedstoneSide redstoneside = this.getConnectingSide(level, pos, dir);
            return redstoneside.isConnected() == state.getValue(PROPERTY_BY_DIRECTION.get(dir)).isConnected() &&
                    !isCross(state) ? state.setValue(PROPERTY_BY_DIRECTION.get(dir), redstoneside) :
                    this.getConnectionState(level, this.crossState.setValue(POWER, state.getValue(POWER))
                            .setValue(PROPERTY_BY_DIRECTION.get(dir), redstoneside), pos);
        }
    }
    
    private static boolean isCross(BlockState state) {
        return state.getValue(NORTH).isConnected() && state.getValue(SOUTH).isConnected() && state.getValue(EAST).isConnected() && state.getValue(WEST).isConnected();
    }

    private static boolean isDot(BlockState state) {
        return !state.getValue(NORTH).isConnected() && !state.getValue(SOUTH).isConnected() && !state.getValue(EAST).isConnected() && !state.getValue(WEST).isConnected();
    }

    public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, int $0, int $1) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = state.getValue(PROPERTY_BY_DIRECTION.get(direction));
            if (redstoneside != RedstoneSide.NONE && !level.getBlockState(blockpos$mutableblockpos.setWithOffset(pos, direction)).is(PrismstoneTags.Block.PRISMSTONE_WIRE)) {
                blockpos$mutableblockpos.move(Direction.DOWN);
                BlockState blockstate = level.getBlockState(blockpos$mutableblockpos);
                if (blockstate.is(PrismstoneTags.Block.PRISMSTONE_WIRE)) {
                    BlockPos blockpos = blockpos$mutableblockpos.relative(direction.getOpposite());
                    level.neighborShapeChanged(direction.getOpposite(), level.getBlockState(blockpos), blockpos$mutableblockpos, blockpos, $0, $1);
                }

                blockpos$mutableblockpos.setWithOffset(pos, direction).move(Direction.UP);
                BlockState blockstate1 = level.getBlockState(blockpos$mutableblockpos);
                if (blockstate1.is(PrismstoneTags.Block.PRISMSTONE_WIRE)) {
                    BlockPos blockpos1 = blockpos$mutableblockpos.relative(direction.getOpposite());
                    level.neighborShapeChanged(direction.getOpposite(), level.getBlockState(blockpos1), blockpos$mutableblockpos, blockpos1, $0, $1);
                }
            }
        }
    }

    private RedstoneSide getConnectingSide(BlockGetter level, BlockPos pos, Direction dir) {
        return this.getConnectingSide(level, pos, dir, !RedoneConnectivityFunctions.
                isPrismstoneConductor(level.getBlockState(pos.above()),level, pos, prismstoneType));
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
        int i = this.calculateTargetStrength(level, pos);
        if (state.getValue(POWER) != i) {
            if (level.getBlockState(pos) == state) {
                level.setBlock(pos, state.setValue(POWER, i), 2);
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

    private void checkCornerChangeAt(Level level, BlockPos pos) {
        if (level.getBlockState(pos).is(this)) {
            level.updateNeighborsAt(pos, this);

            for(Direction direction : Direction.values()) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }

        }
    }

    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState old, boolean unknown_boolean) {
        if (!old.is(state.getBlock()) && !level.isClientSide) {
            this.updatePowerStrength(level, pos, state);

            for(Direction direction : Direction.Plane.VERTICAL) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }

            this.updateNeighborsOfNeighboringWires(level, pos);
        }
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState oldstate, boolean unknownBoolean) {
        if (!unknownBoolean && !state.is(oldstate.getBlock())) {
            super.onRemove(state, level, pos, oldstate, false);
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
            if (RedoneConnectivityFunctions.isPrismstoneConductor(level.getBlockState(blockpos), level, blockpos, prismstoneType)) {
                this.checkCornerChangeAt(level, blockpos.above());
            } else {
                this.checkCornerChangeAt(level, blockpos.below());
            }
        }

    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean unusedBoolean) {
        if (!level.isClientSide) {
            if (state.canSurvive(level, pos)) {
                this.updatePowerStrength(level, pos, state);
            } else {
                dropResources(state, level, pos);
                level.removeBlock(pos, false);
            }

        }
    }

    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return !RedstoneWirePowerController.shouldSignal ? 0 : state.getSignal(level, pos, dir);
    }
    
    /*protected static boolean shouldConnectTo(BlockState p_55641_) {
        return shouldConnectTo(p_55641_, (Direction)null);
    }

    protected static boolean shouldConnectTo(BlockState state, @Nullable Direction dir) {
        if (state.is(Blocks.REDSTONE_WIRE)) {
            return true;
        } else if (state.is(Blocks.REPEATER)) {
            Direction direction = state.getValue(RepeaterBlock.FACING);
            return direction == dir || direction.getOpposite() == dir;
        } else if (state.is(Blocks.OBSERVER)) {
            return dir == state.getValue(ObserverBlock.FACING);
        } else {
            return state.isSignalSource() && dir != null;
        }
    }*/

    public int getColorForPower(int power) {
        Vec3 vec3 = COLORS[power];
        return Mth.color((float)vec3.x(), (float)vec3.y(), (float)vec3.z());
    }
    
    private void spawnParticlesAlongLine(Level level, RandomSource random, BlockPos pos, Vec3 color, Direction dir1, Direction dir2, float float1, float float2) {
        float f = float2 - float1;
        if (!(random.nextFloat() >= 0.2F * f)) {
            float f1 = 0.4375F;
            float f2 = float1 + f * random.nextFloat();
            double d0 = 0.5D + (double)(0.4375F * (float)dir1.getStepX()) + (double)(f2 * (float)dir2.getStepX());
            double d1 = 0.5D + (double)(0.4375F * (float)dir1.getStepY()) + (double)(f2 * (float)dir2.getStepY());
            double d2 = 0.5D + (double)(0.4375F * (float)dir1.getStepZ()) + (double)(f2 * (float)dir2.getStepZ());
            level.addParticle(prismstoneType.getParticle(), (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, 0.0D, 0.0D, 0.0D);
        }
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int i = state.getValue(POWER);
        if (i != 0) {
            for(Direction direction : Direction.Plane.HORIZONTAL) {
                RedstoneSide redstoneside = state.getValue(PROPERTY_BY_DIRECTION.get(direction));
                switch (redstoneside) {
                    case UP:
                        this.spawnParticlesAlongLine(level, random, pos, COLORS[i], direction, Direction.UP, -0.5F, 0.5F);
                    case SIDE:
                        this.spawnParticlesAlongLine(level, random, pos, COLORS[i], Direction.DOWN, direction, 0.0F, 0.5F);
                        break;
                    case NONE:
                    default:
                        this.spawnParticlesAlongLine(level, random, pos, COLORS[i], Direction.DOWN, direction, 0.0F, 0.3F);
                }
            }

        }
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 ->
                    state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default -> state;
        };
    }

    public BlockState mirror(BlockState blockstate, Mirror mirroraxis) {
        return switch (mirroraxis) {
            case LEFT_RIGHT ->
                    blockstate.setValue(NORTH, blockstate.getValue(SOUTH)).setValue(SOUTH, blockstate.getValue(NORTH));
            case FRONT_BACK ->
                    blockstate.setValue(EAST, blockstate.getValue(WEST)).setValue(WEST, blockstate.getValue(EAST));
            default -> super.mirror(blockstate, mirroraxis);
        };
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
        state.add(NORTH, EAST, SOUTH, WEST, POWER);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            if (isCross(state) || isDot(state)) {
                BlockState blockstate = isCross(state) ? this.defaultBlockState() : this.crossState;
                blockstate = blockstate.setValue(POWER, state.getValue(POWER));
                blockstate = this.getConnectionState(level, blockstate, pos);
                if (blockstate != state) {
                    level.setBlock(pos, blockstate, 3);
                    this.updatesOnShapeChange(level, pos, state, blockstate);
                    return InteractionResult.SUCCESS;
                }
            }

            return InteractionResult.PASS;
        }
    }

    private void updatesOnShapeChange(Level level, BlockPos pos, BlockState state1, BlockState state2) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.relative(direction);
            if (state1.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() != state2.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() && level.getBlockState(blockpos).isRedstoneConductor(level, blockpos)) {
                level.updateNeighborsAtExceptFromFacing(blockpos, state2.getBlock(), direction.getOpposite());
            }
        }

    }
}
