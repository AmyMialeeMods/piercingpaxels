package xyz.amymialee.piercingpaxels.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;

public class PaxelTooltipComponent implements TooltipComponent {
    private final DefaultedList<ItemStack> inventory;
    private final Rarity rarity;

    public PaxelTooltipComponent(PaxelTooltipData data) {
        this.rarity = data.rarity();
        this.inventory = data.inventory();
    }

    @Override
    public int getHeight() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) {
                return 18;
            }
        }
        return 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.inventory.size() * 18;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        TooltipComponent.super.drawItems(textRenderer, x, y, context);
        int index = 0;
        for (int m = 0; m < this.inventory.size(); m++) {
            ItemStack stack = this.inventory.get(m);
            if (stack.isEmpty()) continue;
            int n = x + index * 20;
            index++;
            PaxelSlot slot = PaxelSlot.values()[m];
            context.drawItem(stack, n - 1, y - 2, m);
            context.drawItemInSlot(textRenderer, stack, n - 1, y - 2);
            RenderSystem.setShaderTexture(0, slot.getTexture());
            if (this.rarity.formatting.getColorValue() != null) {
                int[] color = this.getColors(this.rarity.formatting.getColorValue());
                RenderSystem.setShaderColor(1.0f / (255f / color[0]), 1.0f / (255f / color[1]), 1.0f / (255f / color[2]), 1.0F);
            }
            int offset = slot == PaxelSlot.DURABILITY ? -1 : 0;
            int size = slot == PaxelSlot.DURABILITY ? 32 : 16;
            context.drawTexture(slot.getTexture(), n - 1 + offset, y - 2 + offset, 500, 0, 0, 16 - offset * 2, 16 - offset * 2, size, size);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private int[] getColors(int num) {
        int[] colors = new int[3];
        colors[0] = (num >> 16) & 0xFF;
        colors[1] = (num >> 8) & 0xFF;
        colors[2] = num & 0xFF;
        return colors;
    }
}