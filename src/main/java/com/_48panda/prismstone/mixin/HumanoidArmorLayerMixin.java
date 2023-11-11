package com._48panda.prismstone.mixin;

import com._48panda.prismstone.blocks.PrismstoneComparatorBlock;
import com._48panda.prismstone.items.armor.PrismstoneGoggles;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin {
    @Shadow
    private void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_, ArmorItem p_289650_, net.minecraft.client.model.Model p_289658_, boolean p_289668_, float p_289678_, float p_289674_, float p_289693_, ResourceLocation armorResource) {}
    @Inject(method="Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V",
            at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ArmorItem;Lnet/minecraft/client/model/Model;ZFFFLnet/minecraft/resources/ResourceLocation;)V", shift = At.Shift.AFTER, ordinal = 2),
    locals= LocalCapture.CAPTURE_FAILHARD)
    public <T extends LivingEntity, A extends HumanoidModel<T>> void prismstone_inject(
            PoseStack p_117119_, MultiBufferSource p_117120_, T p_117121_, EquipmentSlot p_117122_, int p_117123_, A p_117124_,
            CallbackInfo ci, ItemStack itemstack, Item $$9, ArmorItem armoritem, Model model, boolean flag) {
        if (armoritem instanceof PrismstoneGoggles goggles) {
            Vec3 i = goggles.prismstoneType.getPoweredColor();
            renderModel(p_117119_, p_117120_, p_117123_, armoritem, p_117124_, flag, (float)i.x, (float)i.y, (float)i.z, ((HumanoidArmorLayer<?, ?, ?>)(Object)this).getArmorResource(p_117121_, itemstack, p_117122_, "overlay"));
        }
    }
}
