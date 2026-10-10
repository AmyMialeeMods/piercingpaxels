package xyz.amymialee.piercingpaxels.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.piercingpaxels.items.PaxelItem;
import xyz.amymialee.piercingpaxels.registry.ModItems;
import xyz.amymialee.piercingpaxels.util.PaxelSlot;

import java.util.ArrayList;
import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "playerWillDestroy", at = @At("HEAD"), cancellable = true)
    private void piercingPaxels$silence(Level level, BlockPos pos, BlockState state, Player player,
                                         CallbackInfoReturnable<BlockState> callback) {
        ItemStack held = player.getMainHandItem();
        if (held.getItem() instanceof PaxelItem
                && PaxelItem.getUpgrade(held, PaxelSlot.PASSIVE).is(ModItems.PASSIVE_SILENCE.get())) {
            callback.setReturnValue(state);
        }
    }

    @WrapOperation(
            method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;"))
    private static List<ItemStack> piercingPaxels$modifyDrops(BlockState state, ServerLevel level, BlockPos pos,
                                                               @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack tool,
                                                               Operation<List<ItemStack>> original) {
        List<ItemStack> drops = original.call(state, level, pos, blockEntity, entity, tool);
        if (!(tool.getItem() instanceof PaxelItem)) return drops;
        ItemStack smeltUpgrade = PaxelItem.getUpgrade(tool, PaxelSlot.PASSIVE);
        if (smeltUpgrade.is(ModItems.PASSIVE_SMELT.get())) {
            List<ItemStack> smelted = new ArrayList<>(drops.size());
            boolean changed = false;
            for (ItemStack drop : drops) {
                ItemStack result = smelt(level, drop);
                smelted.add(result.isEmpty() ? drop : result);
                changed |= !result.isEmpty();
            }
            drops = smelted;
            if (changed) {
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F);
                level.sendParticles(tool.is(ModItems.NETHERITE_PAXEL.get()) ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, 0.25, 0.25, 0.25, 0.025);
            }
        }
        if (entity != null && PaxelItem.getUpgrade(tool, PaxelSlot.PASSIVE).is(ModItems.PASSIVE_VACUUM.get())) {
            for (ItemStack drop : drops) {
                if (!drop.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
                    ItemEntity item = new ItemEntity(level, entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(), drop);
                    item.setPickUpDelay(1);
                    level.addFreshEntity(item);
                }
            }
            return List.of();
        }
        return drops;
    }

    private static ItemStack smelt(ServerLevel level, ItemStack input) {
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(input), level)
                .map(holder -> {
                    ItemStack output = holder.value().assemble(new SingleRecipeInput(input), level.registryAccess()).copy();
                    output.setCount(output.getCount() * input.getCount());
                    return output;
                }).orElse(ItemStack.EMPTY);
    }
}
