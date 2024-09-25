package toni.immersivetooltips;

import lombok.Getter;
#if MC > "201" import net.minecraft.client.DeltaTracker; #endif
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import toni.immersivetooltips.foundation.ImmersiveTooltip;
import toni.immersivetooltips.foundation.config.AllConfigs;
import toni.immersivetooltips.foundation.renderers.CaxtonRenderer;
import toni.immersivetooltips.foundation.renderers.ITooltipRenderer;
import toni.immersivetooltips.foundation.renderers.VanillaRenderer;
import toni.lib.utils.PlatformUtils;

import java.util.LinkedList;
import java.util.Queue;

public class ImmersiveTooltipManager {
    private static final Queue<ImmersiveTooltip> tooltipQueue = new LinkedList<>();
    @Getter
    private static final ITooltipRenderer renderer = initRenderer();

    private static final float NANOSECONDS_PER_TICK = 1000000000.0f / 20; // 50 million ns per tick for 20 ticks per second
    private static long lastTime = System.nanoTime();

    private static ImmersiveTooltip currentTooltip;
    private static double countdownToNextTooltip = 0f;

    static void render(GuiGraphics graphics, #if MC == "201" float #else DeltaTracker #endif delta) {
        long currentTime = System.nanoTime();
        if (Minecraft.getInstance().isPaused())
        {
            lastTime = currentTime;
            return;
        }

        #if MC == "201"
        var partialTicks = (currentTime - lastTime) / NANOSECONDS_PER_TICK;
        lastTime = currentTime;
        #else
        var partialTicks = delta.getRealtimeDeltaTicks();
        #endif

        countdownToNextTooltip -= partialTicks;
        if (currentTooltip == null) {
            if (tooltipQueue.isEmpty())
                return;

            currentTooltip = tooltipQueue.remove();
        }

        if (countdownToNextTooltip > 0)
            return;

        renderTooltip(graphics, partialTicks, currentTooltip, 0);
    }

    static void renderTooltip(GuiGraphics graphics, float deltaTicks, ImmersiveTooltip tooltip, int depth) {
        tooltip.tickObfuscation(deltaTicks);
        tooltip.animation.advancePlayhead(deltaTicks / 20);

        if (depth == 0 && tooltip.animation.getCurrent() >= tooltip.animation.duration)
        {
            currentTooltip = null;
            countdownToNextTooltip = 20f * AllConfigs.client().timeBetweenTooltips.get();
            return;
        }

        renderer.render(tooltip, graphics);

        if (tooltip.subtext != null)
            renderTooltip(graphics, deltaTicks, tooltip.subtext, depth + 1);
    }


    private static ITooltipRenderer initRenderer() {
        if (PlatformUtils.isModLoaded("caxton"))
            return new CaxtonRenderer();

        return new VanillaRenderer();
    }

    static void showToPlayer(LocalPlayer player, ImmersiveTooltip tooltip) {
        tooltipQueue.add(tooltip);
    }

    public static boolean hasTooltip() {
        return currentTooltip != null;
    }

    public static int queueCount() {
        return tooltipQueue.size();
    }
}
