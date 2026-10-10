package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DurabilityRestorationUpgradeItem extends DurabilityUpgradeItem {
    public DurabilityRestorationUpgradeItem(Item.Properties properties) { super(properties); }

    @Override
    public void onTick(ItemStack upgrade, ItemStack paxel, Level level, Entity entity) {
        if (!level.isClientSide && paxel.getDamageValue() > 0 && level.random.nextInt(256) == 0) {
            paxel.setDamageValue(paxel.getDamageValue() - 1);
        }
    }
}
