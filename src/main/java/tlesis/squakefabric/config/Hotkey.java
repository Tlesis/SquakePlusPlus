package tlesis.squakefabric.config;

import java.util.List;

import com.google.common.collect.ImmutableList;

import fi.dy.masa.malilib.config.options.ConfigHotkey;

public class Hotkey {
    public static final ConfigHotkey ENABLE_BUTTON      = new ConfigHotkey("enableButton", "O", "Enable and Disable the entire Bhop mod");
    public static final ConfigHotkey BHOP_BUTTON        = new ConfigHotkey("bhopButton", "", "Enable and Disable Bhoping");
    public static final ConfigHotkey SHARK_BUTTON       = new ConfigHotkey("sharkButton", "", "Enable and Disable Sharking");
    public static final ConfigHotkey TRIMP_BUTTON       = new ConfigHotkey("trimpButton", "", "Enable and Disable Trimping");
    
    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
        ENABLE_BUTTON,
        BHOP_BUTTON,
        SHARK_BUTTON,
        TRIMP_BUTTON
    );
}
