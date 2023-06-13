package xyz.amymialee.piercingpaxels.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.piercingpaxels.items.PaxelItem;
import xyz.amymialee.piercingpaxels.items.upgrades.DurabilityUpgradeItem;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Inject(method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At(value = "RETURN"), cancellable = true)
    private void piercingPaxels$paxelDamage(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && this.getItem() instanceof PaxelItem) {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"))
    private void piercingPaxels$upgradeDamage(ItemStack stack, int amount, Operation<Void> operation) {
        ItemStack upgrade = DurabilityUpgradeItem.getUpgrade(stack);
        if (!upgrade.isEmpty()) {
            operation.call(stack, ((DurabilityUpgradeItem) upgrade.getItem()).getDurabilityCost(upgrade, stack, amount));
        } else {
            operation.call(stack, amount);
        }
    }
}