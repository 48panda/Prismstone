package com._48panda.prismstone.mixin;

import com._48panda.prismstone.blocks.wire.RedstoneWirePowerController;
import com._48panda.prismstone.init.PrismstoneTags;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import com._48panda.prismstone.prismstone.rewrite.RedoneConnectivityFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RedStoneWireBlock.class)
public abstract class RedstoneDustMixin {
    @Redirect(method="Lnet/minecraft/world/level/block/RedStoneWireBlock;calculateTargetStrength(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I",
    at=@At(value="FIELD", target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;shouldSignal:Z", opcode = Opcodes.PUTFIELD))
    public void redirect(RedStoneWireBlock instance, boolean value) {
        RedstoneWirePowerController.shouldSignal = value;
    }
    
    @Redirect(method="Lnet/minecraft/world/level/block/RedStoneWireBlock;getDirectSignal(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)I",
            at=@At(value="FIELD", target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;shouldSignal:Z", opcode = Opcodes.GETFIELD))
    public boolean redirect2(RedStoneWireBlock instance) {
        return RedstoneWirePowerController.shouldSignal;
    }

    @Redirect(method="Lnet/minecraft/world/level/block/RedStoneWireBlock;getSignal(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)I",
            at=@At(value="FIELD", target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;shouldSignal:Z", opcode = Opcodes.GETFIELD))
    public boolean redirect3(RedStoneWireBlock instance) {
        return RedstoneWirePowerController.shouldSignal;
    }

    @Redirect(method="Lnet/minecraft/world/level/block/RedStoneWireBlock;isSignalSource(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at=@At(value="FIELD", target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;shouldSignal:Z", opcode = Opcodes.GETFIELD))
    public boolean redirect4(RedStoneWireBlock instance) {
        return RedstoneWirePowerController.shouldSignal;
    }
    @Shadow
    protected abstract boolean canSurviveOn(BlockGetter p_55613_, BlockPos p_55614_, BlockState p_55615_);
    @Inject(method="Lnet/minecraft/world/level/block/RedStoneWireBlock;getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;",
    at=@At(value="INVOKE", target="Lnet/minecraft/world/level/BlockGetter;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
            ordinal = 0, shift= At.Shift.AFTER), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void inject(BlockGetter p_55523_, BlockPos p_55524_, Direction p_55525_, boolean p_55526_, CallbackInfoReturnable<RedstoneSide> cir, BlockPos blockpos) {
        BlockState blockstate = p_55523_.getBlockState(blockpos);
        if (p_55526_) {
            boolean flag = blockstate.getBlock() instanceof TrapDoorBlock || this.canSurviveOn(p_55523_, blockpos, blockstate);
            if (flag && RedoneConnectivityFunctions.canPrismstoneConnectToVertically(p_55523_.getBlockState(blockpos.above()), p_55523_, blockpos.above(), p_55525_, PrismstoneType.REDSTONE)) {
                if (blockstate.isFaceSturdy(p_55523_, blockpos, p_55525_.getOpposite())) {
                    cir.setReturnValue(RedstoneSide.UP);
                    return;
                }

                cir.setReturnValue(RedstoneSide.SIDE);
            }
        }
    }
    @Inject(method="Lnet/minecraft/world/level/block/RedStoneWireBlock;getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;",
            at=@At(value="INVOKE", target="Lnet/minecraft/core/BlockPos;below()Lnet/minecraft/core/BlockPos;",
                    ordinal = 0, shift= At.Shift.AFTER), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void inject2(BlockGetter p_55523_, BlockPos p_55524_, Direction p_55525_, boolean p_55526_, CallbackInfoReturnable<RedstoneSide> cir, BlockPos blockpos, BlockState blockstate) {
        if (RedoneConnectivityFunctions.canPrismstoneConnectToVertically(p_55523_.getBlockState(blockpos.below()),p_55523_, blockpos.below(), p_55525_, PrismstoneType.REDSTONE)) {
            cir.setReturnValue(RedstoneSide.SIDE);
        }
    }
    @Redirect(method="Lnet/minecraft/world/level/block/RedStoneWireBlock;updateIndirectNeighbourShapes(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;II)V",
            at=@At(value="INVOKE", target="Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    public boolean redirect7(BlockState instance, Block block) {
        return instance.is(PrismstoneTags.Block.PRISMSTONE_WIRE);
    }
    @Inject(method = "Lnet/minecraft/world/level/block/RedStoneWireBlock;calculateTargetStrength(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I", at = @At("RETURN"), cancellable = true)
    private void injected(Level p_55528_, BlockPos p_55529_, CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValueI() < 15) {
            int j = 0;
            for(Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos blockpos = p_55529_.relative(direction);
                BlockState blockstate = p_55528_.getBlockState(blockpos);
                j = Math.max(j, RedoneConnectivityFunctions.getWireSignal(blockstate, direction, PrismstoneType.REDSTONE));
                BlockPos blockpos1 = p_55529_.above();
                if (blockstate.isRedstoneConductor(p_55528_, blockpos) && !p_55528_.getBlockState(blockpos1).isRedstoneConductor(p_55528_, blockpos1)) {
                    j = Math.max(j, RedoneConnectivityFunctions.getWireSignal(p_55528_.getBlockState(blockpos.above()), direction, PrismstoneType.REDSTONE));
                } else if (!blockstate.isRedstoneConductor(p_55528_, blockpos)) {
                    j = Math.max(j, RedoneConnectivityFunctions.getWireSignal(p_55528_.getBlockState(blockpos.below()), direction, PrismstoneType.REDSTONE));
                }
            }
            cir.setReturnValue(Math.max(j - 1, cir.getReturnValueI()));
        }
    }
}
