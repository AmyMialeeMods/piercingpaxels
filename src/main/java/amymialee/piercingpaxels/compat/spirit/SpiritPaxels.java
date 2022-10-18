package amymialee.piercingpaxels.compat.spirit;

import amymialee.piercingpaxels.registry.PiercingItems;
import me.codexadrian.spirit.items.SoulMetalMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

@SuppressWarnings("unused")
public class SpiritPaxels {
    public static final Item SOUL_STEEL_PAXEL = PiercingItems.registerItem("soul_steel_paxel", new SoulSteelPaxel(SoulMetalMaterial.INSTANCE, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.RARE).group(PiercingItems.PAXELS_GROUP)));

    public static void init() {}
}
