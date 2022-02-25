package tlesis.squakefabric.event;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.MinecraftClient;
import tlesis.squakefabric.config.Hotkeys;
import tlesis.squakefabric.gui.GuiConfigs;

public class KeyCallbacks {

    public static void init(MinecraftClient mc) {
        IHotkeyCallback callbackHotkeys = new KeyCallbackHotkeys(mc);

        Hotkeys.OPEN_GUI_SETTINGS.getKeybind().setCallback(callbackHotkeys);
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

            if (key == Hotkeys.OPEN_GUI_SETTINGS.getKeybind()) {
                GuiBase.openGui(new GuiConfigs());
            }

            return false;
        }
    }
}
