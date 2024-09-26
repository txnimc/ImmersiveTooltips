package toni.immersivemessages.data;

#if FABRIC
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import toni.immersivemessages.ImmersiveMessages;

public class ImmersiveMessagesDatagen  implements DataGeneratorEntrypoint {

    @Override
    public String getEffectiveModId() {
        return ImmersiveMessages.ID;
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(ConfigLangDatagen::new);
    }
}
#endif