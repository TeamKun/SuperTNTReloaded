//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.mcreator.waterworld.procedures;

import java.util.Map;
import net.mcreator.waterworld.WaterworldModElements;
import net.mcreator.waterworld.WaterworldModElements.ModElement;
import net.mcreator.waterworld.WaterworldModElements.ModElement.Tag;
import net.mcreator.waterworld.world.dimension.WaterworldDimension;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

@Tag
public class TheGoldenFishRightClickedOnBlockProcedure extends ModElement {
    public TheGoldenFishRightClickedOnBlockProcedure(WaterworldModElements instance) {
        super(instance, 2);
    }

    public static void executeProcedure(Map<String, Object> dependencies) {
        if (dependencies.get("x") == null) {
            System.err.println("Failed to load dependency x for procedure TheGoldenFishRightClickedOnBlock!");
        } else if (dependencies.get("y") == null) {
            System.err.println("Failed to load dependency y for procedure TheGoldenFishRightClickedOnBlock!");
        } else if (dependencies.get("z") == null) {
            System.err.println("Failed to load dependency z for procedure TheGoldenFishRightClickedOnBlock!");
        } else if (dependencies.get("world") == null) {
            System.err.println("Failed to load dependency world for procedure TheGoldenFishRightClickedOnBlock!");
        } else {
            double x = dependencies.get("x") instanceof Integer ? (double)(Integer)dependencies.get("x") : (Double)dependencies.get("x");
            double y = dependencies.get("y") instanceof Integer ? (double)(Integer)dependencies.get("y") : (Double)dependencies.get("y");
            double z = dependencies.get("z") instanceof Integer ? (double)(Integer)dependencies.get("z") : (Double)dependencies.get("z");
            IWorld world = (IWorld)dependencies.get("world");
            if (world instanceof World) {
                WaterworldDimension.portal.portalSpawn(world.getWorld(), new BlockPos((int)x, (int)y, (int)z));
            }

        }
    }
}
