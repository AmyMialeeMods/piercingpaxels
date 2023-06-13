package xyz.amymialee.piercingpaxels.screens;

import dev.architectury.platform.Platform;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import xyz.amymialee.piercingpaxels.PiercingPaxels;
import xyz.amymialee.piercingpaxels.items.upgrades.AbilityUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.DurabilityUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.PassiveUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.UtilityUpgradeItem;
import xyz.amymialee.piercingpaxels.util.PaxelSlot;

public class PaxelScreenHandler extends ScreenHandler {
    private static final int SLOT_COUNT = 4;
    private final ItemStack stack;
    public final Inventory stackInventory = new SimpleInventory(SLOT_COUNT);

    public PaxelScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, PiercingPaxels.IRON_PAXEL.get().getDefaultStack());
    }

    public PaxelScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack stack) {
        super(PiercingPaxels.PAXEL_SCREEN_HANDLER.get(), syncId);
        this.stack = stack;
        this.readNBT();
        this.stackInventory.onOpen(playerInventory.player);
        this.addSlot(new Slot(this.stackInventory, 0, 26, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof AbilityUpgradeItem;
            }
        });
        this.addSlot(new Slot(this.stackInventory, 1, 62, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof PassiveUpgradeItem;
            }
        });
        if (Platform.isFabric()) {
            this.addSlot(new Slot(this.stackInventory, 2, 98, 20) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.getItem() instanceof UtilityUpgradeItem;
                }
            });
        } else {
            this.addSlot(new Slot(this.stackInventory, 2, 512, 512) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.getItem() instanceof UtilityUpgradeItem;
                }
            });
        }
        this.addSlot(new Slot(this.stackInventory, 3, 134, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof DurabilityUpgradeItem;
            }
        });
        for(int j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, j * 18 + 51));
            }
        }
        for(int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 109));
        }
    }

    private void readNBT() {
        if (this.stack.getNbt() != null) {
            this.stackInventory.clear();
            for (int i = 0; i < PaxelSlot.values().length; i++) {
                this.stackInventory.setStack(i, ItemStack.fromNbt(this.stack.getOrCreateNbt().getCompound(PaxelSlot.values()[i].toString())));
            }
        }
    }

    private void writeNBT() {
        for (int i = 0; i < PaxelSlot.values().length; i++) {
            NbtCompound compound = this.stackInventory.getStack(i).writeNbt(new NbtCompound());
            this.stack.getOrCreateNbt().put(PaxelSlot.values()[i].toString(), compound);
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            if (player.getStackInHand(hand) == this.stack) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index < SLOT_COUNT ? !this.insertItem(slotStack, SLOT_COUNT, this.slots.size(), true) : !this.insertItem(slotStack, 0, SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return stack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.stackInventory.onClose(player);
    }

    @Override
    public void sendContentUpdates() {
        this.writeNBT();
        super.sendContentUpdates();
    }
}