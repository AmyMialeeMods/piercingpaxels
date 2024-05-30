package xyz.amymialee.piercingpaxels.platform;

import xyz.amymialee.piercingpaxels.platform.services.IPlatformHelper;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }
}