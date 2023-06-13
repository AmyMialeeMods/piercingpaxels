package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class UtilityPavingUpgradeItem extends UtilityUpgradeItem {
    public UtilityPavingUpgradeItem(Settings settings) {
        super(settings);
    }

    @Override
    public Item getToolExample() {
        return Items.NETHERITE_SHOVEL;
    }
}