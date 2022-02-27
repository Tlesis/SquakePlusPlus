package org.tlesis.squakeplusplus.config;

import java.io.File;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import org.tlesis.squakeplusplus.Reference;

public class Configs implements IConfigHandler {

    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static class Options {
        public static final ConfigHotkey    OPEN_CONFIG_GUI               = new ConfigHotkey("Open GUI",                            "O,C",              "Open the Config GUI");
        public static final ConfigDouble    AIR_ACCELERATE                = new ConfigDouble("Air Acceleration",                    10.0, 0.0, 30.0,    "A higher value means you can turn more sharply in the air without losing speed");
        public static final ConfigDouble    GROUND_ACCELERATE             = new ConfigDouble("Acceleration",                        10.0, 0.0, 32767,   "A higher value means you accelerate faster on the ground");
        public static final ConfigDouble    MAX_AIR_ACCELERATION_PER_TICK = new ConfigDouble("Max Air Acceleration Per Tick",       0.05, 0.0, 0.1,     "Limit for how much you can accelerate in a tick");
        public static final ConfigDouble    HARD_CAP_THRESHOLD            = new ConfigDouble("Hard Cap Threshold",                  544.0, 0.0, 32767,  "If you jump while above the hard cap speed (moveSpeed*hardCapThreshold), your speed is set to the hard cap speed");
        public static final ConfigDouble    SOFT_CAP_DEGEN                = new ConfigDouble("Soft Cap Degen",                      0.65, 0.0, 32767,   "The modifier used to calculate speed lost when jumping above the soft cap");
        public static final ConfigDouble    SOFT_CAP_THRESHOLD            = new ConfigDouble("Soft Cap Threshold",                  544.0, 0.0, 32767,  "See uncappedBunnyhopEnabled and softCapDegen; soft cap speed = (moveSpeed*softCapThreshold)");
        public static final ConfigDouble    SHARK_SURFACE_TENSION         = new ConfigDouble("Sharking Surface Tension",            0.2, 0.0, 32767,    "Amount of downward momentum you lose while entering water, a higher value means that you are able to shark after hitting the water from higher up");
        public static final ConfigDouble    SHARK_WATER_FRICTION          = new ConfigDouble("Sharking Water Friction",             0.1, 0.0, 1.0,      "Amount of friction while sharking");
        public static final ConfigDouble    TRIMP_MULTIPLIER              = new ConfigDouble("Trimp Multiplier",                    1.4, 0.0, 32767,    "A lower value means less horizontal speed converted to vertical speed");
        public static final ConfigDouble    INCREASED_FALL_DISTANCE       = new ConfigDouble("Fall Distance Threshold",             0.0, 0.0, 32767,    "The distance needed to fall in order to take fall damage (singleplayer only)");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            OPEN_CONFIG_GUI,
            AIR_ACCELERATE,
            GROUND_ACCELERATE,
            MAX_AIR_ACCELERATION_PER_TICK,
            HARD_CAP_THRESHOLD,
            SOFT_CAP_THRESHOLD,
            SOFT_CAP_DEGEN,
            
            SHARK_SURFACE_TENSION,
            SHARK_WATER_FRICTION,
            
            TRIMP_MULTIPLIER,

            INCREASED_FALL_DISTANCE
        );

        public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_CONFIG_GUI
        );
    }

    public static void loadFromFile() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Options", Options.OPTIONS);
                ConfigUtils.readConfigBase(root, "Hotkeys", Options.HOTKEY_LIST);
                ConfigUtils.readHotkeyToggleOptions(root, "GenericHotkeys", "GenericToggles", FeatureToggle.VALUES); 
            }
        }
    }

    public static void saveToFile() {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Options", Options.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Hotkeys", Options.HOTKEY_LIST);
            ConfigUtils.writeHotkeyToggleOptions(root, "GenericHotkeys", "GenericToggles", FeatureToggle.VALUES);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }

    @Override
    public void load() {
        loadFromFile();
    }

    @Override
    public void save() {
        saveToFile();
    }
}
