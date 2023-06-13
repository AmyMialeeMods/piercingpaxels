package xyz.amymialee.piercingpaxels.util;

import net.minecraft.util.Identifier;
import xyz.amymialee.piercingpaxels.PiercingPaxels;

public enum PaxelSlot {
    ABILITY(PiercingPaxels.id("textures/gui/filled_active.png")),
    PASSIVE(PiercingPaxels.id("textures/gui/filled_passive.png")),
    UTILITY(PiercingPaxels.id("textures/gui/filled_usage.png")),
    DURABILITY(PiercingPaxels.id("textures/gui/filled_unbreakable.png"));

    private final Identifier texture;

    PaxelSlot(Identifier texture) {
        this.texture = texture;
    }

    public Identifier getTexture() {
        return this.texture;
    }
}