package toni.immersivetooltips.foundation;

import net.minecraft.resources.ResourceLocation;
import toni.lib.utils.PlatformUtils;

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
            return ResourceLocation.fromNamespaceAndPath("minecraft", "font/default");

        return ResourceLocation.fromNamespaceAndPath("immersivetooltips", font);
    }
}
