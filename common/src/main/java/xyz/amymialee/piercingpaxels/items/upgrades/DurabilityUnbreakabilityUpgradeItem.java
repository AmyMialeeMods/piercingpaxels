package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.item.ItemStack;

public class DurabilityUnbreakabilityUpgradeItem extends DurabilityRestorationUpgradeItem {
    public DurabilityUnbreakabilityUpgradeItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getDurabilityCost(ItemStack upgrade, ItemStack stack, int amount) {
        return 0;
    }
}