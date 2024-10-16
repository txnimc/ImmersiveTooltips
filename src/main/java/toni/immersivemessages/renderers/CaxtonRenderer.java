package toni.immersivemessages.renderers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.joml.Vector3i;
import toni.immersivemessages.api.ImmersiveMessage;
import toni.immersivemessages.util.AnimationUtil;
import toni.immersivemessages.util.ImmersiveColor;
import toni.immersivemessages.util.RenderUtil;
import xyz.flirora.caxton.layout.CaxtonText;
import xyz.flirora.caxton.render.CaxtonTextRenderer;

import java.util.ArrayList;

public class CaxtonRenderer implements ITooltipRenderer {
    @Override
    public void render(ImmersiveMessage tooltip, GuiGraphics graphics) {
        var renderer = CaxtonTextRenderer.getInstance();
        var textLines = new ArrayList<Component>();

        var size = wrapText(textLines, tooltip, renderer);

        if (tooltip.background)
            RenderUtil.drawBackground(tooltip, graphics, size);

        int yOffset = 0;
        for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber)
        {
            Component line = textLines.get(lineNumber);
            if (line != null)
            {
                CaxtonText text = CaxtonText.fromFormatted(
                    line,
                    renderer::getFontStorage,
                    line.getStyle(),
                    false,
                    renderer.rtl,
                    renderer.getHandler().getCache());

                var lineWidth = renderer.getHandler().getWidth(text);
                lineWidth = adjustLineWidthForTypewriter(tooltip, graphics, lineWidth, line, renderer);

                graphics.pose().pushPose();
                AnimationUtil.applyPose(tooltip.animation, graphics, tooltip.anchor, lineWidth, size.y);

                graphics.pose().translate(0.0D, 0.0D, 0.1f);

                renderer.draw(text, 0, yOffset,
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

            yOffset += 10;
        }
    }

    private static float adjustLineWidthForTypewriter(ImmersiveMessage tooltip, GuiGraphics graphics, float lineWidth, Component line, CaxtonTextRenderer renderer) {
        if (!tooltip.typewriter || tooltip.typewriterCenterAligned)
            return lineWidth;

        if (tooltip.wrapMaxWidth > 0)
            return Math.min(tooltip.wrapMaxWidth, graphics.guiWidth() / 2f);

        CaxtonText rawText = CaxtonText.fromFormatted(
            line,
            renderer::getFontStorage,
            tooltip.style,
            false,
            renderer.rtl,
            renderer.getHandler().getCache());

        return renderer.getHandler().getWidth(rawText);
    }

    private static Vector3i wrapText(ArrayList<Component> textLines, ImmersiveMessage tooltip, CaxtonTextRenderer renderer) {
        textLines.add(tooltip.getText());

        var size = RenderUtil.wrapText(textLines, tooltip.wrapMaxWidth, (line) -> {
            CaxtonText txt = CaxtonText.fromFormatted(
                line,
                renderer::getFontStorage,
                line.getStyle(),
                false,
                renderer.rtl,
                renderer.getHandler().getCache());

            return (int) renderer.getHandler().getWidth(txt);
        });

        if (tooltip.subtext != null) {
            var subtextLines = new ArrayList<Component>();
            var subtextSize = wrapText(subtextLines, tooltip.subtext, renderer);

            return new Vector3i(
                Math.max(size.x, subtextSize.x),
                Math.max(size.y, subtextSize.y),
                Math.max(size.z, subtextSize.z)
            );
        }

        return size;
    }
}
