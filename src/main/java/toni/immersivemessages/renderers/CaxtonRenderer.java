package toni.immersivemessages.renderers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector3i;
import toni.immersivemessages.api.ImmersiveMessage;
import toni.immersivemessages.overlay.OverlayRenderer;
import toni.immersivemessages.util.AnimationUtil;
import xyz.flirora.caxton.layout.CaxtonText;
import xyz.flirora.caxton.render.CaxtonTextRenderer;

import java.util.ArrayList;

public class CaxtonRenderer implements ITooltipRenderer {
    @Override
    public void render(ImmersiveMessage tooltip, GuiGraphics graphics) {
        var renderer = CaxtonTextRenderer.getInstance();
        var textLines = new ArrayList<Component>();

        var size = wrapText(textLines, tooltip, renderer);

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
        AnimationUtil.applyPose(tooltip.animation, graphics, tooltip.anchor, width, 10f);

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

    private static Vector3i wrapText(ArrayList<Component> textLines, ImmersiveMessage tooltip, CaxtonTextRenderer renderer) {
        textLines.add(tooltip.getText());
        return OverlayRenderer.wrapText(textLines, -1, (line) -> {
            CaxtonText txt = CaxtonText.fromFormatted(
                line,
                renderer::getFontStorage,
                line.getStyle(),
                false,
                renderer.rtl,
                renderer.getHandler().getCache());

            return (int) renderer.getHandler().getWidth(txt);
        });
    }
}
