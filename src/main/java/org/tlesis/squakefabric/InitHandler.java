package org.tlesis.squakefabric;

import net.minecraft.client.MinecraftClient;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.event.RenderEventHandler;
import fi.dy.masa.malilib.event.TickHandler;
import fi.dy.masa.malilib.event.WorldLoadHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.interfaces.IRenderer;
import fi.dy.masa.malilib.network.ClientPacketChannelHandler;
import org.tlesis.squakefabric.config.Configs;
import org.tlesis.squakefabric.event.InputHandler;
import org.tlesis.squakefabric.event.KeyCallbacks;
import org.tlesis.squakefabric.event.RenderHandler;
import org.tlesis.squakefabric.event.WorldLoadListener;
import org.tlesis.squakefabric.network.CarpetHelloPacketHandler;
import org.tlesis.squakefabric.scheduler.ClientTickHandler;

public class InitHandler implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance().registerConfigHandler(Reference.MOD_ID, new Configs());

        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());

        IRenderer renderer = new RenderHandler();
        RenderEventHandler.getInstance().registerGameOverlayRenderer(renderer);
        RenderEventHandler.getInstance().registerWorldLastRenderer(renderer);

        TickHandler.getInstance().registerClientTickHandler(new ClientTickHandler());

        WorldLoadListener listener = new WorldLoadListener();
        WorldLoadHandler.getInstance().registerWorldLoadPreHandler(listener);
        WorldLoadHandler.getInstance().registerWorldLoadPostHandler(listener);

        ClientPacketChannelHandler.getInstance().registerClientChannelHandler(CarpetHelloPacketHandler.INSTANCE);

        KeyCallbacks.init(MinecraftClient.getInstance());
    }
}