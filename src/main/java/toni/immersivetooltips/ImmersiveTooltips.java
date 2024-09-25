package toni.immersivetooltips;

import com.mojang.blaze3d.platform.Window;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.NbtTagArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import toni.immersivetooltips.foundation.ImmersiveFont;
import toni.immersivetooltips.foundation.ImmersiveTooltip;
import toni.immersivetooltips.foundation.ObfuscateMode;
import toni.immersivetooltips.foundation.config.AllConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toni.immersivetooltips.foundation.networking.TooltipPacket;
import toni.immersivetooltips.foundation.overlay.OverlayRenderer;
import toni.lib.animation.AnimationTimeline;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import static net.minecraft.commands.Commands.*;

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


    public static void showToPlayer(LocalPlayer player, ImmersiveTooltip tooltip) {
        ImmersiveTooltipManager.showToPlayer(player, tooltip);
    }


    #if FABRIC @Override #endif
    public void onInitialize() {
        TooltipPacket.register();

        #if FABRIC
            AllConfigs.register((type, spec) -> {
                #if AFTER_21_1
                NeoForgeConfigRegistry.INSTANCE.register(ImmersiveTooltips.ID, type, spec);
                #else
                ForgeConfigRegistry.INSTANCE.register(ImmersiveTooltips.ID, type, spec);
                #endif
            });
        #endif

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("immersivetooltips")
            .requires(source -> source.hasPermission(2))
            .then(literal("send")
                .then(argument("player", EntityArgument.players())
                    .then(argument("duration", FloatArgumentType.floatArg())
                        .then(argument("string", StringArgumentType.greedyString())
                            .executes(context -> {
                                var players = EntityArgument.getPlayers(context, "player");
                                var duration = FloatArgumentType.getFloat(context, "duration");
                                var string = StringArgumentType.getString(context, "string");

                                ImmersiveTooltip.builder(duration, string)
                                    .slideUp()
                                    .fadeIn()
                                    .fadeOut()
                                    .sendServer(players);

                                return 1;
                            })
                        )
                    )
                )
            )
            .then(literal("sendcustom")
                .then(argument("player", EntityArgument.players())
                    .then(argument("data", CompoundTagArgument.compoundTag())
                        .then(argument("duration", FloatArgumentType.floatArg())
                            .then(argument("string", StringArgumentType.greedyString())
                                .executes(context -> {
                                    var players = EntityArgument.getPlayers(context, "player");
                                    var duration = FloatArgumentType.getFloat(context, "duration");
                                    var data = CompoundTagArgument.getCompoundTag(context, "data");
                                    var string = StringArgumentType.getString(context, "string");

                                    var tooltip = ImmersiveTooltip.builder(duration, string);

                                    if (data.contains("slideup")) tooltip.slideUp();
                                    else if (data.contains("slidedown")) tooltip.slideDown();
                                    else if (data.contains("slideleft")) tooltip.slideLeft();
                                    else if (data.contains("slideright")) tooltip.slideRight();
                                    else {
                                        tooltip.slideUp();
                                    }

                                    if (data.contains("fadein")) tooltip.fadeIn(data.getFloat("fadein"));
                                    else if (data.contains("fadeout")) tooltip.fadeOut(data.getFloat("fadeout"));
                                    else {
                                        tooltip.fadeIn().fadeOut();
                                    }

                                    if (data.contains("bold")) tooltip.bold();
                                    if (data.contains("italic")) tooltip.italic();
                                    if (data.contains("shake")) tooltip.shake();
                                    if (data.contains("wave")) tooltip.wave();
                                    if (data.contains("obfuscate")) tooltip.obfuscate();
                                    if (data.contains("color")) tooltip.color(TextColor.parseColor(data.getString("color")));
                                    if (data.contains("size")) tooltip.size(data.getFloat("size"));
                                    if (data.contains("y")) tooltip.y(data.getFloat("y"));
                                    if (data.contains("x")) tooltip.x(data.getFloat("x"));
                                    if (data.contains("font")) tooltip.font(data.getString("font"));

                                    tooltip.sendServer(players);
                                    return 1;
                                })
                            )
                        )
                    )
                )
            )
        ));
    }

    #if FABRIC @Override #endif
    public void onInitializeClient() {
        TooltipPacket.registerClient();

        #if AFTER_21_1
            #if FABRIC
            ConfigScreenFactoryRegistry.INSTANCE.register(ImmersiveTooltips.ID, ConfigurationScreen::new);
            #endif
        #endif

        HudRenderCallback.EVENT.register(ImmersiveTooltipManager::render);

        HudRenderCallback.EVENT.register((stack, delta) -> {
            Window window = Minecraft.getInstance().getWindow();
            OverlayRenderer.renderOverlay(stack, delta, window);
        });
    }

    // Forg event stubs to call the Fabric initialize methods, and set up cloth config screen
    #if FORGELIKE
    public void commonSetup(FMLCommonSetupEvent event) { onInitialize(); }
    public void clientSetup(FMLClientSetupEvent event) { onInitializeClient(); }
    #endif
}
