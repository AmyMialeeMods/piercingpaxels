package amymialee.piercingpaxels.screens;

import amymialee.piercingpaxels.PiercingPaxels;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PaxelScreen extends HandledScreen<PaxelScreenHandler> implements ScreenHandlerProvider<PaxelScreenHandler> {
    private static final Identifier TEXTURE = PiercingPaxels.id("textures/gui/paxel_inventory.png");
    private static final Identifier EMPTY_ACTIVE = PiercingPaxels.id("textures/gui/empty_active.png");
    private static final Identifier EMPTY_PASSIVE = PiercingPaxels.id("textures/gui/empty_passive.png");
    private static final Identifier EMPTY_USAGE = PiercingPaxels.id("textures/gui/empty_usage.png");
    private static final Identifier EMPTY_UNBREAKABLE = PiercingPaxels.id("textures/gui/empty_unbreakable.png");

    public PaxelScreen(PaxelScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.passEvents = false;
        this.backgroundHeight = 133;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        if (handler.getSlot(0).getStack().isEmpty()) {
            RenderSystem.setShaderTexture(0, EMPTY_ACTIVE);
            DrawableHelper.drawTexture(matrices, x + 26, y + 20, getZOffset(), 0, 0, 16, 16, 16, 16);
        }
        if (handler.getSlot(1).getStack().isEmpty()) {
            RenderSystem.setShaderTexture(0, EMPTY_PASSIVE);
            DrawableHelper.drawTexture(matrices, x + 62, y + 20, getZOffset(), 0, 0, 16, 16, 16, 16);
        }
        if (handler.getSlot(2).getStack().isEmpty()) {
            RenderSystem.setShaderTexture(0, EMPTY_USAGE);
            DrawableHelper.drawTexture(matrices, x + 98, y + 20, getZOffset(), 0, 0, 16, 16, 16, 16);
        }
        if (handler.getSlot(3).getStack().isEmpty()) {
            RenderSystem.setShaderTexture(0, EMPTY_UNBREAKABLE);
            DrawableHelper.drawTexture(matrices, x + 134, y + 20, getZOffset(), 0, 0, 16, 16, 16, 16);
        }
    }
}