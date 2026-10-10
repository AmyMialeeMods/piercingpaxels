package xyz.amymialee.piercingpaxels.util;

import net.minecraft.resources.ResourceLocation;
import xyz.amymialee.piercingpaxels.PiercingPaxels;

public enum PaxelSlot {
    ABILITY("ability", "filled_active.png"),
    PASSIVE("passive", "filled_passive.png"),
    UTILITY("utility", "filled_usage.png"),
    DURABILITY("durability", "filled_unbreakable.png");

    private final String key;
    private final ResourceLocation texture;

    PaxelSlot(String key, String texture) {
        this.key = key;
        this.texture = ResourceLocation.fromNamespaceAndPath(PiercingPaxels.MOD_ID, "textures/gui/" + texture);
    }

    public String key() {
        return key;
    }

    public ResourceLocation texture() {
        return texture;
    }
}
