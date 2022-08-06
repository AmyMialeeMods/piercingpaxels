package amymialee.piercingpaxels.screens;

import amymialee.piercingpaxels.PiercingPaxels;
import amymialee.piercingpaxels.items.PaxelItem;
import amymialee.piercingpaxels.registry.PiercingItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;

public class PaxelScreenHandler extends ScreenHandler {
    private final ItemStack paxel;
    public final Inventory paxelInv = new SimpleInventory(getSize());

    public PaxelScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack paxel) {
        super(PiercingPaxels.PAXEL_SCREEN_HANDLER, syncId);
        this.paxel = paxel;
        readNBT();
        paxelInv.onOpen(playerInventory.player);
        ItemStack upgradePassive = PaxelItem.getUpgrade(paxel, 1);
        if (upgradePassive == null || !upgradePassive.isOf(PiercingItems.PASSIVE_SILENCE)) {
            playerInventory.player.world.playSoundFromEntity(null, playerInventory.player, SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.PLAYERS, 0.5f, playerInventory.player.getRandom().nextFloat() * 0.1f + 0.9f);
        }
        this.addSlot(new Slot(paxelInv, 0, 26, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(PiercingPaxels.ACTIVES);
            }
        });
        this.addSlot(new Slot(paxelInv, 1, 62, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(PiercingPaxels.PASSIVES);
            }
        });
        this.addSlot(new Slot(paxelInv, 2, 98, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(PiercingPaxels.USAGES);
            }
        });
        this.addSlot(new Slot(paxelInv, 3, 134, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(PiercingItems.UPGRADE_UNBREAKABILITY);
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
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(getSize(), ItemStack.EMPTY);
        if (paxel.hasNbt() && paxel.getNbt() != null) {
            Inventories.readNbt(paxel.getNbt(), stacks);
            for (int i = 0; i < stacks.size(); i++) {
                paxelInv.setStack(i, stacks.get(i));
            }
        }
    }

    private void writeNBT() {
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(getSize(), ItemStack.EMPTY);
        double capacity = 0;
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack item = paxelInv.getStack(i);
            stacks.set(i, item);
            capacity += (double) item.getCount() / item.getMaxCount();
        }
        Inventories.writeNbt(paxel.getOrCreateNbt(), stacks, true);
        paxel.getOrCreateNbt().putDouble("pp:capacity", capacity);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private int getSize() {
        return 4;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < getSize() ? !this.insertItem(itemStack2, getSize(), this.slots.size(), true) : !this.insertItem(itemStack2, 0, getSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return itemStack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.paxelInv.onClose(player);
        ItemStack upgradePassive = PaxelItem.getUpgrade(paxel, 1);
        if (upgradePassive == null || !upgradePassive.isOf(PiercingItems.PASSIVE_SILENCE)) {
            player.world.playSoundFromEntity(null, player, SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.PLAYERS, 0.5f, player.getRandom().nextFloat() * 0.1f + 0.9f);
        }
    }

    @Override
    public void sendContentUpdates() {
        writeNBT();
        super.sendContentUpdates();
    }
}