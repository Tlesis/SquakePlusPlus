package org.tlesis.squakeplusplus.scheduler;

import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import net.minecraft.client.MinecraftClient;

public class ClientTickHandler implements IClientTickHandler {
    public static boolean isJumping = false;
    
    @Override
    public void onClientTick(MinecraftClient mc) {

        if (mc.world != null && mc.player != null) {
            TaskScheduler.getInstanceClient().runTasks();
        }
        
        if (mc.player != null) {
            isJumping = mc.player.input.jumping;
        }
    }
}
