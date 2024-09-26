package toni.immersivemessages.renderers;

import net.minecraft.client.gui.GuiGraphics;
import toni.immersivemessages.api.ImmersiveMessage;

public interface ITooltipRenderer {
    public void render(ImmersiveMessage tooltip, GuiGraphics graphics);
}
