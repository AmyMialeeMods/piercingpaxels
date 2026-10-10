package xyz.amymialee.piercingpaxels.registry;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import xyz.amymialee.piercingpaxels.PiercingPaxels;
import xyz.amymialee.piercingpaxels.screens.PaxelScreenHandler;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(PiercingPaxels.MOD_ID, Registries.MENU);
    public static final RegistrySupplier<MenuType<PaxelScreenHandler>> PAXEL = MENUS.register("paxel", () -> MenuRegistry.ofExtended(PaxelScreenHandler::new));
    private ModMenus() {}
}
