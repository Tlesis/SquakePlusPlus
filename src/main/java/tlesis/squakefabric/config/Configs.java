package tlesis.squakefabric.config;

import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import tlesis.squakefabric.Reference;

public class Configs implements IConfigHandler {

    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";
    
    public static class Generic {
        // openConfigGui should be defined in Hotkeys.java, right? this isn't where hotkeys go
        public static final ConfigHotkey    OPEN_CONFIG_GUI               = new ConfigHotkey  ("openConfigGui", "O,C", "Hotkey to open the in-game Config GUI");
        public static final ConfigBoolean   ENABLED                       = new ConfigBoolean ("enable", true, "Enables/disables all changes to movement");
        public static final ConfigBoolean   BHOP                          = new ConfigBoolean ("bhop", true, "Enables bunnyhopping and airstrafing"); // TODO
        public static final ConfigBoolean   UNCAPPED_BHOP_ENABLED         = new ConfigBoolean ("uncappedBunnyhopEnabled", true, "If enabled, the soft and hard speed caps will not be applied at all");
        public static final ConfigDouble    AIR_ACCELERATE                = new ConfigDouble  ("airAccelerate", 14.0, 0.0, 30.0, "A higher value means you can turn more sharply in the air without losing speed");
        public static final ConfigDouble    GROUND_ACCELERATE             = new ConfigDouble  ("groundAccelerate", 10.0, 0.0, Double.MAX_VALUE, "A higher value means you accelerate faster on the ground");
        public static final ConfigDouble    MAX_AIR_ACCELERATION_PER_TICK = new ConfigDouble  ("maxAirAccelerationPerTick", 0.045, 0.0, Double.MAX_VALUE, "Limit for how much you can accelerate in a tick");
        public static final ConfigDouble    HARD_CAP_THRESHOLD            = new ConfigDouble  ("hardCapThreshold", 0.0, 0.0, Double.MAX_VALUE, "If you jump while above the hard cap speed (moveSpeed*hardCapThreshold), your speed is set to the hard cap speed");
        public static final ConfigDouble    SOFT_CAP_DEGEN                = new ConfigDouble  ("softCapDegen", 0.65, 0.0, Double.MAX_VALUE, "The modifier used to calculate speed lost when jumping above the soft cap");
        public static final ConfigDouble    SOFT_CAP_THRESHOLD            = new ConfigDouble  ("softCapThreshold", 1.4, 0.0, Double.MAX_VALUE, "See uncappedBunnyhopEnabled and softCapDegen; soft cap speed = (moveSpeed*softCapThreshold)");
        public static final ConfigBoolean   SHARK                         = new ConfigBoolean ("shark", false, "Enables sharking");
        public static final ConfigDouble    SHARK_SURFACE_TENSION         = new ConfigDouble  ("sharkingSurfaceTension", 0.2, 0.0, Double.MAX_VALUE, "Amount of downward momentum you lose while entering water, a higher value means that you are able to shark after hitting the water from higher up");
        public static final ConfigDouble    SHARK_WATER_FRICTION          = new ConfigDouble  ("sharkingWaterFriction", 0.1, 0.0, 1.0, "Amount of friction while sharking (between 0 and 1)");
        public static final ConfigBoolean   TRIMP                         = new ConfigBoolean ("trimping", false, "Enables trimping");
        public static final ConfigDouble    TRIMP_MULTIPLIER              = new ConfigDouble  ("trimpMultiplier", 1.4, 0.0, Double.MAX_VALUE, "A lower value means less horizontal speed converted to vertical speed");
        public static final ConfigDouble    INCREASED_FALL_DISTANCE       = new ConfigDouble  ("fallDistanceThresholdIncrease", 0.0, 0.0, Double.MAX_VALUE, "Increases the distance needed to fall in order to take fall damage (singleplayer only)");
        // TODO: public static final ConfigBoolean   SPEEDOMETER          = new ConfigBoolean ("speedometer", false, "A Speedometer that is very similar to how MiniHud's speedometer is");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            OPEN_CONFIG_GUI,
            ENABLED,

            BHOP,
            UNCAPPED_BHOP_ENABLED,
            AIR_ACCELERATE,
            GROUND_ACCELERATE,
            MAX_AIR_ACCELERATION_PER_TICK,
            HARD_CAP_THRESHOLD,
            SOFT_CAP_THRESHOLD,
            SOFT_CAP_DEGEN,

            SHARK,
            SHARK_SURFACE_TENSION,
            SHARK_WATER_FRICTION,

            TRIMP,
            TRIMP_MULTIPLIER,

            INCREASED_FALL_DISTANCE
        );
    }

    public static void loadFromFile() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
            }
        }
    }

    public static void saveToFile() {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);

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
