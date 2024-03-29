package org.tlesis.squakeplusplus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ModInitializer;

public class SquakePlusPlus implements ModInitializer {

    public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

    @Override
    public void onInitialize() {
        System.out.println("[Squake++]: Mod Initialize");
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
    }
}
