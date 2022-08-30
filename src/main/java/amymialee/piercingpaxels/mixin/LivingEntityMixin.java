package amymialee.piercingpaxels.mixin;

import amymialee.piercingpaxels.items.PaxelItem;
import amymialee.piercingpaxels.registry.PiercingItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected ItemStack activeItemStack;
    @Shadow protected int itemUseTimeLeft;
    @Shadow protected abstract void setLivingFlag(int mask, boolean value);
    @Shadow @Final protected static int USING_ITEM_FLAG;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "clearActiveItem", at = @At(value = "HEAD"), cancellable = true)
    public void PiercingPaxels$SilentUsage(CallbackInfo ci) {
        if (this.activeItemStack.getItem() instanceof PaxelItem) {
            ItemStack upgradePassive = PaxelItem.getUpgrade(this.activeItemStack, 1);
            if (upgradePassive != null && upgradePassive.isOf(PiercingItems.PASSIVE_SILENCE)) {
                if (!this.world.isClient) {
                    this.setLivingFlag(USING_ITEM_FLAG, false);
                }
                this.activeItemStack = ItemStack.EMPTY;
                this.itemUseTimeLeft = 0;
                ci.cancel();
            }
        }
    }
}