package toni;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import toni.immersivemessages.api.ImmersiveMessage;

public class SoundUtil {
    public static void playSoundEffect(ImmersiveMessage immersiveMessage) {
        var handler = Minecraft.getInstance().getSoundManager();
        handler.play(SimpleSoundInstance.forUI(immersiveMessage.soundEffect.getSoundEvent(), 1f, 0.01f));
    }
}
