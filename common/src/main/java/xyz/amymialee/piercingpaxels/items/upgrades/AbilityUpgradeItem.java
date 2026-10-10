package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AbilityUpgradeItem extends Item {
    protected AbilityUpgradeItem(Item.Properties properties) {
        super(properties);
    }

    public abstract void activate(ItemStack upgrade, ItemStack paxel, Level level, LivingEntity user);
}
