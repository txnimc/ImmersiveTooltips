package toni.lib.animation;

import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import toni.lib.animation.easing.EasingFunctionArgs;
import toni.lib.animation.easing.EasingManager;
import toni.lib.animation.easing.EasingType;

import java.util.ArrayList;
import java.util.List;

public class Transition {

    private AnimationTimeline parent;

    @Getter @Setter
    private float in;
    @Getter @Setter
    private float out;
    @Getter @Setter
    private float startValue;
    @Getter @Setter
    private float endValue;

    private List<Double2DoubleFunction> easingFunctions = new ArrayList<>();

    public Transition(float in, float out, EasingType easing, float start, float end) {
        this.in = in;
        this.out = out;
        this.startValue = start;
        this.endValue = end;

        easingFunctions.add(EasingManager.getEasingFunction.apply(new EasingFunctionArgs(easing, null)));
    }

    public Transition(float in, float out, EasingType easing, float start, float end, double elasticity) {
        this.in = in;
        this.out = out;
        this.startValue = start;
        this.endValue = end;

        easingFunctions.add(EasingManager.getEasingFunction.apply(new EasingFunctionArgs(easing, elasticity)));
    }

    public float eval(Binding binding, float current) {
        var lerp = Mth.clamp((double) (current - in) / (out - in), 0f, 1f);

        for (Double2DoubleFunction f : easingFunctions) {
            lerp = f.apply(lerp);
        }

        lerp = Mth.clamp(lerp, 0, 1f);

        if (binding == Binding.Color)
            return FastColor.ARGB32.lerp((float) lerp, (int) startValue, (int) endValue);

        return (float) Mth.lerp(lerp, startValue, endValue);
    }
}

