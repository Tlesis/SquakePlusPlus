package org.tlesis.squakeplusplus.event;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.MinecraftClient;

import org.tlesis.squakeplusplus.config.Configs.Options;
import org.tlesis.squakeplusplus.gui.GuiConfigs;

public class KeyCallbacks {

    public static void init(MinecraftClient mc) {
        IHotkeyCallback callbackHotkeys = new KeyCallbackHotkeys(mc);

        Options.OPEN_CONFIG_GUI.getKeybind().setCallback(callbackHotkeys);
    }

    private static class KeyCallbackHotkeys implements IHotkeyCallback {

        private final MinecraftClient mc;

        public KeyCallbackHotkeys(MinecraftClient mc) {
            this.mc = mc;
        }

        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key) {

            if (this.mc.player == null || this.mc.world == null) {
                return false;
            }

            if (key == Options.OPEN_CONFIG_GUI.getKeybind()) {
                GuiBase.openGui(new GuiConfigs());
                return true;
            }

            return false;
        }
    }
}
