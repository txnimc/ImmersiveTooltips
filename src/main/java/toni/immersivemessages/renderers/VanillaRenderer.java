package toni.immersivemessages.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import toni.immersivemessages.api.ImmersiveMessage;

public class VanillaRenderer implements ITooltipRenderer {
    @Override
    public void render(ImmersiveMessage tooltip, GuiGraphics graphics) {
        var font = Minecraft.getInstance().font;

        graphics.pose().pushPose();
        var width = tooltip.typewriter && !tooltip.typewriterCenterAligned ? font.width(tooltip.getRawText()) : font.width(tooltip.getText());
        tooltip.animation.applyPose(graphics, width, 10f);

        graphics.drawString(font, tooltip.getText(), 0, 0, tooltip.animation.getColor(), tooltip.shadow);

        graphics.pose().popPose();
    }
}
