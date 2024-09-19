package toni.immersivetooltips;

import lombok.Getter;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import toni.immersivetooltips.foundation.ImmersiveTooltip;
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

    private static ImmersiveTooltip currentTooltip;
    private static double countdownToNextTooltip = 0f;

    static void render(GuiGraphics graphics, DeltaTracker delta) {
        countdownToNextTooltip -= delta.getGameTimeDeltaTicks();
        if (currentTooltip == null) {
            if (tooltipQueue.isEmpty())
                return;

            currentTooltip = tooltipQueue.remove();
        }

        if (countdownToNextTooltip > 0)
            return;

        renderTooltip(graphics, delta, currentTooltip, 0);
    }

    static void renderTooltip(GuiGraphics graphics, DeltaTracker delta, ImmersiveTooltip tooltip, int depth) {
        tooltip.tickObfuscation(delta.getRealtimeDeltaTicks());
        tooltip.animation.advancePlayhead(delta.getRealtimeDeltaTicks() / 20);

        if (depth == 0 && tooltip.animation.getCurrent() >= tooltip.animation.duration)
        {
            currentTooltip = null;
            countdownToNextTooltip = 20f;
            return;
        }

        renderer.render(tooltip, graphics);

        if (tooltip.subtext != null)
            renderTooltip(graphics, delta, tooltip.subtext, depth + 1);
    }


    private static ITooltipRenderer initRenderer() {
        if (PlatformUtils.isModLoaded("caxton"))
            return new CaxtonRenderer();

        return new VanillaRenderer();
    }

    static void showToPlayer(LocalPlayer player, ImmersiveTooltip tooltip) {
        tooltipQueue.add(tooltip);
    }
}
