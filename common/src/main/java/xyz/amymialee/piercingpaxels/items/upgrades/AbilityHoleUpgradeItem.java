package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.server.level.ServerPlayer;
import xyz.amymialee.piercingpaxels.items.PaxelItem;

public final class AbilityHoleUpgradeItem extends AbilityUpgradeItem {
    public AbilityHoleUpgradeItem(Item.Properties properties) { super(properties); }

    @Override
    public void activate(ItemStack upgrade, ItemStack paxel, Level level, LivingEntity user) {
        if (!(user instanceof ServerPlayer player) || !(paxel.getItem() instanceof PaxelItem tool)) return;
        HitResult hit = player.pick(5.0, 1.0F, false);
        if (!(hit instanceof BlockHitResult blockHit)) return;
        for (int depth = 0; depth < 8; depth++) {
            AbilityWallUpgradeItem.breakBlock(player, paxel, blockHit.getBlockPos().relative(blockHit.getDirection(), -depth));
        }
    }
}
