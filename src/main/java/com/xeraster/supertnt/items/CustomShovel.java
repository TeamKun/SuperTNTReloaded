package com.xeraster.supertnt.items;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.Item.ToolMaterial;

public class CustomShovel extends ItemTool {

    private static final Set EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.DIRT, Blocks.SAND, Blocks.NETHERRACK, Blocks.SANDSTONE, Blocks.CLAY, Blocks.GRASS});
    private static final float[] ATTACK_DAMAGES = new float[] { 6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
    private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F};

    public CustomShovel(ToolMaterial material, String idName, float damage, float speed) {
        super(material, CustomShovel.EFFECTIVE_ON);
        this.setRegistryName(idName);
        this.setUnlocalizedName(idName);
        this.attackDamage = CustomShovel.ATTACK_DAMAGES[material.ordinal()];
        this.attackSpeed = CustomShovel.ATTACK_SPEEDS[material.ordinal()];
        this.attackDamage = damage;
        this.attackSpeed = speed;
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();

        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
    }
}
