package com._48panda.prismstone.blocks;

import com._48panda.prismstone.prismstone.interfaces.IPrismstoneInteractor;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PrismstoneLeverBlock extends LeverBlock implements IPrismstoneInteractor {
    public PrismstoneLeverBlock(Properties p_54633_, PrismstoneType type) {
        super(p_54633_);
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

    private void makeParticle(BlockState p_54658_, LevelAccessor p_54659_, BlockPos p_54660_, float p_54661_) {
        Direction direction = p_54658_.getValue(FACING).getOpposite();
        Direction direction1 = getConnectedDirection(p_54658_).getOpposite();
        double d0 = (double)p_54660_.getX() + 0.5D + 0.1D * (double)direction.getStepX() + 0.2D * (double)direction1.getStepX();
        double d1 = (double)p_54660_.getY() + 0.5D + 0.1D * (double)direction.getStepY() + 0.2D * (double)direction1.getStepY();
        double d2 = (double)p_54660_.getZ() + 0.5D + 0.1D * (double)direction.getStepZ() + 0.2D * (double)direction1.getStepZ();
        p_54659_.addParticle(prismstoneType.getParticle(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
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




    public InteractionResult use(BlockState p_54640_, Level p_54641_, BlockPos p_54642_, Player p_54643_, InteractionHand p_54644_, BlockHitResult p_54645_) {
        if (p_54641_.isClientSide) {
            BlockState blockstate1 = p_54640_.cycle(POWERED);
            if (blockstate1.getValue(POWERED)) {
                makeParticle(blockstate1, p_54641_, p_54642_, 1.0F);
            }

            return InteractionResult.SUCCESS;
        } else {
            BlockState blockstate = this.pull(p_54640_, p_54641_, p_54642_);
            float f = blockstate.getValue(POWERED) ? 0.6F : 0.5F;
            p_54641_.playSound((Player)null, p_54642_, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
            p_54641_.gameEvent(p_54643_, blockstate.getValue(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, p_54642_);
            return InteractionResult.CONSUME;
        }
    }

    public void animateTick(BlockState p_221395_, Level p_221396_, BlockPos p_221397_, RandomSource p_221398_) {
        if (p_221395_.getValue(POWERED) && p_221398_.nextFloat() < 0.25F) {
            makeParticle(p_221395_, p_221396_, p_221397_, 0.5F);
        }

    }
    
    
}
