package com._48panda.prismstone.blocks;

import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import com._48panda.prismstone.prismstone.rewrite.RedoneConnectivityFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class PrismstoneTorchWallBlock extends PrismstoneTorchBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = PrismstoneTorchBlock.LIT;

    public PrismstoneTorchWallBlock(BlockBehaviour.Properties properties, PrismstoneType type) {
        super(properties, type);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.TRUE));
    }

    public String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        return WallTorchBlock.getShape(state);
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Blocks.WALL_TORCH.canSurvive(state, level, pos);
    }

    public BlockState updateShape(BlockState state1, Direction dir, BlockState state2, LevelAccessor level, BlockPos pos1, BlockPos pos2) {
        return Blocks.WALL_TORCH.updateShape(state1, dir, state2, level, pos1, pos2);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockstate = Blocks.WALL_TORCH.getStateForPlacement(ctx);
        return blockstate == null ? null : this.defaultBlockState().setValue(FACING, blockstate.getValue(FACING));
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            Direction direction = state.getValue(FACING).getOpposite();
            double d0 = 0.27D;
            double d1 = (double)pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D + 0.27D * (double)direction.getStepX();
            double d2 = (double)pos.getY() + 0.7D + (random.nextDouble() - 0.5D) * 0.2D + 0.22D;
            double d3 = (double)pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D + 0.27D * (double)direction.getStepZ();
            level.addParticle(this.flameParticle, d1, d2, d3, 0.0D, 0.0D, 0.0D);
        }
    }

    protected boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING).getOpposite();
        return RedoneConnectivityFunctions.hasSignal(level, pos.relative(direction), direction, prismstoneType);
    }

    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir, PrismstoneType type) {
        return state.getValue(LIT) && state.getValue(FACING) != dir && type == prismstoneType ? 15 : 0;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return Blocks.WALL_TORCH.rotate(state, rotation);
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return Blocks.WALL_TORCH.mirror(state, mirror);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }
}
