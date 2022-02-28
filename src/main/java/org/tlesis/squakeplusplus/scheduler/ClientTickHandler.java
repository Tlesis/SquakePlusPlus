package org.tlesis.squakeplusplus.scheduler;

import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class ClientTickHandler implements IClientTickHandler {
    public static boolean isJumping = false;
    
    @Override
    public void onClientTick(MinecraftClient mc) {

        if (mc.world != null && mc.player != null) {
            TaskScheduler.getInstanceClient().runTasks();
        }
        
        if (mc.player != null) {

            Entity entity = mc.getCameraEntity();
            
            isJumping = mc.player.input.jumping;

            double dx = entity.getX() - entity.lastRenderX;
            double dy = entity.getY() - entity.lastRenderY;
            double dz = entity.getZ() - entity.lastRenderZ;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        }

    }
}
