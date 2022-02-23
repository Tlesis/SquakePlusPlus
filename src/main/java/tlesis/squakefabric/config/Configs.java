package tlesis.squakefabric.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

public class Configs {
    
    public static class Generic {
        public static final ConfigBoolean       ENABLED                             = new ConfigBoolean("enable", true, "turns off/on the quake-style movement for the client");
        public static final ConfigBoolean       BHOP                                = new ConfigBoolean("bhop", true, "Bunny Hopping is a movement technique where the player can increase their speed by constantly jumping whilst air-strafing.");
        public static final ConfigBoolean       TRIMP                               = new ConfigBoolean("trimping", false, "If you're moving fast enough, holding sneak while jumping will convert some of your horizontal speed into vertical speed.");
        public static final ConfigBoolean       SHARK                               = new ConfigBoolean("shark", false, "Sharking refers to gliding across the surface of water by holding jump.");
        public static final ConfigHotkey        OPEN_CONFIG_GUI                     = new ConfigHotkey("openConfigGui", "O,C", "A hotkey to open the in-game Config GUI");
        public static final ConfigDouble        TRIMP_MULTIPLIER                    = new ConfigDouble("trimpMultiplier", 1.4, "a lower value means less horizontal speed converted to vertical speed and vice versa");
        // TODO: Speedometer
    }
}
