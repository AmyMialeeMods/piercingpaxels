package xyz.amymialee.piercingpaxels.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.amymialee.piercingpaxels.PiercingPaxels;
import xyz.amymialee.piercingpaxels.items.PaxelItem;
import xyz.amymialee.piercingpaxels.items.upgrades.UtilityUpgradeItem;
import xyz.amymialee.piercingpaxels.screens.PaxelScreen;
import xyz.amymialee.piercingpaxels.util.PaxelSlot;
import xyz.amymialee.piercingpaxels.util.PaxelTooltipComponent;
import xyz.amymialee.piercingpaxels.util.PaxelTooltipData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod(PiercingPaxels.MOD_ID)
@Mod.EventBusSubscriber(modid = PiercingPaxels.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PiercingPaxelsForge {
    public PiercingPaxelsForge() {
        EventBuses.registerModEventBus(PiercingPaxels.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        PiercingPaxels.init();
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> HandledScreens.register(PiercingPaxels.PAXEL_SCREEN_HANDLER.get(), PaxelScreen::new));
    }

    @SubscribeEvent
    public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(PaxelTooltipData.class, PaxelTooltipComponent::new);
    }
}

