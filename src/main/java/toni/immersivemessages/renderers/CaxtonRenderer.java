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
                tooltip.text,
                renderer::getFontStorage,
                tooltip.style,
                false,
                renderer.rtl,
                renderer.getHandler().getCache());

        graphics.pose().pushPose();
        tooltip.animation.applyPose(graphics, renderer.getHandler().getWidth(text), 10f);

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
