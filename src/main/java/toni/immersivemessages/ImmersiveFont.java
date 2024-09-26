package toni.immersivemessages;

import net.minecraft.resources.ResourceLocation;
import toni.lib.utils.PlatformUtils;
import toni.lib.utils.VersionUtils;

public enum ImmersiveFont {
    KALAM("kalam"),
    ROBOTO("roboto"),
    MINECRAFTER("minecrafter"),
    NORSE("norse"),
    ANTON("anton");

    private final String font;

    ImmersiveFont(String font) {
        this.font = font;
    }

    public ResourceLocation getLocation() {
        if (!PlatformUtils.isModLoaded("caxton"))
            return VersionUtils.resource("minecraft", "font/default");

        return VersionUtils.resource("immersivemessages", font);
    }
}
