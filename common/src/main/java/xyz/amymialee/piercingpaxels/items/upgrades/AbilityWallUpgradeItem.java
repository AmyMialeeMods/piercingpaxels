package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.server.level.ServerPlayer;
import xyz.amymialee.piercingpaxels.items.PaxelItem;

public final class AbilityWallUpgradeItem extends AbilityUpgradeItem {
    public AbilityWallUpgradeItem(Item.Properties properties) { super(properties); }

    @Override
    public void activate(ItemStack upgrade, ItemStack paxel, Level level, LivingEntity user) {
        if (!(user instanceof ServerPlayer player) || !(paxel.getItem() instanceof PaxelItem tool)) return;
        HitResult hit = player.pick(5.0, 1.0F, false);
        if (!(hit instanceof BlockHitResult blockHit)) return;
        BlockPos origin = blockHit.getBlockPos();
        Direction side = blockHit.getDirection();
        for (int a = -1; a <= 1; a++) for (int b = -1; b <= 1; b++) {
            BlockPos pos = switch (side.getAxis()) {
                case X -> origin.offset(0, a, b);
                case Y -> origin.offset(a, 0, b);
                case Z -> origin.offset(a, b, 0);
            };
            breakBlock(player, paxel, pos);
        }
    }

    static void breakBlock(ServerPlayer player, ItemStack paxel, BlockPos pos) {
        PaxelItem tool = (PaxelItem) paxel.getItem();
        BlockState state = player.level().getBlockState(pos);
        if (!state.isAir() && state.getDestroySpeed(player.level(), pos) >= 0 && tool.isCorrectToolForDrops(paxel, state)) {
            player.gameMode.destroyBlock(pos);
        }
    }
}
