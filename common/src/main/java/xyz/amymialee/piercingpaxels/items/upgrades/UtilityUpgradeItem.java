package xyz.amymialee.piercingpaxels.items.upgrades;

import dev.architectury.platform.Platform;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class UtilityUpgradeItem extends Item {
    public UtilityUpgradeItem(Settings settings) {
        super(settings);
    }

    public ActionResult usedOnBlock(ItemUsageContext context) {
        return this.getToolExample().useOnBlock(context);
    }

    public abstract Item getToolExample();

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Platform.isForge()) {
            tooltip.add(Text.translatable("piercingpaxels.forge"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}