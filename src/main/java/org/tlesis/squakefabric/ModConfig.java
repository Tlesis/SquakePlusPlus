package org.tlesis.squakefabric;

import org.tlesis.squakefabric.config.Configs;
import org.tlesis.squakefabric.config.FeatureToggle;

public class ModConfig {

    public static boolean BHOP_ENABLED = FeatureToggle.BHOP.getBooleanValue();
    
    public static double INCREASED_FALL_DISTANCE = Configs.Options.INCREASED_FALL_DISTANCE.getDoubleValue();

}