package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class UtilityAxeUpgradeItem extends UtilityUpgradeItem {
    public UtilityAxeUpgradeItem(Item.Properties properties) { super(properties); }
    @Override public Item getToolExample() { return Items.DIAMOND_AXE; }
}
