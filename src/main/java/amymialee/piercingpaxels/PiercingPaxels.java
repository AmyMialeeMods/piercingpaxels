package amymialee.piercingpaxels;

import amymialee.piercingpaxels.registry.PiercingItems;
import amymialee.piercingpaxels.screens.PaxelScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class PiercingPaxels implements ModInitializer {
    public static final String MOD_ID = "piercingpaxels";
    public static final TagKey<Block> PAXEL_MINEABLE = TagKey.of(RegistryKeys.BLOCK, id("mineable/paxel"));
    public static final TagKey<Item> ACTIVES = TagKey.of(RegistryKeys.ITEM, id("actives"));
    public static final TagKey<Item> PASSIVES = TagKey.of(RegistryKeys.ITEM, id("passives"));
    public static final TagKey<Item> USAGES = TagKey.of(RegistryKeys.ITEM, id("usages"));

    public static final ScreenHandlerType<PaxelScreenHandler> PAXEL_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, "paxel", new ScreenHandlerType<>((a, b) -> new PaxelScreenHandler(a, b, PiercingItems.WOODEN_PAXEL.getDefaultStack().copy()), FeatureFlags.VANILLA_FEATURES));

    @Override
    public void onInitialize() {
        PiercingItems.init();
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
