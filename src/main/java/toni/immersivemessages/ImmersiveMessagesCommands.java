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

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ImmersiveMessagesCommands {
    
    public static void register() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("immersivemessages")
            .requires(source -> source.hasPermission(2))
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

                                    if (data.contains("typewriter")) tooltip.typewriter(1f, false);
                                    if (data.contains("bold")) tooltip.bold();
                                    if (data.contains("italic")) tooltip.italic();
                                    if (data.contains("shake")) tooltip.shake();
                                    if (data.contains("wave")) tooltip.wave();
                                    if (data.contains("obfuscate")) tooltip.obfuscate();
                                    if (data.contains("color")) tooltip.color(TextColor.parseColor(data.getString("color")) #if MC == "211" .getOrThrow() #endif);
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
}
