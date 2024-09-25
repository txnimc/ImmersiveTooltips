package toni.immersivetooltips.foundation.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector3f;
import toni.immersivetooltips.ImmersiveTooltipManager;
import toni.immersivetooltips.ImmersiveTooltips;
import toni.immersivetooltips.foundation.ImmersiveTooltip;
import toni.lib.networking.ToniPacket;

public class TooltipPacket extends ToniPacket<TooltipPacket> {

    public ImmersiveTooltip tooltip;

    public TooltipPacket(ImmersiveTooltip tooltip) {
        super(ImmersiveTooltips.ID, "tooltip_channel", StreamCodec.composite(
                ImmersiveTooltip.CODEC,
                (packet) -> packet.tooltip,
                TooltipPacket::new));

        this.tooltip = tooltip;
    }


    public static void register() {
        new TooltipPacket(null).registerType();
    }

    public static void registerClient() {
        new TooltipPacket(null).registerClientHandler((packet) -> {
            ImmersiveTooltips.showToPlayer(Minecraft.getInstance().player, packet.tooltip);
        });
    }
}
