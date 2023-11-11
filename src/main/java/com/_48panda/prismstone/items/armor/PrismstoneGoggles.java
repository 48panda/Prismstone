package com._48panda.prismstone.items.armor;

import com._48panda.prismstone.PrismstoneMod;
import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PrismstoneGoggles extends ArmorItem {
    public PrismstoneGoggles(ArmorMaterial material, Type type, Properties properties, PrismstoneType ptype) {
        super(material, type, properties);
        this.prismstoneType = ptype;
    }

    public final PrismstoneType prismstoneType;

}
