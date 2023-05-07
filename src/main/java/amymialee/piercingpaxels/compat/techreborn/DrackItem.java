package amymialee.piercingpaxels.compat.techreborn;

import amymialee.piercingpaxels.items.PaxelItem;
import amymialee.piercingpaxels.registry.PiercingItems;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;

public class DrackItem extends PaxelItem implements RcEnergyItem {
    private final DrackTier drackTier;

    public DrackItem(DrackTier drackTier, float attackDamage, float attackSpeed, Settings settings) {
        super(drackTier.getMaterial(), attackDamage, attackSpeed, settings);
        this.drackTier = drackTier;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (this.getStoredEnergy(stack) >= drackTier.getCost()) {
            if (this.isSuitableFor(state)) {
                if (state.getMaterial() == Material.STONE && MiningLevelManager.getRequiredMiningLevel(state) < 2) {
                    return drackTier.getPoweredSpeed() * 4;
                }
                return drackTier.getPoweredSpeed();
            }
        }
        return drackTier.getUnpoweredSpeed();
    }

    protected boolean isNotBroken(ItemStack stack) {
        if (this.getStoredEnergy(stack) >= drackTier.getCost()) {
            return true;
        }
        ItemStack upgradePassive = getUpgrade(stack, 3);
        return upgradePassive != null && upgradePassive.isOf(PiercingItems.UPGRADE_UNBREAKABILITY);
    }

    protected boolean damagePaxel(ItemStack stack, LivingEntity user) {
        if (user.getRandom().nextInt(EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) + 1) == 0) {
            tryUseEnergy(stack, drackTier.getCost());
        }
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
            ItemStack upgradePassive = getUpgrade(stack, 3);
            if (upgradePassive == null || !upgradePassive.isOf(PiercingItems.UPGRADE_UNBREAKABILITY)) {
                this.damagePaxel(stack, miner);
            }
        }
        return true;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return ItemUtils.getPowerForDurabilityBar(stack);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public long getEnergyCapacity() {
        return drackTier.getMaxCharge() * 3L;
    }

    @Override
    public RcEnergyTier getTier() {
        return drackTier.getTier();
    }

    @Override
    public long getEnergyMaxOutput() {
        return 0;
    }
}
