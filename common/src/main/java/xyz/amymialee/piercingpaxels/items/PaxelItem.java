package xyz.amymialee.piercingpaxels.items;

import dev.architectury.platform.Platform;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.amymialee.piercingpaxels.items.upgrades.AbilityUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.DurabilityUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.UtilityUpgradeItem;
import xyz.amymialee.piercingpaxels.screens.PaxelScreenHandler;
import xyz.amymialee.piercingpaxels.util.PaxelSlot;
import xyz.amymialee.piercingpaxels.util.PaxelTooltipData;

import java.util.List;
import java.util.Optional;

public class PaxelItem extends MiningToolItem {
    public PaxelItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(attackDamage, attackSpeed, material, BlockTags.PICKAXE_MINEABLE, settings.maxDamageIfAbsent(material.getDurability() * 5));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.isSneaking() && !world.isClient) {
            this.openMenu(user, stack);
            return TypedActionResult.success(stack);
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity player && !player.isSneaking()) {
            ItemStack upgrade = getUpgrade(stack, PaxelSlot.ABILITY);
            if (upgrade.getItem() instanceof AbilityUpgradeItem ability) {
                ability.activate(upgrade, stack, world, user);
            }
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        ItemStack upgrade = getUpgrade(stack, PaxelSlot.ABILITY);
        if (!upgrade.isEmpty()) {
            float time = (16 - this.miningSpeed) * 2;
            int efficiency = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack);
            if (efficiency > 0) {
                time -= efficiency + 1;
            }
            return Math.max((int) time, 1);
        }
        return 0;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        ItemStack upgrade = getUpgrade(stack, PaxelSlot.ABILITY);
        if (!upgrade.isEmpty()) {
            return UseAction.BOW;
        }
        return UseAction.NONE;
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        ItemStack stack = entity.getStack();
        for (PaxelSlot slot : PaxelSlot.values()) {
            ItemStack upgrade = getUpgrade(stack, slot);
            entity.dropStack(upgrade);
        }
        super.onItemEntityDestroyed(entity);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return stack.getDamage() <= stack.getMaxDamage() ? this.miningSpeed : 0.01f;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stack.getDamage() > 0) {
            ItemStack upgrade = getUpgrade(stack, PaxelSlot.DURABILITY);
            if (!upgrade.isEmpty() && upgrade.getItem() instanceof DurabilityUpgradeItem durability) {
                durability.onTick(upgrade, stack, world, entity, slot, selected);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return this.getMaterial().getMiningLevel() >= getMiningLevel(state);
    }

    public static int getMiningLevel(BlockState state) {
        if (state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return 3;
        } else if (state.isIn(BlockTags.NEEDS_IRON_TOOL)) {
            return 2;
        } else if (state.isIn(BlockTags.NEEDS_STONE_TOOL)) {
            return 1;
        }
        return 0;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (Platform.isFabric()) {
            ItemStack stack = context.getStack();
            ItemStack upgrade = getUpgrade(stack, PaxelSlot.UTILITY);
            if (!context.getWorld().isClient() && upgrade.getItem() instanceof UtilityUpgradeItem utility) {
                return utility.usedOnBlock(context);
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        boolean empty = true;
        for (PaxelSlot slot : PaxelSlot.values()) {
            ItemStack upgrade = getUpgrade(stack, slot);
            if (!upgrade.isEmpty()) {
                empty = false;
                break;
            }
        }
        if (empty) {
            Rarity rarity = Rarity.values()[Math.max(0, stack.getRarity().ordinal() - 1)];
            tooltip.add(Text.translatable("item.piercingpaxels.paxel.empty_tooltip").formatted(rarity.formatting));
        }
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        DefaultedList<ItemStack> stacks = DefaultedList.of();
        for (PaxelSlot slot : PaxelSlot.values()) {
            ItemStack upgrade = getUpgrade(stack, slot);
            stacks.add(upgrade);
        }
        return Optional.of(new PaxelTooltipData(stack.getRarity(), stacks));
    }

    public static ItemStack getUpgrade(ItemStack stack, PaxelSlot slot) {
        if (stack.getNbt() != null && stack.getNbt().contains(slot.toString())) {
            return ItemStack.fromNbt(stack.getNbt().getCompound(slot.toString()));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        if (stack.getRarity().formatting.getColorValue() != null) {
            return stack.getRarity().formatting.getColorValue();
        }
        return super.getItemBarColor(stack);
    }

    public void openMenu(PlayerEntity player, ItemStack stack) {
        NamedScreenHandlerFactory menu = new NamedScreenHandlerFactory() {
            @Override
            public @NotNull Text getDisplayName() {
                return stack.getName();
            }

            @Override
            public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new PaxelScreenHandler(syncId, playerInventory, stack);
            }
        };
        player.openHandledScreen(menu);
    }
}