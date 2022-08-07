package amymialee.piercingpaxels.items;

import amymialee.piercingpaxels.PiercingPaxels;
import amymialee.piercingpaxels.registry.PiercingItems;
import amymialee.piercingpaxels.screens.PaxelScreenHandler;
import amymialee.piercingpaxels.util.PaxelTooltipData;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor;
import net.fabricmc.fabric.mixin.content.registry.HoeItemAccessor;
import net.fabricmc.fabric.mixin.content.registry.ShovelItemAccessor;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.PillarBlock;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.HoneycombItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PaxelItem extends MiningToolItem {
    public PaxelItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, material, PiercingPaxels.PAXEL_MINEABLE, settings.maxDamageIfAbsent(material.getDurability() * 3));
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.isSneaking() && !world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
            openMenu(serverPlayer, stack);
        }
        ItemStack upgradeActive = getUpgrade(stack, 0);
        if (upgradeActive != null) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(user.getStackInHand(hand));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity player && !player.isSneaking() && stack.getDamage() < stack.getMaxDamage() && player.currentScreenHandler == player.playerScreenHandler) {
            HitResult hitResult = user.raycast(5, 1, false);
            if (hitResult instanceof BlockHitResult blockHitResult && !world.getBlockState(blockHitResult.getBlockPos()).isAir()) {
                ItemStack upgradeActive = getUpgrade(stack, 0);
                ItemStack upgradePassive = getUpgrade(stack, 1);
                if (upgradeActive != null) {
                    Direction direction = blockHitResult.getSide();
                    if (upgradeActive.isOf(PiercingItems.ACTIVE_WALL)) {
                        for (int i = -1; i <= 1; i++) {
                            for (int j = -1; j <= 1; j++) {
                                BlockPos pos = null;
                                switch (direction.getAxis()) {
                                    case X -> pos = blockHitResult.getBlockPos().add(0, i, j);
                                    case Y -> pos = blockHitResult.getBlockPos().add(i, 0, j);
                                    case Z -> pos = blockHitResult.getBlockPos().add(i, j, 0);
                                }
                                BlockState state = world.getBlockState(pos);
                                if (pos != null && isSuitableFor(state) && state.getHardness(world, pos) != -1) {
                                    player.interactionManager.tryBreakBlock(pos);
                                    if (upgradePassive == null || !upgradePassive.isOf(PiercingItems.PASSIVE_SILENCE)) {
                                        world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(state));
                                        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(user, state));
                                    }
                                }
                            }
                        }
                    } else if (upgradeActive.isOf(PiercingItems.ACTIVE_TUNNEL)) {
                        for (int i = 0; i < 3; i++) {
                            for (int j = -1; j <= 1; j++) {
                                BlockPos pos;
                                if (direction.getAxis().isHorizontal()) {
                                    pos = blockHitResult.getBlockPos().add(direction.getVector().multiply(-i)).add(0, j, 0);
                                } else {
                                    pos = blockHitResult.getBlockPos().add(direction.getVector().multiply(-i));
                                }
                                BlockState state = world.getBlockState(pos);
                                if (pos != null && isSuitableFor(state) && state.getHardness(world, pos) != -1) {
                                    player.interactionManager.tryBreakBlock(pos);
                                    if (upgradePassive == null || !upgradePassive.isOf(PiercingItems.PASSIVE_SILENCE)) {
                                        world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(state));
                                        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(user, state));
                                    }
                                }
                            }
                        }
                    } else if (upgradeActive.isOf(PiercingItems.ACTIVE_HOLE)) {
                        for (int i = 0; i < 8; i++) {
                            BlockPos pos = blockHitResult.getBlockPos().add(direction.getVector().multiply(-i));
                            BlockState state = world.getBlockState(pos);
                            if (pos != null && isSuitableFor(state) && state.getHardness(world, pos) != -1) {
                                player.interactionManager.tryBreakBlock(pos);
                                if (upgradePassive == null || !upgradePassive.isOf(PiercingItems.PASSIVE_SILENCE)) {
                                    world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(state));
                                    world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(user, state));
                                }
                            }
                        }
                    }
                }
            }
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        ItemStack upgradeActive = getUpgrade(stack, 0);
        return upgradeActive != null && !(upgradeActive.isEmpty()) ? 16 : 0;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        ItemStack upgradeActive = getUpgrade(stack, 0);
        if (upgradeActive != null) {
            return UseAction.BOW;
        }
        return UseAction.NONE;
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        ItemStack paxel = entity.getStack();
        for (int i = 0; i < 4; i++) {
            ItemStack upgrade = getUpgrade(paxel, i);
            entity.dropStack(upgrade);
        }
        super.onItemEntityDestroyed(entity);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        ItemStack upgradePassive = getUpgrade(stack, 3);
        return (upgradePassive != null && upgradePassive.isOf(PiercingItems.UPGRADE_UNBREAKABILITY)) || stack.getDamage() < stack.getMaxDamage() ? this.miningSpeed : 0.01f;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        ItemStack upgradePassive = getUpgrade(stack, 3);
        if (upgradePassive == null || !upgradePassive.isOf(PiercingItems.UPGRADE_UNBREAKABILITY)) {
            if (stack.getDamage() >= stack.getMaxDamage() || stack.damage(1, attacker.getRandom(), attacker instanceof ServerPlayerEntity player ? player : null)) {
                attacker.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            }
        } else {
            stack.setDamage(stack.getDamage() - 1);
        }
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
            ItemStack upgradePassive = getUpgrade(stack, 3);
            if (upgradePassive == null || !upgradePassive.isOf(PiercingItems.UPGRADE_UNBREAKABILITY)) {
                if (stack.getDamage() >= stack.getMaxDamage() || stack.damage(1, miner.getRandom(), miner instanceof ServerPlayerEntity player ? player : null)) {
                    miner.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                }
            } else {
                stack.setDamage(stack.getDamage() - 1);
            }
        }
        return true;
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        int i = this.getMaterial().getMiningLevel();
        return i >= MiningLevelManager.getRequiredMiningLevel(state);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(4, ItemStack.EMPTY);
        if (stack.hasNbt() && stack.getNbt() != null) {
            Inventories.readNbt(stack.getNbt(), stacks);
        }
        DefaultedList<ItemStack> filteredStacks = DefaultedList.of();
        stacks.stream().filter((a) -> !a.isEmpty()).forEach(filteredStacks::add);
        return Optional.of(new PaxelTooltipData(stack.getRarity(), filteredStacks));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = context.getStack();
        ItemStack upgradeUsage = getUpgrade(itemStack, 2);
        if (upgradeUsage != null) {
            if (upgradeUsage.isOf(PiercingItems.USAGE_AXE)) {
                Optional<BlockState> hasUsed = Optional.empty();
                Optional<BlockState> strip = this.getStrippedState(blockState);
                if (strip.isPresent()) {
                    world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    hasUsed = strip;
                } else {
                    Optional<BlockState> reduction = Oxidizable.getDecreasedOxidationState(blockState);
                    if (reduction.isPresent()) {
                        world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        world.syncWorldEvent(playerEntity, WorldEvents.BLOCK_SCRAPED, blockPos, 0);
                        hasUsed = reduction;
                    } else {
                        Optional<BlockState> unWax = Optional.ofNullable(HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get().get(blockState.getBlock())).map(block -> block.getStateWithProperties(blockState));
                        if (unWax.isPresent()) {
                            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0f, 1.0f);
                            world.syncWorldEvent(playerEntity, WorldEvents.WAX_REMOVED, blockPos, 0);
                            hasUsed = unWax;
                        }
                    }
                }
                if (hasUsed.isPresent()) {
                    if (playerEntity instanceof ServerPlayerEntity) {
                        Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
                    }
                    world.setBlockState(blockPos, hasUsed.get(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, hasUsed.get()));
                    return ActionResult.success(world.isClient);
                }
            } else if (upgradeUsage.isOf(PiercingItems.USAGE_SHOVEL)) {
                if (context.getSide() != Direction.DOWN) {
                    BlockState blockState2 = ShovelItemAccessor.getPathStates().get(blockState.getBlock());
                    BlockState blockState3 = null;
                    if (blockState2 != null && world.getBlockState(blockPos.up()).isAir()) {
                        world.playSound(playerEntity, blockPos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        blockState3 = blockState2;
                    } else if (blockState.getBlock() instanceof CampfireBlock && blockState.get(CampfireBlock.LIT)) {
                        if (!world.isClient()) {
                            world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, blockPos, 0);
                        }
                        CampfireBlock.extinguish(context.getPlayer(), world, blockPos, blockState);
                        blockState3 = blockState.with(CampfireBlock.LIT, false);
                    }
                    if (blockState3 != null) {
                        if (!world.isClient) {
                            world.setBlockState(blockPos, blockState3, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                            world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, blockState3));
                        }
                        return ActionResult.success(world.isClient);
                    }
                    return ActionResult.PASS;
                }
            } else if (upgradeUsage.isOf(PiercingItems.USAGE_HOE)) {
                Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair = HoeItemAccessor.getTillingActions().get(world.getBlockState(blockPos = context.getBlockPos()).getBlock());
                if (pair == null) {
                    return ActionResult.PASS;
                }
                Predicate<ItemUsageContext> predicate = pair.getFirst();
                Consumer<ItemUsageContext> consumer = pair.getSecond();
                if (predicate.test(context)) {
                    world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    if (!world.isClient) {
                        consumer.accept(context);
                    }
                    return ActionResult.success(world.isClient);
                }
            }
        }
        return ActionResult.PASS;
    }

    public static ItemStack getUpgrade(ItemStack stack, int slot) {
        if (slot > 3 || slot < 0) {
            return null;
        }
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(4, ItemStack.EMPTY);
        if (stack.hasNbt() && stack.getNbt() != null) {
            Inventories.readNbt(stack.getNbt(), stacks);
            return stacks.get(slot);
        }
        return null;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        ItemStack upgradePassive = getUpgrade(stack, 3);
        if (upgradePassive != null && upgradePassive.isOf(PiercingItems.UPGRADE_UNBREAKABILITY)) {
            return false;
        }
        return super.isItemBarVisible(stack);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        if (stack.getRarity().formatting.getColorValue() != null) {
            return stack.getRarity().formatting.getColorValue();
        }
        return super.getItemBarColor(stack);
    }

    public void openMenu(ServerPlayerEntity player, ItemStack stack) {
        NamedScreenHandlerFactory menu = new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return stack.getName();
            }
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new PaxelScreenHandler(syncId, playerInventory, stack);
            }
        };
        player.openHandledScreen(menu);
    }

    private Optional<BlockState> getStrippedState(BlockState state) {
        return Optional.ofNullable(AxeItemAccessor.getStrippedBlocks().get(state.getBlock())).map(block -> block.getDefaultState().with(PillarBlock.AXIS, state.get(PillarBlock.AXIS)));
    }
}