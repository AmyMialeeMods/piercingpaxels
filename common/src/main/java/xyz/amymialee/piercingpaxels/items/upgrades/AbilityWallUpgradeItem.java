package xyz.amymialee.piercingpaxels.items.upgrades;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import xyz.amymialee.piercingpaxels.items.PaxelItem;

public class AbilityWallUpgradeItem extends AbilityUpgradeItem {
    public AbilityWallUpgradeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void activate(ItemStack upgrade, ItemStack itemStack, World world, LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayerEntity player && player.currentScreenHandler == player.playerScreenHandler && itemStack.getItem() instanceof PaxelItem paxelItem) {
            HitResult hitResult = player.raycast(5, 1, false);
            if (hitResult instanceof BlockHitResult blockHitResult && !world.getBlockState(blockHitResult.getBlockPos()).isAir()) {
                Direction direction = blockHitResult.getSide();
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        BlockPos pos = null;
                        switch (direction.getAxis()) {
                            case X -> pos = blockHitResult.getBlockPos().add(0, i, j);
                            case Y -> pos = blockHitResult.getBlockPos().add(i, 0, j);
                            case Z -> pos = blockHitResult.getBlockPos().add(i, j, 0);
                        }
                        BlockState state = world.getBlockState(pos);
                        if (pos != null && paxelItem.getMaterial().getMiningLevel() >= PaxelItem.getMiningLevel(state) && state.getHardness(world, pos) != -1) {
                            player.interactionManager.tryBreakBlock(pos);
                        }
                    }
                }
            }
        }
    }
}