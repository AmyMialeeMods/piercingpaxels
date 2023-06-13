package xyz.amymialee.piercingpaxels.util;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;

public record PaxelTooltipData(Rarity rarity, DefaultedList<ItemStack> inventory) implements TooltipData {}