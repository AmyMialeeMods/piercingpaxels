package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xyz.amymialee.piercingpaxels.items.PaxelItem;
import xyz.amymialee.piercingpaxels.util.PaxelSlot;

public class PassiveUpgradeItem extends Item {
    public PassiveUpgradeItem(Settings settings) {
        super(settings);
    }

    public static boolean hasUpgrade(PlayerEntity player, Item upgrade) {
        if (player != null) {
            ItemStack stack = player.getMainHandStack();
            return hasUpgrade(stack, upgrade);
        }
        return false;
    }

    public static boolean hasUpgrade(ItemStack stack, Item wantedUpgrade) {
        if (stack.getItem() instanceof PaxelItem) {
            ItemStack upgrade = PaxelItem.getUpgrade(stack, PaxelSlot.PASSIVE);
            return upgrade.isOf(wantedUpgrade);
        }
        return false;
    }
}