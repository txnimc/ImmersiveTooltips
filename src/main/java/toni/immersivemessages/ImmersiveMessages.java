package toni.immersivemessages;

import net.minecraft.client.player.LocalPlayer;
import toni.immersivemessages.foundation.ImmersiveMessagesCommands;
import toni.immersivemessages.foundation.ImmersiveMessage;
import toni.immersivemessages.foundation.config.AllConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toni.immersivemessages.foundation.networking.TooltipPacket;
import toni.lib.animation.AnimationTimeline;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

#if FABRIC
    import net.fabricmc.api.ClientModInitializer;
    import net.fabricmc.api.ModInitializer;

    #if after_21_1
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
    import net.neoforged.neoforge.client.gui.ConfigurationScreen;
    import toni.lib.animation.Binding;
    import toni.lib.animation.easing.EasingType;
    #endif

    #if current_20_1
    import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
    #endif
#endif

#if FORGE
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
#endif


#if NEO
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
#endif


#if FORGELIKE
@Mod("immersivemessages")
#endif
public class ImmersiveMessages #if FABRIC implements ModInitializer, ClientModInitializer #endif
{
    public static final String MODNAME = "Immersive Messages";
    public static final String ID = "immersivemessages";
    public static final Logger LOGGER = LogManager.getLogger(MODNAME);

    private AnimationTimeline animation;

    public ImmersiveMessages(#if NEO IEventBus modEventBus, ModContainer modContainer #endif) {
        #if FORGE
        var context = FMLJavaModLoadingContext.get();
        var modEventBus = context.getModEventBus();
        #endif

        #if FORGELIKE
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        AllConfigs.register((type, spec) -> {
            #if FORGE
            ModLoadingContext.get().registerConfig(type, spec);
            #elif NEO
            modContainer.registerConfig(type, spec);
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
            #endif
        });
        #endif
    }


    public static void showToPlayer(LocalPlayer player, ImmersiveMessage tooltip) {
        ImmersiveMessagesManager.showToPlayer(player, tooltip);
    }


    #if FABRIC @Override #endif
    public void onInitialize() {
        TooltipPacket.register();

        #if FABRIC
            AllConfigs.register((type, spec) -> {
                #if AFTER_21_1
                NeoForgeConfigRegistry.INSTANCE.register(ImmersiveMessages.ID, type, spec);
                #else
                ForgeConfigRegistry.INSTANCE.register(ImmersiveMessages.ID, type, spec);
                #endif
            });
        #endif

        ImmersiveMessagesCommands.register();
    }

    #if FABRIC @Override #endif
    public void onInitializeClient() {
        TooltipPacket.registerClient();

        #if AFTER_21_1
            #if FABRIC
            ConfigScreenFactoryRegistry.INSTANCE.register(ImmersiveMessages.ID, ConfigurationScreen::new);
            #endif
        #endif

        HudRenderCallback.EVENT.register(ImmersiveMessagesManager::render);

//        HudRenderCallback.EVENT.register((stack, delta) -> {
//            Window window = Minecraft.getInstance().getWindow();
//            OverlayRenderer.renderOverlay(stack, delta, window);
//        });
    }

    // Forg event stubs to call the Fabric initialize methods, and set up cloth config screen
    #if FORGELIKE
    public void commonSetup(FMLCommonSetupEvent event) { onInitialize(); }
    public void clientSetup(FMLClientSetupEvent event) { onInitializeClient(); }
    #endif
}
