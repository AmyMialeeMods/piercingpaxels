package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.world.item.Item;

public abstract class UtilityUpgradeItem extends Item {
    protected UtilityUpgradeItem(Item.Properties properties) {
        super(properties);
    }

    public abstract Item getToolExample();
}
