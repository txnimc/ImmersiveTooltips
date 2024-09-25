package toni.immersivetooltips.foundation.config;

import toni.lib.config.ConfigBase;

public class CClient extends ConfigBase {

    public final ConfigGroup client = group(0, "client", "Client-only settings - If you're looking for general settings, look inside your world's serverconfig folder!");

    public final ConfigFloat timeBetweenTooltips = f(0.5f, 0f, "timeBetweenTooltips", "The time to wait before showing the next tooltip in a list.");

    @Override
    public String getName() {
        return "client";
    }
}
