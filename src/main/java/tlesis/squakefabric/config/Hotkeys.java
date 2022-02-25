package tlesis.squakefabric.config;

import java.util.List;

import com.google.common.collect.ImmutableList;

import fi.dy.masa.malilib.config.options.ConfigHotkey;

public class Hotkeys {
    
    public static final ConfigHotkey OPEN_GUI_SETTINGS  = new ConfigHotkey("openGuiSettings", "O,C",  "Open the Config GUI");
    
    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
        OPEN_GUI_SETTINGS
    );
}
