package amymialee.piercingpaxels.util;

import amymialee.piercingpaxels.PiercingPaxels;
import amymialee.piercingpaxels.registry.PiercingItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;

public class PaxelTooltipComponent implements TooltipComponent {
    private static final Identifier FILLED_ACTIVE = PiercingPaxels.id("textures/gui/filled_active.png");
    private static final Identifier FILLED_PASSIVE = PiercingPaxels.id("textures/gui/filled_passive.png");
    private static final Identifier FILLED_USAGE = PiercingPaxels.id("textures/gui/filled_usage.png");
    private static final Identifier FILLED_UNBREAKABLE = PiercingPaxels.id("textures/gui/filled_unbreakable.png");
    private final DefaultedList<ItemStack> inventory;
    private final Rarity rarity;

    public PaxelTooltipComponent(PaxelTooltipData data) {
        this.rarity = data.rarity();
        this.inventory = data.inventory();
    }

    @Override
    public int getHeight() {
        return inventory.size() > 0 ? 18 : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return inventory.size() * 18;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        for (int m = 0; m < inventory.size(); ++m) {
            int n = x + m * 20;
            ItemStack stack = inventory.get(m);
            itemRenderer.renderInGuiWithOverrides(stack, n, y - 1, m);
            itemRenderer.renderGuiItemOverlay(textRenderer, stack, n, y - 1);
            UpgradeType type = null;
            if (stack.isIn(PiercingPaxels.ACTIVES)) {
                type = UpgradeType.ACTIVE;
            } else if (stack.isIn(PiercingPaxels.PASSIVES)) {
                type = UpgradeType.PASSIVE;
            } else if (stack.isIn(PiercingPaxels.USAGES)) {
                type = UpgradeType.USAGE;
            } else if (stack.isOf(PiercingItems.UPGRADE_UNBREAKABILITY)) {
                type = UpgradeType.UNBREAKABLE;
            }
            if (type != null) {
                RenderSystem.setShaderTexture(0, type.texture);
                if (rarity.formatting.getColorValue() != null) {
                    int[] color = getColors(rarity.formatting.getColorValue());
                    RenderSystem.setShaderColor(1.0f / (255f / color[0]), 1.0f / (255f / color[1]), 1.0f / (255f / color[2]), 1.0F);
                }
                if (type != UpgradeType.UNBREAKABLE) {
                    DrawableHelper.drawTexture(matrices, n, y - 1, 500, 0, 0, 16, 16, 16, 16);
                } else {
                    DrawableHelper.drawTexture(matrices, n - 1, y - 2, 500, 0, 0, 18, 18, 32, 32);
                }
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    private int[] getColors(int num) {
        int[] colors = new int[3];
        colors[0] = (num >> 16) & 0xFF;
        colors[1] = (num >> 8) & 0xFF;
        colors[2] = num & 0xFF;
        return colors;
    }

    enum UpgradeType {
        ACTIVE(FILLED_ACTIVE),
        PASSIVE(FILLED_PASSIVE),
        USAGE(FILLED_USAGE),
        UNBREAKABLE(FILLED_UNBREAKABLE);

        private final Identifier texture;

        UpgradeType(Identifier texture) {
            this.texture = texture;
        }
    }
}