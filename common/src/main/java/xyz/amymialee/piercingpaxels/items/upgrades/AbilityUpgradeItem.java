package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class AbilityUpgradeItem extends Item {
    public AbilityUpgradeItem(Settings settings) {
        super(settings);
    }

    public abstract void activate(ItemStack upgrade, ItemStack itemStack, World world, LivingEntity livingEntity);
}