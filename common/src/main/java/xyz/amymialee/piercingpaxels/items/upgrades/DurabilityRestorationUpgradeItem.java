package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DurabilityRestorationUpgradeItem extends DurabilityUpgradeItem {
    public DurabilityRestorationUpgradeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onTick(ItemStack upgrade, ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && world.random.nextInt(256) == 0) {
            stack.setDamage(stack.getDamage() - 1);
        }
    }
}