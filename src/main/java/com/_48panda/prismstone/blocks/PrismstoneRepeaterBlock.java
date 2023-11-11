package com._48panda.prismstone.blocks;

import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class PrismstoneRepeaterBlock extends PrismstoneDiodeBlock {
    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
    public static final IntegerProperty DELAY = BlockStateProperties.DELAY;

    public PrismstoneRepeaterBlock(BlockBehaviour.Properties properties, PrismstoneType type) {
        super(properties, type);
        this.registerDefaultState(this.stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH).setValue(DELAY, 1).setValue(LOCKED, Boolean.FALSE).setValue(POWERED, false));
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            level.setBlock(pos, state.cycle(DELAY), 3);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    protected int getDelay(BlockState state) {
        return state.getValue(DELAY) * 2;
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockstate = super.getStateForPlacement(ctx);
        assert blockstate != null;
        return blockstate.setValue(LOCKED, this.isLocked(ctx.getLevel(), ctx.getClickedPos(), blockstate));
    }

    public BlockState updateShape(BlockState state1, Direction dir, BlockState state2, LevelAccessor level, BlockPos pos1, BlockPos pos2) {
        return !level.isClientSide() && dir.getAxis() != state1.getValue(HorizontalDirectionalBlock.FACING).getAxis() ?
                state1.setValue(LOCKED, this.isLocked(level, pos1, state1)) : super.updateShape(state1, dir, state2, level, pos1, pos2);
    }

    public boolean isLocked(LevelReader level, BlockPos pos, BlockState state) {
        return this.getAlternateSignal(level, pos, state) > 0;
    }

    protected boolean sideInputDiodesOnly() {
        return true;
    }
    
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
            double d0 = (double)pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
            double d1 = (double)pos.getY() + 0.4D + (random.nextDouble() - 0.5D) * 0.2D;
            double d2 = (double)pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
            float f = -5.0F;
            if (random.nextBoolean()) {
                f = (float)(state.getValue(DELAY) * 2 - 1);
            }

            f /= 16.0F;
            double d3 = (double)(f * (float)direction.getStepX());
            double d4 = (double)(f * (float)direction.getStepZ());
            level.addParticle(prismstoneType.getParticle(), d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING, DELAY, LOCKED, POWERED);
    }
}
