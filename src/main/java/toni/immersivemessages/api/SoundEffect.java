package toni.immersivemessages.api;

import net.minecraft.sounds.SoundEvent;
import toni.immersivemessages.IMClient;

public enum SoundEffect {
    NONE,
    LOW,
    LOWSHORT;

    public SoundEvent getSoundEvent() {
        return switch (this) {
            case NONE -> null;
            case LOW -> IMClient.LOW #if forge .get() #endif;
            case LOWSHORT -> IMClient.LOWSHORT #if forge .get() #endif;
        };
    }
}
