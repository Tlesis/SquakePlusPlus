package tlesis.squakefabric;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SquakeFabric implements ModInitializer {

    public static final String MODID = "squakefabric";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Override
    public void onInitialize() {
        ModConfig.init();
    }
}
