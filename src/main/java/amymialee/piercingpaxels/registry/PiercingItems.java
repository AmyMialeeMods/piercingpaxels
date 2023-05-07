package amymialee.piercingpaxels.registry;

import amymialee.piercingpaxels.PiercingPaxels;
//import amymialee.piercingpaxels.compat.spirit.SpiritPaxels;
import amymialee.piercingpaxels.compat.techreborn.TechRebornPaxels;
import amymialee.piercingpaxels.items.PaxelItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

@SuppressWarnings("unused")
public class PiercingItems {
    public static final ItemGroup PAXELS_GROUP = FabricItemGroup.builder(PiercingPaxels.id("piercingpaxels_group")).icon(PiercingItems::getPaxelIcon).build();

    public static final Item WOODEN_PAXEL = registerItem("wooden_paxel", new PaxelItem(ToolMaterials.WOOD, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item STONE_PAXEL = registerItem("stone_paxel", new PaxelItem(ToolMaterials.STONE, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item GOLDEN_PAXEL = registerItem("golden_paxel", new PaxelItem(ToolMaterials.GOLD, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item IRON_PAXEL = registerItem("iron_paxel", new PaxelItem(ToolMaterials.IRON, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item DIAMOND_PAXEL = registerItem("diamond_paxel", new PaxelItem(ToolMaterials.DIAMOND, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item NETHERITE_PAXEL = registerItem("netherite_paxel", new PaxelItem(ToolMaterials.NETHERITE, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.RARE).fireproof()));

    public static final Item NETHERITE_UPGRADE_KIT = registerItem("netherite_paxel_upgrade_kit", new Item(new FabricItemSettings().rarity(Rarity.RARE)));

    public static final Item ACTIVE_WALL = registerItem("active_wall", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item ACTIVE_TUNNEL = registerItem("active_tunnel", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item ACTIVE_HOLE = registerItem("active_hole", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));

    public static final Item PASSIVE_SMELT = registerItem("passive_smelt", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item PASSIVE_SILENCE = registerItem("passive_silence", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item PASSIVE_VACUUM = registerItem("passive_vacuum", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));

    public static final Item USAGE_AXE = registerItem("usage_axe", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item USAGE_SHOVEL = registerItem("usage_shovel", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item USAGE_HOE = registerItem("usage_hoe", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));

    public static final Item UPGRADE_UNBREAKABILITY = registerItem("upgrade_unbreakable", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)));

    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("techreborn")) {
            TechRebornPaxels.init();
        }
        //if (FabricLoader.getInstance().isModLoaded("spirit")) {
        //    SpiritPaxels.init();
        //}

        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(WOODEN_PAXEL));
        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(STONE_PAXEL));
        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(GOLDEN_PAXEL));
        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(IRON_PAXEL));
        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(DIAMOND_PAXEL));
        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(NETHERITE_PAXEL));

        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(NETHERITE_UPGRADE_KIT));

        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(ACTIVE_WALL));
        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(ACTIVE_TUNNEL));
        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(ACTIVE_HOLE));

        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(PASSIVE_SMELT));
        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(PASSIVE_SILENCE));
        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(PASSIVE_VACUUM));

        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(USAGE_AXE));
        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(USAGE_SHOVEL));
        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(USAGE_HOE));

        ItemGroupEvents.modifyEntriesEvent(PAXELS_GROUP).register(entries -> entries.add(UPGRADE_UNBREAKABILITY));
    }

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, PiercingPaxels.id(name), item);
    }

    public static ItemStack getPaxelIcon() {
        return DIAMOND_PAXEL.getDefaultStack();
    }
}
