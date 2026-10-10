package xyz.amymialee.piercingpaxels.items;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import xyz.amymialee.piercingpaxels.items.upgrades.AbilityUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.DurabilityUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.UtilityUpgradeItem;
import xyz.amymialee.piercingpaxels.util.PaxelTooltipData;
import xyz.amymialee.piercingpaxels.util.PaxelSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaxelItem extends DiggerItem {
    private static final int DURABILITY_MULTIPLIER = 5;
    private final Tier tier;

    public PaxelItem(Tier tier, Item.Properties properties) {
        super(tier, net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE, properties);
        this.tier = tier;
    }

    public static Item.Properties properties(Tier tier) {
        int durability = tier.getUses() * DURABILITY_MULTIPLIER;
        return new PaxelProperties(durability).durability(durability).attributes(createAttributes(tier));
    }
    private static final class PaxelProperties extends Item.Properties {
        private final int paxelDurability;

        private PaxelProperties(int paxelDurability) {
            this.paxelDurability = paxelDurability;
        }

        @Override
        public Item.Properties durability(int ignored) {
            return super.durability(paxelDurability);
        }
    }

    public static net.minecraft.world.item.component.ItemAttributeModifiers createAttributes(Tier tier) {
        return DiggerItem.createAttributes(tier, 5.0F, -2.8F);
    }

    public Tier tier() { return tier; }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                MenuRegistry.openExtendedMenu(serverPlayer,
                        new net.minecraft.world.SimpleMenuProvider(
                                (id, inventory, menuPlayer) -> new xyz.amymialee.piercingpaxels.screens.PaxelScreenHandler(id, inventory, stack),
                                stack.getHoverName()),
                        buf -> {
                            buf.writeUtf(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
                            buf.writeNbt(stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag());
                        });
                return InteractionResultHolder.success(stack);
            }
            return InteractionResultHolder.sidedSuccess(stack, true);
        }
        if (!getUpgrade(stack, PaxelSlot.ABILITY).isEmpty()) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (user instanceof ServerPlayer) {
            ItemStack upgrade = getUpgrade(stack, PaxelSlot.ABILITY);
            if (upgrade.getItem() instanceof AbilityUpgradeItem ability) ability.activate(upgrade, stack, level, user);
        }
        return stack;
    }

    @Override public int getUseDuration(ItemStack stack, LivingEntity user) { return getUpgrade(stack, PaxelSlot.ABILITY).isEmpty() ? 0 : 20; }
    @Override public UseAnim getUseAnimation(ItemStack stack) { return getUpgrade(stack, PaxelSlot.ABILITY).isEmpty() ? UseAnim.NONE : UseAnim.BOW; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        boolean empty = true;
        for (PaxelSlot slot : PaxelSlot.values()) {
            if (!getUpgrade(stack, slot).isEmpty()) {
                empty = false;
                break;
            }
        }
        if (empty) {
            tooltip.add(Component.translatable("item.piercingpaxels.paxel.empty_tooltip")
                    .withStyle(stack.getRarity().color()));
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        List<ItemStack> upgrades = new ArrayList<>(PaxelSlot.values().length);
        for (PaxelSlot slot : PaxelSlot.values()) upgrades.add(getUpgrade(stack, slot));
        return Optional.of(new PaxelTooltipData(stack.getRarity(), List.copyOf(upgrades)));
    }

    @Override public float getDestroySpeed(ItemStack stack, BlockState state) { return usable(stack) ? tier.getSpeed() : 0.01F; }
    @Override public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) { return usable(stack) && !state.isAir() && state.getDestroySpeed(null, BlockPos.ZERO) >= 0; }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) damage(stack, entity, 1);
        return true;
    }

    @Override public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) { damage(stack, attacker, 1); }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        ItemStack upgrade = getUpgrade(stack, PaxelSlot.DURABILITY);
        if (upgrade.getItem() instanceof DurabilityUpgradeItem durability) durability.onTick(upgrade, stack, level, entity);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack upgrade = getUpgrade(context.getItemInHand(), PaxelSlot.UTILITY);
        if (!context.getLevel().isClientSide && upgrade.getItem() instanceof UtilityUpgradeItem utility) {
            return utility.getToolExample().useOn(new UseOnContext(context.getPlayer(), context.getHand(),
                    new net.minecraft.world.phys.BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside())));
        }
        return InteractionResult.PASS;
    }

    private boolean usable(ItemStack stack) { return stack.getDamageValue() < stack.getMaxDamage(); }

    private static void damage(ItemStack stack, LivingEntity entity, int amount) {
        if (stack.getDamageValue() >= stack.getMaxDamage()) return;
        ItemStack upgrade = getUpgrade(stack, PaxelSlot.DURABILITY);
        if (upgrade.getItem() instanceof DurabilityUpgradeItem durability) amount = durability.getDurabilityCost(upgrade, stack, amount);
        stack.setDamageValue(Math.min(stack.getMaxDamage(), stack.getDamageValue() + Math.max(0, amount)));
    }

    public static ItemStack getUpgrade(ItemStack stack, PaxelSlot slot) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
        String id = tag.getString(slot.key());
        if (id.isEmpty()) return ItemStack.EMPTY;
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
        return item == null ? ItemStack.EMPTY : new ItemStack(item, Math.max(1, tag.getInt(slot.key() + "_count")));
    }

    public static void setUpgrade(ItemStack paxel, PaxelSlot slot, ItemStack upgrade) {
        net.minecraft.world.item.component.CustomData.update(DataComponents.CUSTOM_DATA, paxel, tag -> {
            if (upgrade.isEmpty()) {
                tag.remove(slot.key());
                tag.remove(slot.key() + "_count");
            } else {
                tag.putString(slot.key(), BuiltInRegistries.ITEM.getKey(upgrade.getItem()).toString());
                tag.putInt(slot.key() + "_count", 1);
            }
        });
    }
}
