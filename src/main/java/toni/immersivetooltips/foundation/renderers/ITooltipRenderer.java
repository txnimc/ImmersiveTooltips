package toni.immersivetooltips.foundation.renderers;

import net.minecraft.client.gui.GuiGraphics;
import toni.immersivetooltips.foundation.ImmersiveTooltip;

public interface ITooltipRenderer {
    public void render(ImmersiveTooltip tooltip, GuiGraphics graphics);
}
