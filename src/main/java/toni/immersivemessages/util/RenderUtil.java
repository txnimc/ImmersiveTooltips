package toni.immersivemessages.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import org.joml.Vector3i;
import toni.immersivemessages.api.ImmersiveMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RenderUtil {
    public static void drawBackground(ImmersiveMessage tooltip, GuiGraphics graphics, Vector3i size) {
        var width = size.x;
        var height = size.y;
        var titleLinesCount = size.z;

        //timeline.applyPose(graphics, width + 8, height + 8);

        var xMaxL = -3;
        var xMinL = -4;

        var xMinR = width + 3;
        var xMaxR = width + 4;

        var yMaxT = -4;
        var yMinT = -5;

        var yMaxB = height + 4;
        var yMinB = height + 3;

        float fade = FastColor.ARGB32.alpha(tooltip.animation.getColor()) / 255f;
        ImmersiveColor colorBackground = ImmersiveColor.BLACK.copy().scaleAlpha(0.75f * fade);
        ImmersiveColor colorBorderTop = new ImmersiveColor(36,1,89,255).mixWith(ImmersiveColor.WHITE, 0.1f).scaleAlpha(0.75f * fade);
        ImmersiveColor colorBorderBot = new ImmersiveColor(25,1,53,255).scaleAlpha(0.75f * fade);

        var backgroundColor = colorBackground.getRGB();
        var borderColorStart = colorBorderTop.getRGB();
        var borderColorEnd = colorBorderBot.getRGB();

        graphics.pose().pushPose();
        graphics.pose().translate(0.0D, 0.0D, -5f);
        AnimationUtil.applyPose(tooltip.animation, graphics, tooltip.anchor, size.x, size.y);
        // main background
        graphics.fillGradient(xMaxL, yMinT, xMinR, yMaxT, backgroundColor, backgroundColor);

        graphics.fillGradient(xMaxL, yMinB, xMinR, yMaxB, backgroundColor, backgroundColor);
        graphics.fillGradient(xMaxL, yMaxT, xMinR, yMinB, backgroundColor, backgroundColor);
        graphics.fillGradient(xMinL, yMaxT, xMaxL, yMinB, backgroundColor, backgroundColor);
        graphics.fillGradient(xMinR, yMaxT, xMaxR, yMinB, backgroundColor, backgroundColor);

        // side borders
        graphics.fillGradient(xMaxL, yMaxT + 1, xMaxL + 1, yMinB - 1, borderColorStart, borderColorEnd);
        graphics.fillGradient(xMinR - 1, yMaxT + 1, xMinR, yMinB - 1, borderColorStart, borderColorEnd);

        //top border
        graphics.fillGradient(xMaxL, yMaxT, xMinR, yMaxT + 1, borderColorStart, borderColorStart);
        //bottom border
        graphics.fillGradient(xMaxL, yMinB - 1, xMinR, yMinB, borderColorEnd, borderColorEnd);

        graphics.pose().popPose();
    }

    public static Vector3i wrapText(List<FormattedText> textLines, int maxTextWidth, Function<FormattedText, Integer> widthLookup) {
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        var font = mc.font;
        int tooltipTextWidth = 0;

        for (FormattedText textLine : textLines)
        {
            int textLineWidth = widthLookup.apply(textLine);
            if (textLineWidth > tooltipTextWidth)
                tooltipTextWidth = textLineWidth;
        }

        boolean needsWrap = false;
        int titleLinesCount = 1;

        if (maxTextWidth >= 0)
        {
            if (tooltipTextWidth + 2 > screenWidth / 2)
            {
                tooltipTextWidth = (screenWidth / 2);
                needsWrap = true;
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth)
            {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }
        }

        if (needsWrap)
        {
            int wrappedTooltipWidth = 0;
            List<FormattedText> wrappedTextLines = new ArrayList<>();
            for (int i = 0; i < textLines.size(); i++)
            {
                FormattedText textLine = textLines.get(i);
                List<FormattedText> wrappedLine = font.getSplitter().splitLines(textLine, tooltipTextWidth, Style.EMPTY);
                if (i == 0)
                    titleLinesCount = wrappedLine.size();

                for (FormattedText line : wrappedLine)
                {
                    int lineWidth = widthLookup.apply(line);
                    if (lineWidth > wrappedTooltipWidth)
                        wrappedTooltipWidth = lineWidth;

                    wrappedTextLines.add(line);
                }
            }

            tooltipTextWidth = wrappedTooltipWidth;

            textLines.clear();
            textLines.addAll(wrappedTextLines);
        }

        int tooltipHeight = 8;
        if (textLines.size() > 1)
        {
            tooltipHeight += (textLines.size() - 1) * 10;
            if (textLines.size() > titleLinesCount)
                tooltipHeight += 2; // gap between title lines and next lines
        }

        return new Vector3i(tooltipTextWidth, tooltipHeight, titleLinesCount);
    }
}
