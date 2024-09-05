package toni.immersivetooltips;

import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import toni.immersivetooltips.foundation.config.AllConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toni.lib.animation.AnimationTimeline;
import toni.lib.animation.Binding;
import toni.lib.animation.easing.EasingType;


#if FABRIC
    import net.fabricmc.api.ClientModInitializer;
    import net.fabricmc.api.ModInitializer;
    import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
    #if after_21_1
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
    import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import xyz.flirora.caxton.layout.CaxtonText;
import xyz.flirora.caxton.render.CaxtonTextRenderer;
    #endif

    #if current_20_1
    import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
    #endif
#endif

#if FORGE
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
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
@Mod("immersivetooltips")
#endif
public class ImmersiveTooltips #if FABRIC implements ModInitializer, ClientModInitializer #endif
{
    public static final String MODNAME = "Immersive Tooltips";
    public static final String ID = "immersivetooltips";
    public static final Logger LOGGER = LogManager.getLogger(MODNAME);

    private static double gameticks = 0f;
    private AnimationTimeline animation;

    public ImmersiveTooltips(#if NEO IEventBus modEventBus, ModContainer modContainer #endif) {
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


    #if FABRIC @Override #endif
    public void onInitialize() {
        #if FABRIC
            AllConfigs.register((type, spec) -> {
                #if AFTER_21_1
                NeoForgeConfigRegistry.INSTANCE.register(ImmersiveTooltips.ID, type, spec);
                #else
                ForgeConfigRegistry.INSTANCE.register(ImmersiveTooltips.ID, type, spec);
                #endif
            });
        #endif
    }

    #if FABRIC @Override #endif
    public void onInitializeClient() {
        #if AFTER_21_1
        #if FABRIC
        ConfigScreenFactoryRegistry.INSTANCE.register(ImmersiveTooltips.ID, ConfigurationScreen::new);
        #endif
        #endif

        var white = FastColor.ARGB32.color(255, 255, 255, 255);
        var red = FastColor.ARGB32.color(255, 255, 35, 35);

        animation = AnimationTimeline.builder(1f).waveEffect();

        HudRenderCallback.EVENT.register((context, delta) -> {
            var renderer = CaxtonTextRenderer.getInstance();
            var style = Style.EMPTY
                    .withFont(ResourceLocation.parse("immersivetooltips:immersive"))
                    .withItalic(false);

            gameticks += delta.getRealtimeDeltaTicks();
            if (gameticks % 150f > 140f)
            {
                animation = AnimationTimeline.builder(8f)
                        .waveEffect()
                        .transition(Binding.yRot, 0f, 1f, 90f, 0f, EasingType.EaseOutCubic)
                        .transition(Binding.Size, 0f, 2f, 1f, 2f, EasingType.EaseOutCubic)
                        .transition(Binding.yPos, 2f, 3f, 20f, 40f, EasingType.EaseOutCubic)
                        .transition(Binding.xPos, 2f, 3f, 0f, 50f, EasingType.EaseOutCubic)
                        .transition(Binding.xRot, 3f, 4f, 0f, 360f, EasingType.EaseOutCubic)
                        .transition(Binding.Color, 4f, 5f, white, red, EasingType.EaseInOutBounce)
                        .shake(5.5f)
                        .transition(Binding.Size, 6.5f, 7f, 1f, 0.3f, EasingType.EaseOutCubic)
                        .fadeout(1f);


                animation.resetPlayhead(0f);
                return;
            }

            animation.advancePlayhead(delta.getRealtimeDeltaTicks() / 20);
            var string = "The night draws near. Seek shelter, build a campfire.";

            CaxtonText text = CaxtonText.fromFormatted(
                    string,
                    renderer::getFontStorage,
                    style,
                    false,
                    renderer.rtl,
                    renderer.getHandler().getCache());

            context.pose().pushPose();
            var keyframe = animation.applyPose(context, renderer.getHandler().getWidth(text), 10f);

            renderer.draw(text, 0, 0,
                    FastColor.ARGB32.color(
                            Mth.clamp( (int) (keyframe.alpha * 255 + 5), 0, 255),
                            FastColor.ARGB32.red((int) keyframe.color),
                            FastColor.ARGB32.green((int) keyframe.color),
                            FastColor.ARGB32.blue((int) keyframe.color)),
                    true,
                    context.pose().last().pose(),
                    context.bufferSource(),
                    true,
                    0,
                    255,
                    0,
                    1000f);

            context.pose().popPose();
        });
    }

    // Forg event stubs to call the Fabric initialize methods, and set up cloth config screen
    #if FORGELIKE
    public void commonSetup(FMLCommonSetupEvent event) { onInitialize(); }
    public void clientSetup(FMLClientSetupEvent event) { onInitializeClient(); }
    #endif
}
