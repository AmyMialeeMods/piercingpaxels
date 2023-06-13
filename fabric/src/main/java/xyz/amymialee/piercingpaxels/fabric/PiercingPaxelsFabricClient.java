package xyz.amymialee.piercingpaxels.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import xyz.amymialee.piercingpaxels.PiercingPaxels;
import xyz.amymialee.piercingpaxels.screens.PaxelScreen;

public class PiercingPaxelsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(PiercingPaxels.PAXEL_SCREEN_HANDLER.get(), PaxelScreen::new);
    }
}