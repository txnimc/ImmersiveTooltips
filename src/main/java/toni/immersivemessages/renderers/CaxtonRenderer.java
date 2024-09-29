package toni.immersivemessages.renderers;

import net.minecraft.client.gui.GuiGraphics;
import toni.immersivemessages.api.ImmersiveMessage;
import xyz.flirora.caxton.layout.CaxtonText;
import xyz.flirora.caxton.render.CaxtonTextRenderer;

public class CaxtonRenderer implements ITooltipRenderer {
    @Override
    public void render(ImmersiveMessage tooltip, GuiGraphics graphics) {
        var renderer = CaxtonTextRenderer.getInstance();

        CaxtonText text = CaxtonText.fromFormatted(
                tooltip.getText(),
                renderer::getFontStorage,
                tooltip.style,
                false,
                renderer.rtl,
                renderer.getHandler().getCache());

        var width = renderer.getHandler().getWidth(text);
        if (tooltip.typewriter && !tooltip.typewriterCenterAligned) {
            CaxtonText rawText = CaxtonText.fromFormatted(
                tooltip.getRawText(),
                renderer::getFontStorage,
                tooltip.style,
                false,
                renderer.rtl,
                renderer.getHandler().getCache());

            width = renderer.getHandler().getWidth(rawText);
        }

        graphics.pose().pushPose();
        tooltip.animation.applyPose(graphics, width, 10f);

        renderer.draw(text, 0, 0,
                tooltip.animation.getColor(),
                tooltip.shadow,
                graphics.pose().last().pose(),
                graphics.bufferSource(),
                true,
                0,
                255,
                0,
                1000f);

        graphics.pose().popPose();
    }
}
