package xyz.amymialee.piercingpaxels.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.piercingpaxels.PiercingPaxels;
import xyz.amymialee.piercingpaxels.items.upgrades.PassiveUpgradeItem;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected ItemStack activeItemStack;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(method = "clearActiveItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;emitGameEvent(Lnet/minecraft/world/event/GameEvent;)V"))
    public void piercingPaxels$useMeSilently(LivingEntity entity, GameEvent event, Operation<Void> operation) {
        if (!PassiveUpgradeItem.hasUpgrade(this.activeItemStack, PiercingPaxels.PASSIVE_SILENCE.get())) {
            operation.call(entity, event);
        }
    }
}