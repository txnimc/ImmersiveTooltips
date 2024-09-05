package toni.immersivetooltips.foundation.data;

#if FABRIC
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import toni.immersivetooltips.ImmersiveTooltips;

public class ImmersiveTooltipsDatagen  implements DataGeneratorEntrypoint {

    @Override
    public String getEffectiveModId() {
        return ImmersiveTooltips.ID;
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(ConfigLangDatagen::new);
    }
}
#endif