package toni.immersivemessages.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Tuple;
import org.joml.Vector2f;
import org.joml.Vector2i;

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

    public Vector2f getAlign(float width, float height) {
        return switch (this) {
            case CENTER_CENTER -> new Vector2f(0, 0);
            case CENTER_LEFT -> new Vector2f(-width, 0);
            case CENTER_RIGHT -> new Vector2f(width, 0);

            case BOTTOM_CENTER -> new Vector2f(0, height);
            case BOTTOM_LEFT -> new Vector2f(-width, height);
            case BOTTOM_RIGHT -> new Vector2f(width, height);

            case TOP_CENTER -> new Vector2f(0, -height);
            case TOP_LEFT -> new Vector2f(-width, -height);
            case TOP_RIGHT -> new Vector2f(width, -height);
        };
    }

    public Vector2i getNormalized() {
        return switch (this) {
            case CENTER_CENTER -> new Vector2i(0, 0);
            case CENTER_LEFT -> new Vector2i(-1, 0);
            case CENTER_RIGHT -> new Vector2i(1, 0);

            case BOTTOM_CENTER -> new Vector2i(0, 1);
            case BOTTOM_LEFT -> new Vector2i(-1, 1);
            case BOTTOM_RIGHT -> new Vector2i(1, 1);

            case TOP_CENTER -> new Vector2i(0, -1);
            case TOP_LEFT -> new Vector2i(-1, -1);
            case TOP_RIGHT -> new Vector2i(1, -1);
        };
    }
}
