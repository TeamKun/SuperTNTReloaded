package com.xeraster.supertnt.init;

import com.xeraster.supertnt.primedtnt.EntityTNTLargePrimed;
import com.xeraster.supertnt.renders.RenderTNTLargePrimed;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    public void registerRendering() {
        RenderingRegistry.registerEntityRenderingHandler(EntityTNTLargePrimed.class, new RenderTNTLargePrimed(Minecraft.getMinecraft().getRenderManager()));
    }

    public void registerItemRenders() {}
}
