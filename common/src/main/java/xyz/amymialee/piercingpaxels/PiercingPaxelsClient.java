package xyz.amymialee.piercingpaxels;

import dev.architectury.registry.menu.MenuRegistry;
import xyz.amymialee.piercingpaxels.registry.ModMenus;
import xyz.amymialee.piercingpaxels.screens.PaxelScreen;

public class PiercingPaxelsClient {
    public static void init() {
        MenuRegistry.registerScreenFactory(ModMenus.PAXEL.get(), PaxelScreen::new);
    }
}
