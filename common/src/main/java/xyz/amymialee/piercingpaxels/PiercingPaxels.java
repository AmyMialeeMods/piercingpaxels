package xyz.amymialee.piercingpaxels;

import com.google.common.base.Suppliers;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import xyz.amymialee.piercingpaxels.items.PaxelItem;
import xyz.amymialee.piercingpaxels.items.upgrades.AbilityHoleUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.AbilityTunnelUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.AbilityWallUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.DurabilityRestorationUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.DurabilityUnbreakabilityUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.PassiveUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.UtilityFarmingUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.UtilityPavingUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.UtilityStrippingUpgradeItem;
import xyz.amymialee.piercingpaxels.screens.PaxelScreenHandler;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class PiercingPaxels {
    public static final String MOD_ID = "piercingpaxels";
    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
    //Screen Handlers
    public static final Registrar<ScreenHandlerType<?>> SCREEN_HANDLERS = MANAGER.get().get(Registries.SCREEN_HANDLER);
    public static final RegistrySupplier<ScreenHandlerType<PaxelScreenHandler>> PAXEL_SCREEN_HANDLER = SCREEN_HANDLERS.register(id("paxel"), () -> new ScreenHandlerType<>(PaxelScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    //Item Groups
    public static final Registrar<ItemGroup> ITEM_GROUPS = MANAGER.get().get(Registries.ITEM_GROUP);
    public static final RegistrySupplier<ItemGroup> PIERCING_PAXELS_ITEM_GROUP = ITEM_GROUPS.register(id("piercing_paxels"), () -> CreativeTabRegistry.create(Text.translatable("itemGroup.piercingPaxels"), PiercingPaxels::getDefaultStack));
    //Items
    public static final Registrar<Item> ITEMS = MANAGER.get().get(Registries.ITEM);
    //Paxels
    public static final RegistrySupplier<Item> WOODEN_PAXEL = ITEMS.register(id("wooden_paxel"), () -> new PaxelItem(ToolMaterials.WOOD, 5.0f, -2.8f, new Item.Settings().rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    public static final RegistrySupplier<Item> STONE_PAXEL = ITEMS.register(id("stone_paxel"), () -> new PaxelItem(ToolMaterials.STONE, 5.0f, -2.8f, new Item.Settings().rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    public static final RegistrySupplier<Item> GOLDEN_PAXEL = ITEMS.register(id("golden_paxel"), () -> new PaxelItem(ToolMaterials.GOLD, 5.0f, -2.8f, new Item.Settings().rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    public static final RegistrySupplier<Item> IRON_PAXEL = ITEMS.register(id("iron_paxel"), () -> new PaxelItem(ToolMaterials.IRON, 5.0f, -2.8f, new Item.Settings().rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    public static final RegistrySupplier<Item> DIAMOND_PAXEL = ITEMS.register(id("diamond_paxel"), () -> new PaxelItem(ToolMaterials.DIAMOND, 5.0f, -2.8f, new Item.Settings().rarity(Rarity.RARE).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    public static final RegistrySupplier<Item> NETHERITE_PAXEL = ITEMS.register(id("netherite_paxel"), () -> new PaxelItem(ToolMaterials.NETHERITE, 5.0f, -2.8f, new Item.Settings().rarity(Rarity.RARE).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP).fireproof()));
    //Upgrades
    public static final RegistrySupplier<Item> NETHERITE_UPGRADE_KIT = ITEMS.register(id("netherite_paxel_upgrade_kit"), () -> new Item(new Item.Settings().rarity(Rarity.RARE).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    //Abilities
    public static final RegistrySupplier<Item> ACTIVE_WALL = ITEMS.register(id("active_wall"), () -> new AbilityWallUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    public static final RegistrySupplier<Item> ACTIVE_TUNNEL = ITEMS.register(id("active_tunnel"), () -> new AbilityTunnelUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    public static final RegistrySupplier<Item> ACTIVE_HOLE = ITEMS.register(id("active_hole"), () -> new AbilityHoleUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    //Passives
    public static final RegistrySupplier<Item> PASSIVE_SMELT = ITEMS.register(id("passive_smelt"), () -> new PassiveUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    public static final RegistrySupplier<Item> PASSIVE_SILENCE = ITEMS.register(id("passive_silence"), () -> new PassiveUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    public static final RegistrySupplier<Item> PASSIVE_VACUUM = ITEMS.register(id("passive_vacuum"), () -> new PassiveUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    //Durability Upgrades
    public static final RegistrySupplier<Item> UPGRADE_RESTORATION = ITEMS.register(id("upgrade_restoration"), () -> new DurabilityRestorationUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
    public static final RegistrySupplier<Item> UPGRADE_UNBREAKABILITY = ITEMS.register(id("upgrade_unbreakable"), () -> new DurabilityUnbreakabilityUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));

    public static void init() {
        if (Platform.isFabric()) {
            RegistrySupplier<Item> USAGE_AXE = ITEMS.register(id("usage_axe"), () -> new UtilityStrippingUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
            RegistrySupplier<Item> USAGE_SHOVEL = ITEMS.register(id("usage_shovel"), () -> new UtilityPavingUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
            RegistrySupplier<Item> USAGE_HOE = ITEMS.register(id("usage_hoe"), () -> new UtilityFarmingUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).arch$tab(PiercingPaxels.PIERCING_PAXELS_ITEM_GROUP)));
        }
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static ItemStack getDefaultStack() {
        return new ItemStack(IRON_PAXEL.get());
    }
}