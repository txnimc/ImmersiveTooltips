package toni.lib.animation;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Quaternionf;

public class PoseUtils {

    public static void applyScale(GuiGraphics context, float scale) {
        context.pose().translate(context.guiWidth() / scale, context.guiHeight() / scale,0f);
        context.pose().scale(scale, scale, scale);
        context.pose().translate(context.guiWidth() / (-scale * scale), context.guiHeight() / (-scale * scale),0f);
    }

    public static void applyYRotation(GuiGraphics context, float scale, float objectWidth, float objectHeight, float rotY) {
        float d2r = (float) Math.PI / 180;
        var quat = new Quaternionf().rotationXYZ(0 * d2r, rotY * d2r, 0 * d2r);

        var width = objectWidth / 2f;
        var height = objectHeight / 2f;

        context.pose().translate(width, height, 0);
        context.pose().mulPose(quat);
        context.pose().translate(-width, -height, 0);
    }

    public static void applyXRotation(GuiGraphics context, float scale, float objectWidth, float objectHeight, float rotX) {
        float d2r = (float) Math.PI / 180;
        var quat = new Quaternionf().rotationXYZ(rotX * d2r, d2r, 0 * d2r);

        var width = objectWidth / 2f;
        var height = objectHeight / 2f;

        context.pose().translate(width, height, 0);
        context.pose().mulPose(quat);
        context.pose().translate(-width, -height, 0);
    }

    public static void applyZRotation(GuiGraphics context, float scale, float objectWidth, float objectHeight, float rotX) {
        float d2r = (float) Math.PI / 180;
        var quat = new Quaternionf().rotationXYZ(0 * d2r, d2r, rotX * d2r);

        var width = objectWidth / 2f;
        var height = objectHeight / 2f;

        context.pose().translate(width, height, 0);
        context.pose().mulPose(quat);
        context.pose().translate(-width, -height, 0);
    }

    public static void applyPosition(GuiGraphics context, float scale, float objectWidth, float objectHeight, float posX, float posY, float posZ) {
        var centerX = (posX / scale) + (context.guiWidth() - (objectWidth * scale)) / (2f * scale);
        var centerY = (posY / scale) + (context.guiHeight() - (objectHeight * scale)) / (2f * scale);

        context.pose().translate(centerX, centerY, posZ);
    }
}
