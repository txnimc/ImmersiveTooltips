package toni.immersivetooltips.foundation;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import toni.immersivetooltips.ImmersiveTooltips;
import toni.lib.animation.AnimationTimeline;
import toni.lib.animation.Binding;
import toni.lib.animation.easing.EasingType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public class ImmersiveTooltip {
    public Style style = Style.EMPTY;
    public String text;
    public AnimationTimeline animation;
    public ImmersiveTooltip subtext;
    public boolean shadow = true;
    public boolean border;

    public float yLevel = 55f;
    public float xLevel = 0f;
    public float delay = 0f;

    private ObfuscateMode obfuscateMode = ObfuscateMode.NONE;
    private float obfuscateSpeed = 1f;
    private float obfuscateTicks;
    private int obfuscateTimes = 1;

    private ImmersiveTooltip() { }

    public static ImmersiveTooltip builder(float duration, String text) {
        ImmersiveTooltip tooltip = new ImmersiveTooltip();
        tooltip.text = text;
        tooltip.style = Style.EMPTY;
        tooltip.animation = AnimationTimeline.builder(duration);
        tooltip.animation.withYPosition(tooltip.yLevel);
        return tooltip;
    }

    public ImmersiveTooltip subtext(float delay, String subtext, Consumer<ImmersiveTooltip> builder) {
        return subtext(delay, subtext, 10f, builder);
    }

    public ImmersiveTooltip subtext(float delay, String subtext, float offset, Consumer<ImmersiveTooltip> builder) {
        this.subtext = ImmersiveTooltip.builder(animation.duration, subtext);
        this.subtext.delay = delay;
        this.subtext.y(this.yLevel + offset);
        this.subtext.size(0.75f);
        builder.accept(this.subtext);
        return this;
    }

    public ImmersiveTooltip y(float ylevel) {
        this.yLevel = ylevel;
        animation.withYPosition(ylevel);
        return this;
    }

    public ImmersiveTooltip x(float xlevel) {
        this.xLevel = xlevel;
        animation.withXPosition(xlevel);
        return this;
    }

    public ImmersiveTooltip bold() {
        return style(style -> style.withBold(true));
    }

    public ImmersiveTooltip italic() {
        return style(style -> style.withItalic(true));
    }

    public ImmersiveTooltip size(float size) {
        animation.withSize(size);
        return this;
    }

    public ImmersiveTooltip fadeIn() { return fadeIn(1f); }
    public ImmersiveTooltip fadeIn(float duration) {
        animation.transition(Binding.Alpha, delay, delay + duration, 0f, 1f, EasingType.EaseOutSine);
        return this;
    }

    public ImmersiveTooltip fadeOut() { return fadeOut(1f); }
    public ImmersiveTooltip fadeOut(float duration) {
        animation.fadeout(duration);
        return this;
    }

    public ImmersiveTooltip wave() { return wave(5f, 2.5f); }
    public ImmersiveTooltip wave(float speed, float intensity) {
        animation.waveEffect(Binding.zRot, intensity, speed, 0f, animation.duration);
        return this;
    }

    public ImmersiveTooltip slideUp() { return slideUp(1f); }
    public ImmersiveTooltip slideUp(float duration) {
        animation.transition(Binding.yPos, delay, delay + duration, yLevel + 50f, yLevel, EasingType.EaseOutCubic);
        return this;
    }

    public ImmersiveTooltip slideDown() { return slideDown(1f); }
    public ImmersiveTooltip slideDown(float duration) {
        animation.transition(Binding.yPos, delay, delay + duration, yLevel - 50f, yLevel, EasingType.EaseOutCubic);
        return this;
    }

    public ImmersiveTooltip slideLeft() { return slideLeft(1f); }
    public ImmersiveTooltip slideLeft(float duration) {
        animation.transition(Binding.xPos, delay, delay + duration, xLevel - 50f, xLevel, EasingType.EaseOutCubic);
        return this;
    }

    public ImmersiveTooltip slideRight() { return slideRight(1f); }
    public ImmersiveTooltip slideRight(float duration) {
        animation.transition(Binding.xPos, delay, delay + duration, xLevel + 50f, xLevel, EasingType.EaseOutCubic);
        return this;
    }

    public ImmersiveTooltip font(String font) {
        this.style = style.withFont(ResourceLocation.parse(font));
        return this;
    }

    public ImmersiveTooltip font(ImmersiveFont font) {
        this.style = style.withFont(font.getLocation());
        return this;
    }

    public ImmersiveTooltip style(Style style) {
        this.style = style;
        return this;
    }

    public ImmersiveTooltip style(Function<Style, Style> function) {
        this.style = function.apply(style);
        return this;
    }

    public ImmersiveTooltip shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public ImmersiveTooltip animation(Consumer<AnimationTimeline> animationBuilder) {
        animationBuilder.accept(this.animation);
        return this;
    }

    public void send(LocalPlayer player) {
        ImmersiveTooltips.showToPlayer(player, this);
    }

    public ImmersiveTooltip obfuscate() {
        return this.obfuscate(ObfuscateMode.RANDOM, 1f);
    }

    public ImmersiveTooltip obfuscate(ObfuscateMode mode) {
        return this.obfuscate(mode, 1f);
    }

    public ImmersiveTooltip obfuscate(ObfuscateMode mode, float speed) {
        this.obfuscateMode = mode;
        this.obfuscateSpeed = speed;

        var sb = new StringBuilder();
        for (var chr : text.toCharArray()) {
            sb.append("§k");
            sb.append(chr);
            sb.append("§r");
        }

        text = sb.toString();
        return this;
    }

    public void tickObfuscation(float delta) {
        obfuscateTicks += delta;
        if (!(obfuscateTicks > obfuscateTimes * (1f / obfuscateSpeed)))
            return;

        obfuscateTimes++;
        switch (obfuscateMode) {
            case LEFT -> text = text.replaceFirst("§k", "");
            case RIGHT -> {
                int index = text.lastIndexOf("§k");
                if (index != -1) {
                    text = text.substring(0, index) + text.substring(index + 2);
                }
            }
            case CENTER -> {
                int index = getClosestIndexToCenter(text, "§k");
                if (index != -1) {
                    text = text.substring(0, index) + text.substring(index + 2);
                }
            }
            case RANDOM -> {
                List<Integer> occurrences = new ArrayList<>();
                for (int i = 0; i <= text.length() - 2; i++) {
                    if (text.startsWith("§k", i)) {
                        occurrences.add(i);
                    }
                }

                if (!occurrences.isEmpty()) {
                    Random rand = new Random();
                    int index = occurrences.get(rand.nextInt(occurrences.size()));

                    text = text.substring(0, index) + text.substring(index + 2);
                }
            }
        }

    }

    private int getClosestIndexToCenter(String str, String target) {
        int centerIndex = str.length() / 2;
        int closestIndex = -1;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i <= str.length() - target.length(); i++) {
            if (str.substring(i, i + target.length()).equals(target)) {
                int distanceFromCenter = Math.abs(i - centerIndex);
                if (distanceFromCenter < minDistance) {
                    closestIndex = i;
                    minDistance = distanceFromCenter;
                }
            }
        }
        return closestIndex;
    }
}
