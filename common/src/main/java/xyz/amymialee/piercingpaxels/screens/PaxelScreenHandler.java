package xyz.amymialee.piercingpaxels.screens;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import dev.architectury.platform.Platform;
import xyz.amymialee.piercingpaxels.items.PaxelItem;
import xyz.amymialee.piercingpaxels.items.upgrades.AbilityUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.DurabilityUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.PassiveUpgradeItem;
import xyz.amymialee.piercingpaxels.items.upgrades.UtilityUpgradeItem;
import xyz.amymialee.piercingpaxels.registry.ModMenus;
import xyz.amymialee.piercingpaxels.util.PaxelSlot;

public final class PaxelScreenHandler extends AbstractContainerMenu {
    private final ItemStack paxel;
    private boolean loadingUpgrades = true;
    private final SimpleContainer upgrades = new SimpleContainer(4) {
        @Override public void setChanged() { super.setChanged(); syncUpgradeData(); }
    };

    public PaxelScreenHandler(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, readPaxel(buf));
    }

    private static ItemStack readPaxel(FriendlyByteBuf buf) {
        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(buf.readUtf())));
        CustomData data = CustomData.of(buf.readNbt());
        stack.set(DataComponents.CUSTOM_DATA, data);
        return stack;
    }

    public PaxelScreenHandler(int id, Inventory inventory, ItemStack paxel) {
        super(ModMenus.PAXEL.get(), id);
        this.paxel = paxel;
        for (PaxelSlot slot : PaxelSlot.values()) upgrades.setItem(slot.ordinal(), PaxelItem.getUpgrade(paxel, slot));
        addSlot(new UpgradeSlot(upgrades, 0, 26, 20, AbilityUpgradeItem.class));
        addSlot(new UpgradeSlot(upgrades, 1, 62, 20, PassiveUpgradeItem.class));
        addSlot(new UpgradeSlot(upgrades, 2, Platform.isFabric() ? 98 : 512, Platform.isFabric() ? 20 : 512, UtilityUpgradeItem.class));
        addSlot(new UpgradeSlot(upgrades, 3, 134, 20, DurabilityUpgradeItem.class));
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 51 + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 8 + col * 18, 109));
        loadingUpgrades = false;
    }

    private void syncUpgradeData() {
        if (!loadingUpgrades && paxel.getItem() instanceof PaxelItem) {
            for (PaxelSlot slot : PaxelSlot.values()) PaxelItem.setUpgrade(paxel, slot, upgrades.getItem(slot.ordinal()));
        }
    }

    @Override public boolean stillValid(Player player) { return player.getMainHandItem() == paxel || player.getOffhandItem() == paxel; }

    @Override public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack copy = slot.getItem().copy();
        if (index < 4) {
            if (!moveItemStackTo(slot.getItem(), 4, slots.size(), true)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(slot.getItem(), 0, 4, false)) return ItemStack.EMPTY;
        if (slot.getItem().isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        return copy;
    }

    @Override public void removed(Player player) { super.removed(player); }

    private static final class UpgradeSlot extends Slot {
        private final Class<?> type;
        private UpgradeSlot(Container container, int index, int x, int y, Class<?> type) { super(container, index, x, y); this.type = type; }
        @Override public boolean mayPlace(ItemStack stack) { return type.isInstance(stack.getItem()); }
        @Override public int getMaxStackSize() { return 1; }
    }
}
