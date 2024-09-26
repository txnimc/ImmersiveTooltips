package toni.immersivemessages.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.joml.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.GameType;
import com.mojang.blaze3d.platform.Window;
import org.joml.Vector3i;
import toni.immersivemessages.ImmersiveMessagesManager;
import toni.immersivemessages.ImmersiveFont;
import toni.immersivemessages.api.ImmersiveMessage;
import toni.immersivemessages.util.ImmersiveColor;
import toni.lib.animation.Binding;
import toni.lib.animation.easing.EasingType;
import toni.lib.utils.PlatformUtils;
import xyz.flirora.caxton.layout.CaxtonText;
import xyz.flirora.caxton.render.CaxtonTextRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

#if MC > "201" import net.minecraft.client.DeltaTracker; #endif

public class OverlayRenderer {

    //private static final Map<Object, OutlineEntry> outlines = CreateClient.OUTLINER.getOutlines();
    private static final float NANOSECONDS_PER_TICK = 1000000000.0f / 20; // 50 million ns per tick for 20 ticks per second
    private static long lastTime = System.nanoTime();

    private static final ImmersiveColor theme = new ImmersiveColor(0xf0_100010, true);

    public static float ticksSinceNoResult = 0;
    private static ImmersiveMessage currentTooltip;

    private static Block lastBlockHitResult;

    public static void renderOverlay(GuiGraphics graphics, #if MC == "201" float #else DeltaTracker #endif delta, Window window) {
        if (Minecraft.getInstance().isPaused())
            return;

        #if MC == "201"
        long currentTime = System.nanoTime();
        var partialTicks = (currentTime - lastTime) / NANOSECONDS_PER_TICK;
        lastTime = currentTime;
        #else
        var partialTicks = delta.getRealtimeDeltaTicks();
        #endif


        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        if (mc.player == null || !mc.player.isCrouching())
            return;

        var sprint = Arrays.stream(Minecraft.getInstance().options.keyMappings).filter((key) -> key.getName().equals("key.sprint")).findFirst();
        if (sprint.isEmpty() || !sprint.get().isDown())
            return;

        if (mc.hitResult instanceof BlockHitResult blockHitResult) {
            var blockstate = Minecraft.getInstance().level.getBlockState(blockHitResult.getBlockPos()).getBlock();
            if (blockstate != lastBlockHitResult)
            {
                lastBlockHitResult = blockstate;
                currentTooltip = null;
            }
        }
        else {
            return;
        }

        if (lastBlockHitResult == null || lastBlockHitResult == Blocks.AIR)
        {
            currentTooltip = null;
            return;
        }

        if (ImmersiveMessagesManager.hasTooltip())
            return;

        if (currentTooltip == null) {
            currentTooltip = ImmersiveMessage.builder(900f, lastBlockHitResult.getName().getString())
                    .subtext(0.5f, PlatformUtils.getModName(BuiltInRegistries.BLOCK.getKey(lastBlockHitResult).getNamespace()), 10f, subtext -> subtext
                            .font(ImmersiveFont.ROBOTO)
                            .italic()
                            .fadeIn(0.5f))
                    .font(ImmersiveFont.NORSE)
                    .bold()
                    .size(1.5F)
                    .animation(builder -> builder.transition(Binding.yPos, 0f, 0.25f, 55f + 10f, 55f, EasingType.EaseOutCubic))
                    .fadeIn(0.25f);
        }

//        if (!(mc.hitResult instanceof EntityHitResult result) || !(result.getEntity() instanceof Villager))
//        {
//            ticksSinceNoResult += partialTicks;
//            if (ticksSinceNoResult > 20) {
//                timeline.resetPlayhead(0f);
//                timeline = getDefaultAnimation();
//            }
//            return;
//        }

        ticksSinceNoResult = 0;
        currentTooltip.animation.advancePlayhead(partialTicks / 20);
        currentTooltip.subtext.animation.advancePlayhead(partialTicks / 20);
//
//        List<Component> tooltip = new ArrayList<>();
//
//        tooltip.add(.withStyle(Style.EMPTY.withFont(ImmersiveFont.ANTON.getLocation())));
//
        ImmersiveMessagesManager.getRenderer().render(currentTooltip, graphics);
        ImmersiveMessagesManager.getRenderer().render(currentTooltip.subtext, graphics);

//
//        tooltip.add(Component.literal("This Villager is Upset!").withStyle(ChatFormatting.RED));
//        tooltip.add(Component.literal("Workspace too small! You should create a more open space for this villager.")
//                .withStyle(Style.EMPTY.withFont(ImmersiveFont.KALAM.getLocation())));

//        graphics.pose().pushPose();
//
//        float fade = FastColor.ARGB32.alpha(timeline.getColor()) / 255f;
//        ImmersiveColor colorBackground = ImmersiveColor.BLACK.copy().scaleAlpha(0.85f * fade);
//        ImmersiveColor colorBorderTop = new ImmersiveColor(36,1,89,255).mixWith(ImmersiveColor.WHITE, 0.1f).scaleAlpha(0.75f * fade);
//        ImmersiveColor colorBorderBot = new ImmersiveColor(25,1,53,255).scaleAlpha(0.75f * fade);
//
//        drawHoveringText(graphics, tooltip, fade, -1, colorBackground.getRGB(), colorBorderTop.getRGB(), colorBorderBot.getRGB());
//
//        graphics.pose().popPose();
    }

    public static void drawHoveringText(GuiGraphics graphics, List<Component> textLines, float fade, int maxTextWidth, int backgroundColor, int borderColorStart, int borderColorEnd) {
        Minecraft mc = Minecraft.getInstance();
        var font = mc.font;

        if (textLines.isEmpty())
            return;

        //RenderSystem.disableRescaleNormal();
        RenderSystem.disableDepthTest();

        var size = wrapText(textLines, maxTextWidth, (line) -> {
            var renderer = CaxtonTextRenderer.getInstance();

            CaxtonText text = CaxtonText.fromFormatted(
                    line,
                    renderer::getFontStorage,
                    line.getStyle(),
                    false,
                    renderer.rtl,
                    renderer.getHandler().getCache());

            return (int) renderer.getHandler().getWidth(text);
        });

        graphics.pose().pushPose();
        Matrix4f mat = graphics.pose().last().pose();

        var width = size.x;
        var height = size.y;
        var titleLinesCount = size.z;

        //timeline.applyPose(graphics, width + 8, height + 8);

        var xMaxL = 1;
        var xMinL = 0;

        var xMinR = width + 7;
        var xMaxR = width + 8;

        var yMaxT = 1;
        var yMinT = 0;

        var yMaxB = height + 8;
        var yMinB = height + 7;

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
        graphics.fillGradient(xMaxL, height + 6, xMinR, yMinB, borderColorEnd, borderColorEnd);


        MultiBufferSource.BufferSource renderType = MultiBufferSource.immediate(#if MC > "201" new ByteBufferBuilder(512) #else new BufferBuilder(512) #endif);
        graphics.pose().translate(0.0D, 0.0D, 1);

        int yOffset = 0;
        for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber)
        {
            Component line = textLines.get(lineNumber);
            if (line != null)
            {
                    var renderer = CaxtonTextRenderer.getInstance();

                    CaxtonText text = CaxtonText.fromFormatted(
                            line,
                            renderer::getFontStorage,
                            line.getStyle(),
                            false,
                            renderer.rtl,
                            renderer.getHandler().getCache());

//                    renderer.draw(text, 4, 4 + yOffset,
//                            timeline.getColor(),
//                            true,
//                            graphics.pose().last().pose(),
//                            graphics.bufferSource(),
//                            true,
//                            0,
//                            255,
//                            0,
//                            1000f);

                 //font.drawInBatch(Language.getInstance().getVisualOrder(line), (float)x, (float)y, FastColor.ARGB32.color((int) Math.max(0, Math.min(255, fade * 255)), 255, 255, 255), true, mat, renderType, Font.DisplayMode.NORMAL, 0, 15728880);
            }

            if (lineNumber + 1 == titleLinesCount)
                yOffset += 2;

            yOffset += 10;
        }

        renderType.endBatch();
        graphics.pose().popPose();

        RenderSystem.enableDepthTest();
    }

    public static Vector3i wrapText(List<Component> textLines, int maxTextWidth, Function<Component, Integer> widthLookup) {
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        var font = mc.font;
        int tooltipTextWidth = 0;

        for (Component textLine : textLines)
        {
            int textLineWidth = widthLookup.apply(textLine);
            if (textLineWidth > tooltipTextWidth)
                tooltipTextWidth = textLineWidth;
        }

        boolean needsWrap = false;

        int titleLinesCount = 1;
        if (tooltipTextWidth + 4 > screenWidth / 2)
        {
            tooltipTextWidth = (screenWidth / 2);
            needsWrap = true;
        }

        if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth)
        {
            tooltipTextWidth = maxTextWidth;
            needsWrap = true;
        }

        if (needsWrap)
        {
            int wrappedTooltipWidth = 0;
            List<Component> wrappedTextLines = new ArrayList<>();
            for (int i = 0; i < textLines.size(); i++)
            {
                Component textLine = textLines.get(i);
                List<FormattedText> wrappedLine = font.getSplitter().splitLines(textLine, tooltipTextWidth, Style.EMPTY);
                if (i == 0)
                    titleLinesCount = wrappedLine.size();

                for (FormattedText line : wrappedLine)
                {
                    var component = Component.literal(line.getString()).withStyle(textLine.getStyle());

                    int lineWidth = widthLookup.apply(component);
                    if (lineWidth > wrappedTooltipWidth)
                        wrappedTooltipWidth = lineWidth;
                    wrappedTextLines.add(component);
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