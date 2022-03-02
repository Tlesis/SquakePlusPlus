package org.tlesis.squakeplusplus.config;

import java.io.File;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigColor;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigOptionList;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;

import org.tlesis.squakeplusplus.Reference;
import org.tlesis.squakeplusplus.util.ScreenPositions;

public class Configs implements IConfigHandler {

    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static class Options {   
        public static final ConfigHotkey    OPEN_CONFIG_GUI               = new ConfigHotkey("Open GUI",                            "O,C",                     "Open the Config GUI");
        public static final ConfigDouble    SOFT_CAP_THRESHOLD            = new ConfigDouble("Soft Cap Threshold",                  544.0, 0.0, 32767,  false, "See uncappedBunnyhopEnabled and softCapDegen; soft cap speed = (moveSpeed*softCapThreshold)");
        public static final ConfigDouble    HARD_CAP_THRESHOLD            = new ConfigDouble("Hard Cap Threshold",                  544.0, 0.0, 32767,  false, "If you jump while above the hard cap speed (moveSpeed*hardCapThreshold), your speed is set to the hard cap speed");
        public static final ConfigDouble    GROUND_ACCELERATE             = new ConfigDouble("Acceleration",                        10.0,  0.0, 32767,  false, "A higher value means you accelerate faster on the ground");
        public static final ConfigDouble    AIR_ACCELERATE                = new ConfigDouble("Air Acceleration",                    10.0,  0.0, 30.0,   true,  "A higher value means you can turn more sharply in the air without losing speed");
        public static final ConfigDouble    MAX_AIR_ACCELERATION_PER_TICK = new ConfigDouble("Max Air Acceleration Per Tick",       0.05,  0.0, 0.1,    true,  "Limit for how much you can accelerate in a tick");
        public static final ConfigDouble    SOFT_CAP_DEGEN                = new ConfigDouble("Soft Cap Degen",                      0.65,  0.0, 1.0,    true,  "The modifier used to calculate speed lost when jumping above the soft cap");
        public static final ConfigDouble    SHARK_SURFACE_TENSION         = new ConfigDouble("Sharking Surface Tension",            0.2,   0.0, 1.0,    true,  "Amount of downward momentum you lose while entering water, a higher value means that you are able to shark after hitting the water from higher up");
        public static final ConfigDouble    SHARK_WATER_FRICTION          = new ConfigDouble("Sharking Water Friction",             0.1,   0.0, 1.0,    true,  "Amount of friction while sharking");
        public static final ConfigDouble    TRIMP_MULTIPLIER              = new ConfigDouble("Trimp Multiplier",                    1.4,   0.0, 2.0,    true,  "A lower value means less horizontal speed converted to vertical speed");
        public static final ConfigDouble    INCREASED_FALL_DISTANCE       = new ConfigDouble("Fall Distance Threshold",             0.0,   0.0, 10.0,   true,  "The distance needed to fall in order to take fall damage (singleplayer only)");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            OPEN_CONFIG_GUI,
            SOFT_CAP_THRESHOLD,
            HARD_CAP_THRESHOLD,
            GROUND_ACCELERATE,
            AIR_ACCELERATE,
            MAX_AIR_ACCELERATION_PER_TICK,
            SOFT_CAP_DEGEN,
            
            SHARK_SURFACE_TENSION,
            SHARK_WATER_FRICTION,
            
            TRIMP_MULTIPLIER/*,

            INCREASED_FALL_DISTANCE*/
        );

        public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_CONFIG_GUI
        );
    }

    public static class Speedometer {
        public static final ConfigInteger    TICK_INTERVAL      = new ConfigInteger    ("Speedometer Update",     15, 0, 20, true,          "How often the Speedometer should update in game ticks");
        public static final ConfigBoolean    SHOW_LAST_SPEED    = new ConfigBoolean    ("Display Previous Speed", false,                    "Displays your previous jump's speed under your current speed");
        public static final ConfigBoolean    USE_COLORS         = new ConfigBoolean    ("Use Speedometer Colors", true,                     "Use the colors set below for the speedometer");
        public static final ConfigColor      DEFAULT_COLOR      = new ConfigColor      ("Default Color",          "0xFFFFFFFF",             "Default text color of the speedometer");
        public static final ConfigColor      ACCELERATING_COLOR = new ConfigColor      ("Accelerating Color",     "0x0000FF00",             "Color for when you are accelerating");
        public static final ConfigColor      DECELERATING_COLOR = new ConfigColor      ("Decelerating Color",     "0x00FF0000",             "Color for when you are decelerating");
        public static final ConfigOptionList POSITIONS          = new ConfigOptionList ("Screen Position",        ScreenPositions.CENTER,   "Where the speedometer should be displayed");
        public static final ConfigBoolean    WHEN_JUMPING       = new ConfigBoolean    ("Only When Jumping",      false,                    "Only display the speedometer while currently jumping");
        public static final ConfigBoolean    SHOW_DIF           = new ConfigBoolean    ("Show Difference",        false,                    "Show the diffrence between the current speed and the last speed\n§6Work In Progress");
        
        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            TICK_INTERVAL,
            SHOW_LAST_SPEED,
            WHEN_JUMPING,
            SHOW_DIF,
            USE_COLORS,

            DEFAULT_COLOR,
            ACCELERATING_COLOR,
            DECELERATING_COLOR,

            POSITIONS

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
                ConfigUtils.readConfigBase(root, "Speedometer", Speedometer.OPTIONS);
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
            ConfigUtils.writeConfigBase(root, "Speedometer", Speedometer.OPTIONS);
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
