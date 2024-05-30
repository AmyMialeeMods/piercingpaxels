package xyz.amymialee.piercingpaxels;

import net.fabricmc.api.ClientModInitializer;

public class PiercingPaxelsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PiercingPaxelsClient.init();
    }
}