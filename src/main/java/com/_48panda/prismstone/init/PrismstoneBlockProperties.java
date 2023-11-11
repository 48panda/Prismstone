package com._48panda.prismstone.init;

import com._48panda.prismstone.prismstone.rewrite.PrismstoneType;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class PrismstoneBlockProperties {
    public static EnumProperty<PrismstoneType> NEW_PRISMSTONE_TYPE = EnumProperty.create("", PrismstoneType.class);
}
