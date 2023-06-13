package xyz.amymialee.piercingpaxels.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.platform.Platform;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xyz.amymialee.piercingpaxels.PiercingPaxels;

public class PaxelScreen extends HandledScreen<PaxelScreenHandler> implements ScreenHandlerProvider<PaxelScreenHandler> {
    private static final Identifier TEXTURE = PiercingPaxels.id("textures/gui/paxel_inventory.png");
    private static final Identifier EMPTY_ACTIVE = PiercingPaxels.id("textures/gui/empty_active.png");
    private static final Identifier EMPTY_PASSIVE = PiercingPaxels.id("textures/gui/empty_passive.png");
    private static final Identifier EMPTY_USAGE = PiercingPaxels.id("textures/gui/empty_usage.png");
    private static final Identifier EMPTY_UNBREAKABLE = PiercingPaxels.id("textures/gui/empty_unbreakable.png");

    public PaxelScreen(PaxelScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 133;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        if (this.handler.getSlot(0).getStack().isEmpty()) {
            RenderSystem.setShaderTexture(0, EMPTY_ACTIVE);
            context.drawTexture(EMPTY_ACTIVE, this.x + 26, this.y + 20, 40, 0, 0, 16, 16, 16, 16);
        }
        if (this.handler.getSlot(1).getStack().isEmpty()) {
            RenderSystem.setShaderTexture(0, EMPTY_PASSIVE);
            context.drawTexture(EMPTY_PASSIVE, this.x + 62, this.y + 20, 40, 0, 0, 16, 16, 16, 16);
        }
        if (Platform.isFabric() && this.handler.getSlot(2).getStack().isEmpty()) {
            RenderSystem.setShaderTexture(0, EMPTY_USAGE);
            context.drawTexture(EMPTY_USAGE, this.x + 98, this.y + 20, 40, 0, 0, 16, 16, 16, 16);
        }
        if (this.handler.getSlot(3).getStack().isEmpty()) {
            RenderSystem.setShaderTexture(0, EMPTY_UNBREAKABLE);
            context.drawTexture(EMPTY_UNBREAKABLE, this.x + 134, this.y + 20, 40, 0, 0, 16, 16, 16, 16);
        }
    }
}