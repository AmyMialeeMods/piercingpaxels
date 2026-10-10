package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.server.level.ServerPlayer;
import xyz.amymialee.piercingpaxels.items.PaxelItem;

public final class AbilityTunnelUpgradeItem extends AbilityUpgradeItem {
    public AbilityTunnelUpgradeItem(Item.Properties properties) { super(properties); }

    @Override
    public void activate(ItemStack upgrade, ItemStack paxel, Level level, LivingEntity user) {
        if (!(user instanceof ServerPlayer player) || !(paxel.getItem() instanceof PaxelItem tool)) return;
        HitResult hit = player.pick(5.0, 1.0F, false);
        if (!(hit instanceof BlockHitResult blockHit)) return;
        Direction side = blockHit.getDirection();
        for (int depth = 0; depth < 3; depth++) for (int height = -1; height <= 1; height++) {
            BlockPos pos = blockHit.getBlockPos().relative(side, -depth);
            if (side.getAxis().isHorizontal()) pos = pos.above(height);
            AbilityWallUpgradeItem.breakBlock(player, paxel, pos);
        }
    }
}
