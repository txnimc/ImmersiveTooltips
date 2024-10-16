package toni.immersivemessages.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;
import org.joml.Vector3i;
import toni.immersivemessages.api.ImmersiveMessage;
import toni.immersivemessages.util.AnimationUtil;
import toni.immersivemessages.util.RenderUtil;
import xyz.flirora.caxton.layout.CaxtonText;
import xyz.flirora.caxton.render.CaxtonTextRenderer;
import com.mojang.blaze3d.vertex.*;

import java.util.ArrayList;

public class VanillaRenderer implements ITooltipRenderer {
    @Override
    public void render(ImmersiveMessage tooltip, GuiGraphics graphics) {
        var font = Minecraft.getInstance().font;

        var textLines = new ArrayList<Component>();
        textLines.add(tooltip.getText());

        var size = wrapText(textLines, tooltip);

        if (tooltip.background)
            RenderUtil.drawBackground(tooltip, graphics, size);

        int yOffset = 0;
        float fade = FastColor.ARGB32.alpha(tooltip.animation.getColor()) / 255f;
        MultiBufferSource.BufferSource renderType = MultiBufferSource.immediate(#if MC > "201" new ByteBufferBuilder(512) #else new BufferBuilder(512) #endif);

        for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber)
        {
            Component line = textLines.get(lineNumber);
            if (line != null)
            {
                graphics.pose().pushPose();
                var lineWidth = tooltip.typewriter && !tooltip.typewriterCenterAligned ? getTypewriterWidth(graphics, tooltip) : font.width(line);

                AnimationUtil.applyPose(tooltip.animation, graphics, tooltip.anchor, lineWidth, size.y);
                graphics.pose().translate(0.0D, 0.0D, 0.1f);
                Matrix4f mat = graphics.pose().last().pose();

                font.drawInBatch(
                    Language.getInstance().getVisualOrder(line),
                    0,
                    yOffset,
                    FastColor.ARGB32.color((int) Math.max(0, Math.min(255, fade * 255)), 255, 255, 255),
                    true,
                    mat,
                    renderType,
                    Font.DisplayMode.NORMAL,
                    0,
                    15728880);

                graphics.pose().popPose();
            }

            yOffset += 10;
        }

        renderType.endBatch();
    }

    private static Vector3i wrapText(ArrayList<Component> textLines, ImmersiveMessage tooltip) {
        var size = RenderUtil.wrapText(textLines, tooltip.wrapMaxWidth, (line) -> Minecraft.getInstance().font.width(line));

        if (tooltip.subtext != null) {
            var subtextLines = new ArrayList<Component>();
            subtextLines.add(tooltip.subtext.getText());
            var subtextSize = wrapText(subtextLines, tooltip.subtext);

            return new Vector3i(
                Math.max(size.x, subtextSize.x),
                Math.max(size.y, subtextSize.y),
                Math.max(size.z, subtextSize.z)
            );
        }

        return size;
    }

    private float getTypewriterWidth(GuiGraphics graphics, ImmersiveMessage tooltip) {
        if (tooltip.wrapMaxWidth > 0)
            return Math.min(tooltip.wrapMaxWidth, graphics.guiWidth() / 2f);

        return Minecraft.getInstance().font.width(tooltip.getRawText());
    }
}
