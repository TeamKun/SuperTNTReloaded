//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.mcreator.waterworld.item;

import net.mcreator.waterworld.world.dimension.WaterworldDimension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class WaterworldItem extends Item {
    @ObjectHolder("waterworld:waterworld")
    public static final Item block = null;

    public WaterworldItem() {
        super((new Properties()).group((ItemGroup)null).maxDamage(64));
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity entity = context.getPlayer();
        BlockPos pos = context.getPos().offset(context.getFace());
        ItemStack itemstack = context.getItem();
        World world = context.getWorld();
        if (!entity.canPlayerEdit(pos, context.getFace(), itemstack)) {
            return ActionResultType.FAIL;
        } else {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            if (world.isAirBlock(pos)) {
                WaterworldDimension.portal.portalSpawn(world, pos);
            }

            itemstack.damageItem(1, entity, (c) -> {
                c.sendBreakAnimation(context.getHand());
            });
            return ActionResultType.SUCCESS;
        }
    }
}
