package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class UtilityShovelUpgradeItem extends UtilityUpgradeItem {
    public UtilityShovelUpgradeItem(Item.Properties properties) { super(properties); }
    @Override public Item getToolExample() { return Items.DIAMOND_SHOVEL; }
}
