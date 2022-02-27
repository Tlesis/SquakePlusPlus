package org.tlesis.squakefabric.event;

import javax.annotation.Nullable;

import fi.dy.masa.malilib.interfaces.IWorldLoadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.tlesis.squakefabric.data.DataManager;

public class WorldLoadListener implements IWorldLoadListener {
    
    @Override
    public void onWorldLoadPre(@Nullable ClientWorld worldBefore, @Nullable ClientWorld worldAfter, MinecraftClient mc) {
        // Save the settings before the integrated server gets shut down
        if (worldBefore != null) {
            DataManager.save();
        }
    }

    @Override
    public void onWorldLoadPost(@Nullable ClientWorld worldBefore, @Nullable ClientWorld worldAfter, MinecraftClient mc) {

        if (worldAfter != null) {
            DataManager.load();
        } else {
            DataManager.clear();
        }
    }
}
