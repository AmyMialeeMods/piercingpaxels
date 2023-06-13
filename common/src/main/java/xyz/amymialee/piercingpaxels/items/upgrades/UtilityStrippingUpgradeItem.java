package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class UtilityStrippingUpgradeItem extends UtilityUpgradeItem {
    public UtilityStrippingUpgradeItem(Settings settings) {
        super(settings);
    }

    @Override
    public Item getToolExample() {
        return Items.NETHERITE_AXE;
    }
}