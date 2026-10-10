package xyz.amymialee.piercingpaxels.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import xyz.amymialee.piercingpaxels.PiercingPaxels;
import xyz.amymialee.piercingpaxels.items.PaxelItem;
import xyz.amymialee.piercingpaxels.items.upgrades.*;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(PiercingPaxels.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> WOODEN_PAXEL = paxel("wooden_paxel", Tiers.WOOD);
    public static final RegistrySupplier<Item> STONE_PAXEL = paxel("stone_paxel", Tiers.STONE);
    public static final RegistrySupplier<Item> IRON_PAXEL = paxel("iron_paxel", Tiers.IRON);
    public static final RegistrySupplier<Item> GOLDEN_PAXEL = paxel("golden_paxel", Tiers.GOLD);
    public static final RegistrySupplier<Item> DIAMOND_PAXEL = paxel("diamond_paxel", Tiers.DIAMOND);
    public static final RegistrySupplier<Item> NETHERITE_PAXEL = ITEMS.register("netherite_paxel", () -> new PaxelItem(Tiers.NETHERITE, PaxelItem.properties(Tiers.NETHERITE).fireResistant()));

    public static final RegistrySupplier<Item> ACTIVE_WALL = ITEMS.register("active_wall", () -> new AbilityWallUpgradeItem(new Item.Properties()));
    public static final RegistrySupplier<Item> ACTIVE_TUNNEL = ITEMS.register("active_tunnel", () -> new AbilityTunnelUpgradeItem(new Item.Properties()));
    public static final RegistrySupplier<Item> ACTIVE_HOLE = ITEMS.register("active_hole", () -> new AbilityHoleUpgradeItem(new Item.Properties()));
    public static final RegistrySupplier<Item> PASSIVE_SMELT = ITEMS.register("passive_smelt", () -> new PassiveSmeltUpgradeItem(new Item.Properties()));
    public static final RegistrySupplier<Item> PASSIVE_SILENCE = ITEMS.register("passive_silence", () -> new PassiveSilenceUpgradeItem(new Item.Properties()));
    public static final RegistrySupplier<Item> PASSIVE_VACUUM = ITEMS.register("passive_vacuum", () -> new PassiveVacuumUpgradeItem(new Item.Properties()));
    public static final RegistrySupplier<Item> UPGRADE_RESTORATION = ITEMS.register("upgrade_restoration", () -> new DurabilityRestorationUpgradeItem(new Item.Properties()));
    public static final RegistrySupplier<Item> UPGRADE_UNBREAKABLE = ITEMS.register("upgrade_unbreakable", () -> new DurabilityUnbreakabilityUpgradeItem(new Item.Properties()));
    public static final RegistrySupplier<Item> USAGE_AXE = ITEMS.register("usage_axe", () -> new UtilityAxeUpgradeItem(new Item.Properties()));
    public static final RegistrySupplier<Item> USAGE_HOE = ITEMS.register("usage_hoe", () -> new UtilityHoeUpgradeItem(new Item.Properties()));
    public static final RegistrySupplier<Item> USAGE_SHOVEL = ITEMS.register("usage_shovel", () -> new UtilityShovelUpgradeItem(new Item.Properties()));
    public static final RegistrySupplier<Item> NETHERITE_PAXEL_UPGRADE_KIT = ITEMS.register("netherite_paxel_upgrade_kit", () -> new Item(new Item.Properties()));

    private static RegistrySupplier<Item> paxel(String name, net.minecraft.world.item.Tier tier) {
        return ITEMS.register(name, () -> new PaxelItem(tier, PaxelItem.properties(tier)));
    }

    private ModItems() {}
}
