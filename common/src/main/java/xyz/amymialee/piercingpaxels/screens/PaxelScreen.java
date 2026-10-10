package xyz.amymialee.piercingpaxels.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import dev.architectury.platform.Platform;
import xyz.amymialee.piercingpaxels.PiercingPaxels;
import xyz.amymialee.piercingpaxels.util.PaxelSlot;

public final class PaxelScreen extends AbstractContainerScreen<PaxelScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PiercingPaxels.MOD_ID, "textures/gui/paxel_inventory.png");
    private static final ResourceLocation EMPTY_ACTIVE = ResourceLocation.fromNamespaceAndPath(PiercingPaxels.MOD_ID, "textures/gui/empty_active.png");
    private static final ResourceLocation EMPTY_PASSIVE = ResourceLocation.fromNamespaceAndPath(PiercingPaxels.MOD_ID, "textures/gui/empty_passive.png");
    private static final ResourceLocation EMPTY_USAGE = ResourceLocation.fromNamespaceAndPath(PiercingPaxels.MOD_ID, "textures/gui/empty_usage.png");
    private static final ResourceLocation EMPTY_UNBREAKABLE = ResourceLocation.fromNamespaceAndPath(PiercingPaxels.MOD_ID, "textures/gui/empty_unbreakable.png");

    public PaxelScreen(PaxelScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 133;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
        drawEmptySlot(graphics, 0, EMPTY_ACTIVE);
        drawEmptySlot(graphics, 1, EMPTY_PASSIVE);
        if (Platform.isFabric()) drawEmptySlot(graphics, 2, EMPTY_USAGE);
        drawEmptySlot(graphics, 3, EMPTY_UNBREAKABLE);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 8, 6, 0x404040, false);
        graphics.drawString(font, Component.translatable("container.inventory"), 8, 39, 0x404040, false);
    }

    private void drawEmptySlot(GuiGraphics graphics, int slot, ResourceLocation texture) {
        if (!menu.getSlot(slot).hasItem()) {
            int x = switch (slot) {
                case 0 -> 26;
                case 1 -> 62;
                case 2 -> 98;
                default -> 134;
            };
            graphics.blit(texture, leftPos + x, topPos + 20, 0, 0, 16, 16, 16, 16);
        }
    }
}
