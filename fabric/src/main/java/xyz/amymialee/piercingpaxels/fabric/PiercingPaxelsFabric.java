package xyz.amymialee.piercingpaxels.fabric;

import net.fabricmc.api.ModInitializer;
import xyz.amymialee.piercingpaxels.PiercingPaxels;

public class PiercingPaxelsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        PiercingPaxels.init();
    }
}