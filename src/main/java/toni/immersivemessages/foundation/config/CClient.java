package toni.immersivemessages.foundation.config;

import toni.lib.config.ConfigBase;

public class CClient extends ConfigBase {

    public final ConfigGroup client = group(0, "client", "Client-only settings - If you're looking for general settings, look inside your world's serverconfig folder!");

    public final ConfigFloat timeBetweenMessages = f(0.5f, 0f, "timeBetweenMessages", "The time to wait before showing the next message in a list.");

    @Override
    public String getName() {
        return "client";
    }
}
