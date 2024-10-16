package toni.immersivemessages.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Tuple;
import org.joml.Vector2f;

public enum TextAnchor {

    CENTER_CENTER,
    CENTER_LEFT,
    CENTER_RIGHT,

    BOTTOM_CENTER,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,

    TOP_CENTER,
    TOP_LEFT,
    TOP_RIGHT;


    private static TextAnchor[] cachedValues;

    public static TextAnchor fromInt(int i) {
        if (cachedValues == null)
            cachedValues = TextAnchor.values();

        return cachedValues[i];
    }

    public Vector2f getOffset(float guiWidth, float guiHeight, float width, float height) {
        return switch (this) {
            case CENTER_CENTER -> new Vector2f(guiWidth - width, guiHeight - height);
            case CENTER_LEFT -> new Vector2f(0, guiHeight - height);
            case CENTER_RIGHT -> new Vector2f(guiWidth * 2 - width * 2, guiHeight - height);

            case BOTTOM_CENTER -> new Vector2f(guiWidth - width, guiHeight * 2 - height * 2);
            case BOTTOM_LEFT -> new Vector2f(0, guiHeight * 2 - height * 2);
            case BOTTOM_RIGHT -> new Vector2f(guiWidth * 2 - width * 2, guiHeight * 2 - height * 2);

            case TOP_CENTER -> new Vector2f(guiWidth - width, 0);
            case TOP_LEFT -> new Vector2f(0, 0);
            case TOP_RIGHT -> new Vector2f(guiWidth * 2 - width * 2, 0);
        };
    }
}
