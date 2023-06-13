package xyz.amymialee.piercingpaxels.fabric.mixin;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.piercingpaxels.util.PaxelTooltipComponent;
import xyz.amymialee.piercingpaxels.util.PaxelTooltipData;

@Mixin(TooltipComponent.class)
public interface TooltipComponentMixin {
    @Inject(method = "of(Lnet/minecraft/client/item/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;", at = @At("HEAD"), cancellable = true)
    private static void of(TooltipData data, CallbackInfoReturnable<TooltipComponent> cir) {
        if (data instanceof PaxelTooltipData paxelData) {
            cir.setReturnValue(new PaxelTooltipComponent(paxelData));
        }
    }
}