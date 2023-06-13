package xyz.amymialee.piercingpaxels.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.piercingpaxels.PiercingPaxels;
import xyz.amymialee.piercingpaxels.items.upgrades.PassiveUpgradeItem;

import java.util.ArrayList;
import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "onBreak", at = @At("HEAD"), cancellable = true)
    public void piercingPaxels$quietNow(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        if (PassiveUpgradeItem.hasUpgrade(player, PiercingPaxels.PASSIVE_SILENCE.get())) {
            ci.cancel();
        }
    }

    @Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
    private static void piercingPaxels$getPaxel(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool, CallbackInfo ci, @Share("paxel") LocalRef<ItemStack> paxel) {
        paxel.set(tool);
    }

    @WrapOperation(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;"))
    private static List<ItemStack> piercingPaxels$getStacks(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, Operation<List<ItemStack>> operation, @Share("paxel") LocalRef<ItemStack> paxel) {
        List<ItemStack> list = operation.call(state, world, pos, blockEntity, entity, stack);
        if (PassiveUpgradeItem.hasUpgrade(paxel.get(), PiercingPaxels.PASSIVE_SMELT.get())) {
            ArrayList<ItemStack> smelted = new ArrayList<>();
            boolean smeltedAny = false;
            for (ItemStack stackX : list) {
                ItemStack smeltedX = simulateSmelt(world, stackX);
                if (smeltedX != null) {
                    smelted.add(smeltedX);
                    smeltedAny = true;
                } else {
                    smelted.add(stackX);
                }
            }
            if (smeltedAny) {
                world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
                world.spawnParticles(stack.getItem() == PiercingPaxels.NETHERITE_PAXEL.get() ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 8, 0.25D, 0.25D, 0.25D, 0.025D);
            }
            list = smelted;
        }
        if (entity != null && PassiveUpgradeItem.hasUpgrade(stack, PiercingPaxels.PASSIVE_VACUUM.get())) {
            for (ItemStack stackX : list) {
                if (world.isClient || stackX.isEmpty() || !world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
                    continue;
                }
                ItemEntity itemEntity = new ItemEntity(world, entity.getX(), entity.getBodyY(0.5), entity.getZ(), stackX);
                itemEntity.setPickupDelay(1);
                world.spawnEntity(itemEntity);
            }
            return List.of();
        }
        return list;
    }

    @Unique
    private static final SimpleInventory furnace = new SimpleInventory(3);

    @Unique
    private static ItemStack simulateSmelt(World world, ItemStack input) {
        furnace.clear();
        furnace.setStack(0, input);
        List<SmeltingRecipe> recipes = world.getRecipeManager().getAllMatches(RecipeType.SMELTING, furnace, world);
        for (SmeltingRecipe recipe : recipes) {
            if (recipe.getOutput(world.getRegistryManager()) != null && !recipe.getOutput(world.getRegistryManager()).isEmpty()) {
                ItemStack output = recipe.getOutput(world.getRegistryManager()).copy();
                output.setCount(output.getCount() * input.getCount());
                return output;
            }
        }
        return null;
    }
}