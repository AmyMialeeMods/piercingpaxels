package xyz.amymialee.piercingpaxels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import xyz.amymialee.piercingpaxels.registry.ModMenus;
import xyz.amymialee.piercingpaxels.registry.ModItems;

public class PiercingPaxels {
    public static final String MOD_ID = "piercingpaxels";
    public static final String MOD_NAME = "Piercing Paxels";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        ModItems.ITEMS.register();
        ModMenus.MENUS.register();
        CreativeTabRegistry.appendBuiltin(
                BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.TOOLS_AND_UTILITIES),
                ModItems.WOODEN_PAXEL,
                ModItems.STONE_PAXEL,
                ModItems.IRON_PAXEL,
                ModItems.GOLDEN_PAXEL,
                ModItems.DIAMOND_PAXEL,
                ModItems.NETHERITE_PAXEL
                , ModItems.ACTIVE_WALL, ModItems.ACTIVE_TUNNEL, ModItems.ACTIVE_HOLE,
                ModItems.PASSIVE_SMELT, ModItems.PASSIVE_SILENCE, ModItems.PASSIVE_VACUUM,
                ModItems.UPGRADE_RESTORATION, ModItems.UPGRADE_UNBREAKABLE,
                ModItems.USAGE_AXE, ModItems.USAGE_HOE, ModItems.USAGE_SHOVEL,
                ModItems.NETHERITE_PAXEL_UPGRADE_KIT
        );
    }

}
