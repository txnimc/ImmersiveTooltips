package toni.immersivemessages.util;

import net.minecraft.client.gui.GuiGraphics;
import toni.immersivemessages.api.TextAnchor;
import toni.lib.animation.AnimationKeyframe;
import toni.lib.animation.AnimationTimeline;
import toni.lib.animation.PoseUtils;

public class AnimationUtil {

    public static AnimationKeyframe applyPose(AnimationTimeline animation, GuiGraphics context, TextAnchor anchor, float objectWidth, float objectHeight) {
        var key = animation.getKeyframe();

        PoseUtils.applyScale(context, key.size);
        applyPosition(context, anchor, key.size, objectWidth, objectHeight, key.posX, key.posY, key.posZ);
        PoseUtils.applyYRotation(context, key.size, objectWidth, objectHeight, key.rotY);
        PoseUtils.applyXRotation(context, key.size, objectWidth, objectHeight, key.rotX);
        PoseUtils.applyZRotation(context, key.size, objectWidth, objectHeight, key.rotZ);

        return key;
    }

    public static void applyPosition(GuiGraphics context, TextAnchor anchor, float scale, float objectWidth, float objectHeight, float posX, float posY, float posZ) {
        var offset = anchor.getOffset(context.guiWidth(), context.guiHeight(), objectWidth * scale, objectHeight * scale);
        var centerX = (posX / scale) + offset.x / (2f * scale);
        var centerY = (posY / scale) + offset.y / (2f * scale);

        context.pose().translate(centerX, centerY, posZ);
    }
}
