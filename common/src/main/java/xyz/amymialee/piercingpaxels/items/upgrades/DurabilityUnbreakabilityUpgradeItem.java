package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class DurabilityUnbreakabilityUpgradeItem extends DurabilityRestorationUpgradeItem {
    public DurabilityUnbreakabilityUpgradeItem(Item.Properties properties) { super(properties); }

    @Override
    public int getDurabilityCost(ItemStack upgrade, ItemStack paxel, int amount) { return 0; }
}
