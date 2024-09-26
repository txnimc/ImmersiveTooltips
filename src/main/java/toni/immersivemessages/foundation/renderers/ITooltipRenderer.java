package toni.immersivemessages.foundation.renderers;

import net.minecraft.client.gui.GuiGraphics;
import toni.immersivemessages.foundation.ImmersiveMessage;

public interface ITooltipRenderer {
    public void render(ImmersiveMessage tooltip, GuiGraphics graphics);
}
