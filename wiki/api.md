---
outline: deep
---

# Sending Messages to Players

Most of the things you probably care about are in the ImmersiveMessage class, including builder methods for creating new
message configs, sending them to players, and rendering them directly on the client from your own code.

First, you'll want to call `ImmersiveMessages.builder()` to obtain a new message to configure. On this class are many 
helper methods for basically everything you'll need to do, such as setting fonts and colors, bold and italic, and configuring
animations.

```java [ExampleMessage.java]
import toni.immersivemessages.api.ImmersiveMessage;

var message = ImmersiveTooltip.builder(duration, text)
        .color(ChatFormatting.WHITE)
        .font(ImmersiveFont.ROBOTO)
        .bold()
        .italic()
        .fadeIn(0.5f)
        .fadeOut(0.5f);
``` 

In this example, bold & italic Roboto text will be sent, with a short fade-in and fade-out.

Once you have a message, you will need to send it to the player. You can call this at the end of the builder chain if
you would like, and there is a similar `sendClient()` function that does not use the network.

```java [ExampleMessage.java]
message.sendServer((ServerPlayer) player);
```

This command will add it to a queue on the client, and show the player messages one at a time, so you can call this
multiple times in a row to schedule a whole list of messages. For example, you could create a helper method and call it
for each line of dialog, and Immersive Messages will handle proper queueing on the client.

```java
public void chatMessage(ServerPlayer player, String text) {
    ImmersiveTooltip.builder(5f, text)
        .fadeIn(0.5f)
        .fadeOut(0.5f)
        .sendServer(player);
}

chatMessage(player, "hello!");
chatMessage(player, "how are you doing?");
chatMessage(player, "awesome!");
```

# Rendering Messages Directly

You can also just render tooltips directly on the client, by passing in a `GuiGraphics` reference and delta ticks.

```java
message.render((GuiGraphics) graphics, (float) deltaTicks);
```
If you don't want animations to play, pass in `0f` for `deltaTicks`.

---
---

For more styling messages, check out the [next page](/styling).