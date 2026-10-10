package xyz.amymialee.piercingpaxels;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import xyz.amymialee.piercingpaxels.screens.PaxelScreen;
import xyz.amymialee.piercingpaxels.registry.ModMenus;

@Mod(PiercingPaxels.MOD_ID)
public final class PiercingPaxelsNeoForge {
    public PiercingPaxelsNeoForge(IEventBus modEventBus) {
        PiercingPaxels.init();
        if (FMLEnvironment.dist == Dist.CLIENT) modEventBus.addListener(this::registerScreens);
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.PAXEL.get(), PaxelScreen::new);
    }
}
