package amymialee.piercingpaxels;

import amymialee.piercingpaxels.registry.PiercingItems;
import amymialee.piercingpaxels.screens.PaxelScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PiercingPaxels implements ModInitializer {
    public static final String MOD_ID = "piercingpaxels";
    public static final TagKey<Block> PAXEL_MINEABLE = TagKey.of(Registry.BLOCK_KEY, id("mineable/paxel"));
    public static final TagKey<Item> ACTIVES = TagKey.of(Registry.ITEM_KEY, id("actives"));
    public static final TagKey<Item> PASSIVES = TagKey.of(Registry.ITEM_KEY, id("passives"));
    public static final TagKey<Item> USAGES = TagKey.of(Registry.ITEM_KEY, id("usages"));

    public static final ScreenHandlerType<PaxelScreenHandler> PAXEL_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, "paxel", new ScreenHandlerType<>((a, b) -> new PaxelScreenHandler(a, b, PiercingItems.WOODEN_PAXEL.getDefaultStack().copy())));

    @Override
    public void onInitialize() {
        PiercingItems.init();
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}