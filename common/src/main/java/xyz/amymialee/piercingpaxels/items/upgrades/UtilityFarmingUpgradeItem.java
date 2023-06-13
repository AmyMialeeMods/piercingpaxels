package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class UtilityFarmingUpgradeItem extends UtilityUpgradeItem {
    public UtilityFarmingUpgradeItem(Settings settings) {
        super(settings);
    }

    @Override
    public Item getToolExample() {
        return Items.NETHERITE_HOE;
    }
}