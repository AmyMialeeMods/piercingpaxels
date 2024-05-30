package xyz.amymialee.piercingpaxels.platform.services;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

public interface IPlatformHelper {
    String getPlatformName();

    static @NotNull String getEnvironmentName() {
        return FabricLoader.getInstance().isDevelopmentEnvironment() ? "development" : "production";
    }
}