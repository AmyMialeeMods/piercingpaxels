package xyz.amymialee.piercingpaxels.util;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.List;

public record PaxelTooltipData(Rarity rarity, List<ItemStack> upgrades) implements TooltipComponent {
}
