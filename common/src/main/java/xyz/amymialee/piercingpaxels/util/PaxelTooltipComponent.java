package xyz.amymialee.piercingpaxels.util;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public final class PaxelTooltipComponent implements ClientTooltipComponent {
    private final PaxelTooltipData data;

    public PaxelTooltipComponent(PaxelTooltipData data) {
        this.data = data;
    }

    @Override
    public int getHeight() {
        for (ItemStack stack : data.upgrades()) if (!stack.isEmpty()) return 18;
        return 0;
    }

    @Override
    public int getWidth(Font font) {
        return data.upgrades().size() * 18;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        int index = 0;
        for (int slotIndex = 0; slotIndex < data.upgrades().size(); slotIndex++) {
            ItemStack stack = data.upgrades().get(slotIndex);
            if (stack.isEmpty()) continue;

            int drawX = x + index++ * 20;
            graphics.renderItem(stack, drawX - 1, y - 2, slotIndex);
            graphics.renderItemDecorations(font, stack, drawX - 1, y - 2);

            PaxelSlot slot = PaxelSlot.values()[slotIndex];
            int size = slot == PaxelSlot.DURABILITY ? 18 : 16;
            int textureSize = slot == PaxelSlot.DURABILITY ? 32 : 16;
            graphics.blit(slot.texture(), drawX - 1, y - 2, 0, 0.0F, 0.0F,
                    size, size, textureSize, textureSize);
        }
    }
}
