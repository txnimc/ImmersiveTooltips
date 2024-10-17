---
outline: deep
---

# Animating Messages

Immersive Messages includes a fully configurable animation system. Each `ImmersiveMessage` has a cooresponding `AnimationTimeline`,
which has timeline tracks for things like X and Y position, 3D rotation, size, color, and alpha.

You can bind different transitions with keyframes along this track. For example, text that slides up and fades in, 
and then slides down and fades out, might have a track like the following:

```txt
yPos  : [{0f}---{5f}-----------{5f}---{0f}]
alpha : [{0f}---{1f}-----------{1f}---{0f}]
``` 

You can configure this manually by accessing `ImmersiveMessage.animation` and adding a transition effect, which takes an
in/out time, an in/out value, and an easing function.

```java
message.animation.transition(Binding.yPos, 0f, duration, 0f, 5f, EasingType.EaseOutCubic);
```

Of course, you don't have to configure this yourself for 90% of what you'll likely be doing, 
as there are many helper methods for common tasks to make it easier.

# Fade In / Out
To control the alpha, you can use `fadeIn()` and `fadeOut()`, which both optionally take a duration that defaults to one second.

```java
fadeIn();
fadeOut(2f);
```

# Slide In / Out

To slide text in, use the following functions (which can stack with each other for diagonals)

```java
slideUp();
slideDown();
slideLeft();
slideRight();
```

To do the opposite, you can similarly use the out functions:

```java
slideOutUp();
slideOutDown();
slideOutLeft();
slideOutRight();
```

It is recommended to use fade in/out with slide animations.

# Obfuscate

This function will start the text fully hidden with Minecraft's obfuscated text mode, and slowly reveal it, optionally with a few parameters such as random or left-to-right.
```java
obfuscate(ObfuscateMode.RANDOM, speed);
```

# Typewriter

This mode will slowly write the text to the screen one character at a time, rather than all at once. Optionally, you can force it to have
itself center aligned at all times, which may be preferable, but is disabled by default as it makes it harder to read.

```java
typewriter(float speed, boolean centerAligned);
```

Optionally, you can add vocal synth sound effects (e.g. Undertale noises) to the typed text.

```java
sound(SoundEffect effect);
```

# Wave 

This function applies a small sine wave effect to the text Z rotation, resulting in text that looks like it's floating 
and waving around.

```java
wave(float speed, float intensity);
```

You can also access the animation track directly to use the same effect on other bindings, 
such as position, to make text bob up and down for example:

```java
animation.waveEffect(Binding.yPos, intensity, speed, 0f, animation.duration);
```

# Shake

You can make text shake violently with 
```java
shake(float speed, float intensity);
```