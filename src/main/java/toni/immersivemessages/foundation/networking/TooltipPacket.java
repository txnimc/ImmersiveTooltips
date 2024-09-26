package toni.immersivemessages.foundation.networking;

import net.minecraft.client.Minecraft;
import toni.immersivemessages.ImmersiveMessages;
import toni.immersivemessages.foundation.ImmersiveMessage;
import toni.lib.networking.ToniPacket;

#if MC > "201"
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
#else
import toni.lib.networking.codecs.StreamCodec;
#endif

public class TooltipPacket extends ToniPacket<TooltipPacket> {

    public ImmersiveMessage tooltip;

    public TooltipPacket(ImmersiveMessage tooltip) {
        super(ImmersiveMessages.ID, "tooltip_channel", StreamCodec.composite(
                ImmersiveMessage.CODEC,
                (packet) -> packet.tooltip,
                TooltipPacket::new));

        this.tooltip = tooltip;
    }


    public static void register() {
        new TooltipPacket(null).registerType();
    }

    public static void registerClient() {
        new TooltipPacket(null).registerClientHandler((packet) -> {
            ImmersiveMessages.showToPlayer(Minecraft.getInstance().player, packet.tooltip);
        });
    }
}
