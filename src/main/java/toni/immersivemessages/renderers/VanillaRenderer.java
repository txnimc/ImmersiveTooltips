package toni.immersivemessages.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import toni.immersivemessages.api.ImmersiveMessage;

public class VanillaRenderer implements ITooltipRenderer {
    @Override
    public void render(ImmersiveMessage tooltip, GuiGraphics graphics) {
        var font = Minecraft.getInstance().font;

        graphics.pose().pushPose();
        tooltip.animation.applyPose(graphics, font.width(tooltip.text), 10f);

        graphics.drawString(font, tooltip.text, 0, 0, tooltip.animation.getColor(), tooltip.shadow);

        graphics.pose().popPose();
    }
}
