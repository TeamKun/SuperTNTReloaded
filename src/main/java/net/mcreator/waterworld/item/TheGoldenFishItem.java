//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.mcreator.waterworld.item;

import java.util.HashMap;
import java.util.Map;
import net.mcreator.waterworld.WaterworldModElements;
import net.mcreator.waterworld.WaterworldModElements.ModElement;
import net.mcreator.waterworld.WaterworldModElements.ModElement.Tag;
import net.mcreator.waterworld.procedures.TheGoldenFishRightClickedOnBlockProcedure;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

@Tag
public class TheGoldenFishItem extends ModElement {
    @ObjectHolder("waterworld:the_golden_fish")
    public static final Item block = null;

    public TheGoldenFishItem(WaterworldModElements instance) {
        super(instance, 2);
    }

    public void initElements() {
        this.elements.items.add(() -> {
            return new TheGoldenFishItem.ItemCustom();
        });
    }

    public static class ItemCustom extends Item {
        public ItemCustom() {
            super((new Properties()).group(ItemGroup.MISC).maxStackSize(1));
            this.setRegistryName("the_golden_fish");
        }

        public int getItemEnchantability() {
            return 0;
        }

        public int getUseDuration(ItemStack itemstack) {
            return 0;
        }

        public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
            return 1.0F;
        }

        @OnlyIn(Dist.CLIENT)
        public boolean hasEffect(ItemStack itemstack) {
            return true;
        }

        public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
            ActionResultType retval = super.onItemUseFirst(stack, context);
            World world = context.getWorld();
            BlockPos pos = context.getPos();
            PlayerEntity entity = context.getPlayer();
            Direction direction = context.getFace();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            ItemStack itemstack = context.getItem();
            Map<String, Object> $_dependencies = new HashMap();
            $_dependencies.put("x", x);
            $_dependencies.put("y", y);
            $_dependencies.put("z", z);
            $_dependencies.put("world", world);
            TheGoldenFishRightClickedOnBlockProcedure.executeProcedure($_dependencies);
            return retval;
        }
    }
}
