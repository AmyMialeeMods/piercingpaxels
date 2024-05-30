package xyz.amymialee.piercingpaxels;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(PiercingPaxels.MOD_ID)
public class PiercingPaxelsForge {
    public PiercingPaxelsForge() {
        PiercingPaxels.init();
        if (FMLLoader.getDist().isClient()) {
            PiercingPaxelsClient.init();
        }
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(PiercingPaxelsForge::onGatherData);
    }

    private static void onGatherData(GatherDataEvent event) {
        PiercingPaxels.initDatagen(FabricDataGenerator.create(PiercingPaxels.MOD_ID, event));
    }
}