package amymialee.piercingpaxels.mixin;

import amymialee.piercingpaxels.items.PaxelItem;
import amymialee.piercingpaxels.registry.PiercingItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "onBreak", at = @At("HEAD"), cancellable = true)
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        if (player != null) {
            ItemStack stack = player.getMainHandStack();
            if (stack.getItem() instanceof PaxelItem) {
                ItemStack upgradePassive = PaxelItem.getUpgrade(stack, 1);
                if (upgradePassive != null && upgradePassive.isOf(PiercingItems.PASSIVE_SILENCE)) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void PiercingPaxels$PaxelPassives(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfo ci) {
        if (world instanceof ServerWorld serverWorld && stack != null && stack.getItem() instanceof PaxelItem) {
            ItemStack upgradePassive = PaxelItem.getUpgrade(stack, 1);
            if (upgradePassive != null) {
                if (upgradePassive.isOf(PiercingItems.PASSIVE_SMELT)) {
                    Block.getDroppedStacks(state, serverWorld, pos, blockEntity, entity, stack).forEach((stackX) -> {
                        ItemStack smelted = simulateSmelt(world, stackX);
                        if (smelted != null) {
                            Block.dropStack(world, pos, smelted);
                            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
                            serverWorld.spawnParticles(stack.getItem() == PiercingItems.NETHERITE_PAXEL ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 8, 0.25D, 0.25D, 0.25D, 0.025D);
                        } else {
                            Block.dropStack(world, pos, stackX);
                        }
                    });
                    state.onStacksDropped(serverWorld, pos, stack);
                    ci.cancel();
                } else if (upgradePassive.isOf(PiercingItems.PASSIVE_VACUUM)) {
                    Block.getDroppedStacks(state, serverWorld, pos, blockEntity, entity, stack).forEach((stackX) -> dropStackVacuum(world, () -> new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), stackX), stackX));
                    state.onStacksDropped(serverWorld, pos, stack);
                    ci.cancel();
                }
            }
        }
    }

    private static void dropStackVacuum(World world, Supplier<ItemEntity> itemEntitySupplier, ItemStack stack) {
        if (world.isClient || stack.isEmpty() || !world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            return;
        }
        ItemEntity itemEntity = itemEntitySupplier.get();
        itemEntity.setPickupDelay(0);
        world.spawnEntity(itemEntity);
    }

    private static final SimpleInventory fakeFurnace = new SimpleInventory(4);

    private static ItemStack simulateSmelt(World world, ItemStack input) {
        fakeFurnace.clear();
        fakeFurnace.setStack(0, input);
        List<SmeltingRecipe> recipes = world.getRecipeManager().getAllMatches(RecipeType.SMELTING, fakeFurnace, world);
        for (SmeltingRecipe recipe : recipes) {
            if (recipe.getOutput() != null && !recipe.getOutput().isEmpty()) {
                ItemStack output = recipe.getOutput().copy();
                output.setCount(output.getCount() * input.getCount());
                return output;
            }
        }
        return null;
    }
}