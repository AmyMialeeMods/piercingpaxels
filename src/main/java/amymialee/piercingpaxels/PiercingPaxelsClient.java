package amymialee.piercingpaxels;

import amymialee.piercingpaxels.screens.PaxelScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class PiercingPaxelsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(PiercingPaxels.PAXEL_SCREEN_HANDLER, PaxelScreen::new);
    }
}