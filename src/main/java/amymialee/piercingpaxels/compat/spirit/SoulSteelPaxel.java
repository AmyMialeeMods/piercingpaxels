package amymialee.piercingpaxels.compat.spirit;

import amymialee.piercingpaxels.items.PaxelItem;
import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.utils.ToolUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulSteelPaxel extends PaxelItem {
    public SoulSteelPaxel(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    public boolean postHit(@NotNull ItemStack itemStack, @NotNull LivingEntity victim, @NotNull LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            ToolUtils.handleOnHitEntity(itemStack, ToolType.PICKAXE, victim, player);
        }
        return super.postHit(itemStack, victim, attacker);
    }

    public boolean postMine(ItemStack itemStack, World level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        boolean mineBlock = super.postMine(itemStack, level, blockState, blockPos, livingEntity);
        if (mineBlock && livingEntity instanceof PlayerEntity player) {
            ToolUtils.handleBreakBlock(player, ToolType.PICKAXE, itemStack, blockState, level, blockPos);
        }
        return mineBlock;
    }

    public ActionResult useOnBlock(@NotNull ItemUsageContext useOnContext) {
        return ToolUtils.handleOnHitBlock(super.useOnBlock(useOnContext), ToolType.PICKAXE, useOnContext.getPlayer(), useOnContext.getStack(), useOnContext.getWorld(), useOnContext.getBlockPos());
    }

    public void appendTooltip(@NotNull ItemStack itemStack, @Nullable World level, @NotNull List<Text> list, @NotNull TooltipContext tooltipFlag) {
        ToolUtils.appendEmpoweredText(itemStack, list);
        super.appendTooltip(itemStack, level, list, tooltipFlag);
    }

    public int getItemBarColor(@NotNull ItemStack itemStack) {
        return -16711685;
    }
}