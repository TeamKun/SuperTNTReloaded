package com.xeraster.supertnt.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;

public class OtherCustomSword extends ItemSword {

    private final float attackDamage;
    private final ToolMaterial material;

    public OtherCustomSword(String idName, ToolMaterial material, float damageMultiplier) {
        super(material);
        this.material = material;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.attackDamage = 3.0F * damageMultiplier + material.getAttackDamage();
        this.setRegistryName(idName);
        this.setUnlocalizedName(idName);
    }
}
