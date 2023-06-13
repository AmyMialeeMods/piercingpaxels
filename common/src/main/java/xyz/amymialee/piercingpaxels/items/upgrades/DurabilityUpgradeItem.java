package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import xyz.amymialee.piercingpaxels.items.PaxelItem;
import xyz.amymialee.piercingpaxels.util.PaxelSlot;

public abstract class DurabilityUpgradeItem extends Item {
    public DurabilityUpgradeItem(Settings settings) {
        super(settings);
    }

    public int getDurabilityCost(ItemStack upgrade, ItemStack stack, int amount) {
        return amount;
    }

    public void onTick(ItemStack upgrade, ItemStack stack, World world, Entity entity, int slot, boolean selected) {}

    public static ItemStack getUpgrade(PlayerEntity player) {
        if (player != null) {
            ItemStack stack = player.getMainHandStack();
            return getUpgrade(stack);
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getUpgrade(ItemStack stack) {
        if (stack.getItem() instanceof PaxelItem) {
            return PaxelItem.getUpgrade(stack, PaxelSlot.DURABILITY);
        }
        return ItemStack.EMPTY;
    }
}