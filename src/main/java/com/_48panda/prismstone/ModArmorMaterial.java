package com._48panda.prismstone;

import com._48panda.prismstone.PrismstoneMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public record ModArmorMaterial(String name, int durability, int[] protection, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) implements ArmorMaterial {
    private static final int[] DURABILITY_PER_SLOT = new int[] {13, 15, 16, 11};
    
    @Override
    public int getDurabilityForType(ArmorItem.Type slot) {
        return DURABILITY_PER_SLOT[slot.ordinal()] * durability;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type slot) {
        return protection[slot.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial.get();
    }

    @Override
    public String getName() {
        return PrismstoneMod.MODID + ":" + name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}
