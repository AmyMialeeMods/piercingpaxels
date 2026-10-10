package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class DurabilityUpgradeItem extends Item {
    protected DurabilityUpgradeItem(Item.Properties properties) {
        super(properties);
    }

    public int getDurabilityCost(ItemStack upgrade, ItemStack paxel, int amount) {
        return amount;
    }

    public void onTick(ItemStack upgrade, ItemStack paxel, Level level, Entity entity) {
    }
}
