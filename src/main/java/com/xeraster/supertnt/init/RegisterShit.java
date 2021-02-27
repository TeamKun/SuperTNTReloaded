package com.xeraster.supertnt.init;

import com.xeraster.supertnt.renders.RenderTNTLargePrimed;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RegisterShit implements IRenderFactory {

    public Render createRenderFor(RenderManager manager) {
        return new RenderTNTLargePrimed(manager);
    }
}
