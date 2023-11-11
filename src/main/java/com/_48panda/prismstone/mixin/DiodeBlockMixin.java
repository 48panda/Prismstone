package com._48panda.prismstone.mixin;

import com._48panda.prismstone.blocks.PrismstoneDiodeBlock;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DiodeBlock.class)
public class DiodeBlockMixin {
    @Inject(method="Lnet/minecraft/world/level/block/DiodeBlock;isDiode(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at=@At("HEAD"), cancellable = true)
    private static void isDiode(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof PrismstoneDiodeBlock) {
            cir.setReturnValue(true);
        }
    }
}
