package toni.lib.animation;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import toni.lib.animation.easing.EasingType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AnimationTimeline {
    public final float duration;

    @Getter @Setter
    private float current;
    private final AnimationKeyframe effected_keyframe = new AnimationKeyframe();
    private final AnimationKeyframe keyframe = new AnimationKeyframe();

    private boolean hasSortedTransitions = false;
    private final HashMap<Binding, List<Transition>> transitions = new HashMap<>();

    private final HashMap<Binding, List<AnimationEffect>> effects = new HashMap<>();

    public static AnimationTimeline builder(float duration) {
        return new AnimationTimeline(duration);
    }

    private AnimationTimeline(float duration) {
        this.duration = duration;
    }

    public AnimationKeyframe getKeyframe() {
        if (!hasSortedTransitions)
        {
            hasSortedTransitions = true;
            for (var kvp : transitions.entrySet())
                kvp.getValue().sort((a, b) -> Float.compare(a.getOut(), b.getOut()));
        }

        // loop over the list of transitions on the timeline
        for (var kvp : transitions.entrySet()) {
            // they should be sorted, so it's safe to just grab the first start value, and treat them all as if they're not overlapping
            float value = kvp.getValue().getFirst().getStartValue();
            for (var transition : kvp.getValue()) {
                if (current > transition.getOut()) {
                    value = transition.getEndValue();
                }
                else if (current > transition.getIn()) {
                    value = transition.eval(kvp.getKey(), current);
                }
            }

            keyframe.setValue(kvp.getKey(), value);
        }

        //bindings = Binding.values();
        for (var key : Binding.values()) {
            if (effects.containsKey(key)) {
                for (var effect : effects.get(key)) {
                    effected_keyframe.setValue(key, keyframe.getValue(key) + effect.apply(current));
                }
            } else {
                effected_keyframe.setValue(key, keyframe.getValue(key));
            }
        }

        return effected_keyframe;
    }

    public AnimationKeyframe applyPose(GuiGraphics context, float objectWidth, float objectHeight) {
        var key = getKeyframe();

        PoseUtils.applyScale(context, key.size);
        PoseUtils.applyPosition(context, key.size, objectWidth, objectHeight, key.posX, key.posY, key.posZ);
        PoseUtils.applyYRotation(context, key.size, objectWidth, objectHeight, key.rotY);
        PoseUtils.applyXRotation(context, key.size, objectWidth, objectHeight, key.rotX);
        PoseUtils.applyZRotation(context, key.size, objectWidth, objectHeight, key.rotZ);

        return key;
    }




    public AnimationTimeline advancePlayhead(float delta) {
        current = Math.max(0, Math.min(current + delta, duration));
        return this;
    }

    public AnimationTimeline resetPlayhead(float newPosition) {
        current = Math.max(0, Math.min(newPosition, duration));
        return this;
    }

    public AnimationTimeline withColor(int alpha, int red, int green, int blue) {
        keyframe.color = FastColor.ARGB32.color(alpha, red, green, blue);
        return this;
    }

    public AnimationTimeline withXPosition(float x) {
        keyframe.posX = x;
        return this;
    }

    public AnimationTimeline withYPosition(float y) {
        keyframe.posY = y;
        return this;
    }

    public AnimationTimeline withZPosition(float z) {
        keyframe.posZ = z;
        return this;
    }

    public AnimationTimeline withXRotation(float x) {
        keyframe.rotX = x;
        return this;
    }

    public AnimationTimeline withYRotation(float y) {
        keyframe.rotY = y;
        return this;
    }

    public AnimationTimeline withZRotation(float z) {
        keyframe.rotZ = z;
        return this;
    }

    public AnimationTimeline withSize(float size) {
        keyframe.size = size;
        return this;
    }

    public AnimationTimeline transition(Binding binding, float in, float out, float start, float end, EasingType easing) {
        var transition = new Transition(in, out, easing, start, end);

        if (!transitions.containsKey(binding))
            transitions.put(binding, new ArrayList<>());

        transitions.get(binding).add(transition);
        return this;
    }

    public AnimationTimeline fadeout(float time) {
        return transition(Binding.Alpha, duration - time, duration, 1f, 0f, EasingType.EaseOutSine);
    }

    public AnimationTimeline fadeout(float time, EasingType easing) {
        return transition(Binding.Alpha, duration - time, duration, 1f, 0f, easing);
    }


    public AnimationTimeline shake(float time) {
        transition(Binding.zRot, time, time + 0.1f, 0f, 4f, EasingType.EaseInBounce);
        return transition(Binding.zRot, time, time + 0.2f, 4f, 0, EasingType.EaseOutBounce);
    }

    public AnimationTimeline waveEffect(Binding binding, float intensity, float speed) {
        if (!effects.containsKey(binding))
            effects.put(binding, new ArrayList<>());

        effects.get(binding).add((time) -> intensity * Mth.cos(speed * time));
        return this;
    }

    public AnimationTimeline waveEffect() {
        return waveEffect(Binding.zRot, 2.5f, 5f);
    }
}


