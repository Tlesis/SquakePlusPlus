package tlesis.squakefabric;

import tlesis.squakefabric.config.Configs;

public class ModConfig {
    public static boolean ENABLED = Configs.Generic.ENABLED.getBooleanValue();

    public static boolean BHOP_ENABLED = Configs.Generic.BHOP.getBooleanValue();
    public static boolean UNCAPPED_BUNNYHOP_ENABLED = Configs.Generic.UNCAPPED_BHOP_ENABLED.getBooleanValue();
    public static double AIR_ACCELERATE = Configs.Generic.AIR_ACCELERATE.getDoubleValue();
    public static double ACCELERATE = Configs.Generic.GROUND_ACCELERATE.getDoubleValue();
    public static double MAX_AIR_ACCEL_PER_TICK = Configs.Generic.MAX_AIR_ACCELERATION_PER_TICK.getDoubleValue();
    public static float HARD_CAP = (float)Configs.Generic.HARD_CAP_THRESHOLD.getDoubleValue();
    public static float SOFT_CAP = (float)Configs.Generic.SOFT_CAP_THRESHOLD.getDoubleValue();
    public static float SOFT_CAP_DEGEN = (float)Configs.Generic.SOFT_CAP_DEGEN.getDoubleValue();

    public static boolean SHARKING_ENABLED = Configs.Generic.SHARK.getBooleanValue();
    public static double SHARKING_SURFACE_TENSION = Configs.Generic.SHARK_SURFACE_TENSION.getDoubleValue();
    public static double SHARKING_WATER_FRICTION = Configs.Generic.SHARK_WATER_FRICTION.getDoubleValue();

    public static boolean TRIMPING_ENABLED = Configs.Generic.TRIMP.getBooleanValue();
    public static float TRIMP_MULTIPLIER = (float)Configs.Generic.TRIMP_MULTIPLIER.getDoubleValue();
    
    public static double INCREASED_FALL_DISTANCE = Configs.Generic.INCREASED_FALL_DISTANCE.getDoubleValue();

}