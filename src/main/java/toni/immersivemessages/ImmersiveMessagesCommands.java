package toni.immersivemessages;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import toni.immersivemessages.api.ImmersiveMessage;
import toni.immersivemessages.api.SoundEffect;
import toni.immersivemessages.api.TextAnchor;
import toni.immersivemessages.util.ImmersiveColor;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ImmersiveMessagesCommands {
    
    public static void register() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("immersivemessages")
            .requires(source -> source.hasPermission(2))
            .then(literal("popup")
                .then(argument("player", EntityArgument.players())
                    .then(argument("duration", FloatArgumentType.floatArg())
                        .then(argument("title", StringArgumentType.string())
                            .then(argument("subtitle", StringArgumentType.greedyString())
                                .executes(context -> {
                                    var players = EntityArgument.getPlayers(context, "player");
                                    var duration = FloatArgumentType.getFloat(context, "duration");
                                    var title = StringArgumentType.getString(context, "title");
                                    var subtitle = StringArgumentType.getString(context, "subtitle");

                                    ImmersiveMessage.popup(duration, title, subtitle)
                                        .sendServer(players);

                                    return 1;
                                })
                            )
                        )
                    )
                )
            )
            .then(literal("toast")
                .then(argument("player", EntityArgument.players())
                    .then(argument("duration", FloatArgumentType.floatArg())
                        .then(argument("title", StringArgumentType.string())
                            .then(argument("subtitle", StringArgumentType.greedyString())
                                .executes(context -> {
                                    var players = EntityArgument.getPlayers(context, "player");
                                    var duration = FloatArgumentType.getFloat(context, "duration");
                                    var title = StringArgumentType.getString(context, "title");
                                    var subtitle = StringArgumentType.getString(context, "subtitle");

                                    ImmersiveMessage.toast(duration, title, subtitle)
                                        .sendServer(players);

                                    return 1;
                                })
                            )
                        )
                    )
                )
            )
            .then(literal("send")
                .then(argument("player", EntityArgument.players())
                    .then(argument("duration", FloatArgumentType.floatArg())
                        .then(argument("string", StringArgumentType.greedyString())
                            .executes(context -> {
                                var players = EntityArgument.getPlayers(context, "player");
                                var duration = FloatArgumentType.getFloat(context, "duration");
                                var string = StringArgumentType.getString(context, "string");

                                ImmersiveMessage.builder(duration, string)
                                    .slideUp()
                                    .typewriter(1f, false)
                                    .sound(SoundEffect.LOWSHORT)
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

                                    var tooltip = ImmersiveMessage.builder(duration, string);
                                    tooltip.y(0f);
                                    tooltip.animation.withYPosition(0f);

                                    if (data.contains("fadein")) tooltip.fadeIn(data.getFloat("fadein"));
                                    else if (data.contains("fadeout")) tooltip.fadeOut(data.getFloat("fadeout"));
                                    else {
                                        tooltip.fadeIn().fadeOut();
                                    }

                                    if (data.contains("typewriter")) tooltip.typewriter(1f, false);
                                    if (data.contains("sound")) tooltip.sound(SoundEffect.LOWSHORT);
                                    if (data.contains("bold")) tooltip.bold();
                                    if (data.contains("italic")) tooltip.italic();
                                    if (data.contains("wrap")) tooltip.wrap(0);

                                    if (data.contains("background")) tooltip.background();
                                    if (data.contains("bgColor")) tooltip.backgroundColor(new ImmersiveColor(TextColor.parseColor(data.getString("bgColor")).getOrThrow().getValue()));
                                    if (data.contains("borderTop")) tooltip.backgroundColor(new ImmersiveColor(TextColor.parseColor(data.getString("borderTop")).getOrThrow().getValue()));
                                    if (data.contains("borderBottom")) tooltip.backgroundColor(new ImmersiveColor(TextColor.parseColor(data.getString("borderBottom")).getOrThrow().getValue()));
                                    if (data.contains("rainbow")) tooltip.rainbow();

                                    if (data.contains("shake")) tooltip.shake();
                                    if (data.contains("wave")) tooltip.wave();
                                    if (data.contains("obfuscate")) tooltip.obfuscate();
                                    if (data.contains("align")) tooltip.align(TextAnchor.fromInt(data.getInt("align")));
                                    if (data.contains("anchor")) tooltip.anchor(TextAnchor.fromInt(data.getInt("anchor")));
                                    if (data.contains("color")) tooltip.color(TextColor.parseColor(data.getString("color")) #if MC == "211" .getOrThrow() #endif);
                                    if (data.contains("size")) tooltip.size(data.getFloat("size"));
                                    if (data.contains("y")) tooltip.y(data.getFloat("y"));
                                    if (data.contains("x")) tooltip.x(data.getFloat("x"));
                                    if (data.contains("font")) tooltip.font(data.getString("font"));

                                    if (data.contains("slideup")) tooltip.slideUp();
                                    else if (data.contains("slidedown")) tooltip.slideDown();
                                    else if (data.contains("slideleft")) tooltip.slideLeft();
                                    else if (data.contains("slideright")) tooltip.slideRight();
                                    else {
                                        tooltip.slideUp();
                                    }

                                    if (data.contains("slideoutup")) tooltip.slideOutUp();
                                    else if (data.contains("slideoutdown")) tooltip.slideOutDown();
                                    else if (data.contains("slideoutleft")) tooltip.slideOutLeft();
                                    else if (data.contains("slideoutright")) tooltip.slideOutRight();

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
}
