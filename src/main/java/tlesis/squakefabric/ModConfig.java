package tlesis.squakefabric;


import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModConfig {
    public static final String CATEGORY_MOVEMENT = "movement";

    public static float TRIMP_MULTIPLIER;
    public static float HARD_CAP;

    public static float SOFT_CAP;

    public static float SOFT_CAP_DEGEN;



    public static double SHARKING_SURFACE_TENSION;
    public static double SHARKING_WATER_FRICTION;
    public static double ACCELERATE;
    public static double AIR_ACCELERATE;
    public static double INCREASED_FALL_DISTANCE;
    public static double MAX_AIR_ACCEL_PER_TICK;

    public static boolean SHARKING_ENABLED;
    public static boolean UNCAPPED_BUNNYHOP_ENABLED;
    public static boolean TRIMPING_ENABLED;

    public static boolean ENABLED;

    public static String defaultConfig =
            "# Configuration file\n" +
            "\n" +
            "movement {\n" +
            "    # a higher value means you can turn more sharply in the air without losing speed\n" +
            "    D:airAccelerate=14.0\n" +
            "\n" +
            "    # turns off/on the quake-style movement for the client (essentially the saved value of the ingame toggle keybind)\n" +
            "    B:enabled=true\n" +
            "\n" +
            "    # increases the distance needed to fall in order to take fall damage; this is a server-side setting\n" +
            "    D:fallDistanceThresholdIncrease=0.0\n" +
            "\n" +
            "    # a higher value means you accelerate faster on the ground\n" +
            "    D:groundAccelerate=10.0\n" +
            "\n" +
            "    # see uncappedBunnyhopEnabled; if you ever jump while above the hard cap speed (moveSpeed*hardCapThreshold), your speed is set to the hard cap speed\n" +
            "    D:hardCapThreshold=2.0\n" +
            "\n" +
            "    # a higher value means faster air acceleration\n" +
            "    D:maxAirAccelerationPerTick=0.045\n" +
            "\n" +
            "    # if enabled, holding jump while swimming at the surface of water allows you to glide\n" +
            "    B:sharkingEnabled=true\n" +
            "\n" +
            "    # amount of downward momentum you lose while entering water, a higher value means that you are able to shark after hitting the water from higher up\n" +
            "    D:sharkingSurfaceTension=0.2\n" +
            "\n" +
            "    # amount of friction while sharking (between 0 and 1)\n" +
            "    D:sharkingWaterFriction=0.1\n" +
            "\n" +
            "    # the modifier used to calculate speed lost when jumping above the soft cap\n" +
            "    D:softCapDegen=0.65\n" +
            "\n" +
            "    # see uncappedBunnyhopEnabled and softCapDegen; soft cap speed = (moveSpeed*softCapThreshold)\n" +
            "    D:softCapThreshold=1.4\n" +
            "\n" +
            "    # if enabled, holding sneak while jumping will convert your horizontal speed into vertical speed\n" +
            "    B:trimpEnabled=true\n" +
            "\n" +
            "    # a lower value means less horizontal speed converted to vertical speed and vice versa\n" +
            "    D:trimpMultiplier=1.4\n" +
            "\n" +
            "    # if enabled, the soft and hard caps will not be applied at all\n" +
            "    B:uncappedBunnyhopEnabled=true\n" +
            "}\n" +
            "\n" +
            "\n";


    public static void init() {
        FabricLoader fabric = FabricLoader.getInstance();
        Path configDir = fabric.getConfigDir();
        File configFile = new File(String.valueOf(configDir), "squake.cfg");

        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        }


        try {
            Scanner reader = new Scanner(configFile);
            while (reader.hasNextLine()) {
                String nextLine = reader.nextLine();

                Pattern configFieldPattern = Pattern.compile("[D|B]:(.+?)=(.+)$");

                Matcher configFieldMatcher = configFieldPattern.matcher(nextLine);

                while (configFieldMatcher.find()) {
                    String configField = configFieldMatcher.group(1);
                    String value = configFieldMatcher.group(2);
                    loadFields(configField, value);
                }

            }

        } catch (FileNotFoundException e) {
            SquakeFabric.LOGGER.error("\"squake.cfg\" not found");
        }


    }

    public static void createDefaultConfig(File configFile) {
        try {
            if(configFile.createNewFile()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
                writer.write(defaultConfig);
                writer.close();
            }
        } catch (IOException e) {
            SquakeFabric.LOGGER.error("Couldn't create squake.cfg");
        }
    }

    public static void loadFields(String field, String value) {
        boolean isBooleanValue = false;
        boolean booleanValue = false;
        if(Objects.equals(value, "true") ) {
            isBooleanValue = true;
            booleanValue = true;
        }

        if(Objects.equals(value, "false") ) {
            isBooleanValue = true;
            booleanValue = false;
        }

        if (isBooleanValue) {
            switch (field) {
                case "enabled" -> ENABLED = booleanValue;
                case "sharkingEnabled" -> SHARKING_ENABLED = booleanValue;
                case "trimpEnabled" -> TRIMPING_ENABLED = booleanValue;
                case "uncappedBunnyhopEnabled" -> UNCAPPED_BUNNYHOP_ENABLED = booleanValue;
            }
        } else {
            double val = Double.parseDouble(value);
            switch (field) {
                case "airAccelerate" -> AIR_ACCELERATE = val;
                case "fallDistanceThresholdIncrease" -> INCREASED_FALL_DISTANCE = val;
                case "groundAccelerate" -> ACCELERATE = val;
                case "hardCapThreshold" -> HARD_CAP = (float) val;
                case "maxAirAccelerationPerTick" -> MAX_AIR_ACCEL_PER_TICK = val;
                case "sharkingSurfaceTension" -> SHARKING_SURFACE_TENSION = 1.0D - val;
                case "sharkingWaterFriction" -> SHARKING_WATER_FRICTION = 1.0D - val * 0.05D;
                case "softCapDegen" -> SOFT_CAP_DEGEN = (float) val;
                case "softCapThreshold" -> SOFT_CAP = (float) val;
                case "trimpMultiplier" -> TRIMP_MULTIPLIER = (float) val;
            }
        }

    }
}
