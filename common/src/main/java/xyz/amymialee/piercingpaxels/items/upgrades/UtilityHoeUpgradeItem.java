package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class UtilityHoeUpgradeItem extends UtilityUpgradeItem {
    public UtilityHoeUpgradeItem(Item.Properties properties) { super(properties); }
    @Override public Item getToolExample() { return Items.DIAMOND_HOE; }
}
